package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Agente;
import com.contafood.model.Parametro;
import com.contafood.service.AgenteService;
import com.contafood.service.ParametroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/configurazione/parametri")
public class ParametroController {

    private static Logger LOGGER = LoggerFactory.getLogger(ParametroController.class);

    private final ParametroService parametroService;

    @Autowired
    public ParametroController(final ParametroService parametroService){
        this.parametroService = parametroService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Parametro> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'parametri'");
        return parametroService.getAll();
    }

    @RequestMapping(method = GET, path = "/{parametroId}")
    @CrossOrigin
    public Parametro getOne(@PathVariable final Long parametroId) {
        LOGGER.info("Performing GET request for retrieving 'parametro' '{}'", parametroId);
        return parametroService.getOne(parametroId);
    }

    @RequestMapping(method = GET, params = "nome")
    @CrossOrigin
    public Parametro getOneByName(@RequestParam("nome") String parametroName) {
        LOGGER.info("Performing GET request for retrieving 'parametro' by name '{}'", parametroName);
        return parametroService.findByNome(parametroName);
    }

    @RequestMapping(method = PUT, path = "/{parametroId}")
    @CrossOrigin
    public Parametro update(@PathVariable final Long parametroId, @RequestBody final Parametro parametro){
        LOGGER.info("Performing PUT request for updating 'parametro' '{}'", parametroId);
        if (!Objects.equals(parametroId, parametro.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return parametroService.update(parametro);
    }
}
