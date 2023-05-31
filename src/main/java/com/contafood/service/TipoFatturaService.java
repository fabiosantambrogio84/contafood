package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.TipoFattura;
import com.contafood.repository.TipoFatturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class TipoFatturaService {

    private final TipoFatturaRepository tipoFatturaRepository;

    private static final String VENDITA = "VENDITA";
    private static final String ACCOMPAGNATORIA = "ACCOMPAGNATORIA";
    private static final String ACCOMPAGNATORIA_ACQUISTO = "ACCOMPAGNATORIA_ACQUISTO";

    @Autowired
    public TipoFatturaService(final TipoFatturaRepository tipoFatturaRepository){
        this.tipoFatturaRepository = tipoFatturaRepository;
    }

    public Set<TipoFattura> getAll(){
        log.info("Retrieving the list of 'tipiFattura'");
        Set<TipoFattura> TipoFattura = tipoFatturaRepository.findAllByOrderByOrdine();
        log.info("Retrieved {} 'TipoFattura'", TipoFattura.size());
        return TipoFattura;
    }

    public TipoFattura getOne(Long tipoFatturaId){
        log.info("Retrieving 'tipoFattura' '{}'", tipoFatturaId);
        TipoFattura tipoFattura = tipoFatturaRepository.findById(tipoFatturaId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura getVendita(){
        log.info("Retrieving 'tipoFattura' 'VENDITA'");
        TipoFattura tipoFattura = tipoFatturaRepository.findByCodice(VENDITA).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura getAccompagnatoria(){
        log.info("Retrieving 'tipoFattura' 'ACCOMPAGNATORIA'");
        TipoFattura tipoFattura = tipoFatturaRepository.findByCodice(ACCOMPAGNATORIA).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura getAccompagnatoriaAcquisto(){
        log.info("Retrieving 'tipoFattura' 'ACCOMPAGNATORIA_ACQUISTO'");
        TipoFattura tipoFattura = tipoFatturaRepository.findByCodice(ACCOMPAGNATORIA_ACQUISTO).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'tipoFattura' '{}'", tipoFattura);
        return tipoFattura;
    }

    public TipoFattura create(TipoFattura tipoFattura){
        log.info("Creating 'tipoFattura'");
        TipoFattura createdTipoFattura = tipoFatturaRepository.save(tipoFattura);
        log.info("Created 'tipoFattura' '{}'", createdTipoFattura);
        return createdTipoFattura;
    }

    public TipoFattura update(TipoFattura tipoFattura){
        log.info("Updating 'tipoFattura'");
        TipoFattura updatedTipoFattura = tipoFatturaRepository.save(tipoFattura);
        log.info("Updated 'tipoFattura' '{}'", updatedTipoFattura);
        return updatedTipoFattura;
    }

    public void delete(Long tipoFatturaId){
        log.info("Deleting 'tipoFattura' '{}'", tipoFatturaId);
        tipoFatturaRepository.deleteById(tipoFatturaId);
        log.info("Deleted 'tipoFattura' '{}'", tipoFatturaId);
    }
}
