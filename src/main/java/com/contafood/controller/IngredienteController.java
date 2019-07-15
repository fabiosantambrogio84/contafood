package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Ingrediente;
import com.contafood.service.IngredienteService;
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

    private final IngredienteService ingredienteService;

    @Autowired
    public IngredienteController(final IngredienteService ingredienteService){
        this.ingredienteService = ingredienteService;
    }

    @RequestMapping(method = GET)
    public Set<Ingrediente> getAll() {
        return ingredienteService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ingredienteId}")
    public Ingrediente getOne(@PathVariable final Long ingredienteId) {
        return ingredienteService.getOne(ingredienteId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public Ingrediente create(@RequestBody final Ingrediente ingrediente){
        return ingredienteService.create(ingrediente);
    }

    @RequestMapping(method = PUT, path = "/{ingredienteId}")
    public Ingrediente update(@PathVariable final Long ingredienteId, @RequestBody final Ingrediente ingrediente){
        if (!Objects.equals(ingredienteId, ingrediente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ingredienteService.update(ingrediente);
    }

    @RequestMapping(method = DELETE, path = "/{ingredienteId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final Long ingredienteId){
        ingredienteService.delete(ingredienteId);
    }
}
