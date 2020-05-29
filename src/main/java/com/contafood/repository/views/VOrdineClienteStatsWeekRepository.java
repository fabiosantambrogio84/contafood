package com.contafood.repository.views;

import com.contafood.model.views.VOrdineClienteStatsWeek;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VOrdineClienteStatsWeekRepository extends CrudRepository<VOrdineClienteStatsWeek, Long> {

    List<VOrdineClienteStatsWeek> findByIdClienteAndIdPuntoConsegnaIn(Long idCliente, List<Long> idPuntiConsegna);

}
