package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Pagamento;
import com.contafood.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class PagamentoService {

    private static Logger LOGGER = LoggerFactory.getLogger(PagamentoService.class);

    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public PagamentoService(final PagamentoRepository pagamentoRepository){
        this.pagamentoRepository = pagamentoRepository;
    }

    public Set<Pagamento> getAll(){
        LOGGER.info("Retrieving the list of 'pagamenti'");
        Set<Pagamento> pagamenti = pagamentoRepository.findAllByOrderByDataDesc();
        LOGGER.info("Retrieved {} 'pagamenti'", pagamenti.size());
        return pagamenti;
    }

    public Pagamento getOne(Long pagamentoId){
        LOGGER.info("Retrieving 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'pagamento' '{}'", pagamento);
        return pagamento;
    }

    public List<Pagamento> getDdtPagamenti(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        List<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti);
        return pagamenti;
    }

    @Transactional
    public Pagamento create(Pagamento pagamento){
        LOGGER.info("Creating 'pagamento'");

        pagamento.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Pagamento createdPagamento = pagamentoRepository.save(pagamento);

        LOGGER.info("Created 'pagamento' '{}'", createdPagamento);
        return createdPagamento;
    }

    @Transactional
    public Pagamento update(Pagamento pagamento){
        LOGGER.info("Updating 'pagamento'");

        Pagamento pagamentoCurrent = pagamentoRepository.findById(pagamento.getId()).orElseThrow(ResourceNotFoundException::new);
        pagamento.setDataInserimento(pagamentoCurrent.getDataInserimento());
        pagamento.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Pagamento updatedPagamento = pagamentoRepository.save(pagamento);

        LOGGER.info("Updated 'pagamento' '{}'", updatedPagamento);
        return updatedPagamento;
    }

    @Transactional
    public void delete(Long pagamentoId){
        LOGGER.info("Deleting 'pagamento' '{}'", pagamentoId);
        pagamentoRepository.deleteById(pagamentoId);
        LOGGER.info("Deleted 'pagamento' '{}'", pagamentoId);
    }

    @Transactional
    public void deleteByDdtId(Long ddtId){
        LOGGER.info("Deleting all 'pagamenti' of 'ddt' '{}'", ddtId);
        pagamentoRepository.deleteByDdtId(ddtId);
        LOGGER.info("Deleted all 'pagamenti' of 'ddt' '{}'", ddtId);
    }

}
