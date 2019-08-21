package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Parametro;
import com.contafood.model.Ricetta;
import com.contafood.model.RicettaIngrediente;
import com.contafood.repository.RicettaIngredienteRepository;
import com.contafood.repository.RicettaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class RicettaService {

    private final RicettaRepository ricettaRepository;
    private final RicettaIngredienteRepository ricettaIngredienteRepository;
    private final ParametroService parametroService;

    private final static String COSTO_ORARIO = "COSTO_ORARIO_PREPARAZIONE_RICETTA";

    @Autowired
    public RicettaService(final RicettaRepository ricettaRepository, final RicettaIngredienteRepository ricettaIngredienteRepository, final ParametroService parametroService){
        this.ricettaRepository = ricettaRepository;
        this.ricettaIngredienteRepository = ricettaIngredienteRepository;
        this.parametroService = parametroService;
    }

    public Set<Ricetta> getAll(){
        return ricettaRepository.findAll();
    }

    public Ricetta getOne(Long ricettaId){
        Ricetta ricetta = ricettaRepository.findById(ricettaId).orElseThrow(ResourceNotFoundException::new);
        Parametro parametro = parametroService.findByNome(COSTO_ORARIO);
        ricetta.setCostoOrarioPreparazione(Float.parseFloat(parametro.getValore()));
        return ricetta;
    }

    public Ricetta create(Ricetta ricetta){
        Ricetta createdRicetta = ricettaRepository.save(ricetta);
        createdRicetta.getRicettaIngredienti().stream().forEach(ri -> {
            ri.getId().setRicettaId(createdRicetta.getId());
            ricettaIngredienteRepository.save(ri);
        });
        return createdRicetta;
    }

    @Transactional
    public Ricetta update(Ricetta ricetta){
        Set<RicettaIngrediente> ricettaIngredienti = ricetta.getRicettaIngredienti();
        ricetta.setRicettaIngredienti(new HashSet<>());
        ricettaIngredienteRepository.deleteByRicettaId(ricetta.getId());

        Ricetta updatedRicetta = ricettaRepository.save(ricetta);
        ricettaIngredienti.stream().forEach(ri -> {
            ri.getId().setRicettaId(updatedRicetta.getId());
            ricettaIngredienteRepository.save(ri);
        });
        return updatedRicetta;
    }

    @Transactional
    public void delete(Long ricettaId){
        ricettaIngredienteRepository.deleteByRicettaId(ricettaId);
        ricettaRepository.deleteById(ricettaId);
    }
}
