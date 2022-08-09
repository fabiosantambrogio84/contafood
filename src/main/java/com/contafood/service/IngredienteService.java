package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ingrediente;
import com.contafood.repository.GiacenzaIngredienteRepository;
import com.contafood.repository.IngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class IngredienteService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IngredienteService.class);

    private final IngredienteRepository ingredienteRepository;
    private final GiacenzaIngredienteRepository giacenzaIngredienteRepository;

    @Autowired
    public IngredienteService(final IngredienteRepository ingredienteRepository,
                              final GiacenzaIngredienteRepository giacenzaIngredienteRepository){
        this.ingredienteRepository = ingredienteRepository;
        this.giacenzaIngredienteRepository = giacenzaIngredienteRepository;
    }

    public Set<Ingrediente> getAll(){
        LOGGER.info("Retrieving the list of 'ingredienti'");
        Set<Ingrediente> ingredienti = ingredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'ingredienti'", ingredienti.size());
        return ingredienti;
    }

    public Ingrediente getOne(Long ingredienteId){
        LOGGER.info("Retrieving 'ingrediente' '{}'", ingredienteId);
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ingrediente' '{}'", ingrediente);
        return ingrediente;
    }

    public Optional<Ingrediente> getByCodice(String codice){
        LOGGER.info("Retrieving 'ingrediente' with codice '{}'", codice);
        Optional<Ingrediente> ingrediente = ingredienteRepository.findByCodice(codice);
        if(ingrediente.isPresent()){
            LOGGER.info("Retrieved 'ingrediente' '{}'", ingrediente.get());
        } else {
            LOGGER.info("'ingrediente' with codice '{}' not existing", codice);
        }
        return ingrediente;
    }

    public Ingrediente create(Ingrediente ingrediente){
        LOGGER.info("Creating 'ingrediente'");
        ingrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        Ingrediente createdIngrediente = ingredienteRepository.save(ingrediente);
        LOGGER.info("Created 'ingrediente' '{}'", createdIngrediente);
        return createdIngrediente;
    }

    public Ingrediente update(Ingrediente ingrediente){
        LOGGER.info("Updating 'ingrediente'");
        Ingrediente ingredienteCurrent = ingredienteRepository.findById(ingrediente.getId()).orElseThrow(ResourceNotFoundException::new);
        ingrediente.setDataInserimento(ingredienteCurrent.getDataInserimento());

        Ingrediente updatedIngrediente = ingredienteRepository.save(ingrediente);
        LOGGER.info("Updated 'ingrediente' '{}'", updatedIngrediente);
        return updatedIngrediente;
    }

    /*public void emptyAllByFornitoreId(Long idFornitore){
        List<Ingrediente> ingredienti = ingredienteRepository.findByFornitoreId(idFornitore);
        for(Ingrediente ingrediente : ingredienti){
            ingrediente.setFornitore(null);
            update(ingrediente);
        }
    }*/

    public void delete(Long ingredienteId){
        LOGGER.info("Deleting 'giacenze' for 'ingrediente' '{}'", ingredienteId);
        giacenzaIngredienteRepository.deleteByIngredienteId(ingredienteId);
        LOGGER.info("Deleted 'giacenze' for 'ingrediente' '{}'", ingredienteId);

        LOGGER.info("Deleting 'ingrediente' '{}'", ingredienteId);
        ingredienteRepository.deleteById(ingredienteId);
        LOGGER.info("Deleted 'ingrediente' '{}'", ingredienteId);
    }
}
