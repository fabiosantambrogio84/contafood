package com.contafood.repository;

import com.contafood.model.StatoRicevutaPrivato;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StatoRicevutaPrivatoRepository extends CrudRepository<StatoRicevutaPrivato, Long> {

    Set<StatoRicevutaPrivato> findAllByOrderByOrdine();

    Optional<StatoRicevutaPrivato> findByCodice(String codice);

}
