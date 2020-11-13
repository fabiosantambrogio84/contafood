package com.contafood.repository;

import com.contafood.model.RicevutaPrivato;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RicevutaPrivatoRepository extends CrudRepository<RicevutaPrivato, Long> {

    @Override
    Set<RicevutaPrivato> findAll();

    Set<RicevutaPrivato> findAllByOrderByAnnoDescProgressivoDesc();

    List<RicevutaPrivato> findByDataGreaterThanEqualOrderByProgressivoDesc(Date data);

    Optional<RicevutaPrivato> findByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idRicevutaPrivato);

    List<RicevutaPrivato> findByAnnoOrderByProgressivoDesc(Integer anno);
}
