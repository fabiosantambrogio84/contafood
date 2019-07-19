package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.CategoriaRicetta;
import com.contafood.service.CategoriaRicettaService;
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

    private final CategoriaRicettaService categoriaRicettaService;

    @Autowired
    public CategoriaArticoloController(final CategoriaRicettaService categoriaRicettaService){
        this.categoriaRicettaService = categoriaRicettaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<CategoriaRicetta> getAll() {
        return categoriaRicettaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{categoriaArticoloId}")
    @CrossOrigin
    public CategoriaRicetta getOne(@PathVariable final Long categoriaArticoloId) {
        return categoriaRicettaService.getOne(categoriaArticoloId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public CategoriaRicetta create(@RequestBody final CategoriaRicetta categoriaRicetta){
        return categoriaRicettaService.create(categoriaRicetta);
    }

    @RequestMapping(method = PUT, path = "/{categoriaArticoloId}")
    @CrossOrigin
    public CategoriaRicetta update(@PathVariable final Long categoriaArticoloId, @RequestBody final CategoriaRicetta categoriaRicetta){
        if (!Objects.equals(categoriaArticoloId, categoriaRicetta.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return categoriaRicettaService.update(categoriaRicetta);
    }

    @RequestMapping(method = DELETE, path = "/{categoriaArticoloId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long categoriaArticoloId){
        categoriaRicettaService.delete(categoriaArticoloId);
    }
}
