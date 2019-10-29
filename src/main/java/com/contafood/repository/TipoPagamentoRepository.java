package com.contafood.repository;

import com.contafood.model.TipoPagamento;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TipoPagamentoRepository extends CrudRepository<TipoPagamento, Long> {

    @Override
    Set<TipoPagamento> findAll();
}
