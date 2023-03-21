package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VDocumentoAcquisto;

import java.sql.Date;
import java.util.List;

public interface VDocumentoAcquistoCustomRepository {

    List<VDocumentoAcquisto> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato);

    Integer countByFilters(String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato);
}
