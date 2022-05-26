package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Fattura;
import com.contafood.model.views.VFattura;
import com.contafood.service.FatturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/fatture")
public class FatturaController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FatturaController.class);

    private final FatturaService fatturaService;

    @Autowired
    public FatturaController(final FatturaService fatturaService){
        this.fatturaService = fatturaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<VFattura> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                @RequestParam(name = "dataA", required = false) Date dataA,
                                @RequestParam(name = "progressivo", required = false) Integer progressivo,
                                @RequestParam(name = "importo", required = false) Float importo,
                                @RequestParam(name = "tipoPagamento", required = false) String idTipoPagamento,
                                @RequestParam(name = "cliente", required = false) String cliente,
                                @RequestParam(name = "agente", required = false) Integer idAgente,
                                @RequestParam(name = "articolo", required = false) Integer idArticolo,
                                @RequestParam(name = "stato", required = false) Integer idStato,
                                @RequestParam(name = "tipo", required = false) Integer idTipo) {
        LOGGER.info("Performing GET request for retrieving list of 'fatture vendita and fatture accompagnatorie'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, articolo {}, stato {}, tipo {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        return fatturaService.search(dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);
    }

    @RequestMapping(method = GET, path = "/{fatturaId}")
    @CrossOrigin
    public Fattura getOne(@PathVariable final Long fatturaId) {
        LOGGER.info("Performing GET request for retrieving 'fattura' '{}'", fatturaId);
        return fatturaService.getOne(fatturaId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'anno' and 'progressivo' for a new fattura");
        return fatturaService.getAnnoAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Fattura create(@RequestBody final Fattura fattura){
        LOGGER.info("Performing POST request for creating 'fattura'");
        return fatturaService.create(fattura);
    }

    @RequestMapping(method = POST, path = "/creazione-automatica")
    @ResponseStatus(CREATED)
    @CrossOrigin
    public List<Fattura> createBulk(@RequestBody(required = false) final Map<String, Object> body){
        LOGGER.info("Performing POST request for creating bulk 'fatture'");
        return fatturaService.createBulk(body);
    }

    /*
    @RequestMapping(method = PUT, path = "/{fatturaId}")
    @CrossOrigin
    public Fattura update(@PathVariable final Long fatturaId, @RequestBody final Fattura fattura){
        LOGGER.info("Performing PUT request for updating 'fattura' '{}'", fatturaId);
        if (!Objects.equals(fatturaId, fattura.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return fatturaService.update(fattura);
    }
     */

    @RequestMapping(method = PATCH, path = "/{fatturaId}")
    @CrossOrigin
    public Fattura patch(@PathVariable final Long fatturaId, @RequestBody final Map<String,Object> patchFattura) throws Exception{
        LOGGER.info("Performing PATCH request for updating 'fattura' '{}'", fatturaId);
        Long id = Long.valueOf((Integer) patchFattura.get("id"));
        if (!Objects.equals(fatturaId, id)) {
            throw new CannotChangeResourceIdException();
        }
        return fatturaService.patch(patchFattura);
    }

    @RequestMapping(method = DELETE, path = "/{fatturaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fatturaId){
        LOGGER.info("Performing DELETE request for deleting 'fattura' '{}'", fatturaId);
        fatturaService.delete(fatturaId);
    }

}
