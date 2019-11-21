package com.contafood.repository;

import com.contafood.model.CategoriaArticolo;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CategoriaArticoloRepository extends CrudRepository<CategoriaArticolo, Long> {

    @Override
    Set<CategoriaArticolo> findAll();

    Set<CategoriaArticolo> findAllByOrderByNome();
}
