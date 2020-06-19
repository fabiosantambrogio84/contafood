package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.DdtAcquistoIngrediente;
import com.contafood.model.Ingrediente;
import com.contafood.repository.DdtAcquistoIngredienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DdtAcquistoIngredienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtAcquistoIngredienteService.class);

    private final DdtAcquistoIngredienteRepository ddtAcquistoIngredienteRepository;

    private final IngredienteService ingredienteService;

    @Autowired
    public DdtAcquistoIngredienteService(final DdtAcquistoIngredienteRepository ddtAcquistoIngredienteRepository, final IngredienteService ingredienteService){
        this.ddtAcquistoIngredienteRepository = ddtAcquistoIngredienteRepository;
        this.ingredienteService = ingredienteService;
    }

    public Set<DdtAcquistoIngrediente> findAll(){
        LOGGER.info("Retrieving the list of 'ddt acquisto ingredienti'");
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = ddtAcquistoIngredienteRepository.findAll();
        LOGGER.info("Retrieved {} 'ddt acquisto ingredienti'", ddtAcquistoIngredienti.size());
        return ddtAcquistoIngredienti;
    }

    public Set<DdtAcquistoIngrediente> findByDdtAcquistoId(Long idDdtAcquisto){
        LOGGER.info("Retrieving the list of 'ddt acquisto ingredienti' of 'ddtAcquisto' {}", idDdtAcquisto);
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = ddtAcquistoIngredienteRepository.findByDdtAcquistoId(idDdtAcquisto);
        LOGGER.info("Retrieved {} 'ddt acquisto ingredienti'", ddtAcquistoIngredienti.size());
        return ddtAcquistoIngredienti;
    }

    public DdtAcquistoIngrediente create(DdtAcquistoIngrediente ddtAcquistoIngrediente){
        LOGGER.info("Creating 'ddt acquisto ingrediente'");
        ddtAcquistoIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtAcquistoIngrediente.setImponibile(computeImponibile(ddtAcquistoIngrediente));

        DdtAcquistoIngrediente createdDdtAcquistoIngrediente = ddtAcquistoIngredienteRepository.save(ddtAcquistoIngrediente);
        LOGGER.info("Created 'ddt acquisto ingrediente' '{}'", createdDdtAcquistoIngrediente);
        return createdDdtAcquistoIngrediente;
    }

    public DdtAcquistoIngrediente update(DdtAcquistoIngrediente ddtAcquistoIngrediente){
        LOGGER.info("Updating 'ddt acquisto ingrediente'");
        DdtAcquistoIngrediente ddtAcquistoIngredienteCurrent = ddtAcquistoIngredienteRepository.findById(ddtAcquistoIngrediente.getId()).orElseThrow(ResourceNotFoundException::new);
        ddtAcquistoIngrediente.setDataInserimento(ddtAcquistoIngredienteCurrent.getDataInserimento());
        ddtAcquistoIngrediente.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        ddtAcquistoIngrediente.setImponibile(computeImponibile(ddtAcquistoIngrediente));

        DdtAcquistoIngrediente updatedDdtAcquistoIngrediente = ddtAcquistoIngredienteRepository.save(ddtAcquistoIngrediente);
        LOGGER.info("Updated 'ddt acquisto ingrediente' '{}'", updatedDdtAcquistoIngrediente);
        return updatedDdtAcquistoIngrediente;
    }

    public void deleteByDdtAcquistoId(Long ddtAcquistoId){
        LOGGER.info("Deleting 'ddt acquisto ingrediente' by 'ddt' '{}'", ddtAcquistoId);
        ddtAcquistoIngredienteRepository.deleteByDdtAcquistoId(ddtAcquistoId);
        LOGGER.info("Deleted 'ddt acquisto ingrediente' by 'ddt' '{}'", ddtAcquistoId);
    }

    public Ingrediente getIngrediente(DdtAcquistoIngrediente ddtAcquistoIngrediente){
        Long ingredienteId = ddtAcquistoIngrediente.getId().getIngredienteId();
        return ingredienteService.getOne(ingredienteId);
    }

    public Set<DdtAcquistoIngrediente> getByIngredienteIdAndLottoAndScadenza(Long idIngrediente, String lotto, Date scadenza){
        LOGGER.info("Retrieving 'ddt acquisto articoli' by 'idIngrediente' '{}', 'lotto' '{}' and 'scadenza' '{}'", idIngrediente, lotto, scadenza);
        Set<DdtAcquistoIngrediente> ddtAcquistoIngredienti = ddtAcquistoIngredienteRepository.findByIngredienteIdAndLotto(idIngrediente, lotto);
        if(ddtAcquistoIngredienti != null && !ddtAcquistoIngredienti.isEmpty()){
            if(scadenza != null){
                ddtAcquistoIngredienti = ddtAcquistoIngredienti.stream()
                        .filter(dai -> (dai.getDataScadenza() != null && dai.getDataScadenza().toLocalDate().compareTo(scadenza.toLocalDate())==0)).collect(Collectors.toSet());
            }
        }
        LOGGER.info("Retrieved '{}' 'ddt acquisto ingredienti'", ddtAcquistoIngredienti.size());
        return ddtAcquistoIngredienti;
    }

    private BigDecimal computeImponibile(DdtAcquistoIngrediente ddtAcquistoIngrediente){
        BigDecimal imponibile = new BigDecimal(0);

        // imponibile = (quantita*prezzo)-sconto
        Float quantita = ddtAcquistoIngrediente.getQuantita();
        if(quantita == null){
            quantita = 0F;
        }
        BigDecimal prezzo = ddtAcquistoIngrediente.getPrezzo();
        if(prezzo == null){
            prezzo = new BigDecimal(0);
        }
        BigDecimal sconto = ddtAcquistoIngrediente.getSconto();
        if(sconto == null){
            sconto = new BigDecimal(0);
        }
        BigDecimal quantitaPerPrezzo = prezzo.multiply(BigDecimal.valueOf(quantita));
        BigDecimal scontoValue = sconto.divide(BigDecimal.valueOf(100)).multiply(quantitaPerPrezzo);

        imponibile = quantitaPerPrezzo.subtract(scontoValue).setScale(2, RoundingMode.HALF_DOWN);
        return imponibile;
    }

}
