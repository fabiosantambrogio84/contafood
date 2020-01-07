package com.contafood.repository;

import com.contafood.model.OrdineClienteArticolo;
import com.contafood.model.OrdineClienteArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface OrdineClienteArticoloRepository extends CrudRepository<OrdineClienteArticolo, Long> {

    @Override
    Set<OrdineClienteArticolo> findAll();

    Optional<OrdineClienteArticolo> findById(OrdineClienteArticoloKey id);

    Set<OrdineClienteArticolo> findByOrdineClienteId(Long ordineClienteId);

    Set<OrdineClienteArticolo> findByArticoloId(Long articoloId);

    void deleteByOrdineClienteId(Long ordineClienteId);

    void deleteByArticoloId(Long articoloId);
}
