package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoFattura;
import com.contafood.repository.StatoFatturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoFatturaService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoFatturaService.class);

    private final StatoFatturaRepository statoFatturaRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATA = "PARZIALMENTE_PAGATA";
    private static final String PAGATA = "PAGATA";

    @Autowired
    public StatoFatturaService(final StatoFatturaRepository statoFatturaRepository){
        this.statoFatturaRepository = statoFatturaRepository;
    }

    public Set<StatoFattura> getAll(){
        LOGGER.info("Retrieving the list of 'statiFattura'");
        Set<StatoFattura> statiFattura = statoFatturaRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiFattura'", statiFattura.size());
        return statiFattura;
    }

    public StatoFattura getOne(Long statoFatturaId){
        LOGGER.info("Retrieving 'statoFattura' '{}'", statoFatturaId);
        StatoFattura statoFattura = statoFatturaRepository.findById(statoFatturaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoFattura' '{}'", statoFattura);
        return statoFattura;
    }

    public StatoFattura getDaPagare(){
        LOGGER.info("Retrieving 'statoFattura' 'DA_PAGARE'");
        StatoFattura statoFattura = statoFatturaRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoFattura' '{}'", statoFattura);
        return statoFattura;
    }

    public StatoFattura getParzialmentePagata(){
        LOGGER.info("Retrieving 'statoFattura' 'PARZIALMENTE_PAGATA'");
        StatoFattura statoFattura = statoFatturaRepository.findByCodice(PARZIALMENTE_PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoFattura' '{}'", statoFattura);
        return statoFattura;
    }

    public StatoFattura getPagata(){
        LOGGER.info("Retrieving 'statoFattura' 'PAGATA'");
        StatoFattura statoFattura = statoFatturaRepository.findByCodice(PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoFattura' '{}'", statoFattura);
        return statoFattura;
    }

    public StatoFattura create(StatoFattura statoFattura){
        LOGGER.info("Creating 'statoFattura'");
        StatoFattura createdStatoFattura = statoFatturaRepository.save(statoFattura);
        LOGGER.info("Created 'statoFattura' '{}'", createdStatoFattura);
        return createdStatoFattura;
    }

    public StatoFattura update(StatoFattura statoFattura){
        LOGGER.info("Updating 'statoFattura'");
        StatoFattura updatedStatoFattura = statoFatturaRepository.save(statoFattura);
        LOGGER.info("Updated 'statoFattura' '{}'", updatedStatoFattura);
        return updatedStatoFattura;
    }

    public void delete(Long statoFatturaId){
        LOGGER.info("Deleting 'statoFattura' '{}'", statoFatturaId);
        statoFatturaRepository.deleteById(statoFatturaId);
        LOGGER.info("Deleted 'statoFattura' '{}'", statoFatturaId);
    }
}
