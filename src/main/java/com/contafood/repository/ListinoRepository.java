package com.contafood.repository;

import com.contafood.model.Listino;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ListinoRepository extends CrudRepository<Listino, Long> {

    @Override
    Set<Listino> findAll();
}
