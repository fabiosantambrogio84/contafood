package com.contafood.repository;

import com.contafood.model.CategoriaRicetta;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CategoriaRicettaRepository extends CrudRepository<CategoriaRicetta, Long> {

    @Override
    Set<CategoriaRicetta> findAll();
}
