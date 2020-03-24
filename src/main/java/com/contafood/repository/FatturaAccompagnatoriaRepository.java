package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoria;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaRepository extends CrudRepository<FatturaAccompagnatoria, Long> {

    @Override
    Set<FatturaAccompagnatoria> findAll();

    Set<FatturaAccompagnatoria> findAllByOrderByAnnoDescProgressivoDesc();

    Optional<FatturaAccompagnatoria> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFatturaAccompagnatoria);
}
