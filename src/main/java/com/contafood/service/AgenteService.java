package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Agente;
import com.contafood.repository.AgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgenteService {

    private final AgenteRepository agenteRepository;

    @Autowired
    public AgenteService(final AgenteRepository agenteRepository){
        this.agenteRepository = agenteRepository;
    }

    public Set<Agente> getAll(){
        return agenteRepository.findAll();
    }

    public Agente getOne(Long agenteId){
        return agenteRepository.findById(agenteId).orElseThrow(ResourceNotFoundException::new);
    }

    public Agente create(Agente agente){
        Agente createdAgente = agenteRepository.save(agente);
        return createdAgente;
    }

    public Agente update(Agente agente){
        Agente updatedAgente = agenteRepository.save(agente);
        return updatedAgente;
    }

    public void delete(Long agenteId){
        agenteRepository.deleteById(agenteId);
    }
}
