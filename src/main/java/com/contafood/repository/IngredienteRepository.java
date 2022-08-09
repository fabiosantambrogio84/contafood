package com.contafood.repository;

import com.contafood.model.Ingrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

    @Override
    Set<Ingrediente> findAll();

    Optional<Ingrediente> findByCodice(String codice);

    //List<Ingrediente> findByFornitoreId(Long idFornitore);
}
