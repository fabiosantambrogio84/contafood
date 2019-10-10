package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Fornitore;
import com.contafood.repository.FornitoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FornitoreService {

    private static Logger LOGGER = LoggerFactory.getLogger(FornitoreService.class);

    private final FornitoreRepository fornitoreRepository;

    @Autowired
    public FornitoreService(final FornitoreRepository fornitoreRepository){
        this.fornitoreRepository = fornitoreRepository;
    }

    public Set<Fornitore> getAll(){
        LOGGER.info("Retrieving the list of 'fornitori'");
        Set<Fornitore> fornitori = fornitoreRepository.findAll();
        LOGGER.info("Retrieved {} 'fornitori'", fornitori.size());
        return fornitori;
    }

    public Fornitore getOne(Long fornitoreId){
        LOGGER.info("Retrieving 'fornitore' '{}'", fornitoreId);
        Fornitore fornitore = fornitoreRepository.findById(fornitoreId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fornitore' '{}'", fornitore);
        return fornitore;
    }

    public Fornitore create(Fornitore fornitore){
        LOGGER.info("Creating 'fornitore'");
        Fornitore createdFornitore = fornitoreRepository.save(fornitore);
        createdFornitore.setCodice(createdFornitore.getId().intValue());
        fornitoreRepository.save(fornitore);
        LOGGER.info("Created 'fornitore' '{}'", createdFornitore);
        return createdFornitore;
    }

    public Fornitore update(Fornitore fornitore){
        LOGGER.info("Updating 'fornitore'");
        Fornitore updatedFornitore = fornitoreRepository.save(fornitore);
        LOGGER.info("Updated 'fornitore' '{}'", updatedFornitore);
        return updatedFornitore;
    }

    public void delete(Long fornitoreId){
        LOGGER.info("Deleting 'fornitore' '{}'", fornitoreId);
        fornitoreRepository.deleteById(fornitoreId);
        LOGGER.info("Deleted 'fornitore' '{}'", fornitoreId);
    }
}
