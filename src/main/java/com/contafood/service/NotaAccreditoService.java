package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.NotaAccreditoRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NotaAccreditoService {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoService.class);

    private final NotaAccreditoRepository notaAccreditoRepository;
    private final NotaAccreditoTotaleService notaAccreditoTotaleService;
    private final NotaAccreditoRigaService notaAccreditoRigaService;
    private final StatoNotaAccreditoService statoNotaAccreditoService;
    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public NotaAccreditoService(final NotaAccreditoRepository notaAccreditoRepository, final NotaAccreditoTotaleService notaAccreditoTotaleService,
                                final NotaAccreditoRigaService notaAccreditoRigaService, final StatoNotaAccreditoService statoNotaAccreditoService,
                                final PagamentoRepository pagamentoRepository){
        this.notaAccreditoRepository = notaAccreditoRepository;
        this.notaAccreditoTotaleService = notaAccreditoTotaleService;
        this.notaAccreditoRigaService = notaAccreditoRigaService;
        this.statoNotaAccreditoService = statoNotaAccreditoService;
        this.pagamentoRepository = pagamentoRepository;
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

    public Map<Cliente, List<NotaAccredito>> getNoteAccreditoByCliente(Date dateFrom, Date dateTo){
        LOGGER.info("Retrieving the list of note_accredito, grouped by cliente, with speditoAde 'false', dateFrom '{}' and dateTo '{}'", dateFrom, dateTo);

        Map<Cliente, List<NotaAccredito>> noteAccreditoByCliente = new HashMap<>();

        Predicate<NotaAccredito> isNotaAccreditoSpeditoAdeFalse = notaAccredito -> notaAccredito.getSpeditoAde().equals(Boolean.FALSE);

        Predicate<NotaAccredito> isNotaAccreditoDataDaGreaterOrEquals = notaAccredito -> {
            if(dateFrom != null){
                return notaAccredito.getData().compareTo(dateFrom)>=0;
            }
            return true;
        };
        Predicate<NotaAccredito> isNotaAccreditoDataALessOrEquals = notaAccredito -> {
            if(dateTo != null){
                return notaAccredito.getData().compareTo(dateTo)<=0;
            }
            return true;
        };

        Set<NotaAccredito> noteAccredito = notaAccreditoRepository.findAll().stream()
                .filter(isNotaAccreditoDataDaGreaterOrEquals
                .and(isNotaAccreditoDataALessOrEquals)
                .and(isNotaAccreditoSpeditoAdeFalse)).collect(Collectors.toSet());

        if(noteAccredito != null && !noteAccredito.isEmpty()){
            for(NotaAccredito notaAccredito : noteAccredito){
                Cliente cliente = notaAccredito.getCliente();

                List<NotaAccredito> noteAccreditoList = noteAccreditoByCliente.getOrDefault(cliente, new ArrayList<>());
                noteAccreditoList.add(notaAccredito);

                noteAccreditoByCliente.put(cliente, noteAccreditoList);
            }
        }

        LOGGER.info("Successfully retrieved the list of note_accredito grouped by cliente");

        return noteAccreditoByCliente;
    }

    @Transactional
    public NotaAccredito create(NotaAccredito notaAccredito){
        LOGGER.info("Creating 'nota accredito'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaAccredito.getAnno(), notaAccredito.getProgressivo(), Long.valueOf(-1));

        notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getDaPagare());
        notaAccredito.setSpeditoAde(false);
        notaAccredito.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccredito createdNotaAccredito = notaAccreditoRepository.save(notaAccredito);

        createdNotaAccredito.getNotaAccreditoRighe().stream().forEach(nar -> {
            nar.getId().setNotaAccreditoId(createdNotaAccredito.getId());
            nar.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoRigaService.create(nar);
        });

        createdNotaAccredito.getNotaAccreditoTotali().stream().forEach(nat -> {
            nat.getId().setNotaAccreditoId(createdNotaAccredito.getId());
            nat.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoTotaleService.create(nat);
        });

        computeTotali(createdNotaAccredito, createdNotaAccredito.getNotaAccreditoRighe());

        notaAccreditoRepository.save(createdNotaAccredito);
        LOGGER.info("Created 'nota accredito' '{}'", createdNotaAccredito);

        return createdNotaAccredito;
    }

    @Transactional
    public NotaAccredito update(NotaAccredito notaAccredito){
        LOGGER.info("Updating 'nota accredito'");

        checkExistsByAnnoAndProgressivoAndIdNot(notaAccredito.getAnno(), notaAccredito.getProgressivo(), notaAccredito.getId());

        Set<NotaAccreditoTotale> notaAccreditoTotali = notaAccredito.getNotaAccreditoTotali();
        Set<NotaAccreditoRiga> notaAccreditoRighe = notaAccredito.getNotaAccreditoRighe();

        notaAccredito.setNotaAccreditoTotali(new HashSet<>());
        notaAccredito.setNotaAccreditoRighe(new HashSet<>());

        notaAccreditoTotaleService.deleteByNotaAccreditoId(notaAccredito.getId());
        notaAccreditoRigaService.deleteByNotaAccreditoId(notaAccredito.getId());

        NotaAccredito notaAccreditoCurrent = notaAccreditoRepository.findById(notaAccredito.getId()).orElseThrow(ResourceNotFoundException::new);
        notaAccredito.setStatoNotaAccredito(notaAccreditoCurrent.getStatoNotaAccredito());
        notaAccredito.setDataInserimento(notaAccreditoCurrent.getDataInserimento());
        notaAccredito.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        NotaAccredito updatedNotaAccredito = notaAccreditoRepository.save(notaAccredito);

        notaAccreditoRighe.stream().forEach(nar -> {
            nar.getId().setNotaAccreditoId(updatedNotaAccredito.getId());
            nar.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoRigaService.create(nar);
        });

        notaAccreditoTotali.stream().forEach(nat -> {
            nat.getId().setNotaAccreditoId(updatedNotaAccredito.getId());
            nat.getId().setUuid(UUID.randomUUID().toString());
            notaAccreditoTotaleService.create(nat);
        });

        computeTotali(updatedNotaAccredito, notaAccreditoRighe);

        notaAccreditoRepository.save(updatedNotaAccredito);
        LOGGER.info("Updated 'nota accredito' '{}'", updatedNotaAccredito);
        return updatedNotaAccredito;
    }

    @Transactional
    public NotaAccredito patch(Map<String,Object> patchNotaAccredito){
        LOGGER.info("Patching 'notaAccredito'");

        Long id = Long.valueOf((Integer) patchNotaAccredito.get("id"));
        NotaAccredito notaAccredito = notaAccreditoRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchNotaAccredito.forEach((key, value) -> {
            if(key.equals("id")){
                notaAccredito.setId(Long.valueOf((Integer)value));
            } else if(key.equals("speditoAde")){
                if(value != null){
                    notaAccredito.setSpeditoAde((boolean)value);
                } else {
                    notaAccredito.setSpeditoAde(Boolean.FALSE);
                }
            }
        });
        NotaAccredito patchedNotaAccredito = notaAccreditoRepository.save(notaAccredito);

        LOGGER.info("Patched 'notaAccredito' '{}'", patchedNotaAccredito);
        return patchedNotaAccredito;
    }

    @Transactional
    public void patchSpeditoAdeNoteAccreditoByCliente(Map<Cliente, List<NotaAccredito>> noteAccreditoByCliente, boolean speditoAde){
        LOGGER.info("Updating note accredito setting speditoAde='{}'", speditoAde);

        if(noteAccreditoByCliente != null && !noteAccreditoByCliente.isEmpty()){
            for(Cliente cliente : noteAccreditoByCliente.keySet()){
                for(NotaAccredito notaAccredito : noteAccreditoByCliente.get(cliente)){
                    Map<String, Object> patchNotaAccredito = new HashMap<>();
                    patchNotaAccredito.put("id", notaAccredito.getId());
                    patchNotaAccredito.put("speditoAde", speditoAde);

                    patch(patchNotaAccredito);
                }
            }
        }

        LOGGER.info("Successfully updated note accredito setting speditoAde={}", speditoAde);
    }

    @Transactional
    public void delete(Long notaAccreditoId){
        LOGGER.info("Deleting 'nota accredito' '{}'", notaAccreditoId);
        pagamentoRepository.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoTotaleService.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoRigaService.deleteByNotaAccreditoId(notaAccreditoId);
        notaAccreditoRepository.deleteById(notaAccreditoId);
        LOGGER.info("Deleted 'nota accredito' '{}'", notaAccreditoId);
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<NotaAccredito> notaAccredito = notaAccreditoRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(notaAccredito.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.NOTA_ACCREDITO, anno, progressivo);
        }
    }

    private void computeTotali(NotaAccredito notaAccredito, Set<NotaAccreditoRiga> notaAccreditoRighe){
        Map<AliquotaIva, Set<NotaAccreditoRiga>> ivaNotaAccreditoRigheMap = new HashMap<>();
        notaAccreditoRighe.stream().forEach(nar -> {
            AliquotaIva iva = notaAccreditoRigaService.getAliquotaIva(nar);
            Set<NotaAccreditoRiga> notaAccreditoArticoliByIva = ivaNotaAccreditoRigheMap.getOrDefault(iva, new HashSet<>());
            notaAccreditoArticoliByIva.add(nar);
            ivaNotaAccreditoRigheMap.put(iva, notaAccreditoArticoliByIva);
        });
        Float totaleQuantita = 0f;
        BigDecimal totale = new BigDecimal(0);
        for (Map.Entry<AliquotaIva, Set<NotaAccreditoRiga>> entry : ivaNotaAccreditoRigheMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);

            Set<NotaAccreditoRiga> notaAccreditoRigheByIva = entry.getValue();
            for(NotaAccreditoRiga notaAccreditoRiga: notaAccreditoRigheByIva){
                if(notaAccreditoRiga.getImponibile() != null){
                    totaleByIva = totaleByIva.add(notaAccreditoRiga.getImponibile());
                }
                if(notaAccreditoRiga.getQuantita() != null){
                    totaleQuantita = totaleQuantita + notaAccreditoRiga.getQuantita();
                }
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        notaAccredito.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        notaAccredito.setTotaleAcconto(new BigDecimal(0));
        notaAccredito.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

}
