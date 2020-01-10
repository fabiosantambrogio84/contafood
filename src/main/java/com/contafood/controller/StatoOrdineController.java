package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoOrdine;
import com.contafood.service.StatoOrdineService;
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
@RequestMapping(path="/stati-ordine")
public class StatoOrdineController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoOrdineController.class);

    private final StatoOrdineService statoOrdineService;

    @Autowired
    public StatoOrdineController(final StatoOrdineService statoOrdineService){
        this.statoOrdineService = statoOrdineService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoOrdine> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiOrdine'");
        return statoOrdineService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoOrdineId}")
    @CrossOrigin
    public StatoOrdine getOne(@PathVariable final Long statoOrdineId) {
        LOGGER.info("Performing GET request for retrieving 'statoOrdine' '{}'", statoOrdineId);
        return statoOrdineService.getOne(statoOrdineId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoOrdine create(@RequestBody final StatoOrdine statoOrdine){
        LOGGER.info("Performing POST request for creating 'statoOrdine'");
        return statoOrdineService.create(statoOrdine);
    }

    @RequestMapping(method = PUT, path = "/{statoOrdineId}")
    @CrossOrigin
    public StatoOrdine update(@PathVariable final Long statoOrdineId, @RequestBody final StatoOrdine statoOrdine){
        LOGGER.info("Performing PUT request for updating 'tipoPagamento' '{}'", statoOrdineId);
        if (!Objects.equals(statoOrdineId, statoOrdine.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoOrdineService.update(statoOrdine);
    }

    @RequestMapping(method = DELETE, path = "/{statoOrdineId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoOrdineId){
        LOGGER.info("Performing DELETE request for deleting 'statoOrdine' '{}'", statoOrdineId);
        statoOrdineService.delete(statoOrdineId);
    }
}
