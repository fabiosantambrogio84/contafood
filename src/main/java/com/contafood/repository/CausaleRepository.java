package com.contafood.repository;

import com.contafood.model.Causale;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface CausaleRepository extends CrudRepository<Causale, Long> {

    Set<Causale> findAllByOrderByDescrizione();

    Optional<Causale> findByDescrizione(String descrizione);

}
