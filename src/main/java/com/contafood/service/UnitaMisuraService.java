package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.UnitaMisura;
import com.contafood.repository.UnitaMisuraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class UnitaMisuraService {

    private static Logger LOGGER = LoggerFactory.getLogger(UnitaMisuraService.class);

    private final UnitaMisuraRepository unitaMisuraRepository;

    @Autowired
    public UnitaMisuraService(final UnitaMisuraRepository unitaMisuraRepository){
        this.unitaMisuraRepository = unitaMisuraRepository;
    }

    public Set<UnitaMisura> getAll(){
        LOGGER.info("Retrieving the list of 'unitaMisura'");
        Set<UnitaMisura> unitaMisura = unitaMisuraRepository.findAll();
        LOGGER.info("Retrieved {} 'unitaMisura'", unitaMisura.size());
        return unitaMisura;
    }

    public UnitaMisura getOne(Long unitaMisuraId){
        LOGGER.info("Retrieving 'unitaMisura' '{}'", unitaMisuraId);
        UnitaMisura unitaMisura = unitaMisuraRepository.findById(unitaMisuraId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'unitaMisura' '{}'", unitaMisura);
        return unitaMisura;
    }

    public UnitaMisura create(UnitaMisura unitaMisura){
        LOGGER.info("Creating 'unitaMisura'");
        unitaMisura.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        UnitaMisura createdUnitaMisura = unitaMisuraRepository.save(unitaMisura);
        LOGGER.info("Created 'unitaMisura' '{}'", createdUnitaMisura);
        return createdUnitaMisura;
    }

    public UnitaMisura update(UnitaMisura unitaMisura){
        LOGGER.info("Updating 'unitaMisura'");
        UnitaMisura unitaMisuraCurrent = unitaMisuraRepository.findById(unitaMisura.getId()).orElseThrow(ResourceNotFoundException::new);
        unitaMisura.setDataInserimento(unitaMisuraCurrent.getDataInserimento());
        UnitaMisura updatedUnitaMisura = unitaMisuraRepository.save(unitaMisura);
        LOGGER.info("Updated 'unitaMisura' '{}'", updatedUnitaMisura);
        return updatedUnitaMisura;
    }

    public void delete(Long unitaMisuraId){
        LOGGER.info("Deleting 'unitaMisura' '{}'", unitaMisuraId);
        unitaMisuraRepository.deleteById(unitaMisuraId);
        LOGGER.info("Deleted 'unitaMisura' '{}'", unitaMisuraId);
    }
}
