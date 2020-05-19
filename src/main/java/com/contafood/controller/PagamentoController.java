package com.contafood.controller;

import com.contafood.model.Cliente;
import com.contafood.model.Ddt;
import com.contafood.model.NotaAccredito;
import com.contafood.model.Pagamento;
import com.contafood.service.PagamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public Set<Pagamento> getDdtPagamenti(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                          @RequestParam(name = "dataA", required = false) Date dataA,
                                          @RequestParam(name = "cliente", required = false) String cliente,
                                          @RequestParam(name = "importo", required = false) Double importo,
                                          @RequestParam(name = "idDdt", required = false)  Integer idDdt,
                                          @RequestParam(name = "idNotaAccredito", required = false)  Integer idNotaAccredito) {
        LOGGER.info("Performing GET request for retrieving all 'pagamenti'");
        LOGGER.info("Request params: dataDa {}, dataA {}, cliente {}, importo {}, idDdt {}, notaAccredito {}",
                dataDa, dataA, cliente, importo, idDdt, idNotaAccredito);

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
                NotaAccredito notaAccredito = pagamento.getNotaAccredito();
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
                } else if(notaAccredito != null){
                    Cliente notaAccreditoCliente = notaAccredito.getCliente();
                    if(notaAccreditoCliente != null){
                        if(notaAccreditoCliente.getDittaIndividuale() != null && Boolean.TRUE.equals(notaAccreditoCliente.getDittaIndividuale())){
                            String clienteNomeCognome = notaAccreditoCliente.getNome().concat(" ").concat(notaAccreditoCliente.getCognome());
                            if(clienteNomeCognome.contains(cliente)){
                                return true;
                            }
                        }else {
                            if(notaAccreditoCliente.getRagioneSociale().contains(cliente)){
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

        if(idDdt != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'ddt' '{}'", idDdt);
            return pagamentoService.getDdtPagamentiByIdDdt(idDdt.longValue());
        } else if(idNotaAccredito != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'notaAccredito' '{}'", idNotaAccredito);
            return pagamentoService.getNotaAccreditoPagamentiByIdNotaAccredito(idNotaAccredito.longValue());
        } else {
            Set<Pagamento> pagamenti = pagamentoService.getPagamenti();
            return pagamenti.stream().filter(isPagamentoDataDaGreaterOrEquals
                    .and(isPagamentoDataALessOrEquals)
                    .and(isPagamentoClienteContains)
                    .and(isPagamentoImportoEquals))
                    .collect(Collectors.toSet());
        }
    }

    @RequestMapping(method = GET, path = "/{pagamentoId}")
    @CrossOrigin
    public Pagamento getDdtPagamento(@PathVariable final Long pagamentoId) {
        LOGGER.info("Performing GET request for retrieving 'pagamento' '{}'", pagamentoId);
        return pagamentoService.getPagamento(pagamentoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Pagamento createPagamento(@RequestBody final Pagamento pagamento){
        LOGGER.info("Performing POST request for creating 'pagamento'");
        return pagamentoService.createPagamento(pagamento);
    }

    @RequestMapping(method = DELETE, path = "/{pagamentoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void deleteDdtPagamento(@PathVariable final Long pagamentoId){
        LOGGER.info("Performing DELETE request for deleting 'pagamento' '{}'", pagamentoId);
        pagamentoService.deletePagamento(pagamentoId);
    }

}
