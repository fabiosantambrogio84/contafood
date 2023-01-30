package com.contafood.repository;

import com.contafood.model.Fattura;
import org.springframework.data.jpa.repository.Query;
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

    @Query(nativeQuery = true,
            value = "select f.progressivo from fattura f where f.anno = ?1 order by f.progressivo desc limit 1 for update"
    )
    Integer getLastProgressivoByAnnoContabile(Integer anno);
}
