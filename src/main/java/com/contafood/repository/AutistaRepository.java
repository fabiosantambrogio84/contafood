package com.contafood.repository;

import com.contafood.model.Autista;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AutistaRepository extends CrudRepository<Autista, Long> {

    @Override
    Set<Autista> findAll();
}
