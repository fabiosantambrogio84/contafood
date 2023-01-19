package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VProduzione;

import java.util.List;

public interface VProduzioneCustomRepository {

    List<VProduzione> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders);

    Integer countByFilters();
}
