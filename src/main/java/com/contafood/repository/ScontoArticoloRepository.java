package com.contafood.repository;

import com.contafood.model.ScontoArticolo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScontoArticoloRepository extends CrudRepository<ScontoArticolo, Long> {

    @Override
    List<ScontoArticolo> findAll();

    List<ScontoArticolo> findByScontoId(Long idSconto);

    List<ScontoArticolo> findByArticoloId(Long idArticolo);

    void deleteByScontoId(Long idSconto);

    void deleteByArticoloId(Long idArticolo);

}
