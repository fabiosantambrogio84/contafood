package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Ricetta;
import com.contafood.service.RicettaService;
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
@RequestMapping(path="/ricette")
public class RicettaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RicettaController.class);

    private final RicettaService ricettaService;

    @Autowired
    public RicettaController(final RicettaService ricettaService){
        this.ricettaService = ricettaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Ricetta> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'ricette'");
        return ricettaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ricettaId}")
    @CrossOrigin
    public Ricetta getOne(@PathVariable final Long ricettaId) {
        LOGGER.info("Performing GET request for retrieving 'ricetta' '{}'", ricettaId);
        return ricettaService.getOne(ricettaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Ricetta create(@RequestBody final Ricetta ricetta){
        LOGGER.info("Performing POST request for creating 'ricetta'");
        return ricettaService.create(ricetta);
    }

    @RequestMapping(method = PUT, path = "/{ricettaId}")
    @CrossOrigin
    public Ricetta update(@PathVariable final Long ricettaId, @RequestBody final Ricetta ricetta){
        LOGGER.info("Performing PUT request for updating 'ricetta' '{}'", ricettaId);
        if (!Objects.equals(ricettaId, ricetta.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ricettaService.update(ricetta);
    }

    @RequestMapping(method = DELETE, path = "/{ricettaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ricettaId){
        LOGGER.info("Performing DELETE request for deleting 'ricetta' '{}'", ricettaId);
        ricettaService.delete(ricettaId);
    }
}
