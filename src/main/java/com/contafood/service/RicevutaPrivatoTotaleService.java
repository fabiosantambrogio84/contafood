package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.RicevutaPrivatoTotale;
import com.contafood.repository.RicevutaPrivatoTotaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class RicevutaPrivatoTotaleService {

    private static Logger LOGGER = LoggerFactory.getLogger(RicevutaPrivatoTotaleService.class);

    private final RicevutaPrivatoTotaleRepository ricevutaPrivatoTotaleRepository;

    @Autowired
    public RicevutaPrivatoTotaleService(final RicevutaPrivatoTotaleRepository ricevutaPrivatoTotaleRepository){
        this.ricevutaPrivatoTotaleRepository = ricevutaPrivatoTotaleRepository;
    }

    public Set<RicevutaPrivatoTotale> findAll(){
        LOGGER.info("Retrieving the list of 'ricevuta privato totali'");
        Set<RicevutaPrivatoTotale> ricevutaPrivatoTotali = ricevutaPrivatoTotaleRepository.findAll();
        LOGGER.info("Retrieved {} 'ricevuta privato totali'", ricevutaPrivatoTotali.size());
        return ricevutaPrivatoTotali;
    }

    public RicevutaPrivatoTotale create(RicevutaPrivatoTotale ricevutaPrivatoTotale){
        LOGGER.info("Creating 'ricevuta privato totale'");
        ricevutaPrivatoTotale.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        RicevutaPrivatoTotale createdRicevutaPrivatoTotale = ricevutaPrivatoTotaleRepository.save(ricevutaPrivatoTotale);
        LOGGER.info("Created 'ricevuta privato totale' '{}'", createdRicevutaPrivatoTotale);
        return createdRicevutaPrivatoTotale;
    }

    public RicevutaPrivatoTotale update(RicevutaPrivatoTotale ricevutaPrivatoTotale){
        LOGGER.info("Updating 'ricevuta privato totale'");
        RicevutaPrivatoTotale ricevutaPrivatoTotaleCurrent = ricevutaPrivatoTotaleRepository.findById(ricevutaPrivatoTotale.getId()).orElseThrow(ResourceNotFoundException::new);
        ricevutaPrivatoTotale.setDataInserimento(ricevutaPrivatoTotaleCurrent.getDataInserimento());
        ricevutaPrivatoTotale.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        RicevutaPrivatoTotale updatedRicevutaPrivatoTotale = ricevutaPrivatoTotaleRepository.save(ricevutaPrivatoTotale);
        LOGGER.info("Updated 'ricevuta privato totale' '{}'", updatedRicevutaPrivatoTotale);
        return updatedRicevutaPrivatoTotale;
    }

    public void deleteByRicevutaPrivatoId(Long ricevutaPrivatoId){
        LOGGER.info("Deleting 'ricevuta privato totali' by 'ricevuta privato' '{}'", ricevutaPrivatoId);
        ricevutaPrivatoTotaleRepository.deleteByRicevutaPrivatoId(ricevutaPrivatoId);
        LOGGER.info("Deleted 'ricevuta privato totali' by 'ricevuta privato' '{}'", ricevutaPrivatoId);
    }

}
