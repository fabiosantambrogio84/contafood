package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.FatturaAcquisto;
import com.contafood.service.FatturaAcquistoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping(path="/fatture-acquisto")
public class FatturaAcquistoController {

    private final FatturaAcquistoService fatturaAcquistoService;

    @Autowired
    public FatturaAcquistoController(final FatturaAcquistoService fatturaAcquistoService){
        this.fatturaAcquistoService = fatturaAcquistoService;
    }

    @RequestMapping(method = GET, path = "/{fatturaAcquistoId}")
    @CrossOrigin
    public FatturaAcquisto getOne(@PathVariable final Long fatturaAcquistoId) {
        log.info("Performing GET request for retrieving 'fattura acquisto' '{}'", fatturaAcquistoId);
        return fatturaAcquistoService.getOne(fatturaAcquistoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public FatturaAcquisto create(@RequestBody final FatturaAcquisto fatturaAcquisto){
        log.info("Performing POST request for creating 'fattura acquisto'");
        return fatturaAcquistoService.create(fatturaAcquisto);
    }

    @RequestMapping(method = PATCH, path = "/{fatturaAcquistoId}")
    @CrossOrigin
    public FatturaAcquisto patch(@PathVariable final Long fatturaAcquistoId, @RequestBody final Map<String,Object> patchFatturaAcquisto) throws Exception{
        log.info("Performing PATCH request for updating 'fattura acquisto' '{}'", fatturaAcquistoId);
        Long id = Long.valueOf((Integer) patchFatturaAcquisto.get("id"));
        if (!Objects.equals(fatturaAcquistoId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return fatturaAcquistoService.patch(patchFatturaAcquisto);
    }

    @RequestMapping(method = DELETE, path = "/{fatturaAcquistoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fatturaAcquistoId){
        log.info("Performing DELETE request for deleting 'fattura acquisto' '{}'", fatturaAcquistoId);
        fatturaAcquistoService.delete(fatturaAcquistoId);
    }

}
