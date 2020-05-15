package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.DdtArticolo;
import com.contafood.repository.DdtArticoloRepository;
import com.contafood.util.AccountingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class DdtArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtArticoloService.class);

    private final DdtArticoloRepository ddtArticoloRepository;

    private final ArticoloService articoloService;

    @Autowired
    public DdtArticoloService(final DdtArticoloRepository ddtArticoloRepository, final ArticoloService articoloService){
        this.ddtArticoloRepository = ddtArticoloRepository;
        this.articoloService = articoloService;
    }

    public Set<DdtArticolo> findAll(){
        LOGGER.info("Retrieving the list of 'ddt articoli'");
        Set<DdtArticolo> ddtArticoli = ddtArticoloRepository.findAll();
        LOGGER.info("Retrieved {} 'ddt articoli'", ddtArticoli.size());
        return ddtArticoli;
    }

    public DdtArticolo create(DdtArticolo ddtArticolo){
        LOGGER.info("Creating 'ddt articolo'");
        ddtArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtArticolo.setImponibile(computeImponibile(ddtArticolo));
        ddtArticolo.setCosto(computeCosto(ddtArticolo));
        ddtArticolo.setTotale(computeTotale(ddtArticolo));

        DdtArticolo createdDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Created 'ddt articolo' '{}'", createdDdtArticolo);
        return createdDdtArticolo;
    }

    public DdtArticolo update(DdtArticolo ddtArticolo){
        LOGGER.info("Updating 'ddt articolo'");
        DdtArticolo ddtArticoloCurrent = ddtArticoloRepository.findById(ddtArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtArticolo.setDataInserimento(ddtArticoloCurrent.getDataInserimento());
        ddtArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtArticolo.setImponibile(computeImponibile(ddtArticolo));
        ddtArticolo.setCosto(computeCosto(ddtArticolo));
        ddtArticolo.setTotale(computeTotale(ddtArticolo));

        DdtArticolo updatedDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Updated 'ddt articolo' '{}'", updatedDdtArticolo);
        return updatedDdtArticolo;
    }

    public void deleteByDdtId(Long ddtId){
        LOGGER.info("Deleting 'ddt articolo' by 'ddt' '{}'", ddtId);
        ddtArticoloRepository.deleteByDdtId(ddtId);
        LOGGER.info("Deleted 'ddt articolo' by 'ddt' '{}'", ddtId);
    }

    public Articolo getArticolo(DdtArticolo ddtArticolo){
        Long articoloId = ddtArticolo.getId().getArticoloId();
        return articoloService.getOne(articoloId);
    }

    private BigDecimal computeImponibile(DdtArticolo ddtArticolo){

        return AccountingUtils.computeImponibile(ddtArticolo.getQuantita(), ddtArticolo.getPrezzo(), ddtArticolo.getSconto());
    }

    private BigDecimal computeCosto(DdtArticolo ddtArticolo){

        return AccountingUtils.computeCosto(ddtArticolo.getQuantita(), ddtArticolo.getId().getArticoloId(), articoloService);
    }

    private BigDecimal computeTotale(DdtArticolo ddtArticolo){
        return AccountingUtils.computeTotale(ddtArticolo.getQuantita(), ddtArticolo.getPrezzo(), ddtArticolo.getSconto(), ddtArticolo.getId().getArticoloId(), articoloService);
    }

}
