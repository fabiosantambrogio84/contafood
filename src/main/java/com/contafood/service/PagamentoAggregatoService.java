package com.contafood.service;

import com.contafood.model.PagamentoAggregato;
import com.contafood.repository.PagamentoAggregatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Service
public class PagamentoAggregatoService {

    private static Logger LOGGER = LoggerFactory.getLogger(PagamentoAggregatoService.class);

    private final PagamentoAggregatoRepository pagamentoAggregatoRepository;

    @Autowired
    public PagamentoAggregatoService(final PagamentoAggregatoRepository pagamentoAggregatoRepository){
        this.pagamentoAggregatoRepository = pagamentoAggregatoRepository;
    }

    @Transactional
    public PagamentoAggregato createPagamentoAggregato(PagamentoAggregato pagamentoAggregato){
        LOGGER.info("Creating 'pagamento aggregato'");
        pagamentoAggregato.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        PagamentoAggregato createdPagamentoAggregato = pagamentoAggregatoRepository.save(pagamentoAggregato);
        LOGGER.info("Created 'pagamento aggregato' '{}'", createdPagamentoAggregato);
        return createdPagamentoAggregato;
    }

    @Transactional
    public void deletePagamentoAggregato(Long pagamentoAggregatoId){
        LOGGER.info("Deleting 'pagamento aggregato' '{}'", pagamentoAggregatoId);

        pagamentoAggregatoRepository.deleteById(pagamentoAggregatoId);
        LOGGER.info("Deleted 'pagamento aggregato' '{}'", pagamentoAggregatoId);
    }

}
