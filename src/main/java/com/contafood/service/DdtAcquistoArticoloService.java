package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.DdtAcquistoArticolo;
import com.contafood.repository.DdtAcquistoArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class DdtAcquistoArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtAcquistoArticoloService.class);

    private final DdtAcquistoArticoloRepository ddtAcquistoArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public DdtAcquistoArticoloService(final DdtAcquistoArticoloRepository ddtAcquistoArticoloRepository, final ArticoloService articoloService){
        this.ddtAcquistoArticoloRepository = ddtAcquistoArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<DdtAcquistoArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ddt acquisto articoli'");
        Set<DdtAcquistoArticolo> ddtAcquistoArticoli = ddtAcquistoArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ddt acquisto articoli'", ddtAcquistoArticoli.size());
        return ddtAcquistoArticoli;
    }

    public DdtAcquistoArticolo create(DdtAcquistoArticolo ddtAcquistoArticolo){
        LOGGER.info("Creating 'ddt acquistoarticolo'");
        ddtAcquistoArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtAcquistoArticolo.setImponibile(computeImponibile(ddtAcquistoArticolo));

        DdtAcquistoArticolo createdDdtAcquistoArticolo = ddtAcquistoArticoloRepository.save(ddtAcquistoArticolo);
        LOGGER.info("Created 'ddt articolo' '{}'", createdDdtAcquistoArticolo);
        return createdDdtAcquistoArticolo;
    }

    public DdtAcquistoArticolo update(DdtAcquistoArticolo ddtAcquistoArticolo){
        LOGGER.info("Updating 'ddt acquisto articolo'");
        DdtAcquistoArticolo ddtAcquistoArticoloCurrent = ddtAcquistoArticoloRepository.findById(ddtAcquistoArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtAcquistoArticolo.setDataInserimento(ddtAcquistoArticoloCurrent.getDataInserimento());
        ddtAcquistoArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtAcquistoArticolo.setImponibile(computeImponibile(ddtAcquistoArticolo));

        DdtAcquistoArticolo updatedDdtAcquistoArticolo = ddtAcquistoArticoloRepository.save(ddtAcquistoArticolo);
        LOGGER.info("Updated 'ddt acquisto articolo' '{}'", updatedDdtAcquistoArticolo);
        return updatedDdtAcquistoArticolo;
    }

    public void deleteByDdtAcquistoId(Long ddtAcquistoId){
        LOGGER.info("Deleting 'ddt acquisto articolo' by 'ddt' '{}'", ddtAcquistoId);
        ddtAcquistoArticoloRepository.deleteByDdtAcquistoId(ddtAcquistoId);
        LOGGER.info("Deleted 'ddt acquisto articolo' by 'ddt' '{}'", ddtAcquistoId);
    }

    public Articolo getArticolo(DdtAcquistoArticolo ddtAcquistoArticolo){
        Long articoloId = ddtAcquistoArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(DdtAcquistoArticolo ddtAcquistoArticolo){
        BigDecimal imponibile = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = ddtAcquistoArticolo.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = ddtAcquistoArticolo.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = ddtAcquistoArticolo.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.CEILING);
        return imponibile;
    }

}
