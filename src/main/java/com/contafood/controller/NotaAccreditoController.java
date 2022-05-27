package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Agente;
import com.contafood.model.Cliente;
import com.contafood.model.NotaAccredito;
import com.contafood.model.StatoNotaAccredito;
import com.contafood.service.NotaAccreditoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<NotaAccredito> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                           @RequestParam(name = "dataA", required = false) Date dataA,
                           @RequestParam(name = "progressivo", required = false) Integer progressivo,
                           @RequestParam(name = "importo", required = false) Float importo,
                           @RequestParam(name = "cliente", required = false) String cliente,
                           @RequestParam(name = "agente", required = false) Integer idAgente,
                           @RequestParam(name = "articolo", required = false) Integer idArticolo,
                           @RequestParam(name = "stato", required = false) Integer idStato ) {
        LOGGER.info("Performing GET request for retrieving list of 'note accredito'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, cliente {}, agente {}, articolo {}, stato {}",
                dataDa, dataA, progressivo, importo, cliente, idAgente, idArticolo, idStato);

        return notaAccreditoService.search(dataDa, dataA, progressivo, importo, cliente, idAgente, idArticolo, idStato);
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

    @RequestMapping(method = PUT, path = "/{notaAccreditoId}")
    @CrossOrigin
    public NotaAccredito update(@PathVariable final Long notaAccreditoId, @RequestBody final NotaAccredito notaAccredito){
        LOGGER.info("Performing PUT request for updating 'nota accredito' '{}'", notaAccreditoId);
        if (!Objects.equals(notaAccreditoId, notaAccredito.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return notaAccreditoService.update(notaAccredito);
    }

    @RequestMapping(method = PATCH, path = "/{notaAccreditoId}")
    @CrossOrigin
    public NotaAccredito patch(@PathVariable final Long notaAccreditoId, @RequestBody final Map<String,Object> patchNotaAccredito){
        LOGGER.info("Performing PATCH request for updating 'notaAccredito' '{}'", notaAccreditoId);
        Long id = Long.valueOf((Integer) patchNotaAccredito.get("id"));
        if (!Objects.equals(notaAccreditoId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return notaAccreditoService.patch(patchNotaAccredito);
    }

    @RequestMapping(method = DELETE, path = "/{notaAccreditoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long notaAccreditoId){
        LOGGER.info("Performing DELETE request for deleting 'nota accredito' '{}'", notaAccreditoId);
        notaAccreditoService.delete(notaAccreditoId);
    }

}
