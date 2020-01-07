package com.contafood.repository;

import com.contafood.model.OrdineClienteArticolo;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface OrdineClienteArticoloRepository extends CrudRepository<OrdineClienteArticolo, Long> {

    @Override
    Set<OrdineClienteArticolo> findAll();

    Set<OrdineClienteArticolo> findByOrdineClienteId(Long ordineClienteId);

    Set<OrdineClienteArticolo> findByArticoloId(Long articoloId);

    void deleteByOrdineClienteId(Long ordineClienteId);

    void deleteByArticoloId(Long articoloId);
}
