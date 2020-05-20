package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.StatoNotaReso;
import com.contafood.service.StatoNotaResoService;
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
@RequestMapping(path="/stati-note-reso")
public class StatoNotaResoController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatoNotaResoController.class);

    private final StatoNotaResoService statoNotaResoService;

    @Autowired
    public StatoNotaResoController(final StatoNotaResoService statoNotaResoService){
        this.statoNotaResoService = statoNotaResoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<StatoNotaReso> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'statiNoteReso'");
        return statoNotaResoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{statoNotaResoId}")
    @CrossOrigin
    public StatoNotaReso getOne(@PathVariable final Long statoNotaResoId) {
        LOGGER.info("Performing GET request for retrieving 'statoNotaResoId' '{}'", statoNotaResoId);
        return statoNotaResoService.getOne(statoNotaResoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public StatoNotaReso create(@RequestBody final StatoNotaReso statoNotaReso){
        LOGGER.info("Performing POST request for creating 'statoNotaReso'");
        return statoNotaResoService.create(statoNotaReso);
    }

    @RequestMapping(method = PUT, path = "/{statoNotaResoId}")
    @CrossOrigin
    public StatoNotaReso update(@PathVariable final Long statoNotaResoId, @RequestBody final StatoNotaReso statoNotaReso){
        LOGGER.info("Performing PUT request for updating 'statoNotaReso' '{}'", statoNotaResoId);
        if (!Objects.equals(statoNotaResoId, statoNotaReso.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return statoNotaResoService.update(statoNotaReso);
    }

    @RequestMapping(method = DELETE, path = "/{statoNotaResoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long statoNotaResoId){
        LOGGER.info("Performing DELETE request for deleting 'statoNotaReso' '{}'", statoNotaResoId);
        statoNotaResoService.delete(statoNotaResoId);
    }
}
