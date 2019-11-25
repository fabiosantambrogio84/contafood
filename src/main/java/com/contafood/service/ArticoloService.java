package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.ArticoloImmagine;
import com.contafood.repository.ArticoloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ArticoloService {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloService.class);

    private final ArticoloRepository articoloRepository;

    private final ArticoloImmagineService articoloImmagineService;

    @Autowired
    public ArticoloService(final ArticoloRepository articoloRepository, final ArticoloImmagineService articoloImmagineService){
        this.articoloRepository = articoloRepository;
        this.articoloImmagineService = articoloImmagineService;
    }

    public Set<Articolo> getAll(){
        LOGGER.info("Retrieving the list of 'articoli'");
        Set<Articolo> articoli = articoloRepository.findAll();
        LOGGER.info("Retrieved {} 'articoli'", articoli.size());
        return articoli;
    }

    public Articolo getOne(Long articoloId){
        LOGGER.info("Retrieving 'articolo' '{}'", articoloId);
        Articolo articolo = articoloRepository.findById(articoloId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'articolo' '{}'", articolo);
        return articolo;
    }

    public Articolo create(Articolo articolo){
        LOGGER.info("Creating 'articolo'");
        Articolo createdArticolo = articoloRepository.save(articolo);
        createdArticolo.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        articoloRepository.save(createdArticolo);
        LOGGER.info("Created 'articolo' '{}'", createdArticolo);
        return createdArticolo;
    }

    public Articolo update(Articolo articolo){
        LOGGER.info("Updating 'articolo'");
        Articolo articoloCurrent = articoloRepository.findById(articolo.getId()).orElseThrow(ResourceNotFoundException::new);
        articolo.setDataInserimento(articoloCurrent.getDataInserimento());
        Articolo updatedArticolo = articoloRepository.save(articolo);
        LOGGER.info("Updated 'articolo' '{}'", updatedArticolo);
        return updatedArticolo;
    }

    public void delete(Long articoloId){
        LOGGER.info("Deleting 'articolo' '{}'", articoloId);
        articoloRepository.deleteById(articoloId);
        LOGGER.info("Deleted 'articolo' '{}'", articoloId);
    }

    public List<ArticoloImmagine> getArticoloImmagini(Long articoloId){
        LOGGER.info("Retrieving the list of 'articoloImmagini' of the 'articolo' '{}'", articoloId);
        List<ArticoloImmagine> articoloImmagini = articoloImmagineService.getByArticoloId(articoloId);
        LOGGER.info("Retrieved {} 'articoloImmagini'", articoloImmagini.size());
        return articoloImmagini;
    }
}
