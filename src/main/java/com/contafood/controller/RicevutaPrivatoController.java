package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.service.RicevutaPrivatoService;
import com.contafood.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/ricevute-privati")
public class RicevutaPrivatoController {

    private static Logger LOGGER = LoggerFactory.getLogger(RicevutaPrivatoController.class);

    private final RicevutaPrivatoService ricevutaPrivatoService;

    @Autowired
    public RicevutaPrivatoController(final RicevutaPrivatoService ricevutaPrivatoService){
        this.ricevutaPrivatoService = ricevutaPrivatoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<RicevutaPrivato> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
                           @RequestParam(name = "dataA", required = false) Date dataA,
                           @RequestParam(name = "progressivo", required = false) Integer progressivo,
                           @RequestParam(name = "importo", required = false) Float importo,
                           @RequestParam(name = "tipoPagamento", required = false) Integer idTipoPagamento,
                           @RequestParam(name = "cliente", required = false) String cliente,
                           @RequestParam(name = "agente", required = false) Integer idAgente,
                           @RequestParam(name = "autista", required = false) Integer idAutista,
                           @RequestParam(name = "articolo", required = false) Integer idArticolo,
                           @RequestParam(name = "stato", required = false) Integer idStato,
                           @RequestParam(name = "idCliente", required = false) Integer idCliente) {
        LOGGER.info("Performing GET request for retrieving list of 'ricevute privato'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, autista {}, articolo {}, stato {}, idCliente {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idAutista, idArticolo, idStato, idCliente);

        Predicate<RicevutaPrivato> isRicevutaPrivatoDataDaGreaterOrEquals = ricevutaPrivato -> {
            if(dataDa != null){
                return ricevutaPrivato.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoDataALessOrEquals = ricevutaPrivato -> {
            if(dataA != null){
                return ricevutaPrivato.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoProgressivoEquals = ricevutaPrivato -> {
            if(progressivo != null){
                return ricevutaPrivato.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoImportoEquals = ricevutaPrivato -> {
            if(importo != null){
                return ricevutaPrivato.getTotale().compareTo(Utils.roundPrice(new BigDecimal(importo)))==0;
            }
            return true;
        };
        /*Predicate<RicevutaPrivato> isDdtTipoPagamentoEquals = ddt -> {
            if(idTipoPagamento != null){
                Set<Pagamento> pagamenti = ricevutaPrivatoService.getDdtPagamentiByIdDdt(ddt.getId());
                return pagamenti.stream().filter(p -> p.getTipoPagamento() != null).map(p -> p.getTipoPagamento().getId()).filter(tp -> tp.equals(Long.valueOf(idTipoPagamento))).findFirst().isPresent();
            }
            return true;
        };*/
        Predicate<RicevutaPrivato> isRicevutaPrivatoClienteContains = ricevutaPrivato -> {
            if(cliente != null){
                Cliente ricevutaPrivatoCliente = ricevutaPrivato.getCliente();
                if(ricevutaPrivatoCliente != null){
                    if((ricevutaPrivatoCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase())){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoAgenteEquals = ricevutaPrivato -> {
            if(idAgente != null){
                Cliente ricevutaPrivatoCliente = ricevutaPrivato.getCliente();
                if(ricevutaPrivatoCliente != null){
                    Agente agente = ricevutaPrivatoCliente.getAgente();
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
        Predicate<RicevutaPrivato> isRicevutaPrivatoAutistaEquals = ricevutaPrivato -> {
            if(idAutista != null){
                Autista autista = ricevutaPrivato.getAutista();
                if(autista != null){
                    if(autista.getId().equals(Long.valueOf(idAutista))){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoArticoloEquals = ricevutaPrivato -> {
            if(idArticolo != null){
                Set<RicevutaPrivatoArticolo> ricevutaPrivatoArticoli = ricevutaPrivato.getRicevutaPrivatoArticoli();
                if(ricevutaPrivatoArticoli != null && !ricevutaPrivatoArticoli.isEmpty()){
                    return ricevutaPrivatoArticoli.stream().filter(da -> da.getId() != null).map(da -> da.getId()).filter(daId -> daId.getArticoloId() != null && daId.getArticoloId().equals(Long.valueOf(idArticolo))).findFirst().isPresent();
                }
                return false;
            }
            return true;
        };
        Predicate<RicevutaPrivato> isRicevutaPrivatoStatoEquals = ricevutaPrivato -> {
            if(idStato != null){
                StatoRicevutaPrivato statoRicevutaPrivato = ricevutaPrivato.getStatoRicevutaPrivato();
                if(statoRicevutaPrivato != null){
                    return statoRicevutaPrivato.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };

        Set<RicevutaPrivato> ricevutePrivati = ricevutaPrivatoService.getAll();
        return ricevutePrivati.stream().filter(isRicevutaPrivatoDataDaGreaterOrEquals
                .and(isRicevutaPrivatoDataALessOrEquals)
                .and(isRicevutaPrivatoProgressivoEquals)
                .and(isRicevutaPrivatoImportoEquals)
                //.and(isDdtTipoPagamentoEquals)
                .and(isRicevutaPrivatoClienteContains)
                .and(isRicevutaPrivatoAgenteEquals)
                .and(isRicevutaPrivatoAutistaEquals)
                .and(isRicevutaPrivatoArticoloEquals)
                .and(isRicevutaPrivatoStatoEquals)).collect(Collectors.toSet());
    }

    @RequestMapping(method = GET, path = "/{ricevutaPrivatoId}")
    @CrossOrigin
    public RicevutaPrivato getOne(@PathVariable final Long ricevutaPrivatoId) {
        LOGGER.info("Performing GET request for retrieving 'ricevuta privato' '{}'", ricevutaPrivatoId);
        return ricevutaPrivatoService.getOne(ricevutaPrivatoId);
    }

    @RequestMapping(method = GET, path = "/progressivo")
    @CrossOrigin
    public Map<String, Integer> getAnnoAndProgressivo() {
        LOGGER.info("Performing GET request for retrieving 'anno' and 'progressivo' for a new ricevuta privato");
        return ricevutaPrivatoService.getAnnoAndProgressivo();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public RicevutaPrivato create(@RequestBody final RicevutaPrivato ricevutaPrivato){
        LOGGER.info("Performing POST request for creating 'ricevuta privato'");
        return ricevutaPrivatoService.create(ricevutaPrivato);
    }

    @RequestMapping(method = DELETE, path = "/{ricevutaPrivatoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long ricevutaPrivatoId){
        LOGGER.info("Performing DELETE request for deleting 'ricevuta privato' '{}'", ricevutaPrivatoId);
        ricevutaPrivatoService.delete(ricevutaPrivatoId);
    }

}
