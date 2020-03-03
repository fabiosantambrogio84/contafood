package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.Fattura;
import com.contafood.service.FatturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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

    /*
    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Ddt> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                           @RequestParam(name = "dataA", required = false) Date dataA,
                           @RequestParam(name = "progressivo", required = false) Integer progressivo,
                           @RequestParam(name = "importo", required = false) Float importo,
                           @RequestParam(name = "tipoPagamento", required = false) Integer idTipoPagamento,
                           @RequestParam(name = "cliente", required = false) String cliente,
                           @RequestParam(name = "agente", required = false) Integer idAgente,
                           @RequestParam(name = "autista", required = false) Integer idAutista,
                           @RequestParam(name = "articolo", required = false) Integer idArticolo,
                           @RequestParam(name = "stato", required = false) Integer idStato) {
        LOGGER.info("Performing GET request for retrieving list of 'ddts'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, autista {}, articolo {}, stato {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idAutista, idArticolo, idStato);
        // /contafood-be/ddts?dataDa=&dataA=&progressivo=&importo=&tipoPagamento=&cliente=&agente=&autista=&articolo=

        Predicate<Ddt> isDdtDataDaGreaterOrEquals = ddt -> {
            if(dataDa != null){
                return ddt.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<Ddt> isDdtDataALessOrEquals = ddt -> {
            if(dataA != null){
                return ddt.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<Ddt> isDdtProgressivoEquals = ddt -> {
            if(progressivo != null){
                return ddt.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<Ddt> isDdtImportoEquals = ddt -> {
            if(importo != null){
                return ddt.getTotale().compareTo(new BigDecimal(importo).setScale(2, RoundingMode.CEILING))==0;
            }
            return true;
        };
        Predicate<Ddt> isDdtTipoPagamentoEquals = ddt -> {
            if(idTipoPagamento != null){
                List<Pagamento> pagamenti = fatturaService.getDdtPagamentiByIdDdt(ddt.getId());
                return pagamenti.stream().filter(p -> p.getTipoPagamento() != null).map(p -> p.getTipoPagamento().getId()).filter(tp -> tp.equals(Long.valueOf(idTipoPagamento))).findFirst().isPresent();
            }
            return true;
        };
        Predicate<Ddt> isDdtClienteContains = ddt -> {
            if(cliente != null){
                Cliente ddtCliente = ddt.getCliente();
                if(ddtCliente != null){
                    if(ddtCliente.getDittaIndividuale() != null && Boolean.TRUE.equals(ddtCliente.getDittaIndividuale())){
                        String clienteNomeCognome = (ddtCliente.getNome().concat(" ").concat(ddtCliente.getCognome())).toLowerCase();
                        if(clienteNomeCognome.contains(cliente.toLowerCase())){
                            return true;
                        }
                    }else {
                        if((ddtCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase())){
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Ddt> isDdtAgenteEquals = ddt -> {
            if(idAgente != null){
                Cliente ddtCliente = ddt.getCliente();
                if(ddtCliente != null){
                    Agente agente = ddtCliente.getAgente();
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
        Predicate<Ddt> isDdtAutistaEquals = ddt -> {
            if(idAutista != null){
                Autista autista = ddt.getAutista();
                if(autista != null){
                    if(autista.getId().equals(Long.valueOf(idAutista))){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Ddt> isDdtArticoloEquals = ddt -> {
            if(idArticolo != null){
                Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    return ddtArticoli.stream().filter(da -> da.getId() != null).map(da -> da.getId()).filter(daId -> daId.getArticoloId() != null && daId.getArticoloId().equals(Long.valueOf(idArticolo))).findFirst().isPresent();
                }
                return false;
            }
            return true;
        };
        Predicate<Ddt> isDdtStatoEquals = ddt -> {
            if(idStato != null){
                StatoDdt statoDdt = ddt.getStatoDdt();
                if(statoDdt != null){
                    return statoDdt.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };

        Set<Ddt> ddts = fatturaService.getAll();
        return ddts.stream().filter(isDdtDataDaGreaterOrEquals
                .and(isDdtDataALessOrEquals)
                .and(isDdtProgressivoEquals)
                .and(isDdtImportoEquals)
                .and(isDdtTipoPagamentoEquals)
                .and(isDdtClienteContains)
                .and(isDdtAgenteEquals)
                .and(isDdtAutistaEquals)
                .and(isDdtArticoloEquals)
                .and(isDdtStatoEquals)).collect(Collectors.toSet());
    }
    */

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

    @RequestMapping(method = PUT, path = "/{fatturaId}")
    @CrossOrigin
    public Fattura update(@PathVariable final Long fatturaId, @RequestBody final Fattura fattura){
        LOGGER.info("Performing PUT request for updating 'fattura' '{}'", fatturaId);
        if (!Objects.equals(fatturaId, fattura.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return fatturaService.update(fattura);
    }

    @RequestMapping(method = DELETE, path = "/{fatturaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long fatturaId){
        LOGGER.info("Performing DELETE request for deleting 'fattura' '{}'", fatturaId);
        fatturaService.delete(fatturaId);
    }

}
