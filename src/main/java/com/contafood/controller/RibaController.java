package com.contafood.controller;

import com.contafood.exception.GenericException;
import com.contafood.model.Cliente;
import com.contafood.model.Fattura;
import com.contafood.model.StatoFattura;
import com.contafood.model.TipoPagamento;
import com.contafood.service.AdeService;
import com.contafood.service.FatturaService;
import com.contafood.service.RibaService;
import com.contafood.util.Constants;
import com.contafood.util.RibaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/export-riba")
public class RibaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RibaController.class);

    private final FatturaService fatturaService;

    private final RibaService ribaService;

    @Autowired
    public RibaController(final FatturaService fatturaService,
                          final RibaService ribaService){
        this.fatturaService = fatturaService;
        this.ribaService = ribaService;
    }

    @RequestMapping(method = GET, produces=Constants.MEDIA_TYPE_APPLICATION_ZIP)
    @CrossOrigin
    public ResponseEntity<Resource> exportRiba(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                               @RequestParam(name = "dataA", required = false) Date dataA,
                                               @RequestParam(name = "tipoPagamento", required = false) String idTipoPagamento,
                                               @RequestParam(name = "stato", required = false) Integer idStato) throws Exception{
        LOGGER.info("Performing GET request for creating creating 'RiBa' file");
        LOGGER.info("Request params: dataDa {}, dataA {}, tipoPagamento {}, stato {}",
                dataDa, dataA, idTipoPagamento, idStato);

        if(dataDa == null){
            throw new GenericException("Parametro 'Data da' obbligatorio");
        }
        if(dataA == null){
            throw new GenericException("Parametro 'Data a' obbligatorio");
        }
        if(StringUtils.isEmpty(idTipoPagamento)){
            throw new GenericException("Parametro 'Tipo pagamento' obbligatorio");
        }
        if(idStato == null){
            throw new GenericException("Parametro 'Stato' obbligatorio");
        }

        // check if 'stato' is equals to 'DA_PAGARE'
        fatturaService.checkStatoFatturaDaPagare(idStato);

        // check if each 'idTipoPagamento' is valid for RiBa export
        List<Long> idTipiPagamento = new ArrayList<>();
        if(!StringUtils.isEmpty(idTipoPagamento)){
            Arrays.stream(idTipoPagamento.split(",")).mapToLong(id -> Long.parseLong(id)).forEach(l -> idTipiPagamento.add(l));

            for(Long idTipoPag : idTipiPagamento){
                fatturaService.checkTipoPagamentoRicevutaBancaria(idTipoPag);
            }
        }

        // retrieve TipoFattura=VENDITA
        //TipoFattura tipoFatturaVendita = fatturaService.getTipoFatturaVendita();

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
        Predicate<Fattura> isFatturaTipoPagamentoEquals = fattura -> {
            //LOGGER.info("Filter by idTipoPagamento '{}'", idTipiPagamento);
            if(idTipiPagamento != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    TipoPagamento tipoPagamento = fatturaCliente.getTipoPagamento();
                    if(tipoPagamento != null){
                        //LOGGER.info("Cliente id '{}', TipoPagamento id '{}'", fatturaCliente.getId(), tipoPagamento.getId());
                        return idTipiPagamento.contains(tipoPagamento.getId());
                    }
                }
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
        /*Predicate<Fattura> isFatturaTipoEquals = fattura -> {
            if(tipoFatturaVendita != null){
                TipoFattura tipoFattura = fattura.getTipoFattura();
                if(tipoFattura != null){
                    return tipoFattura.getId().equals(tipoFatturaVendita.getId());
                }
                return false;
            }
            return true;
        };*/

        Set<Fattura> fatture = fatturaService.getAllVendite().stream().filter(isFatturaDataDaGreaterOrEquals
                .and(isFatturaDataALessOrEquals)
                .and(isFatturaTipoPagamentoEquals)
                .and(isFatturaStatoEquals)).collect(Collectors.toSet());

        String ribaAsString = ribaService.create(fatture);
        byte[] riba = ribaAsString.getBytes(StandardCharsets.UTF_8);

        ByteArrayResource resource = new ByteArrayResource(riba);

        return ResponseEntity.ok()
                .headers(AdeService.createHttpHeaders(RibaUtils.createFileName()))
                .contentLength(riba.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_TXT))
                .body(resource);
    }

}
