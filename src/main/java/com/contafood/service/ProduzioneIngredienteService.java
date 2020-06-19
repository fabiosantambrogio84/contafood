package com.contafood.service;

import com.contafood.model.ProduzioneIngrediente;
import com.contafood.repository.ProduzioneIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<ProduzioneIngrediente> getByIngredienteIdAndLottoAndScadenza(Long idIngrediente, String lotto, Date scadenza){
        LOGGER.info("Retrieving the list of 'produzione ingredienti' for 'idIngrediente' '{}', 'lotto' '{}' and 'scadenza' '{}'", idIngrediente, lotto, scadenza);
        Set<ProduzioneIngrediente> produzioneIngredienti = produzioneIngredienteRepository.findByIngredienteIdAndLotto(idIngrediente, lotto);
        if(produzioneIngredienti != null && !produzioneIngredienti.isEmpty()){
            if(scadenza != null){
                produzioneIngredienti = produzioneIngredienti.stream()
                        .filter(pi -> (pi.getScadenza() != null && pi.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0)).collect(Collectors.toSet());
            }
        }
        LOGGER.info("Retrieved {} 'produzione ingredienti'", produzioneIngredienti.size());
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
