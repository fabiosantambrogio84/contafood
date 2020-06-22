package com.contafood.service;

import com.contafood.model.GiacenzaIngrediente;
import com.contafood.model.MovimentazioneManualeIngrediente;
import com.contafood.repository.MovimentazioneManualeIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovimentazioneManualeIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(MovimentazioneManualeIngredienteService.class);

    private final MovimentazioneManualeIngredienteRepository movimentazioneManualeIngredienteRepository;

    @Autowired
    public MovimentazioneManualeIngredienteService(final MovimentazioneManualeIngredienteRepository movimentazioneManualeIngredienteRepository){
        this.movimentazioneManualeIngredienteRepository = movimentazioneManualeIngredienteRepository;
    }

    @Transactional
    public MovimentazioneManualeIngrediente create(GiacenzaIngrediente giacenzaIngrediente){
        LOGGER.info("Creating 'movimentazione manuale ingrediente'");

        MovimentazioneManualeIngrediente movimentazioneManualeIngrediente = new MovimentazioneManualeIngrediente();
        movimentazioneManualeIngrediente.setIngrediente(giacenzaIngrediente.getIngrediente());
        movimentazioneManualeIngrediente.setLotto(giacenzaIngrediente.getLotto());
        movimentazioneManualeIngrediente.setScadenza(giacenzaIngrediente.getScadenza());
        movimentazioneManualeIngrediente.setQuantita(giacenzaIngrediente.getQuantita());
        movimentazioneManualeIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        MovimentazioneManualeIngrediente createdMovimentazioneManualeIngrediente = movimentazioneManualeIngredienteRepository.save(movimentazioneManualeIngrediente);

        LOGGER.info("Created 'movimentazione manuale ingrediente' '{}'", createdMovimentazioneManualeIngrediente);
        return createdMovimentazioneManualeIngrediente;
    }

    @Transactional
    public void deleteByIngredienteIdIn(List<Long> idArticoli){
        LOGGER.info("Bulk deleting all the 'movimentazioni manuali ingrediente' by 'idArticolo' (number of elements to delete: {})", idArticoli.size());
        movimentazioneManualeIngredienteRepository.deleteByIngredienteIdIn(idArticoli);
        LOGGER.info("Bulk deleted all the specified 'movimentazioni manuali ingrediente");
    }

    public Set<MovimentazioneManualeIngrediente> getByIngredienteIdAndLottoAndScadenza(Long idIngrediente, String lotto, Date scadenza){
        LOGGER.info("Retrieving 'movimentazioni manuali ingredienti' by 'idIngrediente' '{}', 'lotto' '{}' and 'scadenza' '{}'", idIngrediente, lotto, scadenza);
        Set<MovimentazioneManualeIngrediente> movimentazioniManualiIngredienti = movimentazioneManualeIngredienteRepository.findByIngredienteIdAndLotto(idIngrediente, lotto);
        if(movimentazioniManualiIngredienti != null && !movimentazioniManualiIngredienti.isEmpty()){
            if(scadenza != null){
                movimentazioniManualiIngredienti = movimentazioniManualiIngredienti.stream()
                        .filter(da -> (da.getScadenza() != null && da.getScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0)).collect(Collectors.toSet());
            }
        }
        LOGGER.info("Retrieved '{}' 'movimentazioni manuali ingredienti'", movimentazioniManualiIngredienti.size());
        return movimentazioniManualiIngredienti;
    }

}
