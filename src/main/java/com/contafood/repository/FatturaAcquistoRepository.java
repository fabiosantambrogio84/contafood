package com.contafood.repository;

import com.contafood.model.FatturaAcquisto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FatturaAcquistoRepository extends CrudRepository<FatturaAcquisto, Long> {

    @Override
    Set<FatturaAcquisto> findAll();

    Optional<FatturaAcquisto> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFatturaAcquisto);

    @Query(nativeQuery = true,
            value = "select f.progressivo from fattura_acquisto f where f.anno = ?1 order by f.progressivo desc limit 1 for update"
    )
    Integer getLastProgressivoByAnnoContabile(Integer anno);
}
