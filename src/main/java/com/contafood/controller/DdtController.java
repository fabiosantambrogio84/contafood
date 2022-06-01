package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.model.beans.DdtRicercaLotto;
import com.contafood.model.views.VDdt;
import com.contafood.service.DdtService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@SuppressWarnings({"unused"})
@RestController
@RequestMapping(path="/ddts")
public class DdtController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DdtController.class);

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

    @RequestMapping(method = GET, path = "/search-lotto")
    @CrossOrigin
    public Set<DdtRicercaLotto> getAllByLotto(@RequestParam(name = "lotto") String lotto) {
        LOGGER.info("Performing GET request for retrieving list of 'ddts'");
        LOGGER.info("Request params: lotto {}", lotto);

        return ddtService.getAllByLotto(lotto);
    }

    @RequestMapping(method = GET, path = "/search")
    @CrossOrigin
    public List<VDdt> search(@RequestParam(name = "dataDa", required = false) Date dataDa,
                             @RequestParam(name = "dataA", required = false) Date dataA,
                             @RequestParam(name = "progressivo", required = false) Integer progressivo,
                             @RequestParam(name = "importo", required = false) Float importo,
                             @RequestParam(name = "tipoPagamento", required = false) Integer idTipoPagamento,
                             @RequestParam(name = "cliente", required = false) String cliente,
                             @RequestParam(name = "agente", required = false) Integer idAgente,
                             @RequestParam(name = "autista", required = false) Integer idAutista,
                             @RequestParam(name = "articolo", required = false) Integer idArticolo,
                             @RequestParam(name = "stato", required = false) Integer idStato,
                             @RequestParam(name = "idCliente", required = false) Integer idCliente,
                             @RequestParam(name = "fatturato", required = false) Boolean fatturato) {
        LOGGER.info("Performing GET request for searching list of 'ddts'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, autista {}, articolo {}, stato {}, idCliente {}, fatturato {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idAutista, idArticolo, idStato, idCliente, fatturato);
        // /contafood-be/ddts?dataDa=&dataA=&progressivo=&importo=&tipoPagamento=&cliente=&agente=&autista=&articolo=

        return ddtService.getAllByFilters(dataDa, dataA, progressivo, idCliente, cliente, idAgente, idAutista, idStato, fatturato, importo, idTipoPagamento, idArticolo);
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
    public void delete(@PathVariable final Long ddtId,
                       @RequestParam(name = "modificaGiacenze", required = false) Boolean modificaGiacenze){
        LOGGER.info("Performing DELETE request for deleting 'ddt' '{}'", ddtId);
        LOGGER.info("Request params: modificaGiacenze={}", modificaGiacenze);
        ddtService.delete(ddtId, (modificaGiacenze != null ? modificaGiacenze : Boolean.FALSE));
    }

}
