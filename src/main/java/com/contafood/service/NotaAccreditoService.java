package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.NotaAccreditoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class NotaAccreditoService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoService.class);

    private final NotaAccreditoRepository notaAccreditoRepository;
    private final NotaAccreditoArticoloService notaAccreditoArticoloService;
    private final NotaAccreditoTotaleService notaAccreditoTotaleService;
    private final NotaAccreditoRigaService notaAccreditoRigaService;

    @Autowired
    public NotaAccreditoService(final NotaAccreditoRepository notaAccreditoRepository, final NotaAccreditoArticoloService notaAccreditoArticoloService, final NotaAccreditoTotaleService notaAccreditoTotaleService, final NotaAccreditoRigaService notaAccreditoRigaService){
        this.notaAccreditoRepository = notaAccreditoRepository;
        this.notaAccreditoArticoloService = notaAccreditoArticoloService;
        this.notaAccreditoTotaleService = notaAccreditoTotaleService;
        this.notaAccreditoRigaService = notaAccreditoRigaService;
    }

    public Set<NotaAccredito> getAll(){
        LOGGER.info("Retrieving the list of 'note accredito'");
        Set<NotaAccredito> noteAccredito = notaAccreditoRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'note accredito'", noteAccredito.size());
        return noteAccredito;
    }

    public NotaAccredito getOne(Long notaAccreditoId){
        LOGGER.info("Retrieving 'nota accredito' '{}'", notaAccreditoId);
        NotaAccredito notaAccredito = notaAccreditoRepository.findById(notaAccreditoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'nota accredito' '{}'", notaAccredito);
        return notaAccredito;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        List<NotaAccredito> noteAccredito = notaAccreditoRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(noteAccredito != null && !noteAccredito.isEmpty()){
            Optional<NotaAccredito> lastNotaAccredito = noteAccredito.stream().findFirst();
            if(lastNotaAccredito.isPresent()){
                progressivo = lastNotaAccredito.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    @Transactional
    public NotaAccredito create(NotaAccredito notaAccredito){
        LOGGER.info("Creating 'nota accredito'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaAccredito.getAnno(), notaAccredito.getProgressivo(), Long.valueOf(-1));

        notaAccredito.setSpeditoAde(false);
        notaAccredito.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccredito createdNotaAccredito = notaAccreditoRepository.save(notaAccredito);

        createdNotaAccredito.getNotaAccreditoArticoli().stream().forEach(naa -> {
            naa.getId().setNotaAccreditoId(createdNotaAccredito.getId());
            naa.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoArticoloService.create(naa);
        });

        createdNotaAccredito.getNotaAccreditoTotali().stream().forEach(nat -> {
            nat.getId().setNotaAccreditoId(createdNotaAccredito.getId());
            nat.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoTotaleService.create(nat);
        });

        createdNotaAccredito.getNotaAccreditoRiga().stream().forEach(nar -> {
            nar.getId().setNotaAccreditoId(createdNotaAccredito.getId());
            nar.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoRigaService.create(nar);
        });

        computeTotali(createdNotaAccredito, createdNotaAccredito.getNotaAccreditoArticoli());

        notaAccreditoRepository.save(createdNotaAccredito);
        LOGGER.info("Created 'nota accredito' '{}'", createdNotaAccredito);

        return createdNotaAccredito;
    }

    @Transactional
    public NotaAccredito update(NotaAccredito notaAccredito){
        LOGGER.info("Updating 'nota accredito'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaAccredito.getAnno(), notaAccredito.getProgressivo(), notaAccredito.getId());

        Set<NotaAccreditoArticolo> notaAccreditoArticoli = notaAccredito.getNotaAccreditoArticoli();
        Set<NotaAccreditoTotale> notaAccreditoTotali = notaAccredito.getNotaAccreditoTotali();
        Set<NotaAccreditoRiga> notaAccreditoRiga = notaAccredito.getNotaAccreditoRiga();

        notaAccredito.setNotaAccreditoArticoli(new HashSet<>());
        notaAccredito.setNotaAccreditoTotali(new HashSet<>());
        notaAccredito.setNotaAccreditoRiga(new HashSet<>());

        notaAccreditoArticoloService.deleteByNotaAccreditoId(notaAccredito.getId());
        notaAccreditoTotaleService.deleteByNotaAccreditoId(notaAccredito.getId());
        notaAccreditoRigaService.deleteByNotaAccreditoId(notaAccredito.getId());

        NotaAccredito notaAccreditoCurrent = notaAccreditoRepository.findById(notaAccredito.getId()).orElseThrow(ResourceNotFoundException::new);
        notaAccredito.setDataInserimento(notaAccreditoCurrent.getDataInserimento());
        notaAccredito.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccredito updatedNotaAccredito = notaAccreditoRepository.save(notaAccredito);

        notaAccreditoArticoli.stream().forEach(naa -> {
            naa.getId().setNotaAccreditoId(updatedNotaAccredito.getId());
            naa.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoArticoloService.create(naa);
        });

        notaAccreditoTotali.stream().forEach(nat -> {
            nat.getId().setNotaAccreditoId(updatedNotaAccredito.getId());
            nat.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoTotaleService.create(nat);
        });

        notaAccreditoRiga.stream().forEach(nar -> {
            nar.getId().setNotaAccreditoId(updatedNotaAccredito.getId());
            notaAccreditoRigaService.create(nar);
        });

        computeTotali(updatedNotaAccredito, notaAccreditoArticoli);

        notaAccreditoRepository.save(updatedNotaAccredito);
        LOGGER.info("Updated 'nota accredito' '{}'", updatedNotaAccredito);
        return updatedNotaAccredito;
    }

    @Transactional
    public void delete(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito' '{}'", notaAccreditoId);

        notaAccreditoArticoloService.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoTotaleService.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoRigaService.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoRepository.deleteById(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito' '{}'", notaAccreditoId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<NotaAccredito> notaAccredito = notaAccreditoRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(notaAccredito.isPresent()){
            throw new ResourceAlreadyExistingException("nota accredito", anno, progressivo);
        }
    }

    private void computeTotali(NotaAccredito notaAccredito, Set<NotaAccreditoArticolo> notaAccreditoArticoli){
        Map<AliquotaIva, Set<NotaAccreditoArticolo>> ivaNotaAccreditoArticoliMap = new HashMap<>();
        notaAccreditoArticoli.stream().forEach(naa -> {
            Articolo articolo = notaAccreditoArticoloService.getArticolo(naa);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<NotaAccreditoArticolo> notaAccreditoArticoliByIva = ivaNotaAccreditoArticoliMap.getOrDefault(iva, new HashSet<>());
            notaAccreditoArticoliByIva.add(naa);
            ivaNotaAccreditoArticoliMap.put(iva, notaAccreditoArticoliByIva);
        });
        Float totaleQuantita = 0f;
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<NotaAccreditoArticolo>> entry : ivaNotaAccreditoArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<NotaAccreditoArticolo> notaAccreditoArticoliByIva = entry.getValue();
            for(NotaAccreditoArticolo notaAccreditoArticolo: notaAccreditoArticoliByIva){
                totaleByIva = totaleByIva.add(notaAccreditoArticolo.getImponibile());
                totaleQuantita = totaleQuantita + notaAccreditoArticolo.getQuantita();
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        notaAccredito.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        notaAccredito.setTotaleAcconto(new BigDecimal(0));
        notaAccredito.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

}
