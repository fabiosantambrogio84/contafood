package com.contafood.repository;

import com.contafood.model.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    @Override
    Set<Cliente> findAll();

    Set<Cliente> findByBloccaDdt(Boolean bloccaDdt);
}
