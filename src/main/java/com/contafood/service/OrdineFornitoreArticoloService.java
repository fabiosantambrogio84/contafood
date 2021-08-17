package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineFornitoreArticolo;
import com.contafood.repository.OrdineFornitoreArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class OrdineFornitoreArticoloService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrdineFornitoreArticoloService.class);

    private final OrdineFornitoreArticoloRepository ordineClienteArticoloRepository;

    @Autowired
    public OrdineFornitoreArticoloService(final OrdineFornitoreArticoloRepository ordineClienteArticoloRepository){
        this.ordineClienteArticoloRepository = ordineClienteArticoloRepository;
    }

    public Set<OrdineFornitoreArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ordine fornitore articoli'");
        Set<OrdineFornitoreArticolo> ordineFornitoreArticoli = ordineClienteArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ordine fornitore articoli'", ordineFornitoreArticoli.size());
        return ordineFornitoreArticoli;
    }

    public OrdineFornitoreArticolo create(OrdineFornitoreArticolo ordineFornitoreArticolo){
        LOGGER.info("Creating 'ordine fornitore articolo'");
        ordineFornitoreArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        OrdineFornitoreArticolo createdOrdineFornitoreArticolo = ordineClienteArticoloRepository.save(ordineFornitoreArticolo);
        LOGGER.info("Created 'ordine fornitore articolo' '{}'", createdOrdineFornitoreArticolo);
        return createdOrdineFornitoreArticolo;
    }

    public OrdineFornitoreArticolo update(OrdineFornitoreArticolo ordineFornitoreArticolo){
        LOGGER.info("Updating 'ordine fornitore articolo'");
        OrdineFornitoreArticolo ordineFornitoreArticoloCurrent = ordineClienteArticoloRepository.findById(ordineFornitoreArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineFornitoreArticolo.setDataInserimento(ordineFornitoreArticoloCurrent.getDataInserimento());
        ordineFornitoreArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        OrdineFornitoreArticolo updatedOrdineFornitoreArticolo = ordineClienteArticoloRepository.save(ordineFornitoreArticolo);
        LOGGER.info("Updated 'ordine fornitore articolo' '{}'", updatedOrdineFornitoreArticolo);
        return updatedOrdineFornitoreArticolo;
    }

    public void deleteByOrdineFornitoreId(Long ordineFornitoreId){
        LOGGER.info("Deleting 'ordine fornitore articolo' by 'ordineFornitore' '{}'", ordineFornitoreId);
        ordineClienteArticoloRepository.deleteByOrdineFornitoreId(ordineFornitoreId);
        LOGGER.info("ordineClienteArticoloRepository 'ordine fornitore articolo' by 'ordineFornitore' '{}'", ordineFornitoreId);
    }

}
