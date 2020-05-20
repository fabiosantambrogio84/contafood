package com.contafood.repository;

import com.contafood.model.NotaResoTotale;
import com.contafood.model.NotaResoTotaleKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotaResoTotaleRepository extends CrudRepository<NotaResoTotale, Long> {

    @Override
    Set<NotaResoTotale> findAll();

    Optional<NotaResoTotale> findById(NotaResoTotaleKey id);

    void deleteByNotaResoId(Long notaResoId);

}
