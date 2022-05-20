package com.contafood.controller;

import com.contafood.model.Pagamento;
import com.contafood.model.views.VPagamento;
import com.contafood.service.PagamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path="/pagamenti")
public class PagamentoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PagamentoController.class);

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(final PagamentoService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public Set<Pagamento> getDdtPagamenti(@RequestParam(name = "idDdt", required = false) Integer idDdt,
                                          @RequestParam(name = "idNotaAccredito", required = false) Integer idNotaAccredito,
                                          @RequestParam(name = "idNotaReso", required = false) Integer idNotaReso,
                                          @RequestParam(name = "idRicevutaPrivato", required = false) Integer idRicevutaPrivato,
                                          @RequestParam(name = "idFattura", required = false) Integer idFattura,
                                          @RequestParam(name = "idFatturaAccompagnatoria", required = false) Integer idFatturaAccompagnatoria) {
        LOGGER.info("Performing GET request for retrieving all 'pagamenti'");
        LOGGER.info("Request params: idDdt {}, notaAccredito {}, notaReso {}, ricevutaPrivato {}, fattura {}, fatturaAccompagnatoria {}",
                idDdt, idNotaAccredito, idNotaReso, idRicevutaPrivato, idFattura, idFatturaAccompagnatoria);

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
        LOGGER.info("Performing GET request for searching list of 'pagamenti'");
        LOGGER.info("Request params: tipologia {}, dataDa {}, dataA {}, cliente {}, fornitore {}, importo {}",
                tipologia, dataDa, dataA, cliente, fornitore, importo);

        return pagamentoService.getAllByFilters(tipologia, dataDa, dataA, cliente, fornitore, importo);
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
