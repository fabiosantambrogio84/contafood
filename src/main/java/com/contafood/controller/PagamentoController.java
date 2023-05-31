package com.contafood.controller;

import com.contafood.model.Pagamento;
import com.contafood.model.views.VPagamento;
import com.contafood.service.PagamentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping(path="/pagamenti")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(final PagamentoService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Pagamento> getPagamenti(@RequestParam(name = "idDdt", required = false) Integer idDdt,
                                       @RequestParam(name = "idDdtAcquisto", required = false) Integer idDdtAcquisto,
                                       @RequestParam(name = "idNotaAccredito", required = false) Integer idNotaAccredito,
                                       @RequestParam(name = "idNotaReso", required = false) Integer idNotaReso,
                                       @RequestParam(name = "idRicevutaPrivato", required = false) Integer idRicevutaPrivato,
                                       @RequestParam(name = "idFattura", required = false) Integer idFattura,
                                       @RequestParam(name = "idFatturaAccompagnatoria", required = false) Integer idFatturaAccompagnatoria,
                                       @RequestParam(name = "idFatturaAcquisto", required = false) Integer idFatturaAcquisto,
                                       @RequestParam(name = "idFatturaAccompagnatoriaAcquisto", required = false) Integer idFatturaAccompagnatoriaAcquisto) {
        log.info("Performing GET request for retrieving all 'pagamenti'");
        log.info("Request params: idDdt {}, idDdtAcquisto {}, idNotaAccredito {}, idNotaReso {}, idRicevutaPrivato {}, idFattura {}, idFatturaAccompagnatoria {}, idFatturaAcquisto {}, idFatturaAccompagnatoriaAcquisto {}",
                idDdt, idDdtAcquisto, idNotaAccredito, idNotaReso, idRicevutaPrivato, idFattura, idFatturaAccompagnatoria, idFatturaAcquisto, idFatturaAccompagnatoriaAcquisto);

        if(idDdt != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'ddt' '{}'", idDdt);
            return pagamentoService.getDdtPagamentiByIdDdt(idDdt.longValue());
        } else if(idDdtAcquisto != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'ddt-acquisto' '{}'", idDdtAcquisto);
            return pagamentoService.getDdtAcquistoPagamentiByIdDdtAcquisto(idDdtAcquisto.longValue());
        } else if(idNotaAccredito != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'notaAccredito' '{}'", idNotaAccredito);
            return pagamentoService.getNotaAccreditoPagamentiByIdNotaAccredito(idNotaAccredito.longValue());
        } else if(idNotaReso != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'notaReso' '{}'", idNotaReso);
            return pagamentoService.getNotaResoPagamentiByIdNotaReso(idNotaReso.longValue());
        } else if(idRicevutaPrivato != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'ricevutaPrivato' '{}'", idRicevutaPrivato);
            return pagamentoService.getRicevutaPrivatoPagamentiByIdRicevutaPrivato(idRicevutaPrivato.longValue());
        } else if(idFattura != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'fattura' '{}'", idFattura);
            return pagamentoService.getFatturaPagamentiByIdFattura(idFattura.longValue());
        } else if(idFatturaAccompagnatoria != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'fatturaAccompagnatoria' '{}'", idFatturaAccompagnatoria);
            return pagamentoService.getFatturaAccompagnatoriaPagamentiByIdFatturaAccompagnatoria(idFatturaAccompagnatoria.longValue());
        } else if(idFatturaAcquisto != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'fatturaAcquisto' '{}'", idFatturaAcquisto);
            return pagamentoService.getFatturaAcquistoPagamentiByIdFatturaAcquisto(idFatturaAcquisto.longValue());
        } else if(idFatturaAccompagnatoriaAcquisto != null){
            log.info("Performing GET request for retrieving 'pagamenti' of 'fatturaAccompagnatoriaAcquisto' '{}'", idFatturaAccompagnatoriaAcquisto);
            return pagamentoService.getFatturaAccompagnatoriaAcquistoPagamentiByIdFatturaAccompagnatoriaAcquisto(idFatturaAccompagnatoriaAcquisto.longValue());
        }
        return null;
    }

    @RequestMapping(method = GET, path = "/search")
    @CrossOrigin
    public List<VPagamento> search(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                   @RequestParam(name = "dataA", required = false) Date dataA,
                                   @RequestParam(name = "cliente", required = false) String cliente,
                                   @RequestParam(name = "fornitore", required = false) String fornitore,
                                   @RequestParam(name = "importo", required = false) Float importo,
                                   @RequestParam(name = "tipologia", required = false) String tipologia) {
        log.info("Performing GET request for searching list of 'pagamenti'");
        log.info("Request params: tipologia {}, dataDa {}, dataA {}, cliente {}, fornitore {}, importo {}",
                tipologia, dataDa, dataA, cliente, fornitore, importo);

        return pagamentoService.getAllByFilters(tipologia, dataDa, dataA, cliente, fornitore, importo);
    }

    @RequestMapping(method = GET, path = "/{pagamentoId}")
    @CrossOrigin
    public Pagamento getPagamento(@PathVariable final Long pagamentoId) {
        log.info("Performing GET request for retrieving 'pagamento' '{}'", pagamentoId);
        return pagamentoService.getPagamento(pagamentoId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Pagamento createPagamento(@RequestBody final Pagamento pagamento){
        log.info("Performing POST request for creating 'pagamento'");
        return pagamentoService.createPagamento(pagamento);
    }

    @RequestMapping(method = DELETE, path = "/{pagamentoId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void deletePagamento(@PathVariable final Long pagamentoId){
        log.info("Performing DELETE request for deleting 'pagamento' '{}'", pagamentoId);
        pagamentoService.deletePagamento(pagamentoId);
    }
}