package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ingrediente;
import com.contafood.repository.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    @Autowired
    public IngredienteService(final IngredienteRepository ingredienteRepository){
        this.ingredienteRepository = ingredienteRepository;
    }

    public Set<Ingrediente> getAll(){
        return ingredienteRepository.findAll();
    }

    public Ingrediente getOne(Long ingredienteId){
        return ingredienteRepository.findById(ingredienteId).orElseThrow(ResourceNotFoundException::new);
    }

    public Ingrediente create(Ingrediente ingrediente){
        Ingrediente createdIngrediente = ingredienteRepository.save(ingrediente);
        return createdIngrediente;
    }

    public Ingrediente update(Ingrediente ingrediente){
        Ingrediente updatedIngrediente = ingredienteRepository.save(ingrediente);
        return updatedIngrediente;
    }

    public void delete(Long ingredienteId){
        ingredienteRepository.deleteById(ingredienteId);
    }
}
