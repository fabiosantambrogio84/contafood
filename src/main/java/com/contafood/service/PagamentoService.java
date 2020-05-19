package com.contafood.service;

import com.contafood.exception.PagamentoExceedingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Ddt;
import com.contafood.model.NotaAccredito;
import com.contafood.model.Pagamento;
import com.contafood.repository.DdtRepository;
import com.contafood.repository.NotaAccreditoRepository;
import com.contafood.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class PagamentoService {

    private static Logger LOGGER = LoggerFactory.getLogger(PagamentoService.class);

    private final PagamentoRepository pagamentoRepository;
    private final DdtRepository ddtRepository;
    private final NotaAccreditoRepository notaAccreditoRepository;
    private final StatoDdtService statoDdtService;
    private final StatoNotaAccreditoService statoNotaAccreditoService;

    @Autowired
    public PagamentoService(final PagamentoRepository pagamentoRepository, final DdtRepository ddtRepository, final NotaAccreditoRepository notaAccreditoRepository,
                            final StatoDdtService statoDdtService, final StatoNotaAccreditoService statoNotaAccreditoService){
        this.pagamentoRepository = pagamentoRepository;
        this.ddtRepository = ddtRepository;
        this.notaAccreditoRepository = notaAccreditoRepository;
        this.statoDdtService = statoDdtService;
        this.statoNotaAccreditoService = statoNotaAccreditoService;
    }

    public Set<Pagamento> getPagamenti(){
        LOGGER.info("Retrieving 'pagamenti'");
        Set<Pagamento> pagamenti = pagamentoRepository.findAllByOrderByDataDesc();
        LOGGER.info("Retrieved {} 'pagamenti'", pagamenti.size());
        return pagamenti;
    }

    public Set<Pagamento> getDdtPagamentiByIdDdt(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
    }

    public Set<Pagamento> getNotaAccreditoPagamentiByIdNotaAccredito(Long notaAccreditoId){
        LOGGER.info("Retrieving 'pagamenti' of 'notaAccredito' '{}'", notaAccreditoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(notaAccreditoId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'notaAccredito' '{}'", pagamenti.size(), notaAccreditoId);
        return pagamenti;
    }

    public Pagamento getPagamento(Long pagamentoId){
        LOGGER.info("Retrieving 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'pagamento' '{}'", pagamento);
        return pagamento;
    }

    @Transactional
    public Pagamento createPagamento(Pagamento pagamento){
        LOGGER.info("Creating 'pagamento'");

        if(pagamento.getTipoPagamento() != null && pagamento.getTipoPagamento().getId() == null){
            pagamento.setTipoPagamento(null);
        }

        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }
        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);

        String resource = "";
        Ddt ddt = null;
        NotaAccredito notaAccredito = null;
        if(pagamento.getDdt() != null && pagamento.getDdt().getId() != null){
            ddt = ddtRepository.findById(pagamento.getDdt().getId()).orElseThrow(ResourceNotFoundException::new);;
            totaleAcconto = ddt.getTotaleAcconto();
            totale = ddt.getTotale();

            pagamento.setNotaAccredito(null);

            resource= "DDT";
        } else if(pagamento.getNotaAccredito() != null && pagamento.getNotaAccredito().getId() != null){
            notaAccredito = notaAccreditoRepository.findById(pagamento.getNotaAccredito().getId()).orElseThrow(ResourceNotFoundException::new);;
            totaleAcconto = notaAccredito.getTotaleAcconto();
            totale = notaAccredito.getTotale();

            pagamento.setDdt(null);

            resource= "Nota Accredito";
        }
        LOGGER.info("Resource {}", resource);
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totale == null){
            totale = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = totaleAcconto.add(importo).setScale(2, RoundingMode.HALF_DOWN);
        if(newTotaleAcconto.compareTo(totale) == 1){
            LOGGER.error("The 'importo' '{}' sum to '{}' is greater than 'totale' '{}' (idDdt={}, idNotaAccredito={})", importo, totaleAcconto, totale, pagamento.getDdt().getId(), pagamento.getNotaAccredito().getId());
            throw new PagamentoExceedingException(resource);
        }
        String descrizione = pagamento.getDescrizione();
        if(newTotaleAcconto.compareTo(totale) == 0){
            descrizione = descrizione.replace("Pagamento", "Saldo");
        } else if(newTotaleAcconto.compareTo(totale) == -1){
            descrizione = descrizione.replace("Pagamento", "Acconto");
        }
        pagamento.setDescrizione(descrizione);
        pagamento.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Pagamento createdPagamento = pagamentoRepository.save(pagamento);
        LOGGER.info("Created 'pagamento' '{}'", createdPagamento);

        switch(resource){
            case "DDT":
                LOGGER.info("Updating 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                ddt.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddt);
                ddtRepository.save(ddt);
                LOGGER.info("Updated 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                break;
            case "Nota Accredito":
                LOGGER.info("Updating 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                notaAccredito.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaAccredito);
                notaAccreditoRepository.save(notaAccredito);
                LOGGER.info("Updated 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                break;
            default:
                LOGGER.info("No case found for updating 'totaleAcconto' on ddt or notaAccredito");
        }
        return createdPagamento;
    }

    @Transactional
    public void deletePagamento(Long pagamentoId){
        LOGGER.info("Deleting 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }
        BigDecimal totaleAcconto = new BigDecimal(0);
        String resource= "";
        Ddt ddt = null;
        NotaAccredito notaAccredito = null;

        if(pagamento.getDdt() != null){
            ddt = pagamento.getDdt();
            if(ddt.getId() != null){
                totaleAcconto = ddt.getTotaleAcconto();

                resource= "DDT";
            }
        } else if(pagamento.getNotaAccredito() != null){
            notaAccredito = pagamento.getNotaAccredito();
            if(notaAccredito.getId() != null){
                totaleAcconto = notaAccredito.getTotaleAcconto();

                resource= "Nota Accredito";
            }
        }
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = totaleAcconto.subtract(importo).setScale(2, RoundingMode.HALF_DOWN);

        switch(resource){
            case "DDT":
                LOGGER.info("Updating 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                ddt.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddt);
                ddtRepository.save(ddt);
                LOGGER.info("Updated 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                break;
            case "Nota Accredito":
                LOGGER.info("Updating 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                notaAccredito.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaAccredito);
                notaAccreditoRepository.save(notaAccredito);
                LOGGER.info("Updated 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                break;
            default:
                LOGGER.info("No case found for updating 'totaleAcconto' on ddt or notaAccredito");
        }
        pagamentoRepository.deleteById(pagamentoId);
        LOGGER.info("Deleted 'pagamento' '{}'", pagamentoId);
    }

    private void computeStato(Ddt ddt){
        BigDecimal totaleAcconto = ddt.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            ddt.setStatoDdt(statoDdtService.getDaPagare());
        } else {
            BigDecimal totale = ddt.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) == -1 || result.compareTo(BigDecimal.ZERO) == 0){
                ddt.setStatoDdt(statoDdtService.getPagato());
            } else {
                ddt.setStatoDdt(statoDdtService.getParzialmentePagato());
            }
        }
    }

    private void computeStato(NotaAccredito notaAccredito){
        BigDecimal totaleAcconto = notaAccredito.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getDaPagare());
        } else {
            BigDecimal totale = notaAccredito.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) == -1 || result.compareTo(BigDecimal.ZERO) == 0){
                notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getPagato());
            } else {
                notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getParzialmentePagata());
            }
        }
    }

}
