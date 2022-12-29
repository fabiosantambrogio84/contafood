package com.contafood.controller;

import com.contafood.model.views.VDocumentoAcquistoIngrediente;
import com.contafood.model.views.VProduzioneConfezione;
import com.contafood.model.views.VProduzioneIngrediente;
import com.contafood.service.RicercaLottoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/search-lotto-ingredienti")
public class RicercaLottoIngredienteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RicercaLottoIngredienteController.class);

    private final RicercaLottoService ricercaLottoService;

    public RicercaLottoIngredienteController(final RicercaLottoService ricercaLottoService){
        this.ricercaLottoService = ricercaLottoService;
    }

    @RequestMapping(method = GET, path = "/produzioni-ingredienti")
    @CrossOrigin
    public Set<VProduzioneIngrediente> searchProduzioniIngredientiByLotto(@RequestParam(name = "lotto") String lotto) {
        LOGGER.info("Performing GET request for retrieving list of 'produzioni-ingredienti' filtered by 'lotto' {}", lotto);
        return ricercaLottoService.getProduzioniIngredientiByLotto(lotto);
    }

    @RequestMapping(method = GET, path = "/produzioni-confezioni")
    @CrossOrigin
    public Set<VProduzioneConfezione> searchProduzioniConfezioniByLotto(@RequestParam(name = "lotto") String lotto) {
        LOGGER.info("Performing GET request for retrieving list of 'produzioni-confezioni' filtered by 'lotto' {}", lotto);
        return ricercaLottoService.getProduzioniConfezioniByLotto(lotto);
    }

    @RequestMapping(method = GET, path = "/documenti-acquisti")
    @CrossOrigin
    public Set<VDocumentoAcquistoIngrediente> searchDocumentiAcquistiIngredientiByLotto(@RequestParam(name = "lotto") String lotto) {
        LOGGER.info("Performing GET request for retrieving list of 'documenti-acquisti-ingredientii' filtered by 'lotto' {}", lotto);
        return ricercaLottoService.getDocumentiAcquistiIngredientiByLotto(lotto);
    }
}
