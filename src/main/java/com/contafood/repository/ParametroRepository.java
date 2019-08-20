package com.contafood.repository;

import com.contafood.model.Parametro;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ParametroRepository extends CrudRepository<Parametro, Long> {

    @Override
    Set<Parametro> findAll();
}
