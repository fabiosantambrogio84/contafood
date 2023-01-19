package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Agente;
import com.contafood.repository.AgenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class AgenteService {

    private final AgenteRepository agenteRepository;

    @Autowired
    public AgenteService(final AgenteRepository agenteRepository){
        this.agenteRepository = agenteRepository;
    }

    public Set<Agente> getAll(){
        log.info("Retrieving the list of 'agenti'");
        Set<Agente> agenti = agenteRepository.findAll();
        log.info("Retrieved {} 'agenti'", agenti.size());
        return agenti;
    }

    public Agente getOne(Long agenteId){
        log.info("Retrieving 'agente' '{}'", agenteId);
        Agente agente = agenteRepository.findById(agenteId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'agente' '{}'", agente);
        return agente;
    }

    public Agente create(Agente agente){
        log.info("Creating 'agente'");
        Agente createdAgente = agenteRepository.save(agente);
        log.info("Created 'agente' '{}'", createdAgente);
        return createdAgente;
    }

    public Agente update(Agente agente){
        log.info("Updating 'agente'");
        Agente updatedAgente = agenteRepository.save(agente);
        log.info("Updated 'agente' '{}'", updatedAgente);
        return updatedAgente;
    }

    public void delete(Long agenteId){
        log.info("Deleting 'agente' '{}'", agenteId);
        agenteRepository.deleteById(agenteId);
        log.info("Deleted 'agente' '{}'", agenteId);
    }
}
