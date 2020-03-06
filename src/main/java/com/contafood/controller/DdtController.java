package com.contafood.controller;

import com.contafood.exception.CannotChangeResourceIdException;
import com.contafood.model.*;
import com.contafood.service.DdtService;
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
    public Set<Ddt> getAll(@RequestParam(name = "dataDa", required = false) Date dataDa,
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
        LOGGER.info("Performing GET request for retrieving list of 'ddts'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, autista {}, articolo {}, stato {}, idCliente {}, fatturato {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idAutista, idArticolo, idStato, idCliente, fatturato);
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
                List<Pagamento> pagamenti = ddtService.getDdtPagamentiByIdDdt(ddt.getId());
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
        Predicate<Ddt> isDdtIdClienteEquals = ddt -> {
            if(idCliente != null){
                Cliente ddtCliente = ddt.getCliente();
                if(ddtCliente != null){
                    if(ddtCliente.getId().equals(Long.valueOf(idCliente))){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Ddt> isDdtFatturatoEquals = ddt -> {
            if(fatturato != null){
                return ddt.getFatturato().equals(fatturato);
            }
            return true;
        };

        Set<Ddt> ddts = ddtService.getAll();
        return ddts.stream().filter(isDdtDataDaGreaterOrEquals
                .and(isDdtDataALessOrEquals)
                .and(isDdtProgressivoEquals)
                .and(isDdtImportoEquals)
                .and(isDdtTipoPagamentoEquals)
                .and(isDdtClienteContains)
                .and(isDdtAgenteEquals)
                .and(isDdtAutistaEquals)
                .and(isDdtArticoloEquals)
                .and(isDdtStatoEquals)
                .and(isDdtIdClienteEquals)
                .and(isDdtFatturatoEquals)).collect(Collectors.toSet());
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
    public Set<Pagamento> getDdtPagamenti(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                          @RequestParam(name = "dataA", required = false) Date dataA,
                                          @RequestParam(name = "cliente", required = false) String cliente,
                                          @RequestParam(name = "importo", required = false) Double importo) {
        LOGGER.info("Performing GET request for retrieving all 'pagamenti'");
        LOGGER.info("Request params: dataDa {}, dataA {}, cliente {}, importo {}",
                dataDa, dataA, cliente, importo);

        Predicate<Pagamento> isPagamentoDataDaGreaterOrEquals = pagamento -> {
            if(dataDa != null){
                return pagamento.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoDataALessOrEquals = pagamento -> {
            if(dataA != null){
                return pagamento.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoClienteContains = pagamento -> {
            if(cliente != null){
                Ddt ddt = pagamento.getDdt();
                if(ddt != null){
                    Cliente ddtCliente = ddt.getCliente();
                    if(ddtCliente != null){
                        if(ddtCliente.getDittaIndividuale() != null && Boolean.TRUE.equals(ddtCliente.getDittaIndividuale())){
                            String clienteNomeCognome = ddtCliente.getNome().concat(" ").concat(ddtCliente.getCognome());
                            if(clienteNomeCognome.contains(cliente)){
                                return true;
                            }
                        }else {
                            if(ddtCliente.getRagioneSociale().contains(cliente)){
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoImportoEquals = pagamento -> {
            if(importo != null){
                return pagamento.getImporto().compareTo(new BigDecimal(importo))==0;
            }
            return true;
        };

        Set<Pagamento> pagamenti = ddtService.getDdtPagamenti();
        return pagamenti.stream().filter(isPagamentoDataDaGreaterOrEquals
                .and(isPagamentoDataALessOrEquals)
                .and(isPagamentoClienteContains)
                .and(isPagamentoImportoEquals))
                .collect(Collectors.toSet());
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
