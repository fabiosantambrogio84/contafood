package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaRepository extends CrudRepository<FatturaAccompagnatoria, Long> {

    @Override
    Set<FatturaAccompagnatoria> findAll();

    Set<FatturaAccompagnatoria> findAllByOrderByAnnoDescProgressivoDesc();

    List<FatturaAccompagnatoria> findByDataGreaterThanEqualOrderByProgressivoDesc(Date data);

    Optional<FatturaAccompagnatoria> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFatturaAccompagnatoria);

    @Query(nativeQuery = true,
            value = "select fa.progressivo from fattura_accom fa where fa.anno = ?1 order by fa.progressivo desc limit 1 for update"
    )
    Integer getLastProgressivoByAnnoContabile(Integer anno);
}
