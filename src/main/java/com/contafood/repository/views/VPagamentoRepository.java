package com.contafood.repository.views;

import com.contafood.model.views.VPagamento;
import com.contafood.repository.custom.VPagamentoCustomRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface VPagamentoRepository extends CrudRepository<VPagamento, Long>, VPagamentoCustomRepository {

    @Override
    Set<VPagamento> findAll();

}
