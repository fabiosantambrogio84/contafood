package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.OrdineFornitore;
import com.contafood.service.OrdineFornitoreService;
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
@RequestMapping(path="/ordini-fornitori")
public class OrdineFornitoreController {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdineFornitoreController.class);

    private final OrdineFornitoreService ordineFornitoreService;

    @Autowired
    public OrdineFornitoreController(final OrdineFornitoreService ordineFornitoreService){
        this.ordineFornitoreService = ordineFornitoreService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<OrdineFornitore> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'ordini-fornitori'");
        return ordineFornitoreService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ordineFornitoreId}")
    @CrossOrigin
    public OrdineFornitore getOne(@PathVariable final Long ordineFornitoreId) {
        LOGGER.info("Performing GET request for retrieving 'ordineFornitore' '{}'", ordineFornitoreId);
        return ordineFornitoreService.getOne(ordineFornitoreId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public OrdineFornitore create(@RequestBody final OrdineFornitore ordineFornitore){
        LOGGER.info("Performing POST request for creating 'ordineFornitore'");
        return ordineFornitoreService.create(ordineFornitore);
    }

    @RequestMapping(method = PUT, path = "/{ordineFornitoreId}")
    @CrossOrigin
    public OrdineFornitore update(@PathVariable final Long ordineFornitoreId, @RequestBody final OrdineFornitore ordineFornitore){
        LOGGER.info("Performing PUT request for updating 'ordineFornitore' '{}'", ordineFornitoreId);
        if (!Objects.equals(ordineFornitoreId, ordineFornitore.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ordineFornitoreService.update(ordineFornitore);
    }

    @RequestMapping(method = DELETE, path = "/{ordineFornitoreId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ordineFornitoreId){
        LOGGER.info("Performing DELETE request for deleting 'ordineFornitore' '{}'", ordineFornitoreId);
        ordineFornitoreService.delete(ordineFornitoreId);
    }
}
