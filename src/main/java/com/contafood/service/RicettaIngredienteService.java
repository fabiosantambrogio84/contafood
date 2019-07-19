package com.contafood.service;

import com.contafood.model.RicettaIngrediente;
import com.contafood.repository.RicettaIngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RicettaIngredienteService {

    private final RicettaIngredienteRepository ricettaIngredienteRepository;

    @Autowired
    public RicettaIngredienteService(final RicettaIngredienteRepository ricettaIngredienteRepository){
        this.ricettaIngredienteRepository = ricettaIngredienteRepository;
    }

    public Set<RicettaIngrediente> findAll(){
        return ricettaIngredienteRepository.findAll();
    }

    public Set<RicettaIngrediente> findByRicettaId(Long ricettaId){
        return ricettaIngredienteRepository.findByRicettaId(ricettaId);
    }

    public Set<RicettaIngrediente> findByIngredienteId(Long ingredienteId){
        return ricettaIngredienteRepository.findByIngredienteId(ingredienteId);
    }

    public RicettaIngrediente create(RicettaIngrediente ricettaIngrediente){
        RicettaIngrediente createdRicettaIngrediente = ricettaIngredienteRepository.save(ricettaIngrediente);
        return createdRicettaIngrediente;
    }

    public RicettaIngrediente update(RicettaIngrediente ricettaIngrediente){
        RicettaIngrediente updatedRicettaIngrediente = ricettaIngredienteRepository.save(ricettaIngrediente);
        return updatedRicettaIngrediente;
    }

    public void deleteByRicettaId(Long ricettaId){
        ricettaIngredienteRepository.deleteByRicettaId(ricettaId);
    }

    public void deleteByIngredienteId(Long ingredienteId){
        ricettaIngredienteRepository.deleteByIngredienteId(ingredienteId);
    }
}
