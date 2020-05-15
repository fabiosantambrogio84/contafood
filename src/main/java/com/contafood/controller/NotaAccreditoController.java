package com.contafood.controller;

import com.contafood.model.NotaAccredito;
import com.contafood.service.NotaAccreditoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/note-accredito")
public class NotaAccreditoController {

    private static Logger LOGGER = LoggerFactory.getLogger(NotaAccreditoController.class);

    private final NotaAccreditoService notaAccreditoService;

    @Autowired
    public NotaAccreditoController(final NotaAccreditoService notaAccreditoService){
        this.notaAccreditoService = notaAccreditoService;
    }

    @RequestMapping(method = GET, path = "/{notaAccreditoId}")
    @CrossOrigin
    public NotaAccredito getOne(@PathVariable final Long notaAccreditoId) {
        LOGGER.info("Performing GET request for retrieving 'nota accredito' '{}'", notaAccreditoId);
        return notaAccreditoService.getOne(notaAccreditoId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'anno' and 'progressivo' for a new 'nota accredito'");
        return notaAccreditoService.getAnnoAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public NotaAccredito create(@RequestBody final NotaAccredito notaAccredito){
        LOGGER.info("Performing POST request for creating 'nota accredito'");
        return notaAccreditoService.create(notaAccredito);
    }

    @RequestMapping(method = DELETE, path = "/{notaAccreditoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long notaAccreditoId){
        LOGGER.info("Performing DELETE request for deleting 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoService.delete(notaAccreditoId);
    }

}
