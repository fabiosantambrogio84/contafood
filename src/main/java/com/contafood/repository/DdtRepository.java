package com.contafood.repository;

import com.contafood.model.Ddt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DdtRepository extends CrudRepository<Ddt, Long> {

    @Override
    Set<Ddt> findAll();

    Set<Ddt> findAllByOrderByAnnoContabileDescProgressivoDesc();

    List<Ddt> findByAnnoContabileOrderByProgressivoDesc(Integer annoContabile);

    Optional<Ddt> findByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idDdt);

    @Query(nativeQuery = true,
            value = "select distinct ddt.* from ddt join ddt_articolo on ddt.id = ddt_articolo.id_ddt where ddt_articolo.lotto = ?1 order by ddt.anno_contabile desc, ddt.progressivo desc"
            )
    Set<Ddt> findAllByLotto(String lotto);
}
