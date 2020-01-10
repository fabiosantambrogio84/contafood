package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoOrdine;
import com.contafood.repository.StatoOrdineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoOrdineService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoOrdineService.class);

    private final StatoOrdineRepository statoOrdineRepository;

    private static final String DA_EVADERE = "DA_EVADERE";
    private static final String PARZIALMENTE_EVASO = "PARZIALMENTE_EVASO";
    private static final String EVASO = "EVASO";

    @Autowired
    public StatoOrdineService(final StatoOrdineRepository statoOrdineRepository){
        this.statoOrdineRepository = statoOrdineRepository;
    }

    public Set<StatoOrdine> getAll(){
        LOGGER.info("Retrieving the list of 'statiOrdine'");
        Set<StatoOrdine> statiOrdine = statoOrdineRepository.findAll();
        LOGGER.info("Retrieved {} 'statiOrdine'", statiOrdine.size());
        return statiOrdine;
    }

    public StatoOrdine getOne(Long statoOrdineId){
        LOGGER.info("Retrieving 'statoOrdine' '{}'", statoOrdineId);
        StatoOrdine statoOrdine = statoOrdineRepository.findById(statoOrdineId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoOrdine' '{}'", statoOrdine);
        return statoOrdine;
    }

    public StatoOrdine getDaEvadere(){
        LOGGER.info("Retrieving 'statoOrdine' 'DA_EVADERE'");
        StatoOrdine statoOrdine = statoOrdineRepository.findByCodice(DA_EVADERE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoOrdine' '{}'", statoOrdine);
        return statoOrdine;
    }

    public StatoOrdine getParzialmenteEvaso(){
        LOGGER.info("Retrieving 'statoOrdine' 'PARZIALMENTE_EVASO'");
        StatoOrdine statoOrdine = statoOrdineRepository.findByCodice(PARZIALMENTE_EVASO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoOrdine' '{}'", statoOrdine);
        return statoOrdine;
    }

    public StatoOrdine getEvaso(){
        LOGGER.info("Retrieving 'statoOrdine' 'EVASO'");
        StatoOrdine statoOrdine = statoOrdineRepository.findByCodice(EVASO).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoOrdine' '{}'", statoOrdine);
        return statoOrdine;
    }

    public StatoOrdine create(StatoOrdine statoOrdine){
        LOGGER.info("Creating 'statoOrdine'");
        StatoOrdine createdStatoOrdine = statoOrdineRepository.save(statoOrdine);
        LOGGER.info("Created 'statoOrdine' '{}'", createdStatoOrdine);
        return createdStatoOrdine;
    }

    public StatoOrdine update(StatoOrdine statoOrdine){
        LOGGER.info("Updating 'statoOrdine'");
        StatoOrdine updatedStatoOrdine = statoOrdineRepository.save(statoOrdine);
        LOGGER.info("Updated 'statoOrdine' '{}'", updatedStatoOrdine);
        return updatedStatoOrdine;
    }

    public void delete(Long statoOrdineId){
        LOGGER.info("Deleting 'statoOrdine' '{}'", statoOrdineId);
        // Update all OrdiniClienti and OrdiniFornitori setting stato to 'Da evadere'?
        statoOrdineRepository.deleteById(statoOrdineId);
        LOGGER.info("Deleted 'statoOrdine' '{}'", statoOrdineId);
    }
}
