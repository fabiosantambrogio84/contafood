package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Autista;
import com.contafood.repository.AutistaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AutistaService {

    private static Logger LOGGER = LoggerFactory.getLogger(AutistaService.class);

    private final AutistaRepository autistaRepository;

    @Autowired
    public AutistaService(final AutistaRepository autistaRepository){
        this.autistaRepository = autistaRepository;
    }

    public Set<Autista> getAll(){
        LOGGER.info("Retrieving the list of 'autisti'");
        Set<Autista> autisti = autistaRepository.findAll();
        LOGGER.info("Retrieved {} 'autisti'", autisti.size());
        return autisti;
    }

    public Autista getOne(Long autistaId){
        LOGGER.info("Retrieving 'autista' '{}'", autistaId);
        Autista autista = autistaRepository.findById(autistaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'autista' '{}'", autista);
        return autista;
    }

    public Autista create(Autista autista){
        LOGGER.info("Creating 'autista'");
        Autista createdAutista = autistaRepository.save(autista);
        LOGGER.info("Created 'autista' '{}'", createdAutista);
        return createdAutista;
    }

    public Autista update(Autista autista){
        LOGGER.info("Updating 'autista'");
        Autista updatedAutista = autistaRepository.save(autista);
        LOGGER.info("Updated 'autista' '{}'", updatedAutista);
        return updatedAutista;
    }

    public void delete(Long autistaId){
        LOGGER.info("Deleting 'autista' '{}'", autistaId);
        autistaRepository.deleteById(autistaId);
        LOGGER.info("Deleted 'autista' '{}'", autistaId);
    }
}
