package com.contafood.repository;

import com.contafood.model.ClienteArticolo;
import com.contafood.model.ClienteArticoloKey;
import com.contafood.model.DdtArticolo;
import com.contafood.model.DdtArticoloKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ClienteArticoloRepository extends CrudRepository<ClienteArticolo, Long> {

    @Override
    Set<ClienteArticolo> findAll();

    Optional<ClienteArticolo> findById(ClienteArticoloKey id);

    Set<ClienteArticolo> findByClienteId(Long clienteId);

    void deleteByClienteId(Long clienteId);

    void deleteByArticoloId(Long articoloId);
}
