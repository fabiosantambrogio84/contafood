package com.contafood.repository;

import com.contafood.model.OrdineFornitore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface OrdineFornitoreRepository extends CrudRepository<OrdineFornitore, Long> {

    @Override
    Set<OrdineFornitore> findAll();

    Set<OrdineFornitore> findAllByOrderByAnnoContabileDescProgressivoDesc();

    List<OrdineFornitore> findByAnnoContabileOrderByProgressivoDesc(Integer annoContabile);
}
