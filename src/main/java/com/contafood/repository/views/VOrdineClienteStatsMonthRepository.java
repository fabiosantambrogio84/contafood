package com.contafood.repository.views;

import com.contafood.model.views.VOrdineClienteStatsMonth;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VOrdineClienteStatsMonthRepository extends CrudRepository<VOrdineClienteStatsMonth, Long> {

    List<VOrdineClienteStatsMonth> findByIdClienteAndIdPuntoConsegnaIn(Long idCliente, List<Long> idPuntiConsegna);

}
