package com.contafood.repository;

import com.contafood.model.OrdineFornitoreArticolo;
import com.contafood.model.OrdineFornitoreArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface OrdineFornitoreArticoloRepository extends CrudRepository<OrdineFornitoreArticolo, Long> {

    @Override
    Set<OrdineFornitoreArticolo> findAll();

    Optional<OrdineFornitoreArticolo> findById(OrdineFornitoreArticoloKey id);

    Set<OrdineFornitoreArticolo> findByOrdineFornitoreId(Long ordineFornitoreId);

    Set<OrdineFornitoreArticolo> findByArticoloId(Long articoloId);

    void deleteByOrdineFornitoreId(Long ordineFornitoreId);

    void deleteByArticoloId(Long articoloId);
}
