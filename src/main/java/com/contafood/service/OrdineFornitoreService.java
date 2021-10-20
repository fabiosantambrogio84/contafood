package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineClienteArticolo;
import com.contafood.model.OrdineFornitore;
import com.contafood.model.OrdineFornitoreArticolo;
import com.contafood.repository.OrdineFornitoreRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class OrdineFornitoreService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrdineFornitoreService.class);

    private final OrdineFornitoreRepository ordineFornitoreRepository;
    private final OrdineFornitoreArticoloService ordineFornitoreArticoloService;
    private final OrdineClienteArticoloService ordineClienteArticoloService;

    public OrdineFornitoreService(final OrdineFornitoreRepository ordineFornitoreRepository,
                                  final OrdineFornitoreArticoloService ordineFornitoreArticoloService,
                                  final OrdineClienteArticoloService ordineClienteArticoloService){
        this.ordineFornitoreRepository = ordineFornitoreRepository;
        this.ordineFornitoreArticoloService = ordineFornitoreArticoloService;
        this.ordineClienteArticoloService = ordineClienteArticoloService;
    }

    public Set<OrdineFornitore> getAll(){
        LOGGER.info("Retrieving the list of 'ordini fornitori'");
        Set<OrdineFornitore> ordiniFornitori = ordineFornitoreRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ordini fornitori'", ordiniFornitori.size());
        return ordiniFornitori;
    }

    public OrdineFornitore getOne(Long ordineFornitoreId){
        LOGGER.info("Retrieving 'ordineFornitore' '{}'", ordineFornitoreId);
        OrdineFornitore ordineFornitore = ordineFornitoreRepository.findById(ordineFornitoreId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ordineFornitore' '{}'", ordineFornitore);
        return ordineFornitore;
    }

    @Transactional
    public OrdineFornitore create(OrdineFornitore ordineFornitore){
        LOGGER.info("Creating 'ordineFornitore'");
        ordineFornitore.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Integer annoContabile = ZonedDateTime.now().getYear();
        ordineFornitore.setAnnoContabile(annoContabile);
        ordineFornitore.setProgressivo(computeProgressivo(annoContabile));

        OrdineFornitore createdOrdineFornitore = ordineFornitoreRepository.save(ordineFornitore);

        createdOrdineFornitore.getOrdineFornitoreArticoli().stream().forEach(ofa -> {
            ofa.getId().setOrdineFornitoreId(createdOrdineFornitore.getId());
            ordineFornitoreArticoloService.create(ofa);
        });
        ordineFornitoreRepository.save(createdOrdineFornitore);

        // patch OrdiniClienti
        createdOrdineFornitore.getOrdineFornitoreArticoli().stream().forEach(ofa -> {
            String idOrdiniClienti = ofa.getIdOrdiniClienti();
            if(!StringUtils.isEmpty(idOrdiniClienti)){
                String[] idOrdiniClientiTokens = idOrdiniClienti.split(";");
                for (String idOrdiniClientiToken : idOrdiniClientiTokens) {
                    Map<String, Object> patchOrdineClienteArticolo = new HashMap<>();
                    patchOrdineClienteArticolo.put("idOrdineCliente", Integer.valueOf(idOrdiniClientiToken));
                    patchOrdineClienteArticolo.put("idArticolo", ofa.getId().getArticoloId().intValue());
                    patchOrdineClienteArticolo.put("idOrdineFornitore", ofa.getId().getOrdineFornitoreId().intValue());

                    ordineClienteArticoloService.patch(patchOrdineClienteArticolo);
                }
            }
        });

        LOGGER.info("Created 'ordineFornitore' '{}'", createdOrdineFornitore);
        return createdOrdineFornitore;
    }

    @Transactional
    public OrdineFornitore update(OrdineFornitore ordineFornitore){
        LOGGER.info("Updating 'ordineFornitore'");
        Set<OrdineFornitoreArticolo> ordineFornitoreArticoli = ordineFornitore.getOrdineFornitoreArticoli();
        ordineFornitore.setOrdineFornitoreArticoli(new HashSet<>());
        ordineFornitoreArticoloService.deleteByOrdineFornitoreId(ordineFornitore.getId());

        OrdineFornitore ordineFornitoreCurrent = ordineFornitoreRepository.findById(ordineFornitore.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineFornitore.setDataInserimento(ordineFornitoreCurrent.getDataInserimento());
        ordineFornitore.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        OrdineFornitore updatedOrdineFornitore = ordineFornitoreRepository.save(ordineFornitore);
        ordineFornitoreArticoli.stream().forEach(ofa -> {
            ofa.getId().setOrdineFornitoreId(updatedOrdineFornitore.getId());
            ordineFornitoreArticoloService.create(ofa);
        });
        LOGGER.info("Updated 'ordineFornitore' '{}'", updatedOrdineFornitore);
        return updatedOrdineFornitore;
    }

    @Transactional
    public OrdineFornitore patch(OrdineFornitore ordineFornitore){
        LOGGER.info("Patching 'ordineFornitore'");
        OrdineFornitore patchedOrdineFornitore = ordineFornitoreRepository.save(ordineFornitore);
        LOGGER.info("Patched 'ordineFornitore' '{}'", patchedOrdineFornitore);
        return patchedOrdineFornitore;
    }

    @Transactional
    public void delete(Long ordineFornitoreId){
        LOGGER.info("Deleting 'ordineFornitore' '{}'", ordineFornitoreId);
        Set<OrdineClienteArticolo> ordiniClientiArticoli = ordineClienteArticoloService.getOrdineClienteArticoliByIdOrdineFornitore(ordineFornitoreId);
        if(!ordiniClientiArticoli.isEmpty()){
            for(OrdineClienteArticolo ordineClienteArticolo : ordiniClientiArticoli){
                ordineClienteArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
                ordineClienteArticolo.setOrdineFornitoreId(null);
            }
            ordineClienteArticoloService.saveAll(ordiniClientiArticoli);
        }
        ordineFornitoreArticoloService.deleteByOrdineFornitoreId(ordineFornitoreId);
        ordineFornitoreRepository.deleteById(ordineFornitoreId);
        LOGGER.info("Deleted 'ordineFornitore' '{}'", ordineFornitoreId);
    }

    private Integer computeProgressivo(Integer annoContabile){
        List<OrdineFornitore> ordiniFornitori = ordineFornitoreRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ordiniFornitori != null && !ordiniFornitori.isEmpty()){
            Optional<OrdineFornitore> lastOrdineFornitore = ordiniFornitori.stream().findFirst();
            if(lastOrdineFornitore.isPresent()){
                return lastOrdineFornitore.get().getProgressivo() + 1;
            }
        }
        return 1;
    }


}
