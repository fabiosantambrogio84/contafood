package com.contafood.repository;

import com.contafood.model.FatturaAccompagnatoriaAcquisto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FatturaAccompagnatoriaAcquistoRepository extends CrudRepository<FatturaAccompagnatoriaAcquisto, Long> {

    @Override
    Set<FatturaAccompagnatoriaAcquisto> findAll();

    Set<FatturaAccompagnatoriaAcquisto> findAllByOrderByAnnoDescNumeroDesc();

    Optional<FatturaAccompagnatoriaAcquisto> findByFornitoreIdAndNumeroAndIdNot(Long idFornitore, String numero, Long idFatturaAccompagnatoriaAcquisto);

    @Query(nativeQuery = true,
            value = "select faa.numero from fattura_accom_acquisto faa where faa.anno = ?1 order by faa.numero desc limit 1 for update"
    )
    Integer getLastNumeroByAnnoContabile(Integer anno);
}
