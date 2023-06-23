package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Articolo;
import com.contafood.model.FatturaAccompagnatoriaAcquistoArticolo;
import com.contafood.model.FatturaAccompagnatoriaAcquistoIngrediente;
import com.contafood.model.Ingrediente;
import com.contafood.repository.FatturaAccompagnatoriaAcquistoArticoloRepository;
import com.contafood.repository.FatturaAccompagnatoriaAcquistoIngredienteRepository;
import com.contafood.util.AccountingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FatturaAccompagnatoriaAcquistoIngredienteService {

    private final FatturaAccompagnatoriaAcquistoIngredienteRepository fatturaAccompagnatoriaAcquistoIngredienteRepository;
    private final IngredienteService ingredienteService;

    @Autowired
    public FatturaAccompagnatoriaAcquistoIngredienteService(final FatturaAccompagnatoriaAcquistoIngredienteRepository fatturaAccompagnatoriaAcquistoIngredienteRepository,
                                                            final IngredienteService ingredienteService){
        this.fatturaAccompagnatoriaAcquistoIngredienteRepository = fatturaAccompagnatoriaAcquistoIngredienteRepository;
        this.ingredienteService = ingredienteService;
    }

    public Set<FatturaAccompagnatoriaAcquistoIngrediente> findAll(){
        log.info("Retrieving the list of 'fattura accompagnatoria acquisto ingredienti'");
        Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredienti = fatturaAccompagnatoriaAcquistoIngredienteRepository.findAll();
        log.info("Retrieved {} 'fattura accompagnatoria acquisto ingredienti'", fatturaAccompagnatoriaAcquistoIngredienti.size());
        return fatturaAccompagnatoriaAcquistoIngredienti;
    }

    public Set<FatturaAccompagnatoriaAcquistoIngrediente> findByFatturaAccompagnatoriaAcquistoId(Long idFatturaAccompagnatoriaAcquisto){
        log.info("Retrieving the list of 'fattura accompagnatoria ingredienti' of 'fattura accompagnatoria acquisto' {}", idFatturaAccompagnatoriaAcquisto);
        Set<FatturaAccompagnatoriaAcquistoIngrediente> fatturaAccompagnatoriaAcquistoIngredienti = fatturaAccompagnatoriaAcquistoIngredienteRepository.findByFatturaAccompagnatoriaAcquistoId(idFatturaAccompagnatoriaAcquisto);
        log.info("Retrieved {} 'fattura accompagnatoria ingredienti'", fatturaAccompagnatoriaAcquistoIngredienti.size());
        return fatturaAccompagnatoriaAcquistoIngredienti;
    }

    public FatturaAccompagnatoriaAcquistoIngrediente create(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        log.info("Creating 'fattura accompagnatoria acquisto ingrediente'");
        fatturaAccompagnatoriaAcquistoIngrediente.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaAcquistoIngrediente.setImponibile(computeImponibile(fatturaAccompagnatoriaAcquistoIngrediente));
        fatturaAccompagnatoriaAcquistoIngrediente.setCosto(computeCosto(fatturaAccompagnatoriaAcquistoIngrediente));
        fatturaAccompagnatoriaAcquistoIngrediente.setTotale(computeTotale(fatturaAccompagnatoriaAcquistoIngrediente));

        FatturaAccompagnatoriaAcquistoIngrediente createdFatturaAccompagnatoriaAcquistoIngrediente = fatturaAccompagnatoriaAcquistoIngredienteRepository.save(fatturaAccompagnatoriaAcquistoIngrediente);

        log.info("Created 'fattura accompagnatoria acquisto ingrediente' '{}'", createdFatturaAccompagnatoriaAcquistoIngrediente);
        return createdFatturaAccompagnatoriaAcquistoIngrediente;
    }

    public FatturaAccompagnatoriaAcquistoIngrediente update(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        log.info("Updating 'fattura accompagnatoria acquisto ingrediente'");
        FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngredienteCurrent = fatturaAccompagnatoriaAcquistoIngredienteRepository.findById(fatturaAccompagnatoriaAcquistoIngrediente.getId()).orElseThrow(ResourceNotFoundException::new);
        fatturaAccompagnatoriaAcquistoIngrediente.setDataInserimento(fatturaAccompagnatoriaAcquistoIngredienteCurrent.getDataInserimento());
        fatturaAccompagnatoriaAcquistoIngrediente.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));
        fatturaAccompagnatoriaAcquistoIngrediente.setImponibile(computeImponibile(fatturaAccompagnatoriaAcquistoIngrediente));
        fatturaAccompagnatoriaAcquistoIngrediente.setCosto(computeCosto(fatturaAccompagnatoriaAcquistoIngrediente));
        fatturaAccompagnatoriaAcquistoIngrediente.setTotale(computeTotale(fatturaAccompagnatoriaAcquistoIngrediente));

        FatturaAccompagnatoriaAcquistoIngrediente updatedFatturaAccompagnatoriaAcquistoIngrediente = fatturaAccompagnatoriaAcquistoIngredienteRepository.save(fatturaAccompagnatoriaAcquistoIngrediente);
        log.info("Updated 'fattura accompagnatoria acquisto ingrediente' '{}'", updatedFatturaAccompagnatoriaAcquistoIngrediente);
        return updatedFatturaAccompagnatoriaAcquistoIngrediente;
    }

    public void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId){
        log.info("Deleting 'fattura accompagnatoria acquisto ingrediente' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
        fatturaAccompagnatoriaAcquistoIngredienteRepository.deleteByFatturaAccompagnatoriaAcquistoId(fatturaAccompagnatoriaAcquistoId);
        log.info("Deleted 'fattura accompagnatoria acquisto ingrediente' by 'fattura accompagnatoria acquisto' '{}'", fatturaAccompagnatoriaAcquistoId);
    }

    public Ingrediente getIngrediente(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        Long ingredienteId = fatturaAccompagnatoriaAcquistoIngrediente.getId().getIngredienteId();
        return ingredienteService.getOne(ingredienteId);
    }

    private BigDecimal computeImponibile(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        return AccountingUtils.computeImponibile(fatturaAccompagnatoriaAcquistoIngrediente.getQuantita(), fatturaAccompagnatoriaAcquistoIngrediente.getPrezzo(), fatturaAccompagnatoriaAcquistoIngrediente.getSconto());
    }

    private BigDecimal computeCosto(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        return AccountingUtils.computeCosto(fatturaAccompagnatoriaAcquistoIngrediente.getQuantita(), fatturaAccompagnatoriaAcquistoIngrediente.getId().getIngredienteId(), ingredienteService);
    }

    private BigDecimal computeTotale(FatturaAccompagnatoriaAcquistoIngrediente fatturaAccompagnatoriaAcquistoIngrediente){
        return AccountingUtils.computeTotale(fatturaAccompagnatoriaAcquistoIngrediente.getQuantita(), fatturaAccompagnatoriaAcquistoIngrediente.getPrezzo(), fatturaAccompagnatoriaAcquistoIngrediente.getSconto(), null, fatturaAccompagnatoriaAcquistoIngrediente.getId().getIngredienteId(), ingredienteService);
    }

}