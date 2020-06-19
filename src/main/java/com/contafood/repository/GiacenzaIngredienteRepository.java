package com.contafood.repository;

import com.contafood.model.GiacenzaIngrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GiacenzaIngredienteRepository extends CrudRepository<GiacenzaIngrediente, Long> {

    @Override
    Set<GiacenzaIngrediente> findAll();

    Set<GiacenzaIngrediente> findByIngredienteIdAndLotto(Long idIngrediente, String lotto);

    void deleteByIngredienteId(Long idIngrediente);

    void deleteByIdIn(List<Long> ids);
}
