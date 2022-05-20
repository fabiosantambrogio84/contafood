package com.contafood.repository.custom;

import com.contafood.model.views.VPagamento;

import java.sql.Date;
import java.util.List;

public interface VPagamentoCustomRepository {

    List<VPagamento> findByFilter(String tipologia, Date dataDa, Date dataA, String cliente, String fornitore, Float importo);
}
