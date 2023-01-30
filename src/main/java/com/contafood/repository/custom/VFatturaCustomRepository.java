package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VFattura;

import java.sql.Date;
import java.util.List;

public interface VFatturaCustomRepository {

    List<VFattura> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo);

    Integer countByFilters(Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo);
}
