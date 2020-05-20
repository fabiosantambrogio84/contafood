package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoNotaReso;
import com.contafood.repository.StatoNotaResoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoNotaResoService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoNotaResoService.class);

    private final StatoNotaResoRepository statoNotaResoRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATA = "PARZIALMENTE_PAGATA";
    private static final String PAGATA = "PAGATA";

    @Autowired
    public StatoNotaResoService(final StatoNotaResoRepository statoNotaResoRepository){
        this.statoNotaResoRepository = statoNotaResoRepository;
    }

    public Set<StatoNotaReso> getAll(){
        LOGGER.info("Retrieving the list of 'statiNotaReso'");
        Set<StatoNotaReso> statiNotaReso = statoNotaResoRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiNotaReso'", statiNotaReso.size());
        return statiNotaReso;
    }

    public StatoNotaReso getOne(Long statoNotaResoId){
        LOGGER.info("Retrieving 'statoNotaReso' '{}'", statoNotaResoId);
        StatoNotaReso statoNotaReso = statoNotaResoRepository.findById(statoNotaResoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaReso' '{}'", statoNotaReso);
        return statoNotaReso;
    }

    public StatoNotaReso getDaPagare(){
        LOGGER.info("Retrieving 'statoNotaReso' 'DA_PAGARE'");
        StatoNotaReso statoNotaReso = statoNotaResoRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaReso' '{}'", statoNotaReso);
        return statoNotaReso;
    }

    public StatoNotaReso getParzialmentePagata(){
        LOGGER.info("Retrieving 'statoNotaReso' 'PARZIALMENTE_PAGATA'");
        StatoNotaReso statoNotaReso = statoNotaResoRepository.findByCodice(PARZIALMENTE_PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaReso' '{}'", statoNotaReso);
        return statoNotaReso;
    }

    public StatoNotaReso getPagato(){
        LOGGER.info("Retrieving 'statoNotaReso' 'PAGATA'");
        StatoNotaReso statoNotaReso = statoNotaResoRepository.findByCodice(PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoNotaReso' '{}'", statoNotaReso);
        return statoNotaReso;
    }

    public StatoNotaReso create(StatoNotaReso statoNotaReso){
        LOGGER.info("Creating 'statoNotaReso'");
        StatoNotaReso createdStatoNotaReso = statoNotaResoRepository.save(statoNotaReso);
        LOGGER.info("Created 'statoNotaReso' '{}'", createdStatoNotaReso);
        return createdStatoNotaReso;
    }

    public StatoNotaReso update(StatoNotaReso statoNotaReso){
        LOGGER.info("Updating 'statoNotaReso'");
        StatoNotaReso updatedStatoNotaReso = statoNotaResoRepository.save(statoNotaReso);
        LOGGER.info("Updated 'statoNotaReso' '{}'", updatedStatoNotaReso);
        return updatedStatoNotaReso;
    }

    public void delete(Long statoNotaResoId){
        LOGGER.info("Deleting 'statoNotaReso' '{}'", statoNotaResoId);
        statoNotaResoRepository.deleteById(statoNotaResoId);
        LOGGER.info("Deleted 'statoNotaReso' '{}'", statoNotaResoId);
    }
}
