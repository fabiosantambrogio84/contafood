package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.TipoFattura;
import com.contafood.repository.TipoFatturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TipoFatturaService {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoFatturaService.class);

    private final TipoFatturaRepository tipoFatturaRepository;

    private static final String VENDITA = "VENDITA";
    private static final String ACCOMPAGNATORIA = "ACCOMPAGNATORIA";

    @Autowired
    public TipoFatturaService(final TipoFatturaRepository tipoFatturaRepository){
        this.tipoFatturaRepository = tipoFatturaRepository;
    }

    public Set<TipoFattura> getAll(){
        LOGGER.info("Retrieving the list of 'tipiFattura'");
        Set<TipoFattura> TipoFattura = tipoFatturaRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'TipoFattura'", TipoFattura.size());
        return TipoFattura;
    }

    public TipoFattura getOne(Long tipoFatturaId){
        LOGGER.info("Retrieving 'tipoFattura' '{}'", tipoFatturaId);
        TipoFattura tipoFattura = tipoFatturaRepository.findById(tipoFatturaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura getVendita(){
        LOGGER.info("Retrieving 'tipoFattura' 'VENDITA'");
        TipoFattura tipoFattura = tipoFatturaRepository.findByCodice(VENDITA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura getAccompagnatoria(){
        LOGGER.info("Retrieving 'tipoFattura' 'ACCOMPAGNATORIA'");
        TipoFattura tipoFattura = tipoFatturaRepository.findByCodice(ACCOMPAGNATORIA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura create(TipoFattura tipoFattura){
        LOGGER.info("Creating 'tipoFattura'");
        TipoFattura createdTipoFattura = tipoFatturaRepository.save(tipoFattura);
        LOGGER.info("Created 'tipoFattura' '{}'", createdTipoFattura);
        return createdTipoFattura;
    }

    public TipoFattura update(TipoFattura tipoFattura){
        LOGGER.info("Updating 'tipoFattura'");
        TipoFattura updatedTipoFattura = tipoFatturaRepository.save(tipoFattura);
        LOGGER.info("Updated 'tipoFattura' '{}'", updatedTipoFattura);
        return updatedTipoFattura;
    }

    public void delete(Long tipoFatturaId){
        LOGGER.info("Deleting 'tipoFattura' '{}'", tipoFatturaId);
        tipoFatturaRepository.deleteById(tipoFatturaId);
        LOGGER.info("Deleted 'tipoFattura' '{}'", tipoFatturaId);
    }
}
