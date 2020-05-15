package com.contafood.repository;

import com.contafood.model.NotaAccreditoArticolo;
import com.contafood.model.NotaAccreditoArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotaAccreditoArticoloRepository extends CrudRepository<NotaAccreditoArticolo, Long> {

    @Override
    Set<NotaAccreditoArticolo> findAll();

    Optional<NotaAccreditoArticolo> findById(NotaAccreditoArticoloKey id);

    Set<NotaAccreditoArticolo> findByArticoloId(Long articoloId);

    void deleteByNotaAccreditoId(Long notaAccreditoId);

    void deleteByArticoloId(Long articoloId);
}
