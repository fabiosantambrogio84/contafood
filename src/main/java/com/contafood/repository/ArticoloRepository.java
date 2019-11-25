package com.contafood.repository;

import com.contafood.model.Articolo;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ArticoloRepository extends CrudRepository<Articolo, Long> {

    @Override
    Set<Articolo> findAll();
}
