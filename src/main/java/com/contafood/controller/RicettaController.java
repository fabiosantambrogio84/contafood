package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Ingrediente;
import com.contafood.model.Ricetta;
import com.contafood.resource.RicettaResource;
import com.contafood.service.IngredienteService;
import com.contafood.service.RicettaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/ricette")
public class RicettaController {

    private final RicettaService ricettaService;

    @Autowired
    public RicettaController(final RicettaService ricettaService){
        this.ricettaService = ricettaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Ricetta> getAll() {
        return ricettaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ricettaId}")
    @CrossOrigin
    public RicettaResource getOne(@PathVariable final Long ricettaId) {
        return ricettaService.getOne(ricettaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Ricetta create(@RequestBody final Ricetta ricetta){
        return ricettaService.create(ricetta);
    }

    @RequestMapping(method = PUT, path = "/{ricettaId}")
    @CrossOrigin
    public Ricetta update(@PathVariable final Long ricettaId, @RequestBody final Ricetta ricetta){
        if (!Objects.equals(ricettaId, ricetta.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ricettaService.update(ricetta);
    }

    @RequestMapping(method = DELETE, path = "/{ricettaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ricettaId){
        ricettaService.delete(ricettaId);
    }
}
