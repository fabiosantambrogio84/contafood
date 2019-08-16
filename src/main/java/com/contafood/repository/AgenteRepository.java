package com.contafood.repository;

import com.contafood.model.Agente;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AgenteRepository extends CrudRepository<Agente, Long> {

    @Override
    Set<Agente> findAll();
}
