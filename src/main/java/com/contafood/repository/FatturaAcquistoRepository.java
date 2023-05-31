package com.contafood.repository;

import com.contafood.model.FatturaAcquisto;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface FatturaAcquistoRepository extends CrudRepository<FatturaAcquisto, Long> {

    @Override
    Set<FatturaAcquisto> findAll();

}
