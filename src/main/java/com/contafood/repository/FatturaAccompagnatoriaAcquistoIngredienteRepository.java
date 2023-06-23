package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoriaAcquistoIngrediente;
import com.contafood.model.FatturaAccompagnatoriaAcquistoIngredienteKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaAcquistoIngredienteRepository extends CrudRepository<FatturaAccompagnatoriaAcquistoIngrediente, Long> {

    @Override
    Set<FatturaAccompagnatoriaAcquistoIngrediente> findAll();

    Optional<FatturaAccompagnatoriaAcquistoIngrediente> findById(FatturaAccompagnatoriaAcquistoIngredienteKey id);

    Set<FatturaAccompagnatoriaAcquistoIngrediente> findByFatturaAccompagnatoriaAcquistoId(Long idFatturaAccompagnatoriaAcquisto);

    Set<FatturaAccompagnatoriaAcquistoIngrediente> findByIngredienteId(Long ingredienteId);

    void deleteByFatturaAccompagnatoriaAcquistoId(Long fatturaAccompagnatoriaAcquistoId);

    void deleteByIngredienteId(Long ingredienteId);
}
