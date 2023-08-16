package com.contafood.controller;

import com.contafood.service.EtichettaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping(path="/etichette")
public class EtichettaController {

    private final EtichettaService etichettaService;

    public EtichettaController(final EtichettaService etichettaService){
        this.etichettaService = etichettaService;
    }

    @RequestMapping(method = POST, path="/genera")
    @ResponseStatus(CREATED)
    @CrossOrigin
    public Map<String, String> generate(@RequestParam(value = "idProduzione", required = false) Long idProduzione,
                        @RequestParam("articolo") String articolo,
                        @RequestParam("ingredienti") String ingredienti,
                        @RequestParam("ingredienti2") String ingredienti2,
                        @RequestParam("conservazione") String conservazione,
                        @RequestParam("valoriNutrizionali") String valoriNutrizionali,
                        @RequestParam("dataConsumazione") Date dataConsumazione,
                        @RequestParam("lotto") String lotto,
                        @RequestParam("peso") Double peso,
                        @RequestParam("disposizioniComune") String disposizioniComune,
                        @RequestParam("footer") String footer,
                        @RequestParam("barcodeEan13File") MultipartFile barcodeEan13File,
                        @RequestParam("barcodeEan128File") MultipartFile barcodeEan128File) throws Exception{
        log.info("Performing POST request for generating 'etichetta'");

        Map<String, String> result = etichettaService.generate(idProduzione, articolo, ingredienti, ingredienti2, conservazione, valoriNutrizionali, dataConsumazione, lotto, peso, disposizioniComune, footer, barcodeEan13File, barcodeEan128File);

        log.info("Successfully generated 'etichetta'");

        return result;
    }

    @RequestMapping(method = GET, path = "/{etichettaId}")
    @CrossOrigin
    public String getHtml(@PathVariable final String etichettaId) {
        log.info("Performing GET request for retrieving 'etichetta' '{}' as html", etichettaId);
        return etichettaService.get(etichettaId).getHtml();
    }

    @RequestMapping(method = DELETE, path = "/{etichettaId}")
    @ResponseStatus(NO_CONTENT)
    @CrossOrigin
    public void delete(@PathVariable final String etichettaId){
        log.info("Performing DELETE request for deleting 'etichetta' '{}'", etichettaId);
        etichettaService.delete(etichettaId);
    }

}