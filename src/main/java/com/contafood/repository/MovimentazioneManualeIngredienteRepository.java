package com.contafood.repository;

import com.contafood.model.MovimentazioneManualeIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface MovimentazioneManualeIngredienteRepository extends CrudRepository<MovimentazioneManualeIngrediente, Long> {

    @Override
    Set<MovimentazioneManualeIngrediente> findAll();

    Set<MovimentazioneManualeIngrediente> findByIngredienteIdAndLotto(Long idIngrediente, String lotto);

    Set<MovimentazioneManualeIngrediente> findByIngredienteId(Long idIngrediente);

    void deleteByIngredienteId(Long idIngrediente);

    void deleteByIdIn(List<Long> ids);

    void deleteByIngredienteIdIn(List<Long> idIngredienti);
}
