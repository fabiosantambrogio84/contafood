package com.contafood.controller;

import com.contafood.model.Produzione;
import com.contafood.model.ProduzioneConfezione;
import com.contafood.model.beans.PageResponse;
import com.contafood.model.views.VProduzione;
import com.contafood.model.views.VProduzioneEtichetta;
import com.contafood.service.ProduzioneService;
import com.contafood.util.ResponseUtils;
import com.contafood.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping(path="/produzioni")
public class ProduzioneController {

    private final ProduzioneService produzioneService;

    public ProduzioneController(final ProduzioneService produzioneService){
        this.produzioneService = produzioneService;
    }

    @RequestMapping(method = GET, path = "/search")
    @CrossOrigin
    public PageResponse search(@RequestParam(name = "draw", required = false) Integer draw,
                               @RequestParam(name = "start", required = false) Integer start,
                               @RequestParam(name = "length", required = false) Integer length,
                               @RequestParam(name = "codice", required = false) Integer codice,
                               @RequestParam(name = "ricetta", required = false) String ricetta,
                               @RequestParam(name = "barcodeEan13", required = false) String barcodeEan13,
                               @RequestParam(name = "barcodeEan128", required = false) String barcodeEan128,
                               @RequestParam Map<String,String> allRequestParams) {
        log.info("Performing GET request for retrieving list of 'produzioni'");
        log.info("Request params: draw {}, start {}, length {}, codice {}, ricetta {}, barcodeEan13 {}, barcodeEan128 {}", draw, start, length, codice, ricetta, barcodeEan13, barcodeEan128);

        List<VProduzione> data = produzioneService.getAllByFilters(draw, start, length, Utils.getSortOrders(allRequestParams), codice, ricetta, barcodeEan13, barcodeEan128);
        Integer recordsCount = produzioneService.getCountByFilters(codice, ricetta, barcodeEan13, barcodeEan128);

        return ResponseUtils.createPageResponse(draw, recordsCount, recordsCount, data);
    }

    @RequestMapping(method = GET, path = "/search-lotto")
    @CrossOrigin
    public Set<VProduzione> searchByLotto(@RequestParam(name = "lotto") String lotto) {
        log.info("Performing GET request for retrieving list of 'produzioni' filtered by 'lotto' {}", lotto);
        return produzioneService.getAllByLotto(lotto);
    }

    @RequestMapping(method = GET, path = "/{produzioneId}")
    @CrossOrigin
    public Produzione getOne(@PathVariable final Long produzioneId) {
        log.info("Performing GET request for retrieving 'produzione' '{}'", produzioneId);
        return produzioneService.getOne(produzioneId);
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Produzione create(@RequestBody final Produzione produzione){
        log.info("Performing POST request for creating 'produzione'");
        return produzioneService.create(produzione);
    }

    /*
    @RequestMapping(method = PUT, path = "/{produzioneId}")
    @CrossOrigin
    public Produzione update(@PathVariable final Long produzioneId, @RequestBody final Produzione produzione){
        log.info("Performing PUT request for updating 'produzione' '{}'", produzioneId);
        if (!Objects.equals(produzioneId, produzione.getId())) {
            throw new CannotChangeResourceIdException();
        }
        return produzioneService.update(produzione);
    }
    */

    @RequestMapping(method = DELETE, path = "/{produzioneId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final Long produzioneId){
        log.info("Performing DELETE request for deleting 'produzione' '{}'", produzioneId);
        produzioneService.delete(produzioneId);
    }

    @RequestMapping(method = GET, path = "/{produzioneId}/confezioni")
    @CrossOrigin
    public Set<ProduzioneConfezione> getConfezioni(@PathVariable final Long produzioneId) {
        log.info("Performing GET request for retrieving 'produzioneConfezioni' for produzione '{}'", produzioneId);
        return produzioneService.getProduzioneConfezioni(produzioneId);
    }

    @RequestMapping(method = GET, path = "/{produzioneId}/etichetta")
    @CrossOrigin
    public VProduzioneEtichetta getProduzioneEtichetta(@PathVariable final Long produzioneId) {
        log.info("Performing GET request for retrieving data for 'etichetta' of 'produzione' '{}'", produzioneId);
        return produzioneService.getProduzioneEtichetta(produzioneId);
    }
}
