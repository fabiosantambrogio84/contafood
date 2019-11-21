package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.CategoriaArticolo;
import com.contafood.repository.CategoriaArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class CategoriaArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoriaArticoloService.class);

    private final CategoriaArticoloRepository categoriaArticoloRepository;

    @Autowired
    public CategoriaArticoloService(final CategoriaArticoloRepository categoriaArticoloRepository){
        this.categoriaArticoloRepository = categoriaArticoloRepository;
    }

    public Set<CategoriaArticolo> getAll(){
        LOGGER.info("Retrieving the list of 'categorie articoli'");
        Set<CategoriaArticolo> categorieArticoli = categoriaArticoloRepository.findAllByOrderByNome();
        LOGGER.info("Retrieved {} 'categorie articoli'", categorieArticoli.size());
        return categorieArticoli;
    }

    public CategoriaArticolo getOne(Long categoriaArticoloId){
        LOGGER.info("Retrieving 'categoria articolo' '{}'", categoriaArticoloId);
        CategoriaArticolo categoriaArticolo = categoriaArticoloRepository.findById(categoriaArticoloId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'categoria articolo' '{}'", categoriaArticolo);
        return categoriaArticolo;
    }

    public CategoriaArticolo create(CategoriaArticolo categoriaArticolo){
        LOGGER.info("Creating 'categoria articolo'");
        categoriaArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        CategoriaArticolo createdCategoriaArticolo = categoriaArticoloRepository.save(categoriaArticolo);
        LOGGER.info("Created 'categoria articolo' '{}'", createdCategoriaArticolo);
        return createdCategoriaArticolo;
    }

    public CategoriaArticolo update(CategoriaArticolo categoriaArticolo){
        LOGGER.info("Updating 'categoria articolo'");
        CategoriaArticolo categoriaArticoloCurrent = categoriaArticoloRepository.findById(categoriaArticolo.getId()).orElseThrow(ResourceNotFoundException::new);
        categoriaArticolo.setDataInserimento(categoriaArticoloCurrent.getDataInserimento());
        CategoriaArticolo updatedCategoriaRicetta = categoriaArticoloRepository.save(categoriaArticolo);
        LOGGER.info("Created 'categoria articolo' '{}'", updatedCategoriaRicetta);
        return updatedCategoriaRicetta;
    }

    public void delete(Long categoriaArticoloId){
        LOGGER.info("Deleting 'categoria articolo' '{}'", categoriaArticoloId);
        categoriaArticoloRepository.deleteById(categoriaArticoloId);
        LOGGER.info("Deleted 'categoria articolo' '{}'", categoriaArticoloId);
    }
}
