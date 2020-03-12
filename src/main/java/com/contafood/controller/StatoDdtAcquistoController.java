package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoDdtAcquisto;
import com.contafood.service.StatoDdtAcquistoService;
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
@RequestMapping(path="/stati-ddt-acquisto")
public class StatoDdtAcquistoController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoDdtAcquistoController.class);

    private final StatoDdtAcquistoService statoDdtAcquistoService;

    @Autowired
    public StatoDdtAcquistoController(final StatoDdtAcquistoService statoDdtAcquistoService){
        this.statoDdtAcquistoService = statoDdtAcquistoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoDdtAcquisto> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiDdtAcquisto'");
        return statoDdtAcquistoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoDdtAcquistoId}")
    @CrossOrigin
    public StatoDdtAcquisto getOne(@PathVariable final Long statoDdtAcquistoId) {
        LOGGER.info("Performing GET request for retrieving 'statoDdtAcquistoId' '{}'", statoDdtAcquistoId);
        return statoDdtAcquistoService.getOne(statoDdtAcquistoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoDdtAcquisto create(@RequestBody final StatoDdtAcquisto statoDdtAcquisto){
        LOGGER.info("Performing POST request for creating 'statoDdtAcquisto'");
        return statoDdtAcquistoService.create(statoDdtAcquisto);
    }

    @RequestMapping(method = PUT, path = "/{statoDdtAcquistoId}")
    @CrossOrigin
    public StatoDdtAcquisto update(@PathVariable final Long statoDdtAcquistoId, @RequestBody final StatoDdtAcquisto statoDdtAcquisto){
        LOGGER.info("Performing PUT request for updating 'statoDdtAcquisto' '{}'", statoDdtAcquistoId);
        if (!Objects.equals(statoDdtAcquistoId, statoDdtAcquisto.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoDdtAcquistoService.update(statoDdtAcquisto);
    }

    @RequestMapping(method = DELETE, path = "/{statoDdtAcquistoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoDdtAcquistoId){
        LOGGER.info("Performing DELETE request for deleting 'statoDdtAcquistoId' '{}'", statoDdtAcquistoId);
        statoDdtAcquistoService.delete(statoDdtAcquistoId);
    }
}
