package com.contafood.service;

import com.contafood.model.views.VDocumentoAcquistoIngrediente;
import com.contafood.model.views.VDocumentoVenditaIngrediente;
import com.contafood.model.views.VProduzioneConfezione;
import com.contafood.model.views.VProduzioneIngrediente;
import com.contafood.repository.views.VDocumentoAcquistoIngredienteRepository;
import com.contafood.repository.views.VDocumentoVenditaIngredienteRepository;
import com.contafood.repository.views.VProduzioneConfezioneRepository;
import com.contafood.repository.views.VProduzioneIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RicercaLottoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RicercaLottoService.class);

    private final VProduzioneIngredienteRepository vProduzioneIngredienteRepository;
    private final VProduzioneConfezioneRepository vProduzioneConfezioneRepository;
    private final VDocumentoAcquistoIngredienteRepository vDocumentoAcquistoIngredienteRepository;
    private final VDocumentoVenditaIngredienteRepository vDocumentoVenditaIngredienteRepository;

    public RicercaLottoService(final VProduzioneIngredienteRepository vProduzioneIngredienteRepository,
                               final VProduzioneConfezioneRepository vProduzioneConfezioneRepository,
                               final VDocumentoAcquistoIngredienteRepository vDocumentoAcquistoIngredienteRepository,
                               final VDocumentoVenditaIngredienteRepository vDocumentoVenditaIngredienteRepository){
        this.vProduzioneIngredienteRepository = vProduzioneIngredienteRepository;
        this.vProduzioneConfezioneRepository = vProduzioneConfezioneRepository;
        this.vDocumentoAcquistoIngredienteRepository = vDocumentoAcquistoIngredienteRepository;
        this.vDocumentoVenditaIngredienteRepository = vDocumentoVenditaIngredienteRepository;
    }

    public Set<VProduzioneIngrediente> getProduzioniIngredientiByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'produzioni-ingredienti' filtered by 'lotto' '{}'", lotto);
        Set<VProduzioneIngrediente> produzioniIngredienti = vProduzioneIngredienteRepository.findAllByLottoIngrediente(lotto);
        LOGGER.info("Retrieved {} 'produzioni-ingredienti'", produzioniIngredienti.size());
        return produzioniIngredienti;
    }

    public Set<VProduzioneConfezione> getProduzioniConfezioniByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'produzioni-confezioni' filtered by 'lotto' '{}'", lotto);
        Set<VProduzioneConfezione> produzioniConfezioni = vProduzioneConfezioneRepository.findAllByLottoConfezione(lotto);
        LOGGER.info("Retrieved {} 'produzioni-confezioni'", produzioniConfezioni.size());
        return produzioniConfezioni;
    }

    public Set<VDocumentoAcquistoIngrediente> getDocumentiAcquistiIngredientiByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'documenti-acquisti-ingredienti' filtered by 'lotto' '{}'", lotto);
        Set<VDocumentoAcquistoIngrediente> documentiAcquistiIngredienti = vDocumentoAcquistoIngredienteRepository.findAllByLottoIngrediente(lotto);
        LOGGER.info("Retrieved {} 'documenti-acquisti-ingredienti'", documentiAcquistiIngredienti.size());
        return documentiAcquistiIngredienti;
    }

    public Set<VDocumentoVenditaIngrediente> getDocumentiVenditeIngredientiByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'documenti-vendite-ingredienti' filtered by 'lotto' '{}'", lotto);
        Set<VDocumentoVenditaIngrediente> documentiVenditaIngredienti = vDocumentoVenditaIngredienteRepository.findAllByLottoIngrediente(lotto);
        LOGGER.info("Retrieved {} 'documenti-vendite-ingredienti'", documentiVenditaIngredienti.size());
        return documentiVenditaIngredienti;
    }
}
