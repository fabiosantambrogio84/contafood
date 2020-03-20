package com.contafood.repository;

import com.contafood.model.TipoFattura;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface TipoFatturaRepository extends CrudRepository<TipoFattura, Long> {

    Set<TipoFattura> findAllByOrderByOrdine();

    Optional<TipoFattura> findByCodice(String codice);

}
