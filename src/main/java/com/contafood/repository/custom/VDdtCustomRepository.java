package com.contafood.repository.custom;

import com.contafood.model.views.VDdt;

import java.sql.Date;
import java.util.List;

public interface VDdtCustomRepository {

    List<VDdt> findByFilter(Date dataDa, Date dataA, Integer progressivo, Integer idCliente, String cliente, Integer idAgente, Integer idAutista, Integer idStato, Boolean pagato, Boolean fatturato, Float importo, Integer idTipoPagamento, Integer idArticolo);
}
