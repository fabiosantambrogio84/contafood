package com.contafood.repository;

import com.contafood.model.StatoDdt;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StatoDdtRepository extends CrudRepository<StatoDdt, Long> {

    Set<StatoDdt> findAllByOrderByOrdine();

    Optional<StatoDdt> findByCodice(String codice);

}
