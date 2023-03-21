package com.contafood.service;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VDocumentoAcquisto;
import com.contafood.repository.views.VDocumentoAcquistoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Slf4j
@Service
public class DocumentoAcquistoService {

    private final VDocumentoAcquistoRepository vDocumentoAcquistoRepository;

    @Autowired
    public DocumentoAcquistoService(final VDocumentoAcquistoRepository vDocumentoAcquistoRepository){
        this.vDocumentoAcquistoRepository = vDocumentoAcquistoRepository;
    }

    public List<VDocumentoAcquisto> getAllByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato){
        log.info("Retrieving the list of 'documenti-acquisto' filtered by request parameters");
        List<VDocumentoAcquisto> documentiAcquisto = vDocumentoAcquistoRepository.findByFilters(draw, start, length, sortOrders, fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);
        log.info("Retrieved {} 'documenti-acquisto'", documentiAcquisto.size());
        return documentiAcquisto;
    }

    public Integer getCountByFilters(String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato){
        log.info("Retrieving the count of 'documenti-acquisto' filtered by request parameters");
        Integer count = vDocumentoAcquistoRepository.countByFilters(fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);
        log.info("Retrieved {} 'documenti-acquisto'", count);
        return count;
    }

    public List<VDocumentoAcquisto> getAllByIds(List<String> ids){
        log.info("Retrieving the list of 'documenti-acquisto' filtered by ids");
        List<VDocumentoAcquisto> documentiAcquisto = vDocumentoAcquistoRepository.findByIds(ids);
        log.info("Retrieved {} 'documenti-acquisto'", documentiAcquisto.size());
        return documentiAcquisto;
    }

}
