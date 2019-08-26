package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Agente;
import com.contafood.repository.AgenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgenteService {

    private static Logger LOGGER = LoggerFactory.getLogger(AgenteService.class);

    private final AgenteRepository agenteRepository;

    @Autowired
    public AgenteService(final AgenteRepository agenteRepository){
        this.agenteRepository = agenteRepository;
    }

    public Set<Agente> getAll(){
        LOGGER.info("Retrieving the list of 'agenti'");
        Set<Agente> agenti = agenteRepository.findAll();
        LOGGER.info("Retrieved {} 'agenti'", agenti.size());
        return agenti;
    }

    public Agente getOne(Long agenteId){
        LOGGER.info("Retrieving 'agente' '{}'", agenteId);
        Agente agente = agenteRepository.findById(agenteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'agente' '{}'", agente);
        return agente;
    }

    public Agente create(Agente agente){
        LOGGER.info("Creating 'agente'");
        Agente createdAgente = agenteRepository.save(agente);
        LOGGER.info("Created 'agente' '{}'", createdAgente);
        return createdAgente;
    }

    public Agente update(Agente agente){
        LOGGER.info("Updating 'agente'");
        Agente updatedAgente = agenteRepository.save(agente);
        LOGGER.info("Updated 'agente' '{}'", updatedAgente);
        return updatedAgente;
    }

    public void delete(Long agenteId){
        LOGGER.info("Deleting 'agente' '{}'", agenteId);
        agenteRepository.deleteById(agenteId);
        LOGGER.info("Deleted 'agente' '{}'", agenteId);
    }
}
