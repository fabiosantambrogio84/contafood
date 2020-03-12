package com.contafood.repository;

import com.contafood.model.DdtAcquistoArticolo;
import com.contafood.model.DdtAcquistoArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface DdtAcquistoArticoloRepository extends CrudRepository<DdtAcquistoArticolo, Long> {

    @Override
    Set<DdtAcquistoArticolo> findAll();

    Optional<DdtAcquistoArticolo> findById(DdtAcquistoArticoloKey id);

    Set<DdtAcquistoArticolo> findByDdtAcquistoId(Long ddtAcquistoId);

    Set<DdtAcquistoArticolo> findByArticoloId(Long articoloId);

    void deleteByDdtAcquistoId(Long ddtAcquistoId);

    void deleteByArticoloId(Long articoloId);
}
