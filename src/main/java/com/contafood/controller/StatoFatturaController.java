package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoFattura;
import com.contafood.service.StatoFatturaService;
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
@RequestMapping(path="/stati-fattura")
public class StatoFatturaController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoFatturaController.class);

    private final StatoFatturaService statoFatturaService;

    @Autowired
    public StatoFatturaController(final StatoFatturaService statoFatturaService){
        this.statoFatturaService = statoFatturaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoFattura> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiFattura'");
        return statoFatturaService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoFatturaId}")
    @CrossOrigin
    public StatoFattura getOne(@PathVariable final Long statoFatturaId) {
        LOGGER.info("Performing GET request for retrieving 'statoFattura' '{}'", statoFatturaId);
        return statoFatturaService.getOne(statoFatturaId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoFattura create(@RequestBody final StatoFattura statoFattura){
        LOGGER.info("Performing POST request for creating 'statoFattura'");
        return statoFatturaService.create(statoFattura);
    }

    @RequestMapping(method = PUT, path = "/{statoFatturaId}")
    @CrossOrigin
    public StatoFattura update(@PathVariable final Long statoFatturaId, @RequestBody final StatoFattura statoFattura){
        LOGGER.info("Performing PUT request for updating 'statoFattura' '{}'", statoFatturaId);
        if (!Objects.equals(statoFatturaId, statoFattura.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoFatturaService.update(statoFattura);
    }

    @RequestMapping(method = DELETE, path = "/{statoFatturaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoFatturaId){
        LOGGER.info("Performing DELETE request for deleting 'statoFattura' '{}'", statoFatturaId);
        statoFatturaService.delete(statoFatturaId);
    }
}
