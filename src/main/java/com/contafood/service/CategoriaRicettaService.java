package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.CategoriaRicetta;
import com.contafood.repository.CategoriaRicettaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoriaRicettaService {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoriaRicettaService.class);

    private final CategoriaRicettaRepository categoriaRicettaRepository;

    @Autowired
    public CategoriaRicettaService(final CategoriaRicettaRepository categoriaRicettaRepository){
        this.categoriaRicettaRepository = categoriaRicettaRepository;
    }

    public Set<CategoriaRicetta> getAll(){
        LOGGER.info("Retrieving the list of 'categorie ricette'");
        Set<CategoriaRicetta> categorieRicette = categoriaRicettaRepository.findAllOrderByNome();
        LOGGER.info("Retrieved {} 'categorie ricette'", categorieRicette.size());
        return categorieRicette;
    }

    public CategoriaRicetta getOne(Long categoriaRicettaId){
        LOGGER.info("Retrieving 'categoria ricetta' '{}'", categoriaRicettaId);
        CategoriaRicetta categoriaRicetta = categoriaRicettaRepository.findById(categoriaRicettaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'categoria ricetta' '{}'", categoriaRicetta);
        return categoriaRicetta;
    }

    public CategoriaRicetta create(CategoriaRicetta categoriaRicetta){
        LOGGER.info("Creating 'categoria ricetta'");
        CategoriaRicetta createdCategoriaRicetta = categoriaRicettaRepository.save(categoriaRicetta);
        LOGGER.info("Created 'categoria ricetta' '{}'", createdCategoriaRicetta);
        return createdCategoriaRicetta;
    }

    public CategoriaRicetta update(CategoriaRicetta categoriaRicetta){
        LOGGER.info("Updating 'categoria ricetta'");
        CategoriaRicetta updatedCategoriaRicetta = categoriaRicettaRepository.save(categoriaRicetta);
        LOGGER.info("Created 'categoria ricetta' '{}'", updatedCategoriaRicetta);
        return updatedCategoriaRicetta;
    }

    public void delete(Long categoriaRicettaId){
        LOGGER.info("Deleting 'categoria ricetta' '{}'", categoriaRicettaId);
        categoriaRicettaRepository.deleteById(categoriaRicettaId);
        LOGGER.info("Deleted 'categoria ricetta' '{}'", categoriaRicettaId);
    }
}
