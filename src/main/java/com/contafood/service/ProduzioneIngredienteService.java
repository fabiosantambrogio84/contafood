package com.contafood.service;

import com.contafood.model.ProduzioneIngrediente;
import com.contafood.repository.ProduzioneIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProduzioneIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProduzioneIngredienteService.class);

    private final ProduzioneIngredienteRepository produzioneIngredienteRepository;

    @Autowired
    public ProduzioneIngredienteService(final ProduzioneIngredienteRepository produzioneIngredienteRepository){
        this.produzioneIngredienteRepository = produzioneIngredienteRepository;
    }

    public Set<ProduzioneIngrediente> findAll(){
        LOGGER.info("Retrieving the list of 'produzione ingredienti'");
        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'produzione ingredienti'", produzioneIngredienti.size());
        return produzioneIngredienti;
    }

    public Set<ProduzioneIngrediente> findByProduzioneId(Long produzioneId){
        LOGGER.info("Retrieving the list of 'produzione ingredienti' for 'produzione' '{}'", produzioneId);
        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteRepository.findByProduzioneId(produzioneId);
        LOGGER.info("Retrieved {} 'produzione ingredienti' for 'produzione' '{}'", produzioneIngredienti.size(), produzioneId);
        return produzioneIngredienti;
    }

    public Set<ProduzioneIngrediente> findByIngredienteId(Long ingredienteId){
        LOGGER.info("Retrieving the list of 'produzione ingredienti' for 'ingrediente' '{}'", ingredienteId);
        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteRepository.findByIngredienteId(ingredienteId);
        LOGGER.info("Retrieved {} 'produzione ingredienti' for 'ingrediente' '{}'", produzioneIngredienti.size(), ingredienteId);
        return produzioneIngredienti;
    }

    public ProduzioneIngrediente create(ProduzioneIngrediente produzioneIngrediente){
        LOGGER.info("Creating 'produzione ingrediente'");
        ProduzioneIngrediente createdProduzioneIngrediente = produzioneIngredienteRepository.save(produzioneIngrediente);
        LOGGER.info("Created 'produzione ingrediente' '{}'", createdProduzioneIngrediente);
        return createdProduzioneIngrediente;
    }

    public ProduzioneIngrediente update(ProduzioneIngrediente produzioneIngrediente){
        LOGGER.info("Updating 'produzione ingrediente'");
        ProduzioneIngrediente updatedProduzioneIngrediente = produzioneIngredienteRepository.save(produzioneIngrediente);
        LOGGER.info("Updated 'produzione ingrediente' '{}'", updatedProduzioneIngrediente);
        return updatedProduzioneIngrediente;
    }

    public void deleteByProduzioneId(Long produzioneId){
        LOGGER.info("Deleting 'produzione ingrediente' by 'produzione' '{}'", produzioneId);
        produzioneIngredienteRepository.deleteByProduzioneId(produzioneId);
        LOGGER.info("Deleted 'produzione ingrediente' by 'produzione' '{}'", produzioneId);
    }

    public void deleteByIngredienteId(Long ingredienteId){
        LOGGER.info("Deleting 'produzione ingrediente' by 'ingrediente' '{}'", ingredienteId);
        produzioneIngredienteRepository.deleteByIngredienteId(ingredienteId);
        LOGGER.info("Deleted 'produzione ingrediente' by 'ingrediente' '{}'", ingredienteId);
    }
}
