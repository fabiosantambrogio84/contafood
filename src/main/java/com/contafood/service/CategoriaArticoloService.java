package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.CategoriaArticolo;
import com.contafood.model.Fornitore;
import com.contafood.repository.CategoriaArticoloRepository;
import com.contafood.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoriaArticoloService {

    private final CategoriaArticoloRepository categoriaArticoloRepository;

    @Autowired
    public CategoriaArticoloService(final CategoriaArticoloRepository categoriaArticoloRepository){
        this.categoriaArticoloRepository = categoriaArticoloRepository;
    }

    public Set<CategoriaArticolo> getAll(){
        return categoriaArticoloRepository.findAll();
    }

    public CategoriaArticolo getOne(Long categoriaArticoloId){
        return categoriaArticoloRepository.findById(categoriaArticoloId).orElseThrow(ResourceNotFoundException::new);
    }

    public CategoriaArticolo create(CategoriaArticolo categoriaArticolo){
        CategoriaArticolo createdCategoriaArticolo = categoriaArticoloRepository.save(categoriaArticolo);
        return createdCategoriaArticolo;
    }

    public CategoriaArticolo update(CategoriaArticolo categoriaArticolo){
        CategoriaArticolo updatedCategoriaArticolo = categoriaArticoloRepository.save(categoriaArticolo);
        return updatedCategoriaArticolo;
    }

    public void delete(Long categoriaArticoloId){
        categoriaArticoloRepository.deleteById(categoriaArticoloId);
    }
}
