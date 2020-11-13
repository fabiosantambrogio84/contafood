package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.StatoRicevutaPrivato;
import com.contafood.repository.StatoRicevutaPrivatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatoRicevutaPrivatoService {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoRicevutaPrivatoService.class);

    private final StatoRicevutaPrivatoRepository statoRicevutaPrivatoRepository;

    private static final String DA_PAGARE = "DA_PAGARE";
    private static final String PARZIALMENTE_PAGATA = "PARZIALMENTE_PAGATA";
    private static final String PAGATA = "PAGATA";

    @Autowired
    public StatoRicevutaPrivatoService(final StatoRicevutaPrivatoRepository statoRicevutaPrivatoRepository){
        this.statoRicevutaPrivatoRepository = statoRicevutaPrivatoRepository;
    }

    public Set<StatoRicevutaPrivato> getAll(){
        LOGGER.info("Retrieving the list of 'statiRicevutaPrivato'");
        Set<StatoRicevutaPrivato> statiRicevutaPrivato = statoRicevutaPrivatoRepository.findAllByOrderByOrdine();
        LOGGER.info("Retrieved {} 'statiRicevutaPrivato'", statiRicevutaPrivato.size());
        return statiRicevutaPrivato;
    }

    public StatoRicevutaPrivato getOne(Long statoRicevutaPrivatoId){
        LOGGER.info("Retrieving 'statoRicevutaPrivato' '{}'", statoRicevutaPrivatoId);
        StatoRicevutaPrivato statoRicevutaPrivato = statoRicevutaPrivatoRepository.findById(statoRicevutaPrivatoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoRicevutaPrivato' '{}'", statoRicevutaPrivato);
        return statoRicevutaPrivato;
    }

    public StatoRicevutaPrivato getDaPagare(){
        LOGGER.info("Retrieving 'statoRicevutaPrivato' 'DA_PAGARE'");
        StatoRicevutaPrivato statoRicevutaPrivato = statoRicevutaPrivatoRepository.findByCodice(DA_PAGARE).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoRicevutaPrivato' '{}'", statoRicevutaPrivato);
        return statoRicevutaPrivato;
    }

    public StatoRicevutaPrivato getParzialmentePagata(){
        LOGGER.info("Retrieving 'statoRicevutaPrivato' 'PARZIALMENTE_PAGATA'");
        StatoRicevutaPrivato statoRicevutaPrivato = statoRicevutaPrivatoRepository.findByCodice(PARZIALMENTE_PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoRicevutaPrivato' '{}'", statoRicevutaPrivato);
        return statoRicevutaPrivato;
    }

    public StatoRicevutaPrivato getPagata(){
        LOGGER.info("Retrieving 'statoRicevutaPrivato' 'PAGATA'");
        StatoRicevutaPrivato statoRicevutaPrivato = statoRicevutaPrivatoRepository.findByCodice(PAGATA).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'statoRicevutaPrivato' '{}'", statoRicevutaPrivato);
        return statoRicevutaPrivato;
    }

    public StatoRicevutaPrivato create(StatoRicevutaPrivato statoRicevutaPrivato){
        LOGGER.info("Creating 'statoRicevutaPrivato'");
        StatoRicevutaPrivato createdStatoRicevutaPrivato = statoRicevutaPrivatoRepository.save(statoRicevutaPrivato);
        LOGGER.info("Created 'statoRicevutaPrivato' '{}'", createdStatoRicevutaPrivato);
        return createdStatoRicevutaPrivato;
    }

    public StatoRicevutaPrivato update(StatoRicevutaPrivato statoRicevutaPrivato){
        LOGGER.info("Updating 'statoRicevutaPrivato'");
        StatoRicevutaPrivato updatedStatoRicevutaPrivato = statoRicevutaPrivatoRepository.save(statoRicevutaPrivato);
        LOGGER.info("Updated 'statoRicevutaPrivato' '{}'", updatedStatoRicevutaPrivato);
        return updatedStatoRicevutaPrivato;
    }

    public void delete(Long statoRicevutaPrivatoId){
        LOGGER.info("Deleting 'statoRicevutaPrivato' '{}'", statoRicevutaPrivatoId);
        statoRicevutaPrivatoRepository.deleteById(statoRicevutaPrivatoId);
        LOGGER.info("Deleted 'statoRicevutaPrivato' '{}'", statoRicevutaPrivatoId);
    }
}
