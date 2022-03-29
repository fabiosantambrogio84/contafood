package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.model.views.VFattura;
import com.contafood.service.FatturaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/fatture")
public class FatturaController {

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaController.class);

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

        List<Long> idTipiPagamento = new ArrayList<>();
        if(!StringUtils.isEmpty(idTipoPagamento)){
            Arrays.stream(idTipoPagamento.split(",")).mapToLong(id -> Long.parseLong(id)).forEach(l -> idTipiPagamento.add(l));
        }

        Predicate<VFattura> isFatturaDataDaGreaterOrEquals = fattura -> {
            if(dataDa != null){
                return fattura.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<VFattura> isFatturaDataALessOrEquals = fattura -> {
            if(dataA != null){
                return fattura.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<VFattura> isFatturaProgressivoEquals = fattura -> {
            if(progressivo != null){
                return fattura.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<VFattura> isFatturaImportoEquals = fattura -> {
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
        Predicate<VFattura> isFatturaTipoPagamentoEquals = fattura -> {
            LOGGER.info("Filter by idTipoPagamento '{}'", idTipiPagamento);
            if(idTipiPagamento != null && !idTipiPagamento.isEmpty()){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    TipoPagamento tipoPagamento = fatturaCliente.getTipoPagamento();
                    if(tipoPagamento != null){
                        LOGGER.info("Cliente id '{}', TipoPagamento id '{}'", fatturaCliente.getId(), tipoPagamento.getId());
                        return idTipiPagamento.contains(tipoPagamento.getId());
                    }
                }
            }
            return true;
        };
        Predicate<VFattura> isFatturaClienteContains = fattura -> {
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
        Predicate<VFattura> isFatturaAgenteEquals = fattura -> {
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
        Predicate<VFattura> isFatturaDdtArticoloEquals = fattura -> {
            if(idArticolo != null){
                Set<DdtArticolo> ddtArticoli = fatturaService.getFatturaDdtArticoli(fattura.getId());
                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    return ddtArticoli.stream().filter(da -> da.getId() != null).map(da -> da.getId()).filter(daId -> daId.getArticoloId() != null && daId.getArticoloId().equals(Long.valueOf(idArticolo))).findFirst().isPresent();
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaStatoEquals = fattura -> {
            if(idStato != null){
                StatoFattura statoFattura = fattura.getStatoFattura();
                if(statoFattura != null){
                    return statoFattura.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaTipoEquals = fattura -> {
            if(idTipo != null){
                TipoFattura tipoFattura = fattura.getTipoFattura();
                if(tipoFattura != null){
                    return tipoFattura.getId().equals(Long.valueOf(idTipo));
                }
                return false;
            }
            return true;
        };

        Set<VFattura> fatture = fatturaService.getAll();
        return fatture.stream().filter(isFatturaDataDaGreaterOrEquals
                .and(isFatturaDataALessOrEquals)
                .and(isFatturaProgressivoEquals)
                .and(isFatturaImportoEquals)
                .and(isFatturaTipoPagamentoEquals)
                .and(isFatturaClienteContains)
                .and(isFatturaAgenteEquals)
                .and(isFatturaDdtArticoloEquals)
                .and(isFatturaStatoEquals)
                .and(isFatturaTipoEquals)).collect(Collectors.toSet());
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
