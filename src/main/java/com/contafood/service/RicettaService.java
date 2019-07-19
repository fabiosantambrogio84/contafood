package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ricetta;
import com.contafood.repository.RicettaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RicettaService {

    private final RicettaRepository ricettaRepository;

    @Autowired
    public RicettaService(final RicettaRepository ricettaRepository){
        this.ricettaRepository = ricettaRepository;
    }

    public Set<Ricetta> getAll(){
        return ricettaRepository.findAll();
    }

    public Ricetta getOne(Long ricettaId){
        return ricettaRepository.findById(ricettaId).orElseThrow(ResourceNotFoundException::new);
    }

    public Ricetta create(Ricetta ricetta){
        Ricetta createdRicetta = ricettaRepository.save(ricetta);
        return createdRicetta;
    }

    public Ricetta update(Ricetta ricetta){
        Ricetta updatedRicetta = ricettaRepository.save(ricetta);
        return updatedRicetta;
    }

    public void delete(Long ricettaId){
        ricettaRepository.deleteById(ricettaId);
    }
}
