package com.contafood.repository;

import com.contafood.model.DdtAcquisto;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DdtAcquistoRepository extends CrudRepository<DdtAcquisto, Long> {

    @Override
    Set<DdtAcquisto> findAll();

    Set<DdtAcquisto> findAllByOrderByNumeroDesc();

}
