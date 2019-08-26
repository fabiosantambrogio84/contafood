package com.contafood.service;

import com.contafood.model.RicettaIngrediente;
import com.contafood.repository.RicettaIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RicettaIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(RicettaIngredienteService.class);

    private final RicettaIngredienteRepository ricettaIngredienteRepository;

    @Autowired
    public RicettaIngredienteService(final RicettaIngredienteRepository ricettaIngredienteRepository){
        this.ricettaIngredienteRepository = ricettaIngredienteRepository;
    }

    public Set<RicettaIngrediente> findAll(){
        LOGGER.info("Retrieving the list of 'ricetta ingredienti'");
        Set<RicettaIngrediente> ricettaIngredienti = ricettaIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'ricetta ingredienti'", ricettaIngredienti.size());
        return ricettaIngredienti;
    }

    public Set<RicettaIngrediente> findByRicettaId(Long ricettaId){
        LOGGER.info("Retrieving the list of 'ricetta ingredienti' for 'ricetta' '{}'", ricettaId);
        Set<RicettaIngrediente> ricettaIngredienti = ricettaIngredienteRepository.findByRicettaId(ricettaId);
        LOGGER.info("Retrieved {} 'ricetta ingredienti' for 'ricetta' '{}'", ricettaIngredienti.size(), ricettaId);
        return ricettaIngredienti;
    }

    public Set<RicettaIngrediente> findByIngredienteId(Long ingredienteId){
        LOGGER.info("Retrieving the list of 'ricetta ingredienti' for 'ingrediente' '{}'", ingredienteId);
        Set<RicettaIngrediente> ricettaIngredienti = ricettaIngredienteRepository.findByIngredienteId(ingredienteId);
        LOGGER.info("Retrieved {} 'ricetta ingredienti' for 'ingrediente' '{}'", ricettaIngredienti.size(), ingredienteId);
        return ricettaIngredienti;
    }

    public RicettaIngrediente create(RicettaIngrediente ricettaIngrediente){
        LOGGER.info("Creating 'ricetta ingrediente'");
        RicettaIngrediente createdRicettaIngrediente = ricettaIngredienteRepository.save(ricettaIngrediente);
        LOGGER.info("Created 'ricetta ingrediente' '{}'", createdRicettaIngrediente);
        return createdRicettaIngrediente;
    }

    public RicettaIngrediente update(RicettaIngrediente ricettaIngrediente){
        LOGGER.info("Updating 'ricetta ingrediente'");
        RicettaIngrediente updatedRicettaIngrediente = ricettaIngredienteRepository.save(ricettaIngrediente);
        LOGGER.info("Updated 'ricetta ingrediente' '{}'", updatedRicettaIngrediente);
        return updatedRicettaIngrediente;
    }

    public void deleteByRicettaId(Long ricettaId){
        LOGGER.info("Deleting 'ricetta ingrediente' by 'ricetta' '{}'", ricettaId);
        ricettaIngredienteRepository.deleteByRicettaId(ricettaId);
        LOGGER.info("Deleted 'ricetta ingrediente' by 'ricetta' '{}'", ricettaId);
    }

    public void deleteByIngredienteId(Long ingredienteId){
        LOGGER.info("Deleting 'ricetta ingrediente' by 'ingrediente' '{}'", ingredienteId);
        ricettaIngredienteRepository.deleteByIngredienteId(ingredienteId);
        LOGGER.info("Deleted 'ricetta ingrediente' by 'ingrediente' '{}'", ingredienteId);
    }
}
