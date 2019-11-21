package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.CategoriaArticolo;
import com.contafood.service.CategoriaArticoloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/categorie-articoli")
public class CategoriaArticoloController {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoriaArticoloController.class);

    private final CategoriaArticoloService categoriaArticoloService;

    @Autowired
    public CategoriaArticoloController(final CategoriaArticoloService categoriaArticoloService){
        this.categoriaArticoloService = categoriaArticoloService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<CategoriaArticolo> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'categorie articoli'");
        return categoriaArticoloService.getAll();
    }

    @RequestMapping(method = GET, path = "/{categoriaArticoloId}")
    @CrossOrigin
    public CategoriaArticolo getOne(@PathVariable final Long categoriaArticoloId) {
        LOGGER.info("Performing GET request for retrieving 'categoria articolo' '{}'", categoriaArticoloId);
        return categoriaArticoloService.getOne(categoriaArticoloId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public CategoriaArticolo create(@RequestBody final CategoriaArticolo categoriaArticolo){
        LOGGER.info("Performing POST request for creating 'categoria articolo'");
        return categoriaArticoloService.create(categoriaArticolo);
    }

    @RequestMapping(method = PUT, path = "/{categoriaArticoloId}")
    @CrossOrigin
    public CategoriaArticolo update(@PathVariable final Long categoriaArticoloId, @RequestBody final CategoriaArticolo categoriaArticolo){
        LOGGER.info("Performing PUT request for updating 'categoria articolo' '{}'", categoriaArticoloId);
        if (!Objects.equals(categoriaArticoloId, categoriaArticolo.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return categoriaArticoloService.update(categoriaArticolo);
    }

    @RequestMapping(method = DELETE, path = "/{categoriaArticoloId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long categoriaArticoloId){
        LOGGER.info("Performing DELETE request for deleting 'categoria articolo' '{}'", categoriaArticoloId);
        categoriaArticoloService.delete(categoriaArticoloId);
    }
}
