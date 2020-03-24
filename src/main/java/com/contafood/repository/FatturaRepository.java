package com.contafood.repository;

import com.contafood.model.Fattura;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FatturaRepository extends CrudRepository<Fattura, Long> {

    @Override
    Set<Fattura> findAll();

    Optional<Fattura> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura);
}
