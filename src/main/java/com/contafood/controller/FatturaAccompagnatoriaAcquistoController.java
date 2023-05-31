package com.contafood.controller;

import com.contafood.model.FatturaAccompagnatoriaAcquisto;
import com.contafood.service.FatturaAccompagnatoriaAcquistoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping(path="/fatture-accompagnatorie-acquisto")
public class FatturaAccompagnatoriaAcquistoController {

    private final FatturaAccompagnatoriaAcquistoService fatturaAccompagnatoriaAcquistoService;

    @Autowired
    public FatturaAccompagnatoriaAcquistoController(final FatturaAccompagnatoriaAcquistoService fatturaAccompagnatoriaAcquistoService){
        this.fatturaAccompagnatoriaAcquistoService = fatturaAccompagnatoriaAcquistoService;
    }

    @RequestMapping(method = GET, path = "/{fatturaAccompagnatoriaAcquistoId}")
    @CrossOrigin
    public FatturaAccompagnatoriaAcquisto getOne(@PathVariable final Long fatturaAccompagnatoriaAcquistoId) {
        log.info("Performing GET request for retrieving 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        return fatturaAccompagnatoriaAcquistoService.getOne(fatturaAccompagnatoriaAcquistoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public FatturaAccompagnatoriaAcquisto create(@RequestBody final FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto){
        log.info("Performing POST request for creating 'fattura accompagnatoria acquisto'");
        return fatturaAccompagnatoriaAcquistoService.create(fatturaAccompagnatoriaAcquisto);
    }

    @RequestMapping(method = DELETE, path = "/{fatturaAccompagnatoriaAcquistoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fatturaAccompagnatoriaAcquistoId){
        log.info("Performing DELETE request for deleting 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoService.delete(fatturaAccompagnatoriaAcquistoId);
    }

}
