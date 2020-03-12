package com.contafood.repository;

import com.contafood.model.StatoDdtAcquisto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StatoDdtAcquistoRepository extends CrudRepository<StatoDdtAcquisto, Long> {

    Set<StatoDdtAcquisto> findAllByOrderByOrdine();

    Optional<StatoDdtAcquisto> findByCodice(String codice);

}
