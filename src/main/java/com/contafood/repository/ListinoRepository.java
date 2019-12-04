package com.contafood.repository;

import com.contafood.model.Listino;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ListinoRepository extends CrudRepository<Listino, Long> {

    @Override
    Set<Listino> findAll();

    Set<Listino> findAllByOrderByTipologia();

    Optional<Listino> findFirstByTipologia(String tipologia);

    List<Listino> findByListinoRiferimentoId(Long listinoRiferimentoId);
}
