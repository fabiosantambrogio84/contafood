package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.UnitaMisura;
import com.contafood.service.UnitaMisuraService;
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
@RequestMapping(path="/unita-misura")
public class UnitaMisuraController {

    private static Logger LOGGER = LoggerFactory.getLogger(UnitaMisuraController.class);

    private final UnitaMisuraService unitaMisuraService;

    @Autowired
    public UnitaMisuraController(final UnitaMisuraService unitaMisuraService){
        this.unitaMisuraService = unitaMisuraService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<UnitaMisura> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'unitaMisura'");
        return unitaMisuraService.getAll();
    }

    @RequestMapping(method = GET, path = "/{unitaMisuraId}")
    @CrossOrigin
    public UnitaMisura getOne(@PathVariable final Long unitaMisuraId) {
        LOGGER.info("Performing GET request for retrieving 'unitaMisura' '{}'", unitaMisuraId);
        return unitaMisuraService.getOne(unitaMisuraId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public UnitaMisura create(@RequestBody final UnitaMisura unitaMisura){
        LOGGER.info("Performing POST request for creating 'unitaMisura'");
        return unitaMisuraService.create(unitaMisura);
    }

    @RequestMapping(method = PUT, path = "/{unitaMisuraId}")
    @CrossOrigin
    public UnitaMisura update(@PathVariable final Long unitaMisuraId, @RequestBody final UnitaMisura unitaMisura){
        LOGGER.info("Performing PUT request for updating 'unitaMisura' '{}'", unitaMisuraId);
        if (!Objects.equals(unitaMisuraId, unitaMisura.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return unitaMisuraService.update(unitaMisura);
    }

    @RequestMapping(method = DELETE, path = "/{unitaMisuraId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long unitaMisuraId){
        LOGGER.info("Performing DELETE request for deleting 'unitaMisura' '{}'", unitaMisuraId);
        unitaMisuraService.delete(unitaMisuraId);
    }
}
