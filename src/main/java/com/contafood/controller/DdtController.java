package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Ddt;
import com.contafood.model.Pagamento;
import com.contafood.service.DdtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/ddts")
public class DdtController {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtController.class);

    private final DdtService ddtService;

    @Autowired
    public DdtController(final DdtService ddtService){
        this.ddtService = ddtService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Ddt> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'ddts'");
        return ddtService.getAll();
    }

    @RequestMapping(method = GET, path = "/{ddtId}")
    @CrossOrigin
    public Ddt getOne(@PathVariable final Long ddtId) {
        LOGGER.info("Performing GET request for retrieving 'ddt' '{}'", ddtId);
        return ddtService.getOne(ddtId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoContabileAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'annoContabile' and 'progressivo' for a new ddt");
        return ddtService.getAnnoContabileAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Ddt create(@RequestBody final Ddt ddt){
        LOGGER.info("Performing POST request for creating 'ddt'");
        return ddtService.create(ddt);
    }

    @RequestMapping(method = PUT, path = "/{ddtId}")
    @CrossOrigin
    public Ddt update(@PathVariable final Long ddtId, @RequestBody final Ddt ddt){
        LOGGER.info("Performing PUT request for updating 'ddt' '{}'", ddtId);
        if (!Objects.equals(ddtId, ddt.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return ddtService.update(ddt);
    }

    @RequestMapping(method = PATCH, path = "/{ddtId}")
    @CrossOrigin
    public Ddt patch(@PathVariable final Long ddtId, @RequestBody final Map<String,Object> patchDdt){
        LOGGER.info("Performing PATCH request for updating 'ddt' '{}'", ddtId);
        Long id = Long.valueOf((Integer) patchDdt.get("id"));
        if (!Objects.equals(ddtId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return ddtService.patch(patchDdt);
    }

    @RequestMapping(method = DELETE, path = "/{ddtId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ddtId){
        LOGGER.info("Performing DELETE request for deleting 'ddt' '{}'", ddtId);
        ddtService.delete(ddtId);
    }

    // PAGAMENTI
    @RequestMapping(method = GET, path = "/pagamenti")
    @CrossOrigin
    public Set<Pagamento> getDdtPagamenti() {
        LOGGER.info("Performing GET request for retrieving all 'pagamenti'");
        return ddtService.getDdtPagamenti();
    }

    @RequestMapping(method = GET, path = "/{ddtId}/pagamenti")
    @CrossOrigin
    public List<Pagamento> getDdtPagamentiByIdDdt(@PathVariable final Long ddtId) {
        LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        return ddtService.getDdtPagamentiByIdDdt(ddtId);
    }

    @RequestMapping(method = GET, path = "/pagamenti/{pagamentoId}")
    @CrossOrigin
    public Pagamento getDdtPagamento(@PathVariable final Long pagamentoId) {
        LOGGER.info("Performing GET request for retrieving 'pagamento' '{}'", pagamentoId);
        return ddtService.getDdtPagamento(pagamentoId);
    }

    @RequestMapping(method = POST, path = "/{ddtId}/pagamenti")
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Pagamento createDdtPagamento(@PathVariable final Long ddtId, @RequestBody final Pagamento pagamento){
        LOGGER.info("Performing POST request for creating 'pagamento'");
        return ddtService.createDdtPagamento(pagamento);
    }

    @RequestMapping(method = DELETE, path = "/pagamenti/{pagamentoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void deleteDdtPagamento(@PathVariable final Long pagamentoId){
        LOGGER.info("Performing DELETE request for deleting 'pagamento' '{}'", pagamentoId);
        ddtService.deleteDdtPagamento(pagamentoId);
    }
}
