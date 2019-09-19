package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Produzione;
import com.contafood.model.ProduzioneIngrediente;
import com.contafood.repository.ProduzioneRepository;
import com.contafood.util.LottoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProduzioneService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProduzioneService.class);

    private final ProduzioneRepository produzioneRepository;
    private final ProduzioneIngredienteService produzioneIngredienteService;

    @Autowired
    public ProduzioneService(final ProduzioneRepository produzioneRepository, final ProduzioneIngredienteService produzioneIngredienteService){
        this.produzioneRepository = produzioneRepository;
        this.produzioneIngredienteService = produzioneIngredienteService;
    }

    public Set<Produzione> getAll(){
        LOGGER.info("Retrieving the list of 'produzioni'");
        Set<Produzione> produzioni = produzioneRepository.findAll();
        LOGGER.info("Retrieved {} 'produzioni'", produzioni.size());
        return produzioni;
    }

    public Produzione getOne(Long produzioneId){
        LOGGER.info("Retrieving 'produzione' '{}'", produzioneId);
        Produzione produzione = produzioneRepository.findById(produzioneId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'produzione' '{}'", produzione);
        return produzione;
    }

    public Produzione create(Produzione produzione){
        LOGGER.info("Creating 'produzione'");
        LocalDate today = LocalDate.now();
        Integer anno = today.getYear();
        Integer giorno = today.getDayOfYear();
        Integer numeroProgressivo = produzioneRepository.findNextNumeroProgressivoByLottoAnnoAndLottoGiorno(anno, giorno);
        String lotto = LottoUtils.createLottoProduzione(anno, giorno, numeroProgressivo);

        produzione.setLottoAnno(anno);
        produzione.setLottoGiorno(giorno);
        produzione.setLottoNumeroProgressivo(numeroProgressivo);
        produzione.setLotto(lotto);

        Produzione createdProduzione = produzioneRepository.save(produzione);
        createdProduzione.getProduzioneIngredienti().stream().forEach(pi -> {
            pi.getId().setProduzioneId(createdProduzione.getId());
            produzioneIngredienteService.create(pi);
        });
        LOGGER.info("Created 'produzione' '{}'", createdProduzione);
        return createdProduzione;
    }

    @Transactional
    public Produzione update(Produzione produzione){
        LOGGER.info("Updating 'produzione'");
        Set<ProduzioneIngrediente> produzioneIngredienti = produzione.getProduzioneIngredienti();
        produzione.setProduzioneIngredienti(new HashSet<>());
        produzioneIngredienteService.deleteByProduzioneId(produzione.getId());

        Produzione updatedProduzione = produzioneRepository.save(produzione);
        produzioneIngredienti.stream().forEach(pi -> {
            pi.getId().setProduzioneId(updatedProduzione.getId());
            produzioneIngredienteService.create(pi);
        });
        LOGGER.info("Updated 'produzione' '{}'", updatedProduzione);
        return updatedProduzione;
    }

    @Transactional
    public void delete(Long produzioneId){
        LOGGER.info("Deleting 'produzione' '{}'", produzioneId);
        produzioneIngredienteService.deleteByProduzioneId(produzioneId);
        produzioneRepository.deleteById(produzioneId);
        LOGGER.info("Deleted 'produzione' '{}'", produzioneId);
    }

}
