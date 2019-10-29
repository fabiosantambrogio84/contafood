package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Banca;
import com.contafood.model.TipoPagamento;
import com.contafood.service.TipoPagamentoService;
import com.contafood.service.TipoPagamentoService;
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
@RequestMapping(path="/tipi-pagamento")
public class TipoPagamentoController {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoPagamentoController.class);

    private final TipoPagamentoService tipoPagamentoService;

    @Autowired
    public TipoPagamentoController(final TipoPagamentoService tipoPagamentoService){
        this.tipoPagamentoService = tipoPagamentoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<TipoPagamento> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'tipiPagamento'");
        return tipoPagamentoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{tipoPagamentoId}")
    @CrossOrigin
    public TipoPagamento getOne(@PathVariable final Long tipoPagamentoId) {
        LOGGER.info("Performing GET request for retrieving 'tipoPagamento' '{}'", tipoPagamentoId);
        return tipoPagamentoService.getOne(tipoPagamentoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public TipoPagamento create(@RequestBody final TipoPagamento tipoPagamento){
        LOGGER.info("Performing POST request for creating 'tipoPagamento'");
        return tipoPagamentoService.create(tipoPagamento);
    }

    @RequestMapping(method = PUT, path = "/{tipoPagamentoId}")
    @CrossOrigin
    public TipoPagamento update(@PathVariable final Long tipoPagamentoId, @RequestBody final TipoPagamento tipoPagamento){
        LOGGER.info("Performing PUT request for updating 'tipoPagamento' '{}'", tipoPagamentoId);
        if (!Objects.equals(tipoPagamentoId, tipoPagamento.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return tipoPagamentoService.update(tipoPagamento);
    }

    @RequestMapping(method = DELETE, path = "/{tipoPagamentoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long tipoPagamentoId){
        LOGGER.info("Performing DELETE request for deleting 'tipoPagamento' '{}'", tipoPagamentoId);
        tipoPagamentoService.delete(tipoPagamentoId);
    }
}
