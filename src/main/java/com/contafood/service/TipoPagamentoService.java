package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.TipoPagamento;
import com.contafood.repository.TipoPagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TipoPagamentoService {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoPagamentoService.class);

    private final TipoPagamentoRepository tipoPagamentoRepository;

    @Autowired
    public TipoPagamentoService(final TipoPagamentoRepository tipoPagamentoRepository) {
        this.tipoPagamentoRepository = tipoPagamentoRepository;
    }

    public Set<TipoPagamento> getAll() {
        LOGGER.info("Retrieving the list of 'tipiPagamento'");
        Set<TipoPagamento> tipiPagamento = tipoPagamentoRepository.findAllByOrderByDescrizione();
        LOGGER.info("Retrieved {} 'tipiPagamento'", tipiPagamento.size());
        return tipiPagamento;
    }

    public TipoPagamento getOne(Long tipoPagamentoId) {
        LOGGER.info("Retrieving 'tipoPagamento' '{}'", tipoPagamentoId);
        TipoPagamento tipoPagamento = tipoPagamentoRepository.findById(tipoPagamentoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'tipoPagamento' '{}'", tipoPagamento);
        return tipoPagamento;
    }

    public TipoPagamento create(TipoPagamento tipoPagamento) {
        LOGGER.info("Creating 'tipoPagamento'");
        TipoPagamento createdTipoPagamento = tipoPagamentoRepository.save(tipoPagamento);
        LOGGER.info("Created 'tipoPagamento' '{}'", createdTipoPagamento);
        return createdTipoPagamento;
    }

    public TipoPagamento update(TipoPagamento tipoPagamento) {
        LOGGER.info("Updating 'tipoPagamento'");
        TipoPagamento updatedTipoPagamento = tipoPagamentoRepository.save(tipoPagamento);
        LOGGER.info("Updated 'tipoPagamento' '{}'", updatedTipoPagamento);
        return updatedTipoPagamento;
    }

    public void delete(Long tipoPagamentoId) {
        LOGGER.info("Deleting 'tipoPagamento' '{}'", tipoPagamentoId);
        tipoPagamentoRepository.deleteById(tipoPagamentoId);
        LOGGER.info("Deleted 'tipoPagamento' '{}'", tipoPagamentoId);
    }
}
