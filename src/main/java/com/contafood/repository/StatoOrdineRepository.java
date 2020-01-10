package com.contafood.repository;

import com.contafood.model.StatoOrdine;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StatoOrdineRepository extends CrudRepository<StatoOrdine, Long> {

    @Override
    Set<StatoOrdine> findAll();

    Optional<StatoOrdine> findByCodice(String codice);
}
