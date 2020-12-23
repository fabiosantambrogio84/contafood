package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Causale;
import com.contafood.service.CausaleService;
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
@RequestMapping(path="/causali")
public class CausaleController {

    private static Logger LOGGER = LoggerFactory.getLogger(CausaleController.class);

    private final CausaleService causaleService;

    @Autowired
    public CausaleController(final CausaleService causaleService){
        this.causaleService = causaleService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Causale> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'causali'");
        return causaleService.getAll();
    }

    @RequestMapping(method = GET, path = "/{causaleId}")
    @CrossOrigin
    public Causale getOne(@PathVariable final Long causaleId) {
        LOGGER.info("Performing GET request for retrieving 'causale' '{}'", causaleId);
        return causaleService.getOne(causaleId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Causale create(@RequestBody final Causale causale){
        LOGGER.info("Performing POST request for creating 'causale'");
        return causaleService.create(causale);
    }

    @RequestMapping(method = PUT, path = "/{causaleId}")
    @CrossOrigin
    public Causale update(@PathVariable final Long causaleId, @RequestBody final Causale causale){
        LOGGER.info("Performing PUT request for updating 'causale' '{}'", causaleId);
        if (!Objects.equals(causaleId, causale.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return causaleService.update(causale);
    }

    @RequestMapping(method = DELETE, path = "/{causaleId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long causaleId){
        LOGGER.info("Performing DELETE request for deleting 'causale' '{}'", causaleId);
        causaleService.delete(causaleId);
    }
}
