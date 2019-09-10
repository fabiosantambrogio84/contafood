package com.contafood.repository;

import com.contafood.model.Ricetta;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RicettaRepository extends CrudRepository<Ricetta, Long> {

    @Override
    Set<Ricetta> findAll();

    Set<Ricetta> findAllByOrderByCodice();
}
