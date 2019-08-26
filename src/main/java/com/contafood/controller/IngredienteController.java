package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Ingrediente;
import com.contafood.service.IngredienteService;
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
@RequestMapping(path="/ingredienti")
public class IngredienteController {

    private static Logger LOGGER = LoggerFactory.getLogger(IngredienteController.class);

    private final IngredienteService ingredienteService;

    @Autowired
    public IngredienteController(final IngredienteService ingredienteService){
        this.ingredienteService = ingredienteService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Ingrediente> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'ingredienti'");
        return ingredienteService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ingredienteId}")
    @CrossOrigin
    public Ingrediente getOne(@PathVariable final Long ingredienteId) {
        LOGGER.info("Performing GET request for retrieving 'ingrediente' '{}'", ingredienteId);
        return ingredienteService.getOne(ingredienteId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Ingrediente create(@RequestBody final Ingrediente ingrediente){
        LOGGER.info("Performing POST request for creating 'ingrediente'");
        return ingredienteService.create(ingrediente);
    }

    @RequestMapping(method = PUT, path = "/{ingredienteId}")
    @CrossOrigin
    public Ingrediente update(@PathVariable final Long ingredienteId, @RequestBody final Ingrediente ingrediente){
        LOGGER.info("Performing PUT request for updating 'ingrediente' '{}'", ingredienteId);
        if (!Objects.equals(ingredienteId, ingrediente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ingredienteService.update(ingrediente);
    }

    @RequestMapping(method = DELETE, path = "/{ingredienteId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ingredienteId){
        LOGGER.info("Performing DELETE request for deleting 'ingrediente' '{}'", ingredienteId);
        ingredienteService.delete(ingredienteId);
    }
}
