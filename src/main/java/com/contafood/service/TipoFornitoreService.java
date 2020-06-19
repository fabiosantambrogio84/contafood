package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.TipoFornitore;
import com.contafood.repository.TipoFornitoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TipoFornitoreService {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoFornitoreService.class);

    private final TipoFornitoreRepository tipoFornitoreRepository;

    @Autowired
    public TipoFornitoreService(final TipoFornitoreRepository tipoFornitoreRepository){
        this.tipoFornitoreRepository = tipoFornitoreRepository;
    }

    public Set<TipoFornitore> getAll(){
        LOGGER.info("Retrieving the list of 'tipiFornitore'");
        Set<TipoFornitore> tipiFornitori = tipoFornitoreRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'tipiFornitore'", tipiFornitori.size());
        return tipiFornitori;
    }

    public TipoFornitore getOne(Long tipoFornitoreId){
        LOGGER.info("Retrieving 'tipoFornitore' '{}'", tipoFornitoreId);
        TipoFornitore tipoFornitore = tipoFornitoreRepository.findById(tipoFornitoreId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'tipoFornitore' '{}'", tipoFornitore);
        return tipoFornitore;
    }

    public TipoFornitore create(TipoFornitore tipoFornitore){
        LOGGER.info("Creating 'tipoFornitore'");
        TipoFornitore createdTipoFornitore = tipoFornitoreRepository.save(tipoFornitore);
        LOGGER.info("Created 'tipoFornitore' '{}'", createdTipoFornitore);
        return createdTipoFornitore;
    }

    public TipoFornitore update(TipoFornitore tipoFornitore){
        LOGGER.info("Updating 'tipoFornitore'");
        TipoFornitore updatedTipoFornitore = tipoFornitoreRepository.save(tipoFornitore);
        LOGGER.info("Updated 'tipoFornitore' '{}'", updatedTipoFornitore);
        return updatedTipoFornitore;
    }

    public void delete(Long tipoFornitoreId){
        LOGGER.info("Deleting 'tipoFornitore' '{}'", tipoFornitoreId);
        tipoFornitoreRepository.deleteById(tipoFornitoreId);
        LOGGER.info("Deleted 'tipoFornitore '{}'", tipoFornitoreId);
    }
}
