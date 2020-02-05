package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoDdt;
import com.contafood.service.StatoDdtService;
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
@RequestMapping(path="/stati-ddt")
public class StatoDdtController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoDdtController.class);

    private final StatoDdtService statoDdtService;

    @Autowired
    public StatoDdtController(final StatoDdtService statoDdtService){
        this.statoDdtService = statoDdtService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoDdt> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiDdt'");
        return statoDdtService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoDdtId}")
    @CrossOrigin
    public StatoDdt getOne(@PathVariable final Long statoDdtId) {
        LOGGER.info("Performing GET request for retrieving 'statoDdt' '{}'", statoDdtId);
        return statoDdtService.getOne(statoDdtId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoDdt create(@RequestBody final StatoDdt statoDdt){
        LOGGER.info("Performing POST request for creating 'statoDdt'");
        return statoDdtService.create(statoDdt);
    }

    @RequestMapping(method = PUT, path = "/{statoDdtId}")
    @CrossOrigin
    public StatoDdt update(@PathVariable final Long statoDdtId, @RequestBody final StatoDdt statoDdt){
        LOGGER.info("Performing PUT request for updating 'statoDdt' '{}'", statoDdtId);
        if (!Objects.equals(statoDdtId, statoDdt.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoDdtService.update(statoDdt);
    }

    @RequestMapping(method = DELETE, path = "/{statoDdtId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoDdtId){
        LOGGER.info("Performing DELETE request for deleting 'statoDdt' '{}'", statoDdtId);
        statoDdtService.delete(statoDdtId);
    }
}
