package com.contafood.repository;

import com.contafood.model.NotaReso;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotaResoRepository extends CrudRepository<NotaReso, Long> {

    @Override
    Set<NotaReso> findAll();

    Set<NotaReso> findAllByOrderByAnnoDescProgressivoDesc();

    List<NotaReso> findByAnnoOrderByProgressivoDesc(Integer anno);

    Optional<NotaReso> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idNotaReso);
}
