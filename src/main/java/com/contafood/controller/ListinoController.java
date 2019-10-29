package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Listino;
import com.contafood.service.ListinoService;
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
@RequestMapping(path="/listini")
public class ListinoController {

    private static Logger LOGGER = LoggerFactory.getLogger(ListinoController.class);

    private final ListinoService listinoService;

    @Autowired
    public ListinoController(final ListinoService listinoService){
        this.listinoService = listinoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Listino> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'listini'");
        return listinoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{listinoId}")
    @CrossOrigin
    public Listino getOne(@PathVariable final Long listinoId) {
        LOGGER.info("Performing GET request for retrieving 'listino' '{}'", listinoId);
        return listinoService.getOne(listinoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Listino create(@RequestBody final Listino listino){
        LOGGER.info("Performing POST request for creating 'listino'");
        return listinoService.create(listino);
    }

    @RequestMapping(method = PUT, path = "/{listinoId}")
    @CrossOrigin
    public Listino update(@PathVariable final Long listinoId, @RequestBody final Listino listino){
        LOGGER.info("Performing PUT request for updating 'listino' '{}'", listinoId);
        if (!Objects.equals(listinoId, listino.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return listinoService.update(listino);
    }

    @RequestMapping(method = DELETE, path = "/{listinoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long listinoId){
        LOGGER.info("Performing DELETE request for deleting 'listino' '{}'", listinoId);
        listinoService.delete(listinoId);
    }
}
