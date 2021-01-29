package com.contafood.repository;

import com.contafood.model.Fattura;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

public interface FatturaRepository extends CrudRepository<Fattura, Long> {

    @Override
    Set<Fattura> findAll();

    Optional<Fattura> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura);

    Set<Fattura> findByDataGreaterThanEqualAndDataLessThanEqual(Date dateFrom, Date dateTo);

    Set<Fattura> findByProgressivoGreaterThanEqualAndAnnoGreaterThanEqualAndProgressivoLessThanEqualAndAnnoLessThanEqual(Integer progressivoFrom, Integer annoFrom, Integer progressivoTo, Integer annoto);
}
