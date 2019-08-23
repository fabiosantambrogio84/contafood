package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.CategoriaRicetta;
import com.contafood.service.CategoriaRicettaService;
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
@RequestMapping(path="/categorie-ricette")
public class CategoriaRicettaController {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoriaRicettaController.class);

    private final CategoriaRicettaService categoriaRicettaService;

    @Autowired
    public CategoriaRicettaController(final CategoriaRicettaService categoriaRicettaService){
        this.categoriaRicettaService = categoriaRicettaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<CategoriaRicetta> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'categorie ricette'");
        return categoriaRicettaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{categoriaRicettaId}")
    @CrossOrigin
    public CategoriaRicetta getOne(@PathVariable final Long categoriaRicettaId) {
        LOGGER.info("Performing GET request for retrieving 'categoria ricetta' '{}'", categoriaRicettaId);
        return categoriaRicettaService.getOne(categoriaRicettaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public CategoriaRicetta create(@RequestBody final CategoriaRicetta categoriaRicetta){
        LOGGER.info("Performing POST request for creating 'categoria ricetta'");
        return categoriaRicettaService.create(categoriaRicetta);
    }

    @RequestMapping(method = PUT, path = "/{categoriaRicettaId}")
    @CrossOrigin
    public CategoriaRicetta update(@PathVariable final Long categoriaRicettaId, @RequestBody final CategoriaRicetta categoriaRicetta){
        LOGGER.info("Performing PUT request for updating 'categoria ricetta' '{}'", categoriaRicettaId);
        if (!Objects.equals(categoriaRicettaId, categoriaRicetta.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return categoriaRicettaService.update(categoriaRicetta);
    }

    @RequestMapping(method = DELETE, path = "/{categoriaRicettaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long categoriaRicettaId){
        LOGGER.info("Performing DELETE request for deleting 'categoria ricetta' '{}'", categoriaRicettaId);
        categoriaRicettaService.delete(categoriaRicettaId);
    }
}
