package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.service.ArticoloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/articoli")
public class ArticoloController {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticoloController.class);

    private final ArticoloService articoloService;

    @Autowired
    public ArticoloController(final ArticoloService articoloService){
        this.articoloService = articoloService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Articolo> getAll(@RequestParam(name = "attivo", required = false) Boolean active) {
        LOGGER.info("Performing GET request for retrieving list of 'articoli'");
        LOGGER.info("Query parameter 'attivo' equal to '{}'", active);
        if(!active.equals(null)){
            return articoloService.getAllByAttivo(active);
        } else {
            return articoloService.getAll();
        }
    }

    @RequestMapping(method = GET, path = "/{articoloId}")
    @CrossOrigin
    public Articolo getOne(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'articolo' '{}'", articoloId);
        return articoloService.getOne(articoloId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Articolo create(@RequestBody final Articolo articolo){
        LOGGER.info("Performing POST request for creating 'articolo'");
        return articoloService.create(articolo);
    }

    @RequestMapping(method = PUT, path = "/{articoloId}")
    @CrossOrigin
    public Articolo update(@PathVariable final Long articoloId, @RequestBody final Articolo articolo){
        LOGGER.info("Performing PUT request for updating 'articolo' '{}'", articoloId);
        if (!Objects.equals(articoloId, articolo.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return articoloService.update(articolo);
    }

    @RequestMapping(method = DELETE, path = "/{articoloId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long articoloId){
        LOGGER.info("Performing DELETE request for deleting 'articolo' '{}'", articoloId);
        articoloService.delete(articoloId);
    }

    @RequestMapping(method = GET, path = "/{articoloId}/immagini")
    @CrossOrigin
    public List<ArticoloImmagine> getArticoloImmagini(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'articoloImmagini' of 'articolo' '{}'", articoloId);
        return articoloService.getArticoloImmagini(articoloId);
    }

    @RequestMapping(method = GET, path = "/{articoloId}/listini-prezzi")
    @CrossOrigin
    public List<ListinoPrezzo> getArticoloListiniPrezzi(@PathVariable final Long articoloId) {
        LOGGER.info("Performing GET request for retrieving 'listiniPrezzi' of 'articolo' '{}'", articoloId);
        return articoloService.getArticoloListiniPrezzi(articoloId);
    }
}
