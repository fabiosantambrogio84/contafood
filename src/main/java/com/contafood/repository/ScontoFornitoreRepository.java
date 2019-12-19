package com.contafood.repository;

import com.contafood.model.ScontoFornitore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScontoFornitoreRepository extends CrudRepository<ScontoFornitore, Long> {

    @Override
    List<ScontoFornitore> findAll();

    List<ScontoFornitore> findByScontoId(Long idSconto);

    List<ScontoFornitore> findByFornitoreId(Long idFornitore);

    void deleteByScontoId(Long idSconto);

    void deleteByFornitoreId(Long idFornitore);

}
