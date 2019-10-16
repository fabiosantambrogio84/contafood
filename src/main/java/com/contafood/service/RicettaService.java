package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ricetta;
import com.contafood.model.RicettaIngrediente;
import com.contafood.repository.RicettaIngredienteRepository;
import com.contafood.repository.RicettaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class RicettaService {

    private static Logger LOGGER = LoggerFactory.getLogger(RicettaService.class);

    private final RicettaRepository ricettaRepository;
    private final RicettaIngredienteService ricettaIngredienteService;

    @Autowired
    public RicettaService(final RicettaRepository ricettaRepository, final RicettaIngredienteService ricettaIngredienteService){
        this.ricettaRepository = ricettaRepository;
        this.ricettaIngredienteService = ricettaIngredienteService;
    }

    public Set<Ricetta> getAll(){
        LOGGER.info("Retrieving the list of 'ricette'");
        Set<Ricetta> ricette = ricettaRepository.findAllByOrderByCodice();
        LOGGER.info("Retrieved {} 'ricette'", ricette.size());
        return ricette;
    }

    public Ricetta getOne(Long ricettaId){
        LOGGER.info("Retrieving 'ricetta' '{}'", ricettaId);
        Ricetta ricetta = ricettaRepository.findById(ricettaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ricetta' '{}'", ricetta);
        return ricetta;
    }

    public Ricetta create(Ricetta ricetta){
        LOGGER.info("Creating 'ricetta'");
        Ricetta createdRicetta = ricettaRepository.save(ricetta);
        Float pesoTotale = ricetta.getPesoTotale();
        createdRicetta.getRicettaIngredienti().stream().forEach(ri -> {
            Float percentualeNotRounded = (ri.getQuantita()*100)/pesoTotale;
            float percentuale = (float)Math.round(percentualeNotRounded*100)/100;
            ri.setPercentuale(percentuale);
            ri.getId().setRicettaId(createdRicetta.getId());
            ricettaIngredienteService.create(ri);
        });
        LOGGER.info("Created 'ricetta' '{}'", createdRicetta);
        return createdRicetta;
    }

    @Transactional
    public Ricetta update(Ricetta ricetta){
        LOGGER.info("Updating 'ricetta'");
        Set<RicettaIngrediente> ricettaIngredienti = ricetta.getRicettaIngredienti();
        ricetta.setRicettaIngredienti(new HashSet<>());
        ricettaIngredienteService.deleteByRicettaId(ricetta.getId());

        Ricetta updatedRicetta = ricettaRepository.save(ricetta);
        ricettaIngredienti.stream().forEach(ri -> {
            ri.getId().setRicettaId(updatedRicetta.getId());
            ricettaIngredienteService.create(ri);
        });
        LOGGER.info("Updated 'ricetta' '{}'", updatedRicetta);
        return updatedRicetta;
    }

    @Transactional
    public void delete(Long ricettaId){
        LOGGER.info("Deleting 'ricetta' '{}'", ricettaId);
        ricettaIngredienteService.deleteByRicettaId(ricettaId);
        ricettaRepository.deleteById(ricettaId);
        LOGGER.info("Deleted 'ricetta' '{}'", ricettaId);
    }

}
