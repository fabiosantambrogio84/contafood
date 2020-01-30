package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.DdtArticolo;
import com.contafood.repository.DdtArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class DdtArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtArticoloService.class);

    private final DdtArticoloRepository ddtArticoloRepository;

    @Autowired
    public DdtArticoloService(final DdtArticoloRepository ddtArticoloRepository){
        this.ddtArticoloRepository = ddtArticoloRepository;
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
        DdtArticolo createdDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Created 'ddt articolo' '{}'", createdDdtArticolo);
        return createdDdtArticolo;
    }

    public DdtArticolo update(DdtArticolo ddtArticolo){
        LOGGER.info("Updating 'ddt articolo'");
        DdtArticolo ddtArticoloCurrent = ddtArticoloRepository.findById(ddtArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtArticolo.setDataInserimento(ddtArticoloCurrent.getDataInserimento());
        ddtArticolo.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        DdtArticolo updatedDdtArticolo = ddtArticoloRepository.save(ddtArticolo);
        LOGGER.info("Updated 'ddt articolo' '{}'", updatedDdtArticolo);
        return updatedDdtArticolo;
    }

    public void deleteByDdtId(Long ddtId){
        LOGGER.info("Deleting 'ddt articolo' by 'ddt' '{}'", ddtId);
        ddtArticoloRepository.deleteByDdtId(ddtId);
        LOGGER.info("Deleted 'ddt articolo' by 'ddt' '{}'", ddtId);
    }

}
