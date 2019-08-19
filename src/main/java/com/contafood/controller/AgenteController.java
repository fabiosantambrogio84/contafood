package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Agente;
import com.contafood.service.AgenteService;
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
@RequestMapping(path="/agenti")
public class AgenteController {

    private static Logger LOGGER = LoggerFactory.getLogger(AgenteController.class);

    private final AgenteService agenteService;

    @Autowired
    public AgenteController(final AgenteService agenteService){
        this.agenteService = agenteService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Agente> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'agenti'");
        return agenteService.getAll();
    }

    @RequestMapping(method = GET, path = "/{agenteId}")
    @CrossOrigin
    public Agente getOne(@PathVariable final Long agenteId) {
        LOGGER.info("Performing GET request for retrieving 'agente' '{}'", agenteId);
        return agenteService.getOne(agenteId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Agente create(@RequestBody final Agente agente){
        LOGGER.info("Performing POST request for creating 'agente'");
        return agenteService.create(agente);
    }

    @RequestMapping(method = PUT, path = "/{agenteId}")
    @CrossOrigin
    public Agente update(@PathVariable final Long agenteId, @RequestBody final Agente agente){
        LOGGER.info("Performing PUT request for updating 'agente' '{}'", agenteId);
        if (!Objects.equals(agenteId, agente.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return agenteService.update(agente);
    }

    @RequestMapping(method = DELETE, path = "/{agenteId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long agenteId){
        LOGGER.info("Performing DELETE request for deleting 'agente' '{}'", agenteId);
        agenteService.delete(agenteId);
    }
}
