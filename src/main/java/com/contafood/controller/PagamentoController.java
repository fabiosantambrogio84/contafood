package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Pagamento;
import com.contafood.service.PagamentoService;
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
@RequestMapping(path="/pagamenti")
public class PagamentoController {

    private static Logger LOGGER = LoggerFactory.getLogger(PagamentoController.class);

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(final PagamentoService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Pagamento> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'pagamenti'");
        return pagamentoService.getAll();
    }

    @RequestMapping(method = GET, path = "/{pagamentoId}")
    @CrossOrigin
    public Pagamento getOne(@PathVariable final Long pagamentoId) {
        LOGGER.info("Performing GET request for retrieving 'pagamento' '{}'", pagamentoId);
        return pagamentoService.getOne(pagamentoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Pagamento create(@RequestBody final Pagamento pagamento){
        LOGGER.info("Performing POST request for creating 'pagamento'");
        return pagamentoService.create(pagamento);
    }

    @RequestMapping(method = PUT, path = "/{pagamentoId}")
    @CrossOrigin
    public Pagamento update(@PathVariable final Long pagamentoId, @RequestBody final Pagamento pagamento){
        LOGGER.info("Performing PUT request for updating 'pagamento' '{}'", pagamentoId);
        if (!Objects.equals(pagamentoId, pagamento.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return pagamentoService.update(pagamento);
    }

    @RequestMapping(method = DELETE, path = "/{pagamentoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long pagamentoId){
        LOGGER.info("Performing DELETE request for deleting 'pagamento' '{}'", pagamentoId);
        pagamentoService.delete(pagamentoId);
    }
}
