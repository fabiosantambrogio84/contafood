package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Autista;
import com.contafood.service.AutistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/autisti")
public class AutistaController {

    private final AutistaService autistaService;

    @Autowired
    public AutistaController(final AutistaService autistaService){
        this.autistaService = autistaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Autista> getAll() {
        return autistaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{autistaId}")
    @CrossOrigin
    public Autista getOne(@PathVariable final Long autistaId) {
        return autistaService.getOne(autistaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Autista create(@RequestBody final Autista autista){
        return autistaService.create(autista);
    }

    @RequestMapping(method = PUT, path = "/{autistaId}")
    @CrossOrigin
    public Autista update(@PathVariable final Long autistaId, @RequestBody final Autista autista){
        if (!Objects.equals(autistaId, autista.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return autistaService.update(autista);
    }

    @RequestMapping(method = DELETE, path = "/{autistaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long autistaId){
        autistaService.delete(autistaId);
    }
}
