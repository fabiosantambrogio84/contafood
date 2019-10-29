package com.contafood.repository;

import com.contafood.model.Banca;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BancaRepository extends CrudRepository<Banca, Long> {

    @Override
    Set<Banca> findAll();
}
