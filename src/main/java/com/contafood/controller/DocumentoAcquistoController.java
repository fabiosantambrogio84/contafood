package com.contafood.controller;

import com.contafood.model.beans.PageResponse;
import com.contafood.model.views.VDocumentoAcquisto;
import com.contafood.service.DocumentoAcquistoService;
import com.contafood.util.ResponseUtils;
import com.contafood.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SuppressWarnings({"unused"})
@Slf4j
@RestController
@RequestMapping(path="/documenti-acquisto")
public class DocumentoAcquistoController {

    private final DocumentoAcquistoService documentoAcquistoService;

    @Autowired
    public DocumentoAcquistoController(final DocumentoAcquistoService documentoAcquistoService){
        this.documentoAcquistoService = documentoAcquistoService;
    }

    @RequestMapping(method = GET, path = "/search")
    @CrossOrigin
    public PageResponse search(@RequestParam(name = "draw", required = false) Integer draw,
                               @RequestParam(name = "start", required = false) Integer start,
                               @RequestParam(name = "length", required = false) Integer length,
                               @RequestParam(name = "fornitore", required = false) String fornitore,
                               @RequestParam(name = "numDocumento", required = false) String numDocumento,
                               @RequestParam(name = "tipoDocumento", required = false) String tipoDocumento,
                               @RequestParam(name = "dataDa", required = false) Date dataDa,
                               @RequestParam(name = "dataA", required = false) Date dataA,
                               @RequestParam(name = "idFornitore", required = false) Long idFornitore,
                               @RequestParam(name = "fatturato", required = false) Boolean fatturato,
                               @RequestParam Map<String,String> allRequestParams) {
        log.info("Performing GET request for searching list of 'documenti-acquisto'");
        log.info("Request params: draw {}, start {}, length {}, fornitore {}, numDocumento {}, tipoDocumento {}, dataDa {}, dataA {}, idFornitore {}, fatturato {}",
                draw, start, length, fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        List<VDocumentoAcquisto> data = documentoAcquistoService.getAllByFilters(draw, start, length, Utils.getSortOrders(allRequestParams), fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);
        Integer recordsCount = documentoAcquistoService.getCountByFilters(fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        return ResponseUtils.createPageResponse(draw, recordsCount, recordsCount, data);
    }
}