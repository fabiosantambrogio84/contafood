package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.CategoriaArticolo;
import com.contafood.model.Fornitore;
import com.contafood.service.CategoriaArticoloService;
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

    private final CategoriaArticoloService categoriaArticoloService;

    @Autowired
    public CategoriaArticoloController(final CategoriaArticoloService categoriaArticoloService){
        this.categoriaArticoloService = categoriaArticoloService;
    }

    @RequestMapping(method = GET)
    public Set<CategoriaArticolo> getAll() {
        return categoriaArticoloService.getAll();
    }

    @RequestMapping(method = GET, path = "/{categoriaArticoloId}")
    public CategoriaArticolo getOne(@PathVariable final Long categoriaArticoloId) {
        return categoriaArticoloService.getOne(categoriaArticoloId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public CategoriaArticolo create(@RequestBody final CategoriaArticolo categoriaArticolo){
        return categoriaArticoloService.create(categoriaArticolo);
    }

    @RequestMapping(method = PUT, path = "/{categoriaArticoloId}")
    public CategoriaArticolo update(@PathVariable final Long categoriaArticoloId, @RequestBody final CategoriaArticolo categoriaArticolo){
        if (!Objects.equals(categoriaArticoloId, categoriaArticolo.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return categoriaArticoloService.update(categoriaArticolo);
    }

    @RequestMapping(method = DELETE, path = "/{categoriaArticoloId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final Long categoriaArticoloId){
        categoriaArticoloService.delete(categoriaArticoloId);
    }
}
