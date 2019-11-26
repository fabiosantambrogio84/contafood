package com.contafood.repository;

import com.contafood.model.Sconto;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ScontoRepository extends CrudRepository<Sconto, Long> {

    @Override
    Set<Sconto> findAll();
}
