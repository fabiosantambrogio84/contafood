package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.service.FatturaService;
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

@RestController
@RequestMapping(path="/fatture-vendita")
public class FatturaController {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaController.class);

    private final FatturaService fatturaService;

    @Autowired
    public FatturaController(final FatturaService fatturaService){
        this.fatturaService = fatturaService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Fattura> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                               @RequestParam(name = "dataA", required = false) Date dataA,
                               @RequestParam(name = "progressivo", required = false) Integer progressivo,
                               @RequestParam(name = "importo", required = false) Float importo,
                               @RequestParam(name = "tipoPagamento", required = false) Integer idTipoPagamento,
                               @RequestParam(name = "cliente", required = false) String cliente,
                               @RequestParam(name = "agente", required = false) Integer idAgente,
                               @RequestParam(name = "articolo", required = false) Integer idArticolo,
                               @RequestParam(name = "stato", required = false) Integer idStato) {
        LOGGER.info("Performing GET request for retrieving list of 'fatture'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, articolo {}, stato {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato);

        Predicate<Fattura> isFatturaDataDaGreaterOrEquals = fattura -> {
            if(dataDa != null){
                return fattura.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<Fattura> isFatturaDataALessOrEquals = fattura -> {
            if(dataA != null){
                return fattura.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<Fattura> isFatturaProgressivoEquals = fattura -> {
            if(progressivo != null){
                return fattura.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<Fattura> isFatturaImportoEquals = fattura -> {
            if(importo != null){
                LOGGER.info("Importo {} - Fattura totale {} - Fattura totale float {}", importo, fattura.getTotale(), fattura.getTotale().floatValue());
                if(importo.equals(fattura.getTotale().floatValue())){
                    return true;
                } else {
                    return false;
                }
                //return fattura.getTotale().compareTo(new BigDecimal(importo).setScale(2, BigDecimal.ROUND_DOWN))==0;
            }
            return true;
        };
        Predicate<Fattura> isFatturaTipoPagamentoEquals = fattura -> {
            LOGGER.info("Filter by idTipoPagamento '{}'", idTipoPagamento);
            if(idTipoPagamento != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    TipoPagamento tipoPagamento = fatturaCliente.getTipoPagamento();
                    if(tipoPagamento != null){
                        LOGGER.info("Cliente id '{}', TipoPagamento id '{}'", fatturaCliente.getId(), tipoPagamento.getId());
                        return tipoPagamento.getId().equals(Long.valueOf(idTipoPagamento));
                    }
                }
            }
            return true;
        };
        Predicate<Fattura> isFatturaClienteContains = fattura -> {
            if(cliente != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    if((fatturaCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase())){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Fattura> isFatturaAgenteEquals = fattura -> {
            if(idAgente != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    Agente agente = fatturaCliente.getAgente();
                    if(agente != null){
                        if(agente.getId().equals(Long.valueOf(idAgente))){
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Fattura> isFatturaDdtArticoloEquals = fattura -> {
            if(idArticolo != null){
                Set<DdtArticolo> ddtArticoli = fatturaService.getFatturaDdtArticoli(fattura.getId());
                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    return ddtArticoli.stream().filter(da -> da.getId() != null).map(da -> da.getId()).filter(daId -> daId.getArticoloId() != null && daId.getArticoloId().equals(Long.valueOf(idArticolo))).findFirst().isPresent();
                }
                return false;
            }
            return true;
        };
        Predicate<Fattura> isFatturaStatoEquals = fattura -> {
            if(idStato != null){
                StatoFattura statoFattura = fattura.getStatoFattura();
                if(statoFattura != null){
                    return statoFattura.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };

        Set<Fattura> fatture = fatturaService.getAll();
        return fatture.stream().filter(isFatturaDataDaGreaterOrEquals
                .and(isFatturaDataALessOrEquals)
                .and(isFatturaProgressivoEquals)
                .and(isFatturaImportoEquals)
                .and(isFatturaTipoPagamentoEquals)
                .and(isFatturaClienteContains)
                .and(isFatturaAgenteEquals)
                .and(isFatturaDdtArticoloEquals)
                .and(isFatturaStatoEquals)).collect(Collectors.toSet());
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

    @RequestMapping(method = DELETE, path = "/{fatturaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fatturaId){
        LOGGER.info("Performing DELETE request for deleting 'fattura' '{}'", fatturaId);
        fatturaService.delete(fatturaId);
    }

}
