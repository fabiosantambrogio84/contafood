package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.OrdineClienteArticolo;
import com.contafood.repository.OrdineClienteArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class OrdineClienteArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineClienteArticoloService.class);

    private final OrdineClienteArticoloRepository ordineClienteArticoloRepository;

    @Autowired
    public OrdineClienteArticoloService(final OrdineClienteArticoloRepository ordineClienteArticoloRepository){
        this.ordineClienteArticoloRepository = ordineClienteArticoloRepository;
    }

    public Set<OrdineClienteArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ordine cliente articoli'");
        Set<OrdineClienteArticolo> ordineClienteArticoli = ordineClienteArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ordine cliente articoli'", ordineClienteArticoli.size());
        return ordineClienteArticoli;
    }

    public OrdineClienteArticolo create(OrdineClienteArticolo ordineClienteArticolo){
        LOGGER.info("Creating 'ordine cliente articolo'");
        ordineClienteArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ordineClienteArticolo.setNumeroPezziDaEvadere(ordineClienteArticolo.getNumeroPezziOrdinati());
        OrdineClienteArticolo createdOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);
        LOGGER.info("Created 'ordine cliente articolo' '{}'", createdOrdineClienteArticolo);
        return createdOrdineClienteArticolo;
    }

    public OrdineClienteArticolo update(OrdineClienteArticolo ordineClienteArticolo){
        LOGGER.info("Updating 'ordine cliente articolo'");
        OrdineClienteArticolo ordineClienteArticoloCurrent = ordineClienteArticoloRepository.findById(ordineClienteArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ordineClienteArticolo.setDataInserimento(ordineClienteArticoloCurrent.getDataInserimento());
        ordineClienteArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ordineClienteArticolo.setNumeroPezziDaEvadere(ordineClienteArticolo.getNumeroPezziOrdinati());
        OrdineClienteArticolo updatedOrdineClienteArticolo = ordineClienteArticoloRepository.save(ordineClienteArticolo);
        LOGGER.info("Updated 'ordine cliente articolo' '{}'", updatedOrdineClienteArticolo);
        return updatedOrdineClienteArticolo;
    }

    public void deleteByOrdineClienteId(Long ordineClienteId){
        LOGGER.info("Deleting 'ordine cliente articolo' by 'ordineCliente' '{}'", ordineClienteId);
        ordineClienteArticoloRepository.deleteByOrdineClienteId(ordineClienteId);
        LOGGER.info("Deleted 'ordine cliente articolo' by 'ordineCliente' '{}'", ordineClienteId);
    }

}
