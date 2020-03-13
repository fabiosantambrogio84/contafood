package com.contafood.repository;

import com.contafood.model.DdtAcquisto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DdtAcquistoRepository extends CrudRepository<DdtAcquisto, Long> {

    @Override
    Set<DdtAcquisto> findAll();

    Set<DdtAcquisto> findAllByOrderByNumeroDesc();

    @Query(nativeQuery = true,
            value = "select distinct ddt_acquisto.* from ddt_acquisto join ddt_acquisto_articolo on ddt_acquisto.id = ddt_acquisto_articolo.id_ddt_acquisto where ddt_acquisto_articolo.lotto = ?1 order by ddt_acquisto.data desc, ddt_acquisto.numero desc"
    )
    Set<DdtAcquisto> findAllByLotto(String lotto);

}
