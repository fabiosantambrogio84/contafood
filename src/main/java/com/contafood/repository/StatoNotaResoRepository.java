package com.contafood.repository;

import com.contafood.model.StatoNotaReso;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StatoNotaResoRepository extends CrudRepository<StatoNotaReso, Long> {

    Set<StatoNotaReso> findAllByOrderByOrdine();

    Optional<StatoNotaReso> findByCodice(String codice);

}
