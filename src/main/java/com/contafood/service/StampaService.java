package com.contafood.service;

import com.contafood.model.*;
import com.contafood.model.reports.*;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StampaService {

    private static Logger LOGGER = LoggerFactory.getLogger(StampaService.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final GiacenzaIngredienteService giacenzaIngredienteService;

    private final PagamentoService pagamentoService;

    private final DdtService ddtService;

    private final AutistaService autistaService;

    private final OrdineClienteService ordineClienteService;

    private final NotaAccreditoService notaAccreditoService;

    @Autowired
    public StampaService(final GiacenzaIngredienteService giacenzaIngredienteService, final DdtService ddtService, final PagamentoService pagamentoService,
                         final AutistaService autistaService,
                         final OrdineClienteService ordineClienteService,
                         final NotaAccreditoService notaAccreditoService){
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.ddtService = ddtService;
        this.pagamentoService = pagamentoService;
        this.autistaService = autistaService;
        this.ordineClienteService = ordineClienteService;
        this.notaAccreditoService = notaAccreditoService;
    }

    public List<VGiacenzaIngrediente> getGiacenzeIngredienti(String ids){
        LOGGER.info("Retrieving the list of 'giacenze-ingredienti' with id in '{}' for creating pdf file", ids);

        List<VGiacenzaIngrediente> giacenzeIngredienti = giacenzaIngredienteService.getAll().stream()
                .filter(gi -> ids.contains(gi.getIdIngrediente().toString()))
                .sorted(Comparator.comparing(VGiacenzaIngrediente::getIngrediente))
                .sorted(Comparator.comparing(VGiacenzaIngrediente::getFornitore))
                .sorted(Comparator.comparingDouble(VGiacenzaIngrediente::getQuantita))
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} 'giacenze-ingredienti'", giacenzeIngredienti.size());
        return giacenzeIngredienti;
    }

    public List<PagamentoDataSource> getPagamenti(String ids){
        LOGGER.info("Retrieving the list of 'pagamenti' with id in '{}' for creating pdf file", ids);

        List<Pagamento> pagamenti = pagamentoService.getPagamenti().stream()
                .filter(p -> ids.contains(p.getId().toString()))
                .sorted(Comparator.comparing(Pagamento::getData).reversed())
                .collect(Collectors.toList());

        List<PagamentoDataSource> pagamentiDataSource = new ArrayList<>();
        if(pagamenti != null && !pagamenti.isEmpty()){
            pagamenti.forEach(p -> {
                PagamentoDataSource pagamentoDataSource = new PagamentoDataSource();
                pagamentoDataSource.setData(simpleDateFormat.format(p.getData()));
                String clienteFornitore = "";
                Ddt ddt = p.getDdt();
                NotaAccredito notaAccredito = p.getNotaAccredito();
                Cliente cliente = null;
                Fornitore fornitore = null;
                NotaReso notaReso = p.getNotaReso();
                if(ddt != null){
                    cliente = ddt.getCliente();
                }
                if(notaAccredito != null){
                    cliente = notaAccredito.getCliente();
                }
                if(notaReso != null){
                    fornitore = notaReso.getFornitore();
                }
                if(cliente != null){
                    if(cliente.getDittaIndividuale()){
                        clienteFornitore = cliente.getNome()+" "+cliente.getCognome();
                    } else {
                        clienteFornitore = cliente.getRagioneSociale();
                    }
                }
                if(fornitore != null){
                    clienteFornitore = fornitore.getRagioneSociale();
                }
                pagamentoDataSource.setClienteFornitore(clienteFornitore);

                pagamentoDataSource.setDescrizione(p.getDescrizione());
                pagamentoDataSource.setImporto(p.getImporto());
                if(p.getTipoPagamento() != null){
                    pagamentoDataSource.setTipo(p.getTipoPagamento().getDescrizione());
                }

                pagamentiDataSource.add(pagamentoDataSource);

            });
        }

        LOGGER.info("Retrieved {} 'pagamenti'", pagamentiDataSource.size());
        return pagamentiDataSource;
    }

    public Ddt getDdt(Long idDdt){
        LOGGER.info("Retrieving 'ddt' with id '{}' for creating pdf file", idDdt);
        Ddt ddt = ddtService.getOne(idDdt);
        LOGGER.info("Retrieved 'ddt' with id '{}'", idDdt);
        return ddt;
    }

    public DdtDataSource getDdtDataSource(Ddt ddt){
        Cliente cliente = ddt.getCliente();
        TipoPagamento tipoPagamento = cliente.getTipoPagamento();
        Agente agente = cliente.getAgente();

        DdtDataSource ddtDataSource = new DdtDataSource();
        ddtDataSource.setNumero(ddt.getProgressivo()+"/"+ddt.getAnnoContabile());
        ddtDataSource.setData(simpleDateFormat.format(ddt.getData()));
        ddtDataSource.setClientePartitaIva("");
        ddtDataSource.setClienteCodiceFiscale("");
        ddtDataSource.setCausale(Constants.JASPER_PARAMETER_DDT_CAUSALE);
        ddtDataSource.setPagamento("");
        ddtDataSource.setAgente("");
        if(cliente != null){
            if(!StringUtils.isEmpty(cliente.getPartitaIva())){
                ddtDataSource.setClientePartitaIva(cliente.getPartitaIva());
            }
            if(!StringUtils.isEmpty(cliente.getCodiceFiscale())){
                ddtDataSource.setClienteCodiceFiscale(cliente.getCodiceFiscale());
            }
        }
        if(tipoPagamento != null){
            if(!StringUtils.isEmpty(tipoPagamento.getDescrizione())){
                ddtDataSource.setPagamento(tipoPagamento.getDescrizione());
            }
        }
        if(agente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(agente.getNome())){
                sb.append(agente.getNome()+" ");
            }
            if(!StringUtils.isEmpty(agente.getCognome())){
                sb.append(agente.getCognome());
            }
            ddtDataSource.setAgente(sb.toString());
        }
        return ddtDataSource;
    }

    public List<DdtArticoloDataSource> getDdtArticoliDataSource(Ddt ddt){
        List<DdtArticoloDataSource> ddtArticoloDataSources = new ArrayList<>();
        if(ddt.getDdtArticoli() != null && !ddt.getDdtArticoli().isEmpty()){
            ddt.getDdtArticoli().stream().forEach(da -> {
                DdtArticoloDataSource ddtArticoloDataSource = new DdtArticoloDataSource();
                ddtArticoloDataSource.setCodiceArticolo(da.getArticolo().getCodice());
                ddtArticoloDataSource.setDescrizioneArticolo(da.getArticolo().getDescrizione());
                ddtArticoloDataSource.setLotto(da.getLotto());
                ddtArticoloDataSource.setUdm(da.getArticolo().getUnitaMisura().getEtichetta());
                ddtArticoloDataSource.setQuantita(da.getQuantita());
                ddtArticoloDataSource.setPrezzo(da.getPrezzo() != null ? da.getPrezzo().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                ddtArticoloDataSource.setSconto(da.getSconto() != null ? da.getSconto().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                ddtArticoloDataSource.setImponibile(da.getImponibile() != null ? da.getImponibile().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                ddtArticoloDataSource.setIva(da.getArticolo().getAliquotaIva().getValore().intValue());

                ddtArticoloDataSources.add(ddtArticoloDataSource);
            });
        }
        return ddtArticoloDataSources;
    }

    public List<DdtDataSource> getDdtDataSources(String ids){
        LOGGER.info("Retrieving the list of 'ddts' with id in '{}' for creating pdf file", ids);

        List<DdtDataSource> ddtDataSources = new ArrayList<>();

        List<Ddt> ddts = ddtService.getAll().stream()
                .filter(ddt -> ids.contains(ddt.getId().toString()))
                .sorted(Comparator.comparing(Ddt::getProgressivo).reversed())
                .collect(Collectors.toList());

        if(ddts != null && !ddts.isEmpty()){
            ddts.forEach(ddt -> {
                DdtDataSource ddtDataSource = new DdtDataSource();
                ddtDataSource.setNumero(ddt.getProgressivo().toString());
                ddtDataSource.setData(simpleDateFormat.format(ddt.getData()));
                Cliente cliente = ddt.getCliente();
                if(cliente != null){
                    if(!cliente.getDittaIndividuale()){
                        ddtDataSource.setClienteDescrizione(cliente.getRagioneSociale());
                    } else {
                        ddtDataSource.setClienteDescrizione(cliente.getNome()+" "+cliente.getCognome());
                    }
                }
                BigDecimal totaleAcconto = ddt.getTotaleAcconto() != null ? ddt.getTotaleAcconto().setScale(2, RoundingMode.CEILING) : new BigDecimal(0);
                BigDecimal totale = ddt.getTotale() != null ? ddt.getTotale().setScale(2, RoundingMode.CEILING) : new BigDecimal(0);
                ddtDataSource.setAcconto(totaleAcconto);
                ddtDataSource.setTotale(totale);
                ddtDataSource.setTotaleDaPagare(totale.subtract(totaleAcconto));

                ddtDataSources.add(ddtDataSource);
            });
        }

        LOGGER.info("Retrieved {} 'ddts'", ddtDataSources.size());
        return ddtDataSources;
    }

    public Autista getAutista(Long idAutista){
        return autistaService.getOne(idAutista);
    }

    public List<OrdineAutistaDataSource> getOrdiniAutista(String ids){
        LOGGER.info("Retrieving the list of 'ordini-clienti' with id in '{}' for creating pdf file", ids);

        List<OrdineCliente> ordiniClienti = ordineClienteService.getAll().stream()
                .filter(oc -> ids.contains(oc.getId().toString()))
                .sorted(Comparator.comparing(OrdineCliente::getProgressivo).reversed())
                .sorted(Comparator.comparing(OrdineCliente::getAnnoContabile).reversed())
                .collect(Collectors.toList());

        List<OrdineAutistaDataSource> ordiniAutistaDataSource = new ArrayList<>();
        if(ordiniClienti != null && !ordiniClienti.isEmpty()){
            ordiniClienti.forEach(oc -> {
                Integer annoContabile = oc.getAnnoContabile();
                Integer progressivo = oc.getProgressivo();
                String codiceOrdine = progressivo +"/" + annoContabile;

                String cliente = "";
                if(oc.getCliente() != null){
                    if(oc.getCliente().getDittaIndividuale()){
                        cliente = oc.getCliente().getNome() + " " + oc.getCliente().getCognome();
                    } else {
                        cliente = oc.getCliente().getRagioneSociale();
                    }
                }

                OrdineAutistaDataSource ordineAutistaDataSource = new OrdineAutistaDataSource();
                ordineAutistaDataSource.setCodiceOrdine(codiceOrdine);
                ordineAutistaDataSource.setCliente(cliente);

                List<OrdineAutistaArticoloDataSource> ordineAutistaArticoliDataSource = new ArrayList<>();
                Set<OrdineClienteArticolo> ordineClienteArticoli = oc.getOrdineClienteArticoli();
                if(ordineClienteArticoli != null && !ordineClienteArticoli.isEmpty()){
                    ordineClienteArticoli.forEach(oca -> {

                        OrdineAutistaArticoloDataSource ordineAutistaArticoloDataSource = new OrdineAutistaArticoloDataSource();

                        String articolo = "";
                        if(oca.getArticolo() != null){
                            articolo = oca.getArticolo().getCodice() + " " + oca.getArticolo().getDescrizione();
                        }
                        ordineAutistaArticoloDataSource.setArticolo(articolo);
                        ordineAutistaArticoloDataSource.setNumeroPezzi(oca.getNumeroPezziOrdinati());
                        ordineAutistaArticoliDataSource.add(ordineAutistaArticoloDataSource);
                    });
                }

                ordineAutistaDataSource.setOrdineAutistaArticoloDataSources(ordineAutistaArticoliDataSource);

                ordiniAutistaDataSource.add(ordineAutistaDataSource);

            });
        }

        LOGGER.info("Retrieved {} 'ordini-clienti'", ordiniAutistaDataSource.size());
        return ordiniAutistaDataSource;
    }

    public NotaAccredito getNotaAccredito(Long idNotaAccredito){
        LOGGER.info("Retrieving 'nota-accredito' with id '{}' for creating pdf file", idNotaAccredito);
        NotaAccredito notaAccredito = notaAccreditoService.getOne(idNotaAccredito);
        LOGGER.info("Retrieved 'nota-accredito' with id '{}'", idNotaAccredito);
        return notaAccredito;
    }

    public NotaAccreditoDataSource getNotaAccreditoDataSource(NotaAccredito notaAccredito){
        Cliente cliente = notaAccredito.getCliente();
        TipoPagamento tipoPagamento = cliente.getTipoPagamento();
        Agente agente = cliente.getAgente();

        NotaAccreditoDataSource notaAccreditoDataSource = new NotaAccreditoDataSource();
        notaAccreditoDataSource.setNumero(notaAccredito.getProgressivo()+"/"+notaAccredito.getAnno());
        notaAccreditoDataSource.setData(simpleDateFormat.format(notaAccredito.getData()));
        notaAccreditoDataSource.setClientePartitaIva("");
        notaAccreditoDataSource.setClienteCodiceFiscale("");
        notaAccreditoDataSource.setPagamento("");
        notaAccreditoDataSource.setAgente("");
        if(cliente != null){
            if(!StringUtils.isEmpty(cliente.getPartitaIva())){
                notaAccreditoDataSource.setClientePartitaIva(cliente.getPartitaIva());
            }
            if(!StringUtils.isEmpty(cliente.getCodiceFiscale())){
                notaAccreditoDataSource.setClienteCodiceFiscale(cliente.getCodiceFiscale());
            }
        }
        if(tipoPagamento != null){
            if(!StringUtils.isEmpty(tipoPagamento.getDescrizione())){
                notaAccreditoDataSource.setPagamento(tipoPagamento.getDescrizione());
            }
        }
        if(agente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(agente.getNome())){
                sb.append(agente.getNome()+" ");
            }
            if(!StringUtils.isEmpty(agente.getCognome())){
                sb.append(agente.getCognome());
            }
            notaAccreditoDataSource.setAgente(sb.toString());
        }
        return notaAccreditoDataSource;
    }

    public List<NotaAccreditoRigaDataSource> getNotaAccreditoRigheDataSource(NotaAccredito notaAccredito){
        List<NotaAccreditoRigaDataSource> notaAccreditoRigaDataSources = new ArrayList<>();
        if(notaAccredito.getNotaAccreditoRighe() != null && !notaAccredito.getNotaAccreditoRighe().isEmpty()){
            notaAccredito.getNotaAccreditoRighe().stream().forEach(na -> {
                NotaAccreditoRigaDataSource notaAccreditoRigaDataSource = new NotaAccreditoRigaDataSource();
                notaAccreditoRigaDataSource.setCodiceArticolo(na.getArticolo() != null ? na.getArticolo().getCodice() : "");
                notaAccreditoRigaDataSource.setDescrizioneArticolo(na.getDescrizione());
                notaAccreditoRigaDataSource.setLotto(na.getLotto());
                notaAccreditoRigaDataSource.setUdm(na.getArticolo() != null ? (na.getArticolo().getUnitaMisura() != null ? na.getArticolo().getUnitaMisura().getEtichetta() : "") : "");
                notaAccreditoRigaDataSource.setQuantita(na.getQuantita());
                notaAccreditoRigaDataSource.setPrezzo(na.getPrezzo() != null ? na.getPrezzo().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setSconto(na.getSconto() != null ? na.getSconto().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setImponibile(na.getImponibile() != null ? na.getImponibile().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setIva(na.getArticolo() != null ? na.getArticolo().getAliquotaIva().getValore().intValue() : null);

                notaAccreditoRigaDataSources.add(notaAccreditoRigaDataSource);
            });
        }
        return notaAccreditoRigaDataSources;
    }

    public List<NotaAccreditoTotaleDataSource> getNotaAccreditoTotaliDataSource(NotaAccredito notaAccredito){
        List<NotaAccreditoTotaleDataSource> notaAccreditoTotaleDataSources = new ArrayList<>();
        if(notaAccredito.getNotaAccreditoTotali() != null && !notaAccredito.getNotaAccreditoTotali().isEmpty()){
            notaAccredito.getNotaAccreditoTotali().stream().forEach(na -> {
                NotaAccreditoTotaleDataSource notaAccreditoTotaleDataSource = new NotaAccreditoTotaleDataSource();
                notaAccreditoTotaleDataSource.setAliquotaIva(na.getAliquotaIva().getValore().intValue());
                notaAccreditoTotaleDataSource.setTotaleImponibile(na.getTotaleImponibile() != null ? na.getTotaleImponibile().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));
                notaAccreditoTotaleDataSource.setTotaleIva(na.getTotaleIva() != null ? na.getTotaleIva().setScale(2, RoundingMode.CEILING) : new BigDecimal(0));

                notaAccreditoTotaleDataSources.add(notaAccreditoTotaleDataSource);
            });
        }
        return notaAccreditoTotaleDataSources.stream().sorted(Comparator.comparing(NotaAccreditoTotaleDataSource::getAliquotaIva))
                .collect(Collectors.toList());
    }

    public static HttpHeaders createHttpHeaders(String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+fileName);
        headers.add(HttpHeaders.CACHE_CONTROL, Constants.HTTP_HEADER_CACHE_CONTROL_VALUE);
        headers.add(HttpHeaders.PRAGMA, Constants.HTTP_HEADER_PRAGMA_VALUE);
        headers.add(HttpHeaders.EXPIRES, Constants.HTTP_HEADER_EXPIRES_VALUE);
        return headers;
    }

    public Map<String, Object> createParameters(){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", this.getClass().getResource("/reports/logo.png"));
        parameters.put("bollino", this.getClass().getResource("/reports/bollino.png"));
        return parameters;
    }

}
