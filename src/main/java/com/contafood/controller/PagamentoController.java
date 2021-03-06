package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.service.PagamentoService;
import com.contafood.util.enumeration.TipologiaPagamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                                          @RequestParam(name = "fornitore", required = false) String fornitore,
                                          @RequestParam(name = "importo", required = false) Double importo,
                                          @RequestParam(name = "tipologia", required = false) String tipologia,
                                          @RequestParam(name = "idDdt", required = false) Integer idDdt,
                                          @RequestParam(name = "idNotaAccredito", required = false) Integer idNotaAccredito,
                                          @RequestParam(name = "idNotaReso", required = false) Integer idNotaReso,
                                          @RequestParam(name = "idRicevutaPrivato", required = false) Integer idRicevutaPrivato,
                                          @RequestParam(name = "idFattura", required = false) Integer idFattura,
                                          @RequestParam(name = "idFatturaAccompagnatoria", required = false) Integer idFatturaAccompagnatoria) {
        LOGGER.info("Performing GET request for retrieving all 'pagamenti'");
        LOGGER.info("Request params: dataDa {}, dataA {}, cliente {}, fornitore {}, importo {}, tipologia {}, idDdt {}, notaAccredito {}, notaReso {}, ricevutaPrivato {}, fattura {}, fatturaAccompagnatoria {}",
                dataDa, dataA, cliente, fornitore, importo, tipologia, idDdt, idNotaAccredito, idNotaReso, idRicevutaPrivato, idFattura, idFatturaAccompagnatoria);

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
                RicevutaPrivato ricevutaPrivato = pagamento.getRicevutaPrivato();
                Fattura fattura = pagamento.getFattura();
                FatturaAccompagnatoria fatturaAccompagnatoria = pagamento.getFatturaAccompagnatoria();
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
                } else if(ricevutaPrivato != null){
                    Cliente ricevutaPrivatoCliente = ricevutaPrivato.getCliente();
                    if(ricevutaPrivatoCliente != null){
                        String clienteNomeCognome = ricevutaPrivatoCliente.getNome().concat(" ").concat(ricevutaPrivatoCliente.getCognome());
                        if(clienteNomeCognome.contains(cliente)){
                            return true;
                        }
                    }
                } else if(fattura != null){
                    Cliente fatturaCliente = fattura.getCliente();
                    if(fatturaCliente != null){
                        if(fatturaCliente.getDittaIndividuale() != null && Boolean.TRUE.equals(fatturaCliente.getDittaIndividuale())){
                            String clienteNomeCognome = fatturaCliente.getNome().concat(" ").concat(fatturaCliente.getCognome());
                            if(clienteNomeCognome.contains(cliente)){
                                return true;
                            }
                        }else {
                            if(fatturaCliente.getRagioneSociale().contains(cliente)){
                                return true;
                            }
                        }
                    }
                } else if(fatturaAccompagnatoria != null){
                    Cliente fatturaAccompagnatoriaCliente = fatturaAccompagnatoria.getCliente();
                    if(fatturaAccompagnatoriaCliente != null){
                        if(fatturaAccompagnatoriaCliente.getDittaIndividuale() != null && Boolean.TRUE.equals(fatturaAccompagnatoriaCliente.getDittaIndividuale())){
                            String clienteNomeCognome = fatturaAccompagnatoriaCliente.getNome().concat(" ").concat(fatturaAccompagnatoriaCliente.getCognome());
                            if(clienteNomeCognome.contains(cliente)){
                                return true;
                            }
                        }else {
                            if(fatturaAccompagnatoriaCliente.getRagioneSociale().contains(cliente)){
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoFornitoreContains = pagamento -> {
            if(fornitore != null){
                NotaReso notaReso = pagamento.getNotaReso();
                if(notaReso != null){
                    Fornitore notaResoFornitore = notaReso.getFornitore();
                    if(notaResoFornitore != null){
                        if(notaResoFornitore.getRagioneSociale().contains(fornitore)){
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoImportoEquals = pagamento -> {
            if(importo != null){
                return pagamento.getImporto().compareTo(new BigDecimal(importo).setScale(2, RoundingMode.HALF_DOWN))==0;
            }
            return true;
        };
        Predicate<Pagamento> isPagamentoTipologiaEquals = pagamento -> {
            if(tipologia != null){
                try{
                    TipologiaPagamento tipologiaPagamento = TipologiaPagamento.valueOf(tipologia);
                    return pagamento.getTipologia().equalsIgnoreCase(tipologiaPagamento.name());
                } catch(Exception e){
                    return true;
                }
            }
            return true;
        };

        if(idDdt != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'ddt' '{}'", idDdt);
            return pagamentoService.getDdtPagamentiByIdDdt(idDdt.longValue());
        } else if(idNotaAccredito != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'notaAccredito' '{}'", idNotaAccredito);
            return pagamentoService.getNotaAccreditoPagamentiByIdNotaAccredito(idNotaAccredito.longValue());
        } else if(idNotaReso != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'notaReso' '{}'", idNotaReso);
            return pagamentoService.getNotaResoPagamentiByIdNotaReso(idNotaReso.longValue());
        } else if(idRicevutaPrivato != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'ricevutaPrivato' '{}'", idRicevutaPrivato);
            return pagamentoService.getRicevutaPrivatoPagamentiByIdRicevutaPrivato(idRicevutaPrivato.longValue());
        } else if(idFattura != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'fattura' '{}'", idFattura);
            return pagamentoService.getFatturaPagamentiByIdRicevutaPrivato(idFattura.longValue());
        } else if(idFatturaAccompagnatoria != null){
            LOGGER.info("Performing GET request for retrieving 'pagamenti' of 'fatturaAccompagnatoria' '{}'", idFatturaAccompagnatoria);
            return pagamentoService.getFatturaAccompagnatoriaPagamentiByIdRicevutaPrivato(idFatturaAccompagnatoria.longValue());
        } else {
            Set<Pagamento> pagamenti = pagamentoService.getPagamenti();
            return pagamenti.stream().filter(isPagamentoDataDaGreaterOrEquals
                    .and(isPagamentoDataALessOrEquals)
                    .and(isPagamentoClienteContains)
                    .and(isPagamentoFornitoreContains)
                    .and(isPagamentoImportoEquals)
                    .and(isPagamentoTipologiaEquals))
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
