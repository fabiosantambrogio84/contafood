package com.contafood.service;

import com.contafood.exception.PagamentoExceedingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.views.VPagamento;
import com.contafood.repository.*;
import com.contafood.repository.views.VPagamentoRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final DdtRepository ddtRepository;
    private final DdtAcquistoRepository ddtAcquistoRepository;
    private final NotaAccreditoRepository notaAccreditoRepository;
    private final NotaResoRepository notaResoRepository;
    private final RicevutaPrivatoRepository ricevutaPrivatoRepository;
    private final FatturaRepository fatturaRepository;
    private final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository;
    private final FatturaAcquistoRepository fatturaAcquistoRepository;
    private final StatoDdtService statoDdtService;
    private final StatoNotaAccreditoService statoNotaAccreditoService;
    private final StatoNotaResoService statoNotaResoService;
    private final StatoRicevutaPrivatoService statoRicevutaPrivatoService;
    private final StatoFatturaService statoFatturaService;
    private final VPagamentoRepository vPagamentoRepository;

    @Autowired
    public PagamentoService(final PagamentoRepository pagamentoRepository,
                            final DdtRepository ddtRepository,
                            final DdtAcquistoRepository ddtAcquistoRepository,
                            final NotaAccreditoRepository notaAccreditoRepository,
                            final NotaResoRepository notaResoRepository,
                            final RicevutaPrivatoRepository ricevutaPrivatoRepository,
                            final FatturaRepository fatturaRepository,
                            final FatturaAccompagnatoriaRepository fatturaAccompagnatoriaRepository,
                            final FatturaAcquistoRepository fatturaAcquistoRepository,
                            final StatoDdtService statoDdtService,
                            final StatoNotaAccreditoService statoNotaAccreditoService,
                            final StatoNotaResoService statoNotaResoService,
                            final StatoRicevutaPrivatoService statoRicevutaPrivatoService,
                            final StatoFatturaService statoFatturaService,
                            final VPagamentoRepository vPagamentoRepository){
        this.pagamentoRepository = pagamentoRepository;
        this.ddtRepository = ddtRepository;
        this.ddtAcquistoRepository = ddtAcquistoRepository;
        this.notaAccreditoRepository = notaAccreditoRepository;
        this.notaResoRepository = notaResoRepository;
        this.ricevutaPrivatoRepository = ricevutaPrivatoRepository;
        this.fatturaRepository = fatturaRepository;
        this.fatturaAccompagnatoriaRepository = fatturaAccompagnatoriaRepository;
        this.fatturaAcquistoRepository = fatturaAcquistoRepository;
        this.statoDdtService = statoDdtService;
        this.statoNotaAccreditoService = statoNotaAccreditoService;
        this.statoNotaResoService = statoNotaResoService;
        this.statoRicevutaPrivatoService = statoRicevutaPrivatoService;
        this.statoFatturaService = statoFatturaService;
        this.vPagamentoRepository = vPagamentoRepository;
    }

    public Set<Pagamento> getPagamenti(){
        log.info("Retrieving 'pagamenti'");
        Set<Pagamento> pagamenti = pagamentoRepository.findAllByOrderByDataDesc();
        log.info("Retrieved {} 'pagamenti'", pagamenti.size());
        return pagamenti;
    }

    public List<VPagamento> getAllByFilters(String tipologia, Date dataDa, Date dataA, String cliente, String fornitore, Float importo){
        log.info("Retrieving the list of 'pagamenti' filtered by request paramters");
        List<VPagamento> pagamenti = vPagamentoRepository.findByFilter(tipologia, dataDa, dataA, cliente, fornitore, importo);
        log.info("Retrieved {} 'pagamenti'", pagamenti.size());
        return pagamenti;
    }

    public Set<Pagamento> getDdtPagamentiByIdDdt(Long ddtId){
        log.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        log.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
    }

    public Set<Pagamento> getDdtAcquistoPagamentiByIdDdtAcquisto(Long ddtAcquistoId){
        log.info("Retrieving 'pagamenti' of 'ddt acquisto' '{}'", ddtAcquistoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtAcquistoIdOrderByDataDesc(ddtAcquistoId);
        log.info("Retrieved {} 'pagamenti' of 'ddt acquisto' '{}'", pagamenti.size(), ddtAcquistoId);
        return pagamenti;
    }

    public Set<Pagamento> getNotaAccreditoPagamentiByIdNotaAccredito(Long notaAccreditoId){
        log.info("Retrieving 'pagamenti' of 'notaAccredito' '{}'", notaAccreditoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByNotaAccreditoIdOrderByDataDesc(notaAccreditoId);
        log.info("Retrieved {} 'pagamenti' of 'notaAccredito' '{}'", pagamenti.size(), notaAccreditoId);
        return pagamenti;
    }

    public Set<Pagamento> getNotaResoPagamentiByIdNotaReso(Long notaResoId){
        log.info("Retrieving 'pagamenti' of 'notaReso' '{}'", notaResoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByNotaResoIdOrderByDataDesc(notaResoId);
        log.info("Retrieved {} 'pagamenti' of 'notaReso' '{}'", pagamenti.size(), notaResoId);
        return pagamenti;
    }

    public Set<Pagamento> getRicevutaPrivatoPagamentiByIdRicevutaPrivato(Long ricevutaPrivatoId){
        log.info("Retrieving 'pagamenti' of 'ricevutaPrivato' '{}'", ricevutaPrivatoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByRicevutaPrivatoIdOrderByDataDesc(ricevutaPrivatoId);
        log.info("Retrieved {} 'pagamenti' of 'ricevutaPrivato' '{}'", pagamenti.size(), ricevutaPrivatoId);
        return pagamenti;
    }

    public Set<Pagamento> getFatturaPagamentiByIdFattura(Long fatturaId){
        log.info("Retrieving 'pagamenti' of 'fattura' '{}'", fatturaId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByFatturaIdOrderByDataDesc(fatturaId);
        log.info("Retrieved {} 'pagamenti' of 'fattura' '{}'", pagamenti.size(), fatturaId);
        return pagamenti;
    }

    public Set<Pagamento> getFatturaAccompagnatoriaPagamentiByIdFatturaAccompagnatoria(Long fatturaAccompagnatoriaId){
        log.info("Retrieving 'pagamenti' of 'fatturaAccompagnatoria' '{}'", fatturaAccompagnatoriaId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByFatturaAccompagnatoriaIdOrderByDataDesc(fatturaAccompagnatoriaId);
        log.info("Retrieved {} 'pagamenti' of 'fatturaAccompagnatoria' '{}'", pagamenti.size(), fatturaAccompagnatoriaId);
        return pagamenti;
    }

    public Set<Pagamento> getFatturaAcquistoPagamentiByIdFatturaAcquisto(Long fatturaAcquistoId){
        log.info("Retrieving 'pagamenti' of 'fatturaAcquisto' '{}'", fatturaAcquistoId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByFatturaAcquistoIdOrderByDataDesc(fatturaAcquistoId);
        log.info("Retrieved {} 'pagamenti' of 'fatturaAcquisto' '{}'", pagamenti.size(), fatturaAcquistoId);
        return pagamenti;
    }

    public Pagamento getPagamento(Long pagamentoId){
        log.info("Retrieving 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        log.info("Retrieved 'pagamento' '{}'", pagamento);
        return pagamento;
    }

    @Transactional
    public Pagamento createPagamento(Pagamento pagamento){
        log.info("Creating 'pagamento'");

        if(pagamento.getTipoPagamento() != null && pagamento.getTipoPagamento().getId() == null){
            pagamento.setTipoPagamento(null);
        }

        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }
        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);

        Resource resource = null;
        Ddt ddt = null;
        DdtAcquisto ddtAcquisto = null;
        NotaAccredito notaAccredito = null;
        NotaReso notaReso = null;
        RicevutaPrivato ricevutaPrivato = null;
        Fattura fattura = null;
        FatturaAccompagnatoria fatturaAccompagnatoria = null;
        FatturaAcquisto fatturaAcquisto = null;
        if(pagamento.getDdt() != null && pagamento.getDdt().getId() != null){
            ddt = ddtRepository.findById(pagamento.getDdt().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = ddt.getTotaleAcconto();
            totale = ddt.getTotale();

            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource = Resource.DDT;

        } else if(pagamento.getDdtAcquisto() != null && pagamento.getDdtAcquisto().getId() != null){
            ddtAcquisto = ddtAcquistoRepository.findById(pagamento.getDdtAcquisto().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = ddtAcquisto.getTotaleAcconto();
            totale = ddtAcquisto.getTotale();

            pagamento.setDdt(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource = Resource.DDT_ACQUISTO;

        } else if(pagamento.getNotaAccredito() != null && pagamento.getNotaAccredito().getId() != null){
            notaAccredito = notaAccreditoRepository.findById(pagamento.getNotaAccredito().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = notaAccredito.getTotaleAcconto();
            totale = notaAccredito.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource= Resource.NOTA_ACCREDITO;

        } else if(pagamento.getNotaReso() != null && pagamento.getNotaReso().getId() != null){
            notaReso = notaResoRepository.findById(pagamento.getNotaReso().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = notaReso.getTotaleAcconto();
            totale = notaReso.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource= Resource.NOTA_RESO;

        } else if(pagamento.getRicevutaPrivato() != null && pagamento.getRicevutaPrivato().getId() != null){
            ricevutaPrivato = ricevutaPrivatoRepository.findById(pagamento.getRicevutaPrivato().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = ricevutaPrivato.getTotaleAcconto();
            totale = ricevutaPrivato.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource= Resource.RICEVUTA_PRIVATO;

        } else if(pagamento.getFattura() != null && pagamento.getFattura().getId() != null){
            fattura = fatturaRepository.findById(pagamento.getFattura().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = fattura.getTotaleAcconto();
            totale = fattura.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFatturaAccompagnatoria(null);
            pagamento.setFatturaAcquisto(null);

            resource= Resource.FATTURA;

        } else if(pagamento.getFatturaAccompagnatoria() != null && pagamento.getFatturaAccompagnatoria().getId() != null){
            fatturaAccompagnatoria = fatturaAccompagnatoriaRepository.findById(pagamento.getFatturaAccompagnatoria().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = fatturaAccompagnatoria.getTotaleAcconto();
            totale = fatturaAccompagnatoria.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAcquisto(null);

            resource= Resource.FATTURA_ACCOMPAGNATORIA;
            
        } else if(pagamento.getFatturaAcquisto() != null && pagamento.getFatturaAcquisto().getId() != null){
            fatturaAcquisto = fatturaAcquistoRepository.findById(pagamento.getFatturaAcquisto().getId()).orElseThrow(ResourceNotFoundException::new);
            totaleAcconto = fatturaAcquisto.getTotaleAcconto();
            totale = fatturaAcquisto.getTotale();

            pagamento.setDdt(null);
            pagamento.setDdtAcquisto(null);
            pagamento.setNotaAccredito(null);
            pagamento.setNotaReso(null);
            pagamento.setRicevutaPrivato(null);
            pagamento.setFattura(null);
            pagamento.setFatturaAccompagnatoria(null);

            resource= Resource.FATTURA_ACQUISTO;
        }

        log.info("Resource {}", resource);
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totale == null){
            totale = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = Utils.roundPrice(totaleAcconto.add(importo));
        if(newTotaleAcconto.compareTo(totale) > 0){
            log.error("The 'importo' '{}' sum to '{}' is greater than 'totale' '{}' (idDdt={}, idNotaAccredito={})", importo, totaleAcconto, totale, pagamento.getDdt().getId(), pagamento.getNotaAccredito().getId());
            throw new PagamentoExceedingException(resource);
        }
        String descrizione = pagamento.getDescrizione();
        if(newTotaleAcconto.compareTo(totale) == 0){
            descrizione = descrizione.replace("Pagamento", "Saldo");
        } else if(newTotaleAcconto.compareTo(totale) < 0){
            descrizione = descrizione.replace("Pagamento", "Acconto");
        }
        pagamento.setDescrizione(descrizione);
        pagamento.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Pagamento createdPagamento = pagamentoRepository.save(pagamento);
        log.info("Created 'pagamento' '{}'", createdPagamento);

        switch(resource){
            case DDT:
                log.info("Updating 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                ddt.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddt, true, importo, "CREATE_PAGAMENTO");
                ddtRepository.save(ddt);
                log.info("Updated 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                break;
            case DDT_ACQUISTO:
                log.info("Updating 'totaleAcconto' of 'ddt acquisto' '{}'", ddtAcquisto.getId());
                ddtAcquisto.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddtAcquisto, true, importo, "CREATE_PAGAMENTO");
                ddtAcquistoRepository.save(ddtAcquisto);
                log.info("Updated 'totaleAcconto' of 'ddt acquisto' '{}'", ddtAcquisto.getId());
                break;
            case NOTA_ACCREDITO:
                log.info("Updating 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                notaAccredito.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaAccredito);
                notaAccreditoRepository.save(notaAccredito);
                log.info("Updated 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                break;
            case NOTA_RESO:
                log.info("Updating 'totaleAcconto' of 'notaReso' '{}'", notaReso.getId());
                notaReso.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaReso);
                notaResoRepository.save(notaReso);
                log.info("Updated 'totaleAcconto' of 'notaReso' '{}'", notaReso.getId());
                break;
            case RICEVUTA_PRIVATO:
                log.info("Updating 'totaleAcconto' of 'ricevutaPrivato' '{}'", ricevutaPrivato.getId());
                ricevutaPrivato.setTotaleAcconto(newTotaleAcconto);
                computeStato(ricevutaPrivato);
                ricevutaPrivatoRepository.save(ricevutaPrivato);
                log.info("Updated 'totaleAcconto' of 'ricevutaPrivato' '{}'", ricevutaPrivato.getId());
                break;
            case FATTURA:
                log.info("Updating 'totaleAcconto' of 'fattura' '{}'", fattura.getId());
                fattura.setTotaleAcconto(newTotaleAcconto);
                computeStato(fattura, true, importo, "CREATE_PAGAMENTO");
                fatturaRepository.save(fattura);
                log.info("Updated 'totaleAcconto' of 'fattura' '{}'", fattura.getId());
                break;
            case FATTURA_ACCOMPAGNATORIA:
                log.info("Updating 'totaleAcconto' of 'fatturaAccompagnatoria' '{}'", fatturaAccompagnatoria.getId());
                fatturaAccompagnatoria.setTotaleAcconto(newTotaleAcconto);
                computeStato(fatturaAccompagnatoria);
                fatturaAccompagnatoriaRepository.save(fatturaAccompagnatoria);
                log.info("Updated 'totaleAcconto' of 'fatturaAccompagnatoria' '{}'", fatturaAccompagnatoria.getId());
                break;
            case FATTURA_ACQUISTO:
                log.info("Updating 'totaleAcconto' of 'fattura acquisto' '{}'", fatturaAcquisto.getId());
                fatturaAcquisto.setTotaleAcconto(newTotaleAcconto);
                computeStato(fatturaAcquisto, true, importo, "CREATE_PAGAMENTO");
                fatturaAcquistoRepository.save(fatturaAcquisto);
                log.info("Updated 'totaleAcconto' of 'fattura' '{}'", fatturaAcquisto.getId());
                break;
            default:
                log.info("No case found for updating 'totaleAcconto' on ddt or notaAccredito");
        }
        return createdPagamento;
    }

    public Pagamento updateSimple(Pagamento pagamento){
        log.info("Updating 'pagamento'");
        Pagamento updatedPagamento = pagamentoRepository.save(pagamento);
        log.info("Updated 'pagamento' '{}'", updatedPagamento);
        return updatedPagamento;
    }

    @Transactional
    public void deletePagamento(Long pagamentoId){
        log.info("Deleting 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }
        BigDecimal totaleAcconto = new BigDecimal(0);
        Resource resource = null;
        Ddt ddt = null;
        DdtAcquisto ddtAcquisto = null;
        NotaAccredito notaAccredito = null;
        NotaReso notaReso = null;
        RicevutaPrivato ricevutaPrivato = null;
        Fattura fattura = null;
        FatturaAccompagnatoria fatturaAccompagnatoria = null;
        FatturaAcquisto fatturaAcquisto = null;

        if(pagamento.getDdt() != null){
            ddt = pagamento.getDdt();
            if(ddt.getId() != null){
                totaleAcconto = ddt.getTotaleAcconto();

                resource = Resource.DDT;
            }
        } else if(pagamento.getDdtAcquisto() != null){
            ddtAcquisto = pagamento.getDdtAcquisto();
            if(ddtAcquisto.getId() != null){
                totaleAcconto = ddtAcquisto.getTotaleAcconto();

                resource = Resource.DDT_ACQUISTO;
            }
        } else if(pagamento.getNotaAccredito() != null){
            notaAccredito = pagamento.getNotaAccredito();
            if(notaAccredito.getId() != null){
                totaleAcconto = notaAccredito.getTotaleAcconto();

                resource = Resource.NOTA_ACCREDITO;
            }
        } else if(pagamento.getNotaReso() != null){
            notaReso = pagamento.getNotaReso();
            if(notaReso.getId() != null){
                totaleAcconto = notaReso.getTotaleAcconto();

                resource = Resource.NOTA_RESO;
            }
        } else if(pagamento.getRicevutaPrivato() != null){
            ricevutaPrivato = pagamento.getRicevutaPrivato();
            if(ricevutaPrivato.getId() != null){
                totaleAcconto = ricevutaPrivato.getTotaleAcconto();

                resource = Resource.RICEVUTA_PRIVATO;
            }
        } else if(pagamento.getFattura() != null){
            fattura = pagamento.getFattura();
            if(fattura.getId() != null){
                totaleAcconto = fattura.getTotaleAcconto();

                resource = Resource.FATTURA;
            }

        } else if(pagamento.getFatturaAccompagnatoria() != null){
            fatturaAccompagnatoria = pagamento.getFatturaAccompagnatoria();
            if(fatturaAccompagnatoria.getId() != null){
                totaleAcconto = fatturaAccompagnatoria.getTotaleAcconto();

                resource = Resource.FATTURA_ACCOMPAGNATORIA;
            }
        } else if(pagamento.getFatturaAcquisto() != null){
            fatturaAcquisto = pagamento.getFatturaAcquisto();
            if(fatturaAcquisto.getId() != null){
                totaleAcconto = fatturaAcquisto.getTotaleAcconto();

                resource = Resource.FATTURA_ACQUISTO;
            }
        }

        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = Utils.roundPrice(totaleAcconto.subtract(importo));

        switch(resource){
            case DDT:
                log.info("Updating 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                ddt.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddt, true, importo, "DELETE_PAGAMENTO");
                ddtRepository.save(ddt);
                log.info("Updated 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
                break;
            case DDT_ACQUISTO:
                log.info("Updating 'totaleAcconto' of 'ddt acquisto' '{}'", ddtAcquisto.getId());
                ddtAcquisto.setTotaleAcconto(newTotaleAcconto);
                computeStato(ddtAcquisto, true, importo, "DELETE_PAGAMENTO");
                ddtAcquistoRepository.save(ddtAcquisto);
                log.info("Updated 'totaleAcconto' of 'ddt acquisto' '{}'", ddtAcquisto.getId());
                break;
            case NOTA_ACCREDITO:
                log.info("Updating 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                notaAccredito.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaAccredito);
                notaAccreditoRepository.save(notaAccredito);
                log.info("Updated 'totaleAcconto' of 'notaAccredito' '{}'", notaAccredito.getId());
                break;
            case NOTA_RESO:
                log.info("Updating 'totaleAcconto' of 'notaReso' '{}'", notaReso.getId());
                notaReso.setTotaleAcconto(newTotaleAcconto);
                computeStato(notaReso);
                notaResoRepository.save(notaReso);
                log.info("Updated 'totaleAcconto' of 'notaReso' '{}'", notaReso.getId());
                break;
            case RICEVUTA_PRIVATO:
                log.info("Updating 'totaleAcconto' of 'ricevutaPrivato' '{}'", ricevutaPrivato.getId());
                ricevutaPrivato.setTotaleAcconto(newTotaleAcconto);
                computeStato(ricevutaPrivato);
                ricevutaPrivatoRepository.save(ricevutaPrivato);
                log.info("Updated 'totaleAcconto' of 'ricevutaPrivato' '{}'", ricevutaPrivato.getId());
                break;
            case FATTURA:
                log.info("Updating 'totaleAcconto' of 'fattura' '{}'", fattura.getId());
                fattura.setTotaleAcconto(newTotaleAcconto);
                computeStato(fattura, true, importo, "DELETE_PAGAMENTO");
                fatturaRepository.save(fattura);
                log.info("Updated 'totaleAcconto' of 'fattura' '{}'", fattura.getId());
                break;
            case FATTURA_ACCOMPAGNATORIA:
                log.info("Updating 'totaleAcconto' of 'fatturaAccompagnatoria' '{}'", fatturaAccompagnatoria.getId());
                fatturaAccompagnatoria.setTotaleAcconto(newTotaleAcconto);
                computeStato(fatturaAccompagnatoria);
                fatturaAccompagnatoriaRepository.save(fatturaAccompagnatoria);
                log.info("Updated 'totaleAcconto' of 'fatturaAccompagnatoria' '{}'", fatturaAccompagnatoria.getId());
                break;
            case FATTURA_ACQUISTO:
                log.info("Updating 'totaleAcconto' of 'fattura acquisto' '{}'", fatturaAcquisto.getId());
                fatturaAcquisto.setTotaleAcconto(newTotaleAcconto);
                computeStato(fatturaAcquisto, true, importo, "DELETE_PAGAMENTO");
                fatturaAcquistoRepository.save(fatturaAcquisto);
                log.info("Updated 'totaleAcconto' of 'fattura acquisto' '{}'", fatturaAcquisto.getId());
                break;
            default:
                log.info("No case found for updating 'totaleAcconto' on ddt or notaAccredito");
        }
        pagamentoRepository.deleteById(pagamentoId);
        log.info("Deleted 'pagamento' '{}'", pagamentoId);
    }

    private void computeStato(Ddt ddt, boolean aggiornaFatture, BigDecimal importoPagamento, String context){
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
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                ddt.setStatoDdt(statoDdtService.getPagato());
            } else {
                ddt.setStatoDdt(statoDdtService.getParzialmentePagato());
            }
        }

        if(aggiornaFatture){

            BigDecimal newImportoPagamento = importoPagamento;

            // compute stato for associated Fatture
            List<Fattura> fatture = new ArrayList<>();
            fatturaRepository.findAll().forEach(f -> {
                Set<FatturaDdt> fatturaDdts = f.getFatturaDdts();
                if(fatturaDdts != null && ! fatturaDdts.isEmpty()){
                    for(FatturaDdt fatturaDdt: fatturaDdts){
                        Ddt d = fatturaDdt.getDdt();
                        if(d != null && d.getId().equals(ddt.getId())){
                            fatture.add(f);
                        }
                    }
                }
            });

            if(context.equals("CREATE_PAGAMENTO")) {
                // newImportoPagamento = importoPagamento
                // per ogni fattura associata al DDT
                // prendo il totaleAcconto
                // caso 1) totaleAcconto+newImportoPagamento > totaleFattura
                //          newTotaleAcconto = totaleFattura-totaleAcconto
                //          newImportoPagamento = newImportoPagamento-newTotaleAcconto
                // caso 2) totaleAcconto+newImportoPagamento <= totaleFattura
                //          newTotaleAcconto = totaleAcconto+importoPagamento
                //          newImportoPagamento = 0
                // aggiorno Fattura.totaleAcconto=newTotaleAcconto
                // calcolo statoFattura

                // compute totaleAcconto and stato for associated Fatture
                if(!fatture.isEmpty()){
                    for(Fattura fattura: fatture){
                        log.info("Updating totaleAcconto for fattura '{}' associated to ddt '{}'", fattura.getId(), ddt.getId());
                        BigDecimal newFatturaTotaleAcconto;
                        BigDecimal fatturaTotaleAcconto = fattura.getTotaleAcconto();
                        BigDecimal fatturaTotale = fattura.getTotale();
                        BigDecimal accontoPlusPagamento = fatturaTotaleAcconto.add(newImportoPagamento);
                        if(accontoPlusPagamento.compareTo(fatturaTotale) > 0){
                            newFatturaTotaleAcconto = fatturaTotale.subtract(fatturaTotaleAcconto);
                            newImportoPagamento = newImportoPagamento.subtract(newFatturaTotaleAcconto);
                        } else {
                            newFatturaTotaleAcconto = fatturaTotaleAcconto.add(newImportoPagamento);
                            newImportoPagamento = BigDecimal.ZERO;
                        }
                        log.info("Update 'totaleAcconto' with value={} for fattura '{}'", newFatturaTotaleAcconto, fattura.getId());
                        if(newFatturaTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                            newFatturaTotaleAcconto = BigDecimal.ZERO;
                        }
                        fattura.setTotaleAcconto(newFatturaTotaleAcconto);
                        fatturaRepository.save(fattura);

                        computeStato(fattura, false, null, null);
                    }
                }

            } else {
                // newImportoPagamento = importoPagamento
                // per ogni fattura associata al DDT
                // prendo il totaleAcconto
                // caso 1) totaleAcconto-newImportoPagamento <= 0
                //          newTotaleAcconto = 0
                //          newImportoPagamento = newImportoPagamento-totaleAcconto
                // caso 2) totaleAcconto-newImportoPagamento > 0
                //          newTotaleAcconto = totaleAcconto-importoPagamento
                //          newImportoPagamento = 0
                // aggiorno Fattura.totaleAcconto=newTotaleAcconto
                // calcolo statoFattura
                if(!fatture.isEmpty()) {
                    for (Fattura fattura : fatture) {
                        log.info("Updating totaleAcconto for fattura '{}' associated to ddt '{}'", fattura.getId(), ddt.getId());
                        BigDecimal newFatturaTotaleAcconto = BigDecimal.ZERO;
                        BigDecimal fatturaTotaleAcconto = fattura.getTotaleAcconto();
                        BigDecimal accontoMinusPagamento = fatturaTotaleAcconto.subtract(newImportoPagamento);
                        if(accontoMinusPagamento.compareTo(BigDecimal.ZERO) > 0){
                            newFatturaTotaleAcconto = fatturaTotaleAcconto.subtract(newImportoPagamento);
                            newImportoPagamento = BigDecimal.ZERO;
                        } else {
                            newImportoPagamento = newImportoPagamento.subtract(fatturaTotaleAcconto);
                        }
                        log.info("Update 'totaleAcconto' with value={} for fattura '{}'", newFatturaTotaleAcconto, fattura.getId());
                        if(newFatturaTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                            newFatturaTotaleAcconto = BigDecimal.ZERO;
                        }
                        fattura.setTotaleAcconto(newFatturaTotaleAcconto);
                        fatturaRepository.save(fattura);

                        computeStato(fattura, false, null, null);
                    }
                }
            }

        }
    }

    private void computeStato(DdtAcquisto ddtAcquisto, boolean aggiornaFatture, BigDecimal importoPagamento, String context){
        BigDecimal totaleAcconto = ddtAcquisto.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            ddtAcquisto.setStatoDdt(statoDdtService.getDaPagare());
        } else {
            BigDecimal totale = ddtAcquisto.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                ddtAcquisto.setStatoDdt(statoDdtService.getPagato());
            } else {
                ddtAcquisto.setStatoDdt(statoDdtService.getParzialmentePagato());
            }
        }

        if(aggiornaFatture){

            BigDecimal newImportoPagamento = importoPagamento;

            // compute stato for associated list of FatturaAcquisto
            List<FatturaAcquisto> fattureAcquisto = new ArrayList<>();
            fatturaAcquistoRepository.findAll().forEach(fa -> {
                Set<FatturaAcquistoDdtAcquisto> fatturaDdtsAcquisto = fa.getFatturaAcquistoDdtAcquisti();
                if(fatturaDdtsAcquisto != null && ! fatturaDdtsAcquisto.isEmpty()){
                    for(FatturaAcquistoDdtAcquisto fatturaAcquistoDdtAcquisto: fatturaDdtsAcquisto){
                        DdtAcquisto d = fatturaAcquistoDdtAcquisto.getDdtAcquisto();
                        if(d != null && d.getId().equals(ddtAcquisto.getId())){
                            fattureAcquisto.add(fa);
                        }
                    }
                }
            });

            if(context.equals("CREATE_PAGAMENTO")) {
                // newImportoPagamento = importoPagamento
                // per ogni fatturaAcquisto associata al DDT Acquisto
                // prendo il totaleAcconto
                // caso 1) totaleAcconto+newImportoPagamento > totaleFattura
                //          newTotaleAcconto = totaleFattura-totaleAcconto
                //          newImportoPagamento = newImportoPagamento-newTotaleAcconto
                // caso 2) totaleAcconto+newImportoPagamento <= totaleFattura
                //          newTotaleAcconto = totaleAcconto+importoPagamento
                //          newImportoPagamento = 0
                // aggiorno Fattura.totaleAcconto=newTotaleAcconto
                // calcolo statoFattura

                // compute totaleAcconto and stato for associated list of FatturaAcquisto
                if(!fattureAcquisto.isEmpty()){
                    for(FatturaAcquisto fatturaAcquisto: fattureAcquisto){
                        log.info("Updating totaleAcconto for fattura acquisto '{}' associated to ddt acquisto '{}'", fatturaAcquisto.getId(), ddtAcquisto.getId());
                        BigDecimal newFatturaAcquistoTotaleAcconto;
                        BigDecimal fatturaAcquistoTotaleAcconto = fatturaAcquisto.getTotaleAcconto();
                        BigDecimal fatturaTotale = fatturaAcquisto.getTotale();
                        BigDecimal accontoPlusPagamento = fatturaAcquistoTotaleAcconto.add(newImportoPagamento);
                        if(accontoPlusPagamento.compareTo(fatturaTotale) > 0){
                            newFatturaAcquistoTotaleAcconto = fatturaTotale.subtract(fatturaAcquistoTotaleAcconto);
                            newImportoPagamento = newImportoPagamento.subtract(newFatturaAcquistoTotaleAcconto);
                        } else {
                            newFatturaAcquistoTotaleAcconto = fatturaAcquistoTotaleAcconto.add(newImportoPagamento);
                            newImportoPagamento = BigDecimal.ZERO;
                        }
                        log.info("Update 'totaleAcconto' with value={} for fattura acquisto '{}'", newFatturaAcquistoTotaleAcconto, fatturaAcquisto.getId());
                        if(newFatturaAcquistoTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                            newFatturaAcquistoTotaleAcconto = BigDecimal.ZERO;
                        }
                        fatturaAcquisto.setTotaleAcconto(newFatturaAcquistoTotaleAcconto);
                        fatturaAcquistoRepository.save(fatturaAcquisto);

                        computeStato(fatturaAcquisto, false, null, null);
                    }
                }

            } else {
                // newImportoPagamento = importoPagamento
                // per ogni fattura acquisto associata al DDT acquisto
                // prendo il totaleAcconto
                // caso 1) totaleAcconto-newImportoPagamento <= 0
                //          newTotaleAcconto = 0
                //          newImportoPagamento = newImportoPagamento-totaleAcconto
                // caso 2) totaleAcconto-newImportoPagamento > 0
                //          newTotaleAcconto = totaleAcconto-importoPagamento
                //          newImportoPagamento = 0
                // aggiorno Fattura.totaleAcconto=newTotaleAcconto
                // calcolo statoFattura
                if(!fattureAcquisto.isEmpty()) {
                    for (FatturaAcquisto fatturaAcquisto : fattureAcquisto) {
                        log.info("Updating totaleAcconto for fattura acquisto '{}' associated to ddt acquisto '{}'", fatturaAcquisto.getId(), ddtAcquisto.getId());
                        BigDecimal newFatturaAcquistoTotaleAcconto = BigDecimal.ZERO;
                        BigDecimal fatturaAcquistoTotaleAcconto = fatturaAcquisto.getTotaleAcconto();
                        BigDecimal accontoMinusPagamento = fatturaAcquistoTotaleAcconto.subtract(newImportoPagamento);
                        if(accontoMinusPagamento.compareTo(BigDecimal.ZERO) > 0){
                            newFatturaAcquistoTotaleAcconto = fatturaAcquistoTotaleAcconto.subtract(newImportoPagamento);
                            newImportoPagamento = BigDecimal.ZERO;
                        } else {
                            newImportoPagamento = newImportoPagamento.subtract(fatturaAcquistoTotaleAcconto);
                        }
                        log.info("Update 'totaleAcconto' with value={} for fattura acquisto '{}'", newFatturaAcquistoTotaleAcconto, fatturaAcquisto.getId());
                        if(newFatturaAcquistoTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                            newFatturaAcquistoTotaleAcconto = BigDecimal.ZERO;
                        }
                        fatturaAcquisto.setTotaleAcconto(newFatturaAcquistoTotaleAcconto);
                        fatturaAcquistoRepository.save(fatturaAcquisto);

                        computeStato(fatturaAcquisto, false, null, null);
                    }
                }
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
            if(result.compareTo(BigDecimal.ZERO) < 0 || result.compareTo(BigDecimal.ZERO) == 0){
                notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getPagato());
            } else {
                notaAccredito.setStatoNotaAccredito(statoNotaAccreditoService.getParzialmentePagata());
            }
        }
    }

    private void computeStato(NotaReso notaReso){
        BigDecimal totaleAcconto = notaReso.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            notaReso.setStatoNotaReso(statoNotaResoService.getDaPagare());
        } else {
            BigDecimal totale = notaReso.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) < 0 || result.compareTo(BigDecimal.ZERO) == 0){
                notaReso.setStatoNotaReso(statoNotaResoService.getPagato());
            } else {
                notaReso.setStatoNotaReso(statoNotaResoService.getParzialmentePagata());
            }
        }
    }

    private void computeStato(RicevutaPrivato ricevutaPrivato){
        BigDecimal totaleAcconto = ricevutaPrivato.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            ricevutaPrivato.setStatoRicevutaPrivato(statoRicevutaPrivatoService.getDaPagare());
        } else {
            BigDecimal totale = ricevutaPrivato.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                ricevutaPrivato.setStatoRicevutaPrivato(statoRicevutaPrivatoService.getPagata());
            } else {
                ricevutaPrivato.setStatoRicevutaPrivato(statoRicevutaPrivatoService.getParzialmentePagata());
            }
        }
    }

    private void computeStato(Fattura fattura, boolean aggiornaDdt, BigDecimal importoPagamento, String context){
        BigDecimal totaleAcconto = fattura.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            fattura.setStatoFattura(statoFatturaService.getDaPagare());
        } else {
            BigDecimal totale = fattura.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                fattura.setStatoFattura(statoFatturaService.getPagata());
            } else {
                fattura.setStatoFattura(statoFatturaService.getParzialmentePagata());
            }
        }


        if(aggiornaDdt){
            BigDecimal newImportoPagamento = importoPagamento;
            Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();

            if(context.equals("CREATE_PAGAMENTO")){
                // newImportoPagamento = importoPagamento
                // per ogni ddt associato alla fattura
                // prendo il totaleAcconto
                // caso 1) totaleAcconto+newImportoPagamento > totaleDdt
                //          newTotaleAcconto = totaleDdt-totaleAcconto
                //          newImportoPagamento = newImportoPagamento-newTotaleAcconto
                // caso 2) totaleAcconto+newImportoPagamento <= totaleDdt
                //          newTotaleAcconto = totaleAcconto+importoPagamento
                //          newImportoPagamento = 0
                // aggiorno DDT.totaleAcconto=newTotaleAcconto
                // calcolo statoDdt

                // compute totaleAcconto and stato for associated DDTs

                if(fatturaDdts != null && ! fatturaDdts.isEmpty()){
                    for(FatturaDdt fatturaDdt: fatturaDdts){
                        BigDecimal newDdtTotaleAcconto;

                        Ddt ddt = fatturaDdt.getDdt();
                        if(ddt != null){
                            log.info("Updating 'totaleAcconto' for ddt '{}' associated to fattura '{}'", ddt.getId(), fattura.getId());
                            BigDecimal ddtTotaleAcconto = ddt.getTotaleAcconto();
                            BigDecimal ddtTotale = ddt.getTotale();
                            BigDecimal accontoPlusPagamento = ddtTotaleAcconto.add(newImportoPagamento);
                            if(accontoPlusPagamento.compareTo(ddtTotale) > 0){
                                newDdtTotaleAcconto = ddtTotale.subtract(ddtTotaleAcconto);
                                newImportoPagamento = newImportoPagamento.subtract(newDdtTotaleAcconto);
                                newDdtTotaleAcconto = ddtTotaleAcconto.add(newDdtTotaleAcconto);
                            } else {
                                newDdtTotaleAcconto = ddtTotaleAcconto.add(newImportoPagamento);
                                newImportoPagamento = BigDecimal.ZERO;
                            }
                            log.info("Update 'totaleAcconto' with value={} for ddt '{}'", newDdtTotaleAcconto, ddt.getId());
                            if(newDdtTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                                newDdtTotaleAcconto = BigDecimal.ZERO;
                            }
                            ddt.setTotaleAcconto(newDdtTotaleAcconto);
                            ddtRepository.save(ddt);

                            computeStato(ddt, false, null, null);
                        }
                    }
                }

            } else {
                // newImportoPagamento = importoPagamento
                // per ogni ddt associato alla fattura
                // prendo il totaleAcconto
                // caso 1) totaleAcconto-newImportoPagamento <= 0
                //          newTotaleAcconto = 0
                //          newImportoPagamento = newImportoPagamento-totaleAcconto
                // caso 2) totaleAcconto-newImportoPagamento > 0
                //          newTotaleAcconto = totaleAcconto-importoPagamento
                //          newImportoPagamento = 0
                // aggiorno DDT.totaleAcconto=newTotaleAcconto
                // calcolo statoDdt

                if(fatturaDdts != null && ! fatturaDdts.isEmpty()){
                    for(FatturaDdt fatturaDdt: fatturaDdts){
                        BigDecimal newDdtTotaleAcconto = BigDecimal.ZERO;

                        Ddt ddt = fatturaDdt.getDdt();
                        if(ddt != null){
                            log.info("Updating 'totaleAcconto' for ddt '{}' associated to fattura '{}'", ddt.getId(), fattura.getId());
                            BigDecimal ddtTotaleAcconto = ddt.getTotaleAcconto();
                            BigDecimal accontoMinusPagamento = ddtTotaleAcconto.subtract(newImportoPagamento);
                            if(accontoMinusPagamento.compareTo(BigDecimal.ZERO) > 0){
                                newDdtTotaleAcconto = ddtTotaleAcconto.subtract(newImportoPagamento);
                                newImportoPagamento = BigDecimal.ZERO;
                            } else {
                                newImportoPagamento = newImportoPagamento.subtract(ddtTotaleAcconto);
                            }
                            log.info("Update 'totaleAcconto' with value={} for ddt '{}'", newDdtTotaleAcconto, ddt.getId());
                            if(newDdtTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                                newDdtTotaleAcconto = BigDecimal.ZERO;
                            }
                            ddt.setTotaleAcconto(newDdtTotaleAcconto);
                            ddtRepository.save(ddt);

                            computeStato(ddt, false, null, null);
                        }
                    }
                }
            }
        }
    }

    private void computeStato(FatturaAccompagnatoria fatturaAccompagnatoria){
        BigDecimal totaleAcconto = fatturaAccompagnatoria.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            fatturaAccompagnatoria.setStatoFattura(statoFatturaService.getDaPagare());
        } else {
            BigDecimal totale = fatturaAccompagnatoria.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                fatturaAccompagnatoria.setStatoFattura(statoFatturaService.getPagata());
            } else {
                fatturaAccompagnatoria.setStatoFattura(statoFatturaService.getParzialmentePagata());
            }
        }
    }

    private void computeStato(FatturaAcquisto fatturaAcquisto, boolean aggiornaDdtAcquisto, BigDecimal importoPagamento, String context){
        BigDecimal totaleAcconto = fatturaAcquisto.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        if(totaleAcconto.compareTo(BigDecimal.ZERO) == 0){
            fatturaAcquisto.setStatoFattura(statoFatturaService.getDaPagare());
        } else {
            BigDecimal totale = fatturaAcquisto.getTotale();
            if(totale == null){
                totale = new BigDecimal(0);
            }
            BigDecimal result = totale.subtract(totaleAcconto);
            if(result.compareTo(BigDecimal.ZERO) <= 0){
                fatturaAcquisto.setStatoFattura(statoFatturaService.getPagata());
            } else {
                fatturaAcquisto.setStatoFattura(statoFatturaService.getParzialmentePagata());
            }
        }

        if(aggiornaDdtAcquisto){
            BigDecimal newImportoPagamento = importoPagamento;
            Set<FatturaAcquistoDdtAcquisto> fatturaDdtsAcquisto = fatturaAcquisto.getFatturaAcquistoDdtAcquisti();

            if(context.equals("CREATE_PAGAMENTO")){
                // newImportoPagamento = importoPagamento
                // per ogni ddt acquisto associato alla fattura acquisto
                // prendo il totaleAcconto
                // caso 1) totaleAcconto+newImportoPagamento > totaleDdt
                //          newTotaleAcconto = totaleDdt-totaleAcconto
                //          newImportoPagamento = newImportoPagamento-newTotaleAcconto
                // caso 2) totaleAcconto+newImportoPagamento <= totaleDdt
                //          newTotaleAcconto = totaleAcconto+importoPagamento
                //          newImportoPagamento = 0
                // aggiorno DDT.totaleAcconto=newTotaleAcconto
                // calcolo statoDdt

                // compute totaleAcconto and stato for associated DDTs

                if(fatturaDdtsAcquisto != null && ! fatturaDdtsAcquisto.isEmpty()){
                    for(FatturaAcquistoDdtAcquisto fatturaDdtAcquisto: fatturaDdtsAcquisto){
                        BigDecimal newDdtAcquistoTotaleAcconto;

                        DdtAcquisto ddtAcquisto = fatturaDdtAcquisto.getDdtAcquisto();
                        if(ddtAcquisto != null){
                            log.info("Updating 'totaleAcconto' for ddt acquisto '{}' associated to fattura acquisto '{}'", ddtAcquisto.getId(), fatturaAcquisto.getId());
                            BigDecimal ddtAcquistoTotaleAcconto = ddtAcquisto.getTotaleAcconto();
                            BigDecimal ddtAcquistoTotale = ddtAcquisto.getTotale();
                            BigDecimal accontoPlusPagamento = ddtAcquistoTotaleAcconto.add(newImportoPagamento);
                            if(accontoPlusPagamento.compareTo(ddtAcquistoTotale) > 0){
                                newDdtAcquistoTotaleAcconto = ddtAcquistoTotale.subtract(ddtAcquistoTotaleAcconto);
                                newImportoPagamento = newImportoPagamento.subtract(newDdtAcquistoTotaleAcconto);
                                newDdtAcquistoTotaleAcconto = ddtAcquistoTotaleAcconto.add(ddtAcquistoTotaleAcconto);
                            } else {
                                newDdtAcquistoTotaleAcconto = ddtAcquistoTotaleAcconto.add(newImportoPagamento);
                                newImportoPagamento = BigDecimal.ZERO;
                            }
                            log.info("Update 'totaleAcconto' with value={} for ddt acquisto '{}'", newDdtAcquistoTotaleAcconto, ddtAcquisto.getId());
                            if(newDdtAcquistoTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                                newDdtAcquistoTotaleAcconto = BigDecimal.ZERO;
                            }
                            ddtAcquisto.setTotaleAcconto(newDdtAcquistoTotaleAcconto);
                            ddtAcquistoRepository.save(ddtAcquisto);

                            computeStato(ddtAcquisto, false, null, null);
                        }
                    }
                }

            } else {
                // newImportoPagamento = importoPagamento
                // per ogni ddt acquisto associato alla fattura acquisto
                // prendo il totaleAcconto
                // caso 1) totaleAcconto-newImportoPagamento <= 0
                //          newTotaleAcconto = 0
                //          newImportoPagamento = newImportoPagamento-totaleAcconto
                // caso 2) totaleAcconto-newImportoPagamento > 0
                //          newTotaleAcconto = totaleAcconto-importoPagamento
                //          newImportoPagamento = 0
                // aggiorno DDT.totaleAcconto=newTotaleAcconto
                // calcolo statoDdt

                if(fatturaDdtsAcquisto != null && ! fatturaDdtsAcquisto.isEmpty()){
                    for(FatturaAcquistoDdtAcquisto fatturaDdtAcquisto: fatturaDdtsAcquisto){
                        BigDecimal newDdtAcquistoTotaleAcconto;

                        DdtAcquisto ddtAcquisto = fatturaDdtAcquisto.getDdtAcquisto();
                        if(ddtAcquisto != null){
                            log.info("Updating 'totaleAcconto' for ddt '{}' associated to fattura '{}'", ddtAcquisto.getId(), fatturaAcquisto.getId());
                            BigDecimal ddtAcquistoTotaleAcconto = ddtAcquisto.getTotaleAcconto();
                            BigDecimal accontoMinusPagamento = ddtAcquistoTotaleAcconto.subtract(newImportoPagamento);
                            if(accontoMinusPagamento.compareTo(BigDecimal.ZERO) > 0){
                                newDdtAcquistoTotaleAcconto = ddtAcquistoTotaleAcconto.subtract(newImportoPagamento);
                                newImportoPagamento = BigDecimal.ZERO;
                            } else {
                                newDdtAcquistoTotaleAcconto = BigDecimal.ZERO;
                                newImportoPagamento = newImportoPagamento.subtract(ddtAcquistoTotaleAcconto);
                            }
                            log.info("Update 'totaleAcconto' with value={} for ddt acquisto '{}'", newDdtAcquistoTotaleAcconto, ddtAcquisto.getId());
                            if(newDdtAcquistoTotaleAcconto.compareTo(BigDecimal.ZERO) < 0){
                                newDdtAcquistoTotaleAcconto = BigDecimal.ZERO;
                            }
                            ddtAcquisto.setTotaleAcconto(newDdtAcquistoTotaleAcconto);
                            ddtAcquistoRepository.save(ddtAcquisto);

                            computeStato(ddtAcquisto, false, null, null);
                        }
                    }
                }
            }
        }
    }
}