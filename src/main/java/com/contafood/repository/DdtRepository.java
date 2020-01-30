package com.contafood.repository;

import com.contafood.model.Ddt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface DdtRepository extends CrudRepository<Ddt, Long> {

    @Override
    Set<Ddt> findAll();

    Set<Ddt> findAllByOrderByAnnoContabileDescProgressivoDesc();

    List<Ddt> findByAnnoContabileOrderByProgressivoDesc(Integer annoContabile);
}
