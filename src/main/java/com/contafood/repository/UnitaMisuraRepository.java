package com.contafood.repository;

import com.contafood.model.UnitaMisura;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UnitaMisuraRepository extends CrudRepository<UnitaMisura, Long> {

    @Override
    Set<UnitaMisura> findAll();
}
