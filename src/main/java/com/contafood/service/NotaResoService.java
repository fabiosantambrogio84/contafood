package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.AliquotaIva;
import com.contafood.model.NotaReso;
import com.contafood.model.NotaResoRiga;
import com.contafood.model.NotaResoTotale;
import com.contafood.repository.NotaResoRepository;
import com.contafood.repository.PagamentoRepository;
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
public class NotaResoService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaResoService.class);

    private final NotaResoRepository notaResoRepository;
    private final NotaResoTotaleService notaResoTotaleService;
    private final NotaResoRigaService notaResoRigaService;
    private final StatoNotaResoService statoNotaResoService;
    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public NotaResoService(final NotaResoRepository notaResoRepository, final NotaResoTotaleService notaResoTotaleService,
                           final NotaResoRigaService notaResoRigaService, final StatoNotaResoService statoNotaResoService,
                           final PagamentoRepository pagamentoRepository){
        this.notaResoRepository = notaResoRepository;
        this.notaResoTotaleService = notaResoTotaleService;
        this.notaResoRigaService = notaResoRigaService;
        this.statoNotaResoService = statoNotaResoService;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Set<NotaReso> getAll(){
        LOGGER.info("Retrieving the list of 'note reso'");
        Set<NotaReso> noteReso = notaResoRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'note reso'", noteReso.size());
        return noteReso;
    }

    public NotaReso getOne(Long notaResoId){
        LOGGER.info("Retrieving 'nota reso' '{}'", notaResoId);
        NotaReso notaReso = notaResoRepository.findById(notaResoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'nota reso' '{}'", notaReso);
        return notaReso;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        List<NotaReso> noteReso = notaResoRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(noteReso != null && !noteReso.isEmpty()){
            Optional<NotaReso> lastNotaReso = noteReso.stream().findFirst();
            if(lastNotaReso.isPresent()){
                progressivo = lastNotaReso.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    @Transactional
    public NotaReso create(NotaReso notaReso){
        LOGGER.info("Creating 'nota reso'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaReso.getAnno(), notaReso.getProgressivo(), Long.valueOf(-1));

        notaReso.setStatoNotaReso(statoNotaResoService.getDaPagare());
        notaReso.setSpeditoAde(false);
        notaReso.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaReso createdNotaReso = notaResoRepository.save(notaReso);

        createdNotaReso.getNotaResoRighe().stream().forEach(nrr -> {
            nrr.getId().setNotaResoId(createdNotaReso.getId());
            nrr.getId().setUuid(UUID.randomUUID().toString());
            notaResoRigaService.create(nrr);
        });

        createdNotaReso.getNotaResoTotali().stream().forEach(nrt -> {
            nrt.getId().setNotaResoId(createdNotaReso.getId());
            nrt.getId().setUuid(UUID.randomUUID().toString());
            notaResoTotaleService.create(nrt);
        });

        computeTotali(createdNotaReso, createdNotaReso.getNotaResoRighe());

        notaResoRepository.save(createdNotaReso);
        LOGGER.info("Created 'nota reso' '{}'", createdNotaReso);

        return createdNotaReso;
    }

    @Transactional
    public NotaReso update(NotaReso notaReso){
        LOGGER.info("Updating 'nota reso'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaReso.getAnno(), notaReso.getProgressivo(), notaReso.getId());

        Set<NotaResoTotale> notaResoTotali = notaReso.getNotaResoTotali();
        Set<NotaResoRiga> notaResoRighe = notaReso.getNotaResoRighe();

        notaReso.setNotaResoTotali(new HashSet<>());
        notaReso.setNotaResoRighe(new HashSet<>());

        notaResoTotaleService.deleteByNotaResoId(notaReso.getId());
        notaResoRigaService.deleteByNotaResoId(notaReso.getId());

        NotaReso notaResoCurrent = notaResoRepository.findById(notaReso.getId()).orElseThrow(ResourceNotFoundException::new);
        notaReso.setStatoNotaReso(notaResoCurrent.getStatoNotaReso());
        notaReso.setDataInserimento(notaResoCurrent.getDataInserimento());
        notaReso.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaReso updatedNotaReso = notaResoRepository.save(notaReso);

        notaResoRighe.stream().forEach(nrr -> {
            nrr.getId().setNotaResoId(updatedNotaReso.getId());
            nrr.getId().setUuid(UUID.randomUUID().toString());
            notaResoRigaService.create(nrr);
        });

        notaResoTotali.stream().forEach(nrt -> {
            nrt.getId().setNotaResoId(updatedNotaReso.getId());
            nrt.getId().setUuid(UUID.randomUUID().toString());
            notaResoTotaleService.create(nrt);
        });

        computeTotali(updatedNotaReso, notaResoRighe);

        notaResoRepository.save(updatedNotaReso);
        LOGGER.info("Updated 'nota reso' '{}'", updatedNotaReso);
        return updatedNotaReso;
    }

    @Transactional
    public void delete(Long notaResoId){
        LOGGER.info("Deleting 'nota reso' '{}'", notaResoId);
        pagamentoRepository.deleteByNotaResoId(notaResoId);
        notaResoTotaleService.deleteByNotaResoId(notaResoId);
        notaResoRigaService.deleteByNotaResoId(notaResoId);
        notaResoRepository.deleteById(notaResoId);
        LOGGER.info("Deleted 'nota reso' '{}'", notaResoId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<NotaReso> notaReso = notaResoRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(notaReso.isPresent()){
            throw new ResourceAlreadyExistingException("nota reso", anno, progressivo);
        }
    }

    private void computeTotali(NotaReso notaReso, Set<NotaResoRiga> notaResoRighe){
        Map<AliquotaIva, Set<NotaResoRiga>> ivaNotaResoRigheMap = new HashMap<>();
        notaResoRighe.stream().forEach(nrr -> {
            AliquotaIva iva = notaResoRigaService.getAliquotaIva(nrr);
            Set<NotaResoRiga> notaResoArticoliByIva = ivaNotaResoRigheMap.getOrDefault(iva, new HashSet<>());
            notaResoArticoliByIva.add(nrr);
            ivaNotaResoRigheMap.put(iva, notaResoArticoliByIva);
        });
        Float totaleQuantita = 0f;
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<NotaResoRiga>> entry : ivaNotaResoRigheMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);

            Set<NotaResoRiga> notaResoRigheByIva = entry.getValue();
            for(NotaResoRiga notaResoRiga: notaResoRigheByIva){
                if(notaResoRiga.getImponibile() != null){
                    totaleByIva = totaleByIva.add(notaResoRiga.getImponibile());
                }
                if(notaResoRiga.getQuantita() != null){
                    totaleQuantita = totaleQuantita + notaResoRiga.getQuantita();
                }
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        notaReso.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        notaReso.setTotaleAcconto(new BigDecimal(0));
        notaReso.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

}
