package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Sconto;
import com.contafood.service.ScontoService;
import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/sconti")
public class ScontoController {

    private static Logger LOGGER = LoggerFactory.getLogger(ScontoController.class);

    private final ScontoService scontoService;

    @Autowired
    public ScontoController(final ScontoService scontoService){
        this.scontoService = scontoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public List<Sconto> getAll(@RequestParam(required = false) String tipologia,
                               @RequestParam(required = false) Integer idCliente,
                               @RequestParam(required = false) Integer idFornitore,
                               @RequestParam(required = false) Date data) {

        if(!StringUtils.isEmpty(tipologia) && idCliente == null && data == null){
            LOGGER.info("Performing GET request for retrieving list of 'sconti' filtered by tipologia '{}'", tipologia);
            return scontoService.getAllByTipologia(tipologia);
        } else if(idCliente != null && data != null){
            LOGGER.info("Performing GET request for retrieving list of 'sconti' filtered by cliente '{}' and date '{}'", idCliente, data);
            return scontoService.getValidSconti(Resource.CLIENTE, Long.valueOf(idCliente), data);
        } else if(idFornitore != null && data != null) {
            LOGGER.info("Performing GET request for retrieving list of 'sconti' filtered by fornitore '{}' and date '{}'", idFornitore, data);
            return scontoService.getValidSconti(Resource.FORNITORE, Long.valueOf(idFornitore), data);
        } else {
            LOGGER.info("Performing GET request for retrieving list of 'sconti'");
            return scontoService.getAll();
        }
    }

    @RequestMapping(method = GET, path = "/{scontoId}")
    @CrossOrigin
    public Sconto getOne(@PathVariable final Long scontoId) {
        LOGGER.info("Performing GET request for retrieving 'sconto' '{}'", scontoId);
        return scontoService.getOne(scontoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public List<Sconto> create(@RequestBody final List<Sconto> sconti){
        LOGGER.info("Performing POST request for creating 'sconto'");
        return scontoService.create(sconti);
    }

    @RequestMapping(method = PUT, path = "/{scontoId}")
    @CrossOrigin
    public Sconto update(@PathVariable final Long scontoId, @RequestBody final Sconto sconto){
        LOGGER.info("Performing PUT request for updating 'sconto' '{}'", scontoId);
        if (!Objects.equals(scontoId, sconto.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return scontoService.update(sconto);
    }

    @RequestMapping(method = DELETE, path = "/{scontoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long scontoId){
        LOGGER.info("Performing DELETE request for deleting 'sconto' '{}'", scontoId);
        scontoService.delete(scontoId);
    }
}
