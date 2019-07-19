package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.CategoriaRicetta;
import com.contafood.repository.CategoriaRicettaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoriaRicettaService {

    private final CategoriaRicettaRepository categoriaRicettaRepository;

    @Autowired
    public CategoriaRicettaService(final CategoriaRicettaRepository categoriaRicettaRepository){
        this.categoriaRicettaRepository = categoriaRicettaRepository;
    }

    public Set<CategoriaRicetta> getAll(){
        return categoriaRicettaRepository.findAll();
    }

    public CategoriaRicetta getOne(Long categoriaRicettaId){
        return categoriaRicettaRepository.findById(categoriaRicettaId).orElseThrow(ResourceNotFoundException::new);
    }

    public CategoriaRicetta create(CategoriaRicetta categoriaRicetta){
        CategoriaRicetta createdCategoriaRicetta = categoriaRicettaRepository.save(categoriaRicetta);
        return createdCategoriaRicetta;
    }

    public CategoriaRicetta update(CategoriaRicetta categoriaRicetta){
        CategoriaRicetta updatedCategoriaRicetta = categoriaRicettaRepository.save(categoriaRicetta);
        return updatedCategoriaRicetta;
    }

    public void delete(Long categoriaRicettaId){
        categoriaRicettaRepository.deleteById(categoriaRicettaId);
    }
}
