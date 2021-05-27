package com.contafood.service;

import com.contafood.model.*;
import com.contafood.model.reports.*;
import com.contafood.model.views.VFattura;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.util.Constants;
import com.contafood.util.enumeration.Provincia;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;

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

    private final FatturaService fatturaService;

    private final FatturaAccompagnatoriaService fatturaAccompagnatoriaService;

    private final AliquotaIvaService aliquotaIvaService;

    private final NotaResoService notaResoService;

    private final RicevutaPrivatoService ricevutaPrivatoService;

    @Autowired
    public StampaService(final GiacenzaIngredienteService giacenzaIngredienteService, final DdtService ddtService, final PagamentoService pagamentoService,
                         final AutistaService autistaService,
                         final OrdineClienteService ordineClienteService,
                         final NotaAccreditoService notaAccreditoService,
                         final FatturaService fatturaService,
                         final FatturaAccompagnatoriaService fatturaAccompagnatoriaService,
                         final AliquotaIvaService aliquotaIvaService,
                         final NotaResoService notaResoService,
                         final RicevutaPrivatoService ricevutaPrivatoService){
        this.giacenzaIngredienteService = giacenzaIngredienteService;
        this.ddtService = ddtService;
        this.pagamentoService = pagamentoService;
        this.autistaService = autistaService;
        this.ordineClienteService = ordineClienteService;
        this.notaAccreditoService = notaAccreditoService;
        this.fatturaService = fatturaService;
        this.fatturaAccompagnatoriaService = fatturaAccompagnatoriaService;
        this.aliquotaIvaService = aliquotaIvaService;
        this.notaResoService = notaResoService;
        this.ricevutaPrivatoService = ricevutaPrivatoService;
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
        ddtDataSource.setCausale(ddt.getCausale() != null ? ddt.getCausale().getDescrizione() : "");
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
                ddtArticoloDataSource.setPrezzo(da.getPrezzo() != null ? da.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                ddtArticoloDataSource.setSconto(da.getSconto() != null ? da.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                ddtArticoloDataSource.setImponibile(da.getImponibile() != null ? da.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
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
                BigDecimal totaleAcconto = ddt.getTotaleAcconto() != null ? ddt.getTotaleAcconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                BigDecimal totale = ddt.getTotale() != null ? ddt.getTotale().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
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
        notaAccreditoDataSource.setCausale(notaAccredito.getCausale() != null ? notaAccredito.getCausale().getDescrizione() : "");
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
            notaAccredito.getNotaAccreditoRighe().stream().sorted(Comparator.comparing(NotaAccreditoRiga::getNumRiga, Comparator.nullsLast(Comparator.naturalOrder()))).forEach(na -> {
                NotaAccreditoRigaDataSource notaAccreditoRigaDataSource = new NotaAccreditoRigaDataSource();
                notaAccreditoRigaDataSource.setCodiceArticolo(na.getArticolo() != null ? na.getArticolo().getCodice() : "");
                notaAccreditoRigaDataSource.setDescrizioneArticolo(na.getDescrizione());
                notaAccreditoRigaDataSource.setLotto(na.getLotto());
                notaAccreditoRigaDataSource.setUdm(na.getArticolo() != null ? (na.getArticolo().getUnitaMisura() != null ? na.getArticolo().getUnitaMisura().getEtichetta() : "") : "");
                notaAccreditoRigaDataSource.setQuantita(na.getQuantita());
                notaAccreditoRigaDataSource.setPrezzo(na.getPrezzo() != null ? na.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setSconto(na.getSconto() != null ? na.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setImponibile(na.getImponibile() != null ? na.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaAccreditoRigaDataSource.setIva(na.getAliquotaIva() != null ? na.getAliquotaIva().getValore().intValue() : null);

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
                notaAccreditoTotaleDataSource.setTotaleImponibile(na.getTotaleImponibile() != null ? na.getTotaleImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaAccreditoTotaleDataSource.setTotaleIva(na.getTotaleIva() != null ? na.getTotaleIva().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));

                notaAccreditoTotaleDataSources.add(notaAccreditoTotaleDataSource);
            });
        }
        return notaAccreditoTotaleDataSources.stream().sorted(Comparator.comparing(NotaAccreditoTotaleDataSource::getAliquotaIva))
                .collect(Collectors.toList());
    }

    public List<NotaAccreditoDataSource> getNotaAccreditoDataSources(String ids){
        LOGGER.info("Retrieving the list of 'note accredito' with id in '{}' for creating pdf file", ids);

        List<NotaAccreditoDataSource> notaAccreditoDataSources = new ArrayList<>();

        List<NotaAccredito> noteAccredito = notaAccreditoService.getAll().stream()
                .filter(notaAccredito -> ids.contains(notaAccredito.getId().toString()))
                .sorted(Comparator.comparing(NotaAccredito::getProgressivo).reversed())
                .collect(Collectors.toList());

        if(noteAccredito != null && !noteAccredito.isEmpty()){
            noteAccredito.forEach(notaAccredito -> {
                NotaAccreditoDataSource notaAccreditoDataSource = new NotaAccreditoDataSource();
                notaAccreditoDataSource.setNumero(notaAccredito.getProgressivo().toString());
                notaAccreditoDataSource.setData(simpleDateFormat.format(notaAccredito.getData()));
                Cliente cliente = notaAccredito.getCliente();
                if(cliente != null){
                    if(!cliente.getDittaIndividuale()){
                        notaAccreditoDataSource.setClienteDescrizione(cliente.getRagioneSociale());
                    } else {
                        notaAccreditoDataSource.setClienteDescrizione(cliente.getNome()+" "+cliente.getCognome());
                    }
                }
                BigDecimal totaleAcconto = notaAccredito.getTotaleAcconto() != null ? notaAccredito.getTotaleAcconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                BigDecimal totale = notaAccredito.getTotale() != null ? notaAccredito.getTotale().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                notaAccreditoDataSource.setAcconto(totaleAcconto);
                notaAccreditoDataSource.setTotale(totale);
                notaAccreditoDataSource.setTotaleDaPagare(totale.subtract(totaleAcconto));

                notaAccreditoDataSources.add(notaAccreditoDataSource);
            });
        }

        LOGGER.info("Retrieved {} 'note accredito'", notaAccreditoDataSources.size());
        return notaAccreditoDataSources;
    }

    public List<FatturaDataSource> getFatturaDataSources(String ids){
        LOGGER.info("Retrieving the list of 'fatture' with id in '{}' for creating pdf file", ids);

        List<FatturaDataSource> fatturaDataSources = new ArrayList<>();

        List<VFattura> fatture = fatturaService.getAll().stream()
                .filter(fattura -> ids.contains(fattura.getId().toString()))
                .sorted(Comparator.comparing(VFattura::getProgressivo).reversed())
                .collect(Collectors.toList());

        if(fatture != null && !fatture.isEmpty()){
            fatture.forEach(fattura -> {
                FatturaDataSource fatturaDataSource = new FatturaDataSource();
                fatturaDataSource.setNumero(fattura.getProgressivo().toString());
                fatturaDataSource.setData(simpleDateFormat.format(fattura.getData()));
                Cliente cliente = fattura.getCliente();
                if(cliente != null){
                    if(!cliente.getDittaIndividuale()){
                        fatturaDataSource.setClienteDescrizione(cliente.getRagioneSociale());
                    } else {
                        fatturaDataSource.setClienteDescrizione(cliente.getNome()+" "+cliente.getCognome());
                    }
                }
                BigDecimal totaleAcconto = fattura.getTotaleAcconto() != null ? fattura.getTotaleAcconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                BigDecimal totale = fattura.getTotale() != null ? fattura.getTotale().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                fatturaDataSource.setAcconto(totaleAcconto);
                fatturaDataSource.setTotale(totale);
                fatturaDataSource.setTotaleDaPagare(totale.subtract(totaleAcconto));

                fatturaDataSources.add(fatturaDataSource);
            });
        }

        LOGGER.info("Retrieved {} 'fatture'", fatturaDataSources.size());
        return fatturaDataSources;
    }

    public Fattura getFattura(Long idFattura){
        LOGGER.info("Retrieving 'fattura' with id '{}' for creating pdf file", idFattura);
        Fattura fattura = fatturaService.getOne(idFattura);
        LOGGER.info("Retrieved 'fattura' with id '{}'", idFattura);
        return fattura;
    }

    public FatturaAccompagnatoria getFatturaAccompagnatoria(Long idFatturaAccompagnatoria){
        LOGGER.info("Retrieving 'fattura accompagnatoria' with id '{}' for creating pdf file", idFatturaAccompagnatoria);
        FatturaAccompagnatoria fatturaAccompagnatoria = fatturaAccompagnatoriaService.getOne(idFatturaAccompagnatoria);
        LOGGER.info("Retrieved 'fattura accompagnatoria' with id '{}'", idFatturaAccompagnatoria);
        return fatturaAccompagnatoria;
    }

    public FatturaAccompagnatoriaDataSource getFatturaAccompagnatoriaDataSource(FatturaAccompagnatoria fatturaAccompagnatoria){
        Cliente cliente = fatturaAccompagnatoria.getCliente();
        TipoPagamento tipoPagamento = cliente.getTipoPagamento();
        Agente agente = cliente.getAgente();

        FatturaAccompagnatoriaDataSource fatturaAccompagnatoriaDataSource = new FatturaAccompagnatoriaDataSource();
        fatturaAccompagnatoriaDataSource.setNumero(fatturaAccompagnatoria.getProgressivo()+"/"+fatturaAccompagnatoria.getAnno());
        fatturaAccompagnatoriaDataSource.setData(simpleDateFormat.format(fatturaAccompagnatoria.getData()));
        fatturaAccompagnatoriaDataSource.setClientePartitaIva("");
        fatturaAccompagnatoriaDataSource.setClienteCodiceFiscale("");
        fatturaAccompagnatoriaDataSource.setCausale(fatturaAccompagnatoria.getCausale() != null ? fatturaAccompagnatoria.getCausale().getDescrizione() : "");
        fatturaAccompagnatoriaDataSource.setPagamento("");
        fatturaAccompagnatoriaDataSource.setAgente("");
        if(cliente != null){
            if(!StringUtils.isEmpty(cliente.getPartitaIva())){
                fatturaAccompagnatoriaDataSource.setClientePartitaIva(cliente.getPartitaIva());
            }
            if(!StringUtils.isEmpty(cliente.getCodiceFiscale())){
                fatturaAccompagnatoriaDataSource.setClienteCodiceFiscale(cliente.getCodiceFiscale());
            }
        }
        if(tipoPagamento != null){
            if(!StringUtils.isEmpty(tipoPagamento.getDescrizione())){
                fatturaAccompagnatoriaDataSource.setPagamento(tipoPagamento.getDescrizione());
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
            fatturaAccompagnatoriaDataSource.setAgente(sb.toString());
        }
        return fatturaAccompagnatoriaDataSource;
    }

    public List<FatturaAccompagnatoriaRigaDataSource> getFatturaAccompagnatoriaRigheDataSource(FatturaAccompagnatoria fatturaAccompagnatoria){
        List<FatturaAccompagnatoriaRigaDataSource> fatturaAccompagnatoriaRigaDataSources = new ArrayList<>();
        if(fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli() != null && !fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli().isEmpty()){
            fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli().stream().forEach(faa -> {
                Articolo articolo = faa.getArticolo();
                FatturaAccompagnatoriaRigaDataSource fatturaAccompagnatoriaRigaDataSource = new FatturaAccompagnatoriaRigaDataSource();
                fatturaAccompagnatoriaRigaDataSource.setCodiceArticolo(articolo != null ? articolo.getCodice() : "");
                fatturaAccompagnatoriaRigaDataSource.setDescrizioneArticolo(articolo != null ? articolo.getDescrizione() : "");
                fatturaAccompagnatoriaRigaDataSource.setLotto(faa.getLotto());
                fatturaAccompagnatoriaRigaDataSource.setUdm(articolo != null ? (articolo.getUnitaMisura() != null ? articolo.getUnitaMisura().getEtichetta() : "") : "");
                fatturaAccompagnatoriaRigaDataSource.setQuantita(faa.getQuantita());
                fatturaAccompagnatoriaRigaDataSource.setPrezzo(faa.getPrezzo() != null ? faa.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                fatturaAccompagnatoriaRigaDataSource.setSconto(faa.getSconto() != null ? faa.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                fatturaAccompagnatoriaRigaDataSource.setImponibile(faa.getImponibile() != null ? faa.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                fatturaAccompagnatoriaRigaDataSource.setIva(articolo != null ? (articolo.getAliquotaIva() != null ? articolo.getAliquotaIva().getValore().intValue() : null) : null);

                fatturaAccompagnatoriaRigaDataSources.add(fatturaAccompagnatoriaRigaDataSource);
            });
        }
        return fatturaAccompagnatoriaRigaDataSources;
    }

    public List<FatturaAccompagnatoriaTotaleDataSource> getFatturaAccompagnatoriaTotaliDataSource(FatturaAccompagnatoria fatturaAccompagnatoria){
        List<FatturaAccompagnatoriaTotaleDataSource> fatturaAccompagnatoriaTotaleDataSources = new ArrayList<>();
        if(fatturaAccompagnatoria.getFatturaAccompagnatoriaTotali() != null && !fatturaAccompagnatoria.getFatturaAccompagnatoriaTotali() .isEmpty()){
            fatturaAccompagnatoria.getFatturaAccompagnatoriaTotali() .stream().forEach(fat -> {
                FatturaAccompagnatoriaTotaleDataSource fatturaAccompagnatoriaTotaleDataSource = new FatturaAccompagnatoriaTotaleDataSource();
                fatturaAccompagnatoriaTotaleDataSource.setAliquotaIva(fat.getAliquotaIva().getValore().intValue());
                fatturaAccompagnatoriaTotaleDataSource.setTotaleImponibile(fat.getTotaleImponibile() != null ? fat.getTotaleImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                fatturaAccompagnatoriaTotaleDataSource.setTotaleIva(fat.getTotaleIva() != null ? fat.getTotaleIva().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));

                fatturaAccompagnatoriaTotaleDataSources.add(fatturaAccompagnatoriaTotaleDataSource);
            });
        }
        return fatturaAccompagnatoriaTotaleDataSources.stream().sorted(Comparator.comparing(FatturaAccompagnatoriaTotaleDataSource::getAliquotaIva))
                .collect(Collectors.toList());
    }

    public FatturaDataSource getFatturaDataSource(Fattura fattura){
        Cliente cliente = fattura.getCliente();
        TipoPagamento tipoPagamento = cliente.getTipoPagamento();
        Agente agente = cliente.getAgente();

        FatturaDataSource fatturaDataSource = new FatturaDataSource();
        fatturaDataSource.setNumero(fattura.getProgressivo()+"/"+fattura.getAnno());
        fatturaDataSource.setData(simpleDateFormat.format(fattura.getData()));
        fatturaDataSource.setClientePartitaIva("");
        fatturaDataSource.setClienteCodiceFiscale("");
        fatturaDataSource.setCausale(fattura.getCausale() != null ? fattura.getCausale().getDescrizione() : "");
        fatturaDataSource.setPagamento("");
        fatturaDataSource.setAgente("");
        if(cliente != null){
            if(!StringUtils.isEmpty(cliente.getPartitaIva())){
                fatturaDataSource.setClientePartitaIva(cliente.getPartitaIva());
            }
            if(!StringUtils.isEmpty(cliente.getCodiceFiscale())){
                fatturaDataSource.setClienteCodiceFiscale(cliente.getCodiceFiscale());
            }
        }
        if(tipoPagamento != null){
            if(!StringUtils.isEmpty(tipoPagamento.getDescrizione())){
                fatturaDataSource.setPagamento(tipoPagamento.getDescrizione());
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
            fatturaDataSource.setAgente(sb.toString());
        }
        return fatturaDataSource;
    }

    public List<FatturaRigaDataSource> getFatturaRigheDataSource(Fattura fattura){
        Integer numeroRiga = 0;

        List<FatturaRigaDataSource> fatturaRigaDataSources = new ArrayList<>();
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        if(fatturaDdts != null && !fatturaDdts.isEmpty()){
            for(FatturaDdt fatturaDdt : fatturaDdts){
               Ddt ddt = fatturaDdt.getDdt();
               if(ddt != null){
                    String descrizione = "Riferimento ns. DDT n. "+ddt.getProgressivo()+"/"+ddt.getAnnoContabile()+" del "+simpleDateFormat.format(ddt.getData());
                    FatturaRigaDataSource fatturaRigaDdtDataSource = new FatturaRigaDataSource();
                    fatturaRigaDdtDataSource.setNumeroRiga(numeroRiga);
                    fatturaRigaDdtDataSource.setDescrizioneArticolo(descrizione);
                    fatturaRigaDdtDataSource.setCodiceArticolo("");
                    fatturaRigaDdtDataSource.setLotto("");
                    fatturaRigaDataSources.add(fatturaRigaDdtDataSource);

                    numeroRiga += 1;

                    if(ddt.getDdtArticoli() != null && !ddt.getDdtArticoli().isEmpty()){
                        for(DdtArticolo da : ddt.getDdtArticoli()){
                            Articolo articolo = da.getArticolo();

                            FatturaRigaDataSource fatturaRigaDataSource = new FatturaRigaDataSource();
                            fatturaRigaDataSource.setNumeroRiga(numeroRiga);
                            fatturaRigaDataSource.setCodiceArticolo(articolo != null ? articolo.getCodice() : "");
                            fatturaRigaDataSource.setDescrizioneArticolo(articolo != null ? articolo.getDescrizione() : "");
                            fatturaRigaDataSource.setLotto(da.getLotto());
                            fatturaRigaDataSource.setUdm(articolo != null ? (articolo.getUnitaMisura() != null ? articolo.getUnitaMisura().getEtichetta() : "") : "");
                            fatturaRigaDataSource.setQuantita(da.getQuantita());
                            fatturaRigaDataSource.setPrezzo(da.getPrezzo() != null ? da.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                            fatturaRigaDataSource.setSconto(da.getSconto() != null ? da.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                            fatturaRigaDataSource.setImponibile(da.getImponibile() != null ? da.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                            fatturaRigaDataSource.setIva(articolo != null ? (articolo.getAliquotaIva() != null ? articolo.getAliquotaIva().getValore().intValue() : null) : null);

                            fatturaRigaDataSources.add(fatturaRigaDataSource);

                            numeroRiga += 1;
                        }
                    }
               }
            }
        }

        return fatturaRigaDataSources.stream().sorted(Comparator.comparing(FatturaRigaDataSource::getNumeroRiga))
                .collect(Collectors.toList());
    }

    public List<FatturaTotaleDataSource> getFatturaTotaliDataSource(Fattura fattura){
        List<FatturaTotaleDataSource> fatturaTotaleDataSources = new ArrayList<>();

        // get all AliquotaIva
        Map<AliquotaIva, Set<DdtArticolo>> ivaDdtArticoliMap = new HashMap<>();
        Set<AliquotaIva> aliquoteIva = aliquotaIvaService.getAll();
        for(AliquotaIva aliquotaIva : aliquoteIva){
            ivaDdtArticoliMap.putIfAbsent(aliquotaIva, new HashSet<>());
        }

        // create a map with the list of DdtArticoli grouped by AliquotaIva
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        if(fatturaDdts != null && !fatturaDdts.isEmpty()){
            for(FatturaDdt fatturaDdt : fatturaDdts){
                Ddt ddt = fatturaDdt.getDdt();
                if(ddt != null){
                    if(ddt.getDdtArticoli() != null && !ddt.getDdtArticoli().isEmpty()){
                        for(DdtArticolo ddtArticolo : ddt.getDdtArticoli()){
                            AliquotaIva aliquotaIva = ddtArticolo.getArticolo().getAliquotaIva();
                            Set<DdtArticolo> ddtArticoli = ivaDdtArticoliMap.get(aliquotaIva);
                            ddtArticoli.add(ddtArticolo);
                            ivaDdtArticoliMap.put(aliquotaIva, ddtArticoli);
                        }
                    }
                }
            }
        }

        for (Map.Entry<AliquotaIva, Set<DdtArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleImponibile = BigDecimal.ZERO;
            BigDecimal totaleIva = BigDecimal.ZERO;

            for(DdtArticolo ddtArticolo : entry.getValue()){
                BigDecimal imponibile = ddtArticolo.getImponibile() != null ? ddtArticolo.getImponibile() : BigDecimal.ZERO;
                BigDecimal ddtArticoloIva = ddtArticolo.getImponibile() != null ? (imponibile.multiply(iva.divide(new BigDecimal(100)))) : BigDecimal.ZERO;
                totaleImponibile = totaleImponibile.add(imponibile);
                totaleIva = totaleIva.add(ddtArticoloIva);
            }

            FatturaTotaleDataSource fatturaTotaleDataSource = new FatturaTotaleDataSource();
            fatturaTotaleDataSource.setAliquotaIva(iva.intValue());
            fatturaTotaleDataSource.setTotaleImponibile(totaleImponibile.setScale(2, RoundingMode.HALF_DOWN));
            fatturaTotaleDataSource.setTotaleIva(totaleIva.setScale(2, RoundingMode.HALF_DOWN));

            fatturaTotaleDataSources.add(fatturaTotaleDataSource);
        }

        return fatturaTotaleDataSources.stream().sorted(Comparator.comparing(FatturaTotaleDataSource::getAliquotaIva))
                .collect(Collectors.toList());
    }

    public NotaReso getNotaReso(Long idNotaReso){
        LOGGER.info("Retrieving 'nota reso' with id '{}' for creating pdf file", idNotaReso);
        NotaReso notaReso = notaResoService.getOne(idNotaReso);
        LOGGER.info("Retrieved 'nota reso' with id '{}'", idNotaReso);
        return notaReso;
    }

    public NotaResoDataSource getNotaResoDataSource(NotaReso notaReso){
        Fornitore fornitore = notaReso.getFornitore();
        String tipoPagamento = fornitore.getPagamento();

        NotaResoDataSource notaResoDataSource = new NotaResoDataSource();
        notaResoDataSource.setNumero(notaReso.getProgressivo()+"/"+notaReso.getAnno());
        notaResoDataSource.setData(simpleDateFormat.format(notaReso.getData()));
        notaResoDataSource.setFornitorePartitaIva("");
        notaResoDataSource.setFornitoreCodiceFiscale("");
        notaResoDataSource.setCausale(notaReso.getCausale() != null ? notaReso.getCausale().getDescrizione() : "");
        notaResoDataSource.setPagamento(tipoPagamento);
        notaResoDataSource.setAgente("");
        if(fornitore != null){
            if(!StringUtils.isEmpty(fornitore.getPartitaIva())){
                notaResoDataSource.setFornitorePartitaIva(fornitore.getPartitaIva());
            }
            if(!StringUtils.isEmpty(fornitore.getCodiceFiscale())){
                notaResoDataSource.setFornitoreCodiceFiscale(fornitore.getCodiceFiscale());
            }
        }

        return notaResoDataSource;
    }

    public List<NotaResoRigaDataSource> getNotaResoRigheDataSource(NotaReso notaReso){
        List<NotaResoRigaDataSource> notaResoRigaDataSources = new ArrayList<>();
        if(notaReso.getNotaResoRighe() != null && !notaReso.getNotaResoRighe().isEmpty()){
            notaReso.getNotaResoRighe().stream().forEach(nr -> {
                NotaResoRigaDataSource notaResoRigaDataSource = new NotaResoRigaDataSource();
                notaResoRigaDataSource.setCodiceArticolo(nr.getArticolo() != null ? nr.getArticolo().getCodice() : "");
                notaResoRigaDataSource.setDescrizioneArticolo(nr.getDescrizione());
                notaResoRigaDataSource.setLotto(nr.getLotto());
                notaResoRigaDataSource.setUdm(nr.getArticolo() != null ? (nr.getArticolo().getUnitaMisura() != null ? nr.getArticolo().getUnitaMisura().getEtichetta() : "") : "");
                notaResoRigaDataSource.setQuantita(nr.getQuantita());
                notaResoRigaDataSource.setPrezzo(nr.getPrezzo() != null ? nr.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaResoRigaDataSource.setSconto(nr.getSconto() != null ? nr.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaResoRigaDataSource.setImponibile(nr.getImponibile() != null ? nr.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaResoRigaDataSource.setIva(nr.getAliquotaIva() != null ? nr.getAliquotaIva().getValore().intValue() : null);

                notaResoRigaDataSources.add(notaResoRigaDataSource);
            });
        }
        return notaResoRigaDataSources;
    }

    public List<NotaResoTotaleDataSource> getNotaResoTotaliDataSource(NotaReso notaReso){
        List<NotaResoTotaleDataSource> notaResoTotaleDataSources = new ArrayList<>();
        if(notaReso.getNotaResoTotali() != null && !notaReso.getNotaResoTotali().isEmpty()){
            notaReso.getNotaResoTotali().stream().forEach(nr -> {
                NotaResoTotaleDataSource notaResoTotaleDataSource = new NotaResoTotaleDataSource();
                notaResoTotaleDataSource.setAliquotaIva(nr.getAliquotaIva().getValore().intValue());
                notaResoTotaleDataSource.setTotaleImponibile(nr.getTotaleImponibile() != null ? nr.getTotaleImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                notaResoTotaleDataSource.setTotaleIva(nr.getTotaleIva() != null ? nr.getTotaleIva().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));

                notaResoTotaleDataSources.add(notaResoTotaleDataSource);
            });
        }
        return notaResoTotaleDataSources.stream().sorted(Comparator.comparing(NotaResoTotaleDataSource::getAliquotaIva))
                .collect(Collectors.toList());
    }

    public RicevutaPrivato getRicevutaPrivato(Long idRicevutaPrivato){
        LOGGER.info("Retrieving 'ricevuta privato' with id '{}' for creating pdf file", idRicevutaPrivato);
        RicevutaPrivato ricevutaPrivato = ricevutaPrivatoService.getOne(idRicevutaPrivato);
        LOGGER.info("Retrieved 'ricevuta privato' with id '{}'", idRicevutaPrivato);
        return ricevutaPrivato;
    }

    public RicevutaPrivatoDataSource getRicevutaPrivatoDataSource(RicevutaPrivato ricevutaPrivato){
        Cliente cliente = ricevutaPrivato.getCliente();
        TipoPagamento tipoPagamento = cliente.getTipoPagamento();
        Agente agente = cliente.getAgente();

        RicevutaPrivatoDataSource ricevutaPrivatoDataSource = new RicevutaPrivatoDataSource();
        ricevutaPrivatoDataSource.setNumero(ricevutaPrivato.getProgressivo()+"/"+ricevutaPrivato.getAnno());
        ricevutaPrivatoDataSource.setData(simpleDateFormat.format(ricevutaPrivato.getData()));
        ricevutaPrivatoDataSource.setClientePartitaIva("");
        ricevutaPrivatoDataSource.setClienteCodiceFiscale("");
        ricevutaPrivatoDataSource.setCausale(ricevutaPrivato.getCausale() != null ? ricevutaPrivato.getCausale().getDescrizione() : "");
        ricevutaPrivatoDataSource.setPagamento("");
        ricevutaPrivatoDataSource.setAgente("");
        if(cliente != null){
            if(!StringUtils.isEmpty(cliente.getCodiceFiscale())){
                ricevutaPrivatoDataSource.setClienteCodiceFiscale(cliente.getCodiceFiscale());
            }
        }
        if(tipoPagamento != null){
            if(!StringUtils.isEmpty(tipoPagamento.getDescrizione())){
                ricevutaPrivatoDataSource.setPagamento(tipoPagamento.getDescrizione());
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
            ricevutaPrivatoDataSource.setAgente(sb.toString());
        }
        return ricevutaPrivatoDataSource;
    }

    public List<RicevutaPrivatoDataSource> getRicevutaPrivatoDataSources(String ids){
        LOGGER.info("Retrieving the list of 'ricevute privati' with id in '{}' for creating pdf file", ids);

        List<RicevutaPrivatoDataSource> ricevutaPrivatoDataSources = new ArrayList<>();

        List<RicevutaPrivato> ricevutePrivato = ricevutaPrivatoService.getAll().stream()
                .filter(ricevutaPrivato -> ids.contains(ricevutaPrivato.getId().toString()))
                .sorted(Comparator.comparing(RicevutaPrivato::getProgressivo).reversed())
                .collect(Collectors.toList());

        if(ricevutePrivato != null && !ricevutePrivato.isEmpty()){
            ricevutePrivato.forEach(ricevutaPrivato -> {
                RicevutaPrivatoDataSource ricevutaPrivatoDataSource = new RicevutaPrivatoDataSource();
                ricevutaPrivatoDataSource.setNumero(ricevutaPrivato.getProgressivo().toString());
                ricevutaPrivatoDataSource.setData(simpleDateFormat.format(ricevutaPrivato.getData()));
                Cliente cliente = ricevutaPrivato.getCliente();
                if(cliente != null){
                    ricevutaPrivatoDataSource.setClienteDescrizione(cliente.getNome()+" "+cliente.getCognome());
                }
                BigDecimal totaleAcconto = ricevutaPrivato.getTotaleAcconto() != null ? ricevutaPrivato.getTotaleAcconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                BigDecimal totale = ricevutaPrivato.getTotale() != null ? ricevutaPrivato.getTotale().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                ricevutaPrivatoDataSource.setAcconto(totaleAcconto);
                ricevutaPrivatoDataSource.setTotale(totale);
                ricevutaPrivatoDataSource.setTotaleDaPagare(totale.subtract(totaleAcconto));

                ricevutaPrivatoDataSources.add(ricevutaPrivatoDataSource);
            });
        }

        LOGGER.info("Retrieved {} 'ricevute privati'", ricevutaPrivatoDataSources.size());
        return ricevutaPrivatoDataSources;
    }

    public List<RicevutaPrivatoArticoloDataSource> getRicevutaPrivatoArticoliDataSource(RicevutaPrivato ricevutaPrivato){
        List<RicevutaPrivatoArticoloDataSource> ricevutaPrivatoArticoloDataSources = new ArrayList<>();
        if(ricevutaPrivato.getRicevutaPrivatoArticoli() != null && !ricevutaPrivato.getRicevutaPrivatoArticoli().isEmpty()){
            ricevutaPrivato.getRicevutaPrivatoArticoli().stream().forEach(rpa -> {
                RicevutaPrivatoArticoloDataSource ricevutaPrivatoArticoloDataSource = new RicevutaPrivatoArticoloDataSource();

                Float quantita = rpa.getQuantita();
                BigDecimal imponibile = rpa.getImponibile() != null ? rpa.getImponibile().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0);
                BigDecimal iva = rpa.getArticolo().getAliquotaIva().getValore();

                BigDecimal totale = imponibile.add(imponibile.multiply(iva.divide(new BigDecimal(100))));
                BigDecimal prezzoConIva = totale.divide(BigDecimal.valueOf(quantita), 2, RoundingMode.HALF_DOWN);

                ricevutaPrivatoArticoloDataSource.setCodiceArticolo(rpa.getArticolo().getCodice());
                ricevutaPrivatoArticoloDataSource.setDescrizioneArticolo(rpa.getArticolo().getDescrizione());
                ricevutaPrivatoArticoloDataSource.setLotto(rpa.getLotto());
                ricevutaPrivatoArticoloDataSource.setUdm(rpa.getArticolo().getUnitaMisura().getEtichetta());
                ricevutaPrivatoArticoloDataSource.setQuantita(quantita);
                ricevutaPrivatoArticoloDataSource.setPrezzo(rpa.getPrezzo() != null ? rpa.getPrezzo().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                ricevutaPrivatoArticoloDataSource.setSconto(rpa.getSconto() != null ? rpa.getSconto().setScale(2, RoundingMode.HALF_DOWN) : new BigDecimal(0));
                ricevutaPrivatoArticoloDataSource.setImponibile(imponibile);
                ricevutaPrivatoArticoloDataSource.setIva(iva.intValue());
                ricevutaPrivatoArticoloDataSource.setPrezzoConIva(prezzoConIva.setScale(2, RoundingMode.HALF_DOWN));
                ricevutaPrivatoArticoloDataSource.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));

                ricevutaPrivatoArticoloDataSources.add(ricevutaPrivatoArticoloDataSource);
            });
        }
        return ricevutaPrivatoArticoloDataSources;
    }

    public List<FatturaCommercianteDataSource> getFattureCommercianti(Set<Fattura> inputFatture){
        LOGGER.info("Retrieving the list of 'fatture commercianti' for creating pdf file");

        List<Fattura> fatture = inputFatture.stream()
                .sorted(Comparator.comparing(Fattura::getProgressivo))
                .collect(Collectors.toList());

        List<FatturaCommercianteDataSource> fatturaCommercianteDataSources = new ArrayList<>();
        if(fatture != null && !fatture.isEmpty()){
            fatture.forEach(fattura -> {
                String date = simpleDateFormat.format(fattura.getData());

                String ragioneSociale = "";
                String indirizzo = "";
                String citta = "";
                String partitaIva = "";
                String codiceFiscale = "";

                Cliente cliente = fattura.getCliente();
                if(cliente != null){
                    if(cliente.getDittaIndividuale()){
                        ragioneSociale = cliente.getNome() + " " + cliente.getCognome();
                    } else {
                        ragioneSociale = cliente.getRagioneSociale();
                    }
                    indirizzo = cliente.getIndirizzo();
                    citta = cliente.getCitta();
                    if(!StringUtils.isEmpty(cliente.getProvincia())){
                        Provincia provincia = Provincia.getByLabel(cliente.getProvincia());
                        if(provincia != null){
                            citta += " ("+provincia.getSigla()+")";
                        }
                    }
                    partitaIva = "P.I. " + cliente.getPartitaIva();
                    codiceFiscale = "C.F. " + cliente.getCodiceFiscale();
                }

                FatturaCommercianteDataSource fatturaCommercianteDataSource = new FatturaCommercianteDataSource();
                fatturaCommercianteDataSource.setNumero(fattura.getProgressivo().toString());
                fatturaCommercianteDataSource.setData(date);
                fatturaCommercianteDataSource.setRagioneSociale(ragioneSociale);
                fatturaCommercianteDataSource.setIndirizzo(indirizzo);
                fatturaCommercianteDataSource.setCitta(citta);
                fatturaCommercianteDataSource.setPartitaIva(partitaIva);
                fatturaCommercianteDataSource.setCodiceFiscale(codiceFiscale);
                fatturaCommercianteDataSource.setTotale(fattura.getTotale());

                List<FatturaCommercianteTotaleDataSource> fatturaCommercianteTotaleDataSources = new ArrayList<>();
                Map<AliquotaIva, Set<DdtArticolo>> ivaDdtArticoliMap = new HashMap<>();

                // create a map with the list of DdtArticoli grouped by AliquotaIva
                Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
                if(fatturaDdts != null && !fatturaDdts.isEmpty()){
                    for(FatturaDdt fatturaDdt : fatturaDdts){
                        Ddt ddt = fatturaDdt.getDdt();
                        if(ddt != null){
                            if(ddt.getDdtArticoli() != null && !ddt.getDdtArticoli().isEmpty()){
                                for(DdtArticolo ddtArticolo : ddt.getDdtArticoli()){
                                    AliquotaIva aliquotaIva = ddtArticolo.getArticolo().getAliquotaIva();
                                    Set<DdtArticolo> ddtArticoli = ivaDdtArticoliMap.getOrDefault(aliquotaIva, new HashSet<>());
                                    ddtArticoli.add(ddtArticolo);
                                    ivaDdtArticoliMap.put(aliquotaIva, ddtArticoli);
                                }
                            }
                        }
                    }
                }

                for (Map.Entry<AliquotaIva, Set<DdtArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
                    BigDecimal iva = entry.getKey().getValore();
                    BigDecimal totaleImponibile = BigDecimal.ZERO;
                    BigDecimal totaleIva = BigDecimal.ZERO;

                    for(DdtArticolo ddtArticolo : entry.getValue()){
                        BigDecimal imponibile = ddtArticolo.getImponibile() != null ? ddtArticolo.getImponibile() : BigDecimal.ZERO;
                        BigDecimal ddtArticoloIva = ddtArticolo.getImponibile() != null ? (imponibile.multiply(iva.divide(new BigDecimal(100)))) : BigDecimal.ZERO;
                        totaleImponibile = totaleImponibile.add(imponibile);
                        totaleIva = totaleIva.add(ddtArticoloIva);
                    }

                    FatturaCommercianteTotaleDataSource fatturaCommercianteTotaleDataSource = new FatturaCommercianteTotaleDataSource();
                    fatturaCommercianteTotaleDataSource.setImponibile(totaleImponibile.setScale(2, RoundingMode.HALF_DOWN));
                    fatturaCommercianteTotaleDataSource.setIva(iva.intValue());
                    fatturaCommercianteTotaleDataSource.setImposta(totaleIva.setScale(2, RoundingMode.HALF_DOWN));

                    fatturaCommercianteTotaleDataSources.add(fatturaCommercianteTotaleDataSource);
                }

                fatturaCommercianteDataSource.setFatturaCommercianteTotaleDataSources(fatturaCommercianteTotaleDataSources);

                fatturaCommercianteDataSources.add(fatturaCommercianteDataSource);

            });
        }

        LOGGER.info("Retrieved {} 'fatture commercianti'", fatturaCommercianteDataSources.size());
        return fatturaCommercianteDataSources;
    }

    public byte[] generateDdt(Long idDdt) throws Exception{

        // retrieve the Ddt
        Ddt ddt = getDdt(idDdt);
        PuntoConsegna puntoConsegna = ddt.getPuntoConsegna();
        Cliente cliente = ddt.getCliente();

        // create DdtDataSource
        List<DdtDataSource> ddtDataSources = new ArrayList<>();
        ddtDataSources.add(getDdtDataSource(ddt));

        // create data parameters
        String ddtTitleParam = ddt.getProgressivo()+"/"+ddt.getAnnoContabile()+" del "+simpleDateFormat.format(ddt.getData());
        String puntoConsegnaParam = "";
        String destinatarioParam = "";

        // create data parameters for PuntoConsegna
        if(puntoConsegna != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(puntoConsegna.getNome())){
                sb.append(puntoConsegna.getNome()+"\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getIndirizzo())){
                sb.append(puntoConsegna.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getCap())){
                sb.append(puntoConsegna.getCap()+" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getLocalita())){
                sb.append(puntoConsegna.getLocalita()+" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getProvincia())){
                sb.append("("+Provincia.getByLabel(puntoConsegna.getProvincia()).getSigla()+")");
            }

            puntoConsegnaParam = sb.toString();
        }

        // create data parameters for Cliente
        if(cliente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(cliente.getRagioneSociale())){
                sb.append(cliente.getRagioneSociale()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getIndirizzo())){
                sb.append(cliente.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getCap())){
                sb.append(cliente.getCap()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getCitta())){
                sb.append(cliente.getCitta()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getProvincia())){
                sb.append("("+Provincia.getByLabel(cliente.getProvincia()).getSigla()+")");
            }

            destinatarioParam = sb.toString();
        }

        // create 'ddtTrasportoDataOra' param
        String ddtTrasportoDataOraParam = simpleDateFormat.format(ddt.getDataTrasporto())+" "+ddt.getOraTrasporto();

        // create list of DdtArticoloDataSource from DdtArticolo
        List<DdtArticoloDataSource> ddtArticoloDataSources = getDdtArticoliDataSource(ddt);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_DDT);

        // create report datasource for Ddt
        JRBeanCollectionDataSource ddtCollectionDataSource = new JRBeanCollectionDataSource(ddtDataSources);

        // create report datasource for DdtArticoli
        JRBeanCollectionDataSource ddtArticoliCollectionDataSource = new JRBeanCollectionDataSource(ddtArticoloDataSources);

        // create report parameters
        Map<String, Object> parameters = createParameters();

        // add data to parameters
        parameters.put("ddtTitle", ddtTitleParam);
        parameters.put("puntoConsegna", puntoConsegnaParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("note", ddt.getNote());
        parameters.put("trasportatore", ddt.getTrasportatore());
        parameters.put("nota", Constants.JASPER_PARAMETER_DDT_NOTA);
        parameters.put("ddtTrasportoTipo", ddt.getTipoTrasporto());
        parameters.put("ddtTrasportoDataOra", ddtTrasportoDataOraParam);
        parameters.put("ddtNumeroColli", ddt.getNumeroColli());
        parameters.put("ddtTotImponibile", ddt.getTotaleImponibile().setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("ddtTotIva", ddt.getTotaleIva().setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("ddtTotDocumento", ddt.getTotale().setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("ddtArticoliCollection", ddtArticoliCollectionDataSource);
        parameters.put("ddtCollection", ddtCollectionDataSource);

        // create report
        return JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

    }

    @Transactional
    public byte[] generateFattura(Long idFattura) throws Exception{

        // retrieve the Fattura
        Fattura fattura = getFattura(idFattura);
        Cliente cliente = fattura.getCliente();

        // create data parameters
        String fatturaTitleParam = fattura.getProgressivo()+"/"+fattura.getAnno()+" del "+simpleDateFormat.format(fattura.getData());
        String destinatarioParam = "";

        // create data parameters for Cliente
        if(cliente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(cliente.getRagioneSociale())){
                sb.append(cliente.getRagioneSociale()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getIndirizzo())){
                sb.append(cliente.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getCap())){
                sb.append(cliente.getCap()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getCitta())){
                sb.append(cliente.getCitta()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getProvincia())){
                sb.append("("+ Provincia.getByLabel(cliente.getProvincia()).getSigla()+")");
            }

            destinatarioParam = sb.toString();
        }

        // create FatturaDataSource
        List<FatturaDataSource> fatturaDataSources = new ArrayList<>();
        fatturaDataSources.add(getFatturaDataSource(fattura));

        // create list of FatturaRigheDataSource from FatturaDdts
        List<FatturaRigaDataSource> fatturaRigaDataSources = getFatturaRigheDataSource(fattura);

        // create list of FatturaAccompagnatoriaTotaliDataSource from FatturaAccompagnatoriaTotale
        List<FatturaTotaleDataSource> fatturaTotaleDataSources = getFatturaTotaliDataSource(fattura);

        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);

        for(FatturaTotaleDataSource fatturaTotale: fatturaTotaleDataSources){
            totaleImponibile = totaleImponibile.add(fatturaTotale.getTotaleImponibile());
            totaleIva = totaleIva.add(fatturaTotale.getTotaleIva());
        }


        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_FATTURA);

        // create report datasource for Fattura
        JRBeanCollectionDataSource fatturaCollectionDataSource = new JRBeanCollectionDataSource(fatturaDataSources);

        // create report datasource for FatturaRighe
        JRBeanCollectionDataSource fatturaRigheCollectionDataSource = new JRBeanCollectionDataSource(fatturaRigaDataSources);

        // create report datasource for FatturaTotali
        JRBeanCollectionDataSource fatturaTotaliCollectionDataSource = new JRBeanCollectionDataSource(fatturaTotaleDataSources);

        // create report parameters
        Map<String, Object> parameters = createParameters();

        // add data to parameters
        parameters.put("fatturaTitle", fatturaTitleParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("note", fattura.getNote());
        parameters.put("nota", Constants.JASPER_PARAMETER_FATTURA_NOTA);
        parameters.put("totaleImponibile", totaleImponibile);
        parameters.put("totaleIva", totaleIva);
        parameters.put("fatturaTotDocumento", totaleImponibile.add(totaleIva).setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("fatturaCollection", fatturaCollectionDataSource);
        parameters.put("fatturaRigheCollection", fatturaRigheCollectionDataSource);
        parameters.put("fatturaTotaliCollection", fatturaTotaliCollectionDataSource);

        // create report
        return JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());
    }

    public byte[] generateFatturaAccompagnatoria(Long idFatturaAccompagnatoria) throws Exception{

        // retrieve the FatturaAccompagnatoria
        FatturaAccompagnatoria fatturaAccompagnatoria = getFatturaAccompagnatoria(idFatturaAccompagnatoria);
        PuntoConsegna puntoConsegna = fatturaAccompagnatoria.getPuntoConsegna();
        Cliente cliente = fatturaAccompagnatoria.getCliente();

        // create data parameters
        String fatturaAccompagnatoriaTitleParam = fatturaAccompagnatoria.getProgressivo()+"/"+fatturaAccompagnatoria.getAnno()+" del "+simpleDateFormat.format(fatturaAccompagnatoria.getData());
        String puntoConsegnaParam = "";
        String destinatarioParam = "";

        // create data parameters for PuntoConsegna
        if(puntoConsegna != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(puntoConsegna.getNome())){
                sb.append(puntoConsegna.getNome()+"\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getIndirizzo())){
                sb.append(puntoConsegna.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getCap())){
                sb.append(puntoConsegna.getCap()+" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getLocalita())){
                sb.append(puntoConsegna.getLocalita()+" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getProvincia())){
                sb.append("("+Provincia.getByLabel(puntoConsegna.getProvincia()).getSigla()+")");
            }

            puntoConsegnaParam = sb.toString();
        }

        // create data parameters for Cliente
        if(cliente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(cliente.getRagioneSociale())){
                sb.append(cliente.getRagioneSociale()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getIndirizzo())){
                sb.append(cliente.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getCap())){
                sb.append(cliente.getCap()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getCitta())){
                sb.append(cliente.getCitta()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getProvincia())){
                sb.append(cliente.getProvincia());
            }

            destinatarioParam = sb.toString();
        }

        // create FatturaAccompagnatoriaDataSource
        List<FatturaAccompagnatoriaDataSource> fatturaAccompagnatoriaDataSources = new ArrayList<>();
        fatturaAccompagnatoriaDataSources.add(getFatturaAccompagnatoriaDataSource(fatturaAccompagnatoria));

        // create list of FatturaAccompagnatoriaRigheDataSource from FatturaAccompagnatoriaRiga
        List<FatturaAccompagnatoriaRigaDataSource> fatturaAccompagnatoriaRigaDataSources = getFatturaAccompagnatoriaRigheDataSource(fatturaAccompagnatoria);

        // create list of FatturaAccompagnatoriaTotaliDataSource from FatturaAccompagnatoriaTotale
        List<FatturaAccompagnatoriaTotaleDataSource> fatturaAccompagnatoriaTotaleDataSources = getFatturaAccompagnatoriaTotaliDataSource(fatturaAccompagnatoria);

        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);

        for(FatturaAccompagnatoriaTotaleDataSource fatturaAccompagnatoriaTotale: fatturaAccompagnatoriaTotaleDataSources){
            totaleImponibile = totaleImponibile.add(fatturaAccompagnatoriaTotale.getTotaleImponibile());
            totaleIva = totaleIva.add(fatturaAccompagnatoriaTotale.getTotaleIva());
        }

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_FATTURA_ACCOMPAGNATORIA);

        // create report datasource for FatturaAccompagnatoria
        JRBeanCollectionDataSource fatturaAccompagnatoriaCollectionDataSource = new JRBeanCollectionDataSource(fatturaAccompagnatoriaDataSources);

        // create report datasource for FatturaAccompagnatoriaRighe
        JRBeanCollectionDataSource fatturaAccompagnatoriaRigheCollectionDataSource = new JRBeanCollectionDataSource(fatturaAccompagnatoriaRigaDataSources);

        // create report datasource for FatturaAccompagnatoriaTotali
        JRBeanCollectionDataSource fatturaAccompagnatoriaTotaliCollectionDataSource = new JRBeanCollectionDataSource(fatturaAccompagnatoriaTotaleDataSources);

        // create report parameters
        Map<String, Object> parameters = createParameters();

        // add data to parameters
        parameters.put("fatturaAccompagnatoriaTitle", fatturaAccompagnatoriaTitleParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("puntoConsegna", puntoConsegnaParam);
        parameters.put("note", fatturaAccompagnatoria.getNote());
        parameters.put("nota", Constants.JASPER_PARAMETER_FATTURA_ACCOMPAGNATORIA_NOTA);
        parameters.put("trasportatore", fatturaAccompagnatoria.getTrasportatore());
        parameters.put("tipoTrasporto", fatturaAccompagnatoria.getTipoTrasporto());
        parameters.put("numeroColli", fatturaAccompagnatoria.getNumeroColli());
        parameters.put("dataOraTrasporto", simpleDateFormat.format(fatturaAccompagnatoria.getDataTrasporto())+" "+fatturaAccompagnatoria.getOraTrasporto());
        parameters.put("totaleImponibile", totaleImponibile);
        parameters.put("totaleIva", totaleIva);
        parameters.put("fatturaAccompagnatoriaTotDocumento", totaleImponibile.add(totaleIva).setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("fatturaAccompagnatoriaCollection", fatturaAccompagnatoriaCollectionDataSource);
        parameters.put("fatturaAccompagnatoriaRigheCollection", fatturaAccompagnatoriaRigheCollectionDataSource);
        parameters.put("fatturaAccompagnatoriaTotaliCollection", fatturaAccompagnatoriaTotaliCollectionDataSource);


        // create report
        return JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());
    }

    public byte[] generateNotaAccredito(Long idNotaAccredito) throws Exception {

        // retrieve the NotaAccredito
        NotaAccredito notaAccredito = getNotaAccredito(idNotaAccredito);
        Cliente cliente = notaAccredito.getCliente();

        // create data parameters
        String notaAccreditoTitleParam = notaAccredito.getProgressivo()+"/"+notaAccredito.getAnno()+" del "+simpleDateFormat.format(notaAccredito.getData());
        String destinatarioParam = "";

        String riferimentoDocumento = "Riferimento "+notaAccredito.getTipoRiferimento();
        if(!StringUtils.isEmpty(notaAccredito.getDocumentoRiferimento())){
            riferimentoDocumento += " n. "+notaAccredito.getDocumentoRiferimento();
        }
        if(notaAccredito.getDataDocumentoRiferimento() != null){
            riferimentoDocumento += " del "+simpleDateFormat.format(notaAccredito.getDataDocumentoRiferimento());
        }

        // create data parameters for Cliente
        if(cliente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(cliente.getRagioneSociale())){
                sb.append(cliente.getRagioneSociale()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getIndirizzo())){
                sb.append(cliente.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(cliente.getCap())){
                sb.append(cliente.getCap()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getCitta())){
                sb.append(cliente.getCitta()+" ");
            }
            if(!StringUtils.isEmpty(cliente.getProvincia())){
                sb.append(cliente.getProvincia());
            }

            destinatarioParam = sb.toString();
        }

        // create NotaAccreditoDataSource
        List<NotaAccreditoDataSource> notaAccreditoDataSources = new ArrayList<>();
        notaAccreditoDataSources.add(getNotaAccreditoDataSource(notaAccredito));

        // create list of NotaAccreditoRigheDataSource from NotaAccreditoRiga
        List<NotaAccreditoRigaDataSource> notaAccreditoRigaDataSources = getNotaAccreditoRigheDataSource(notaAccredito);

        // create list of NotaAccreditoTotaliDataSource from NotaAccreditoTotale
        List<NotaAccreditoTotaleDataSource> notaAccreditoTotaleDataSources = getNotaAccreditoTotaliDataSource(notaAccredito);

        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);

        for(NotaAccreditoTotaleDataSource notaAccreditoTotale: notaAccreditoTotaleDataSources){
            totaleImponibile = totaleImponibile.add(notaAccreditoTotale.getTotaleImponibile());
            totaleIva = totaleIva.add(notaAccreditoTotale.getTotaleIva());
        }

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_NOTA_ACCREDITO);

        // create report datasource for NotaAccredito
        JRBeanCollectionDataSource notaAccreditoCollectionDataSource = new JRBeanCollectionDataSource(notaAccreditoDataSources);

        // create report datasource for NotaAccreditoRighe
        JRBeanCollectionDataSource notaAccreditoRigheCollectionDataSource = new JRBeanCollectionDataSource(notaAccreditoRigaDataSources);

        // create report datasource for NotaAccreditoTotali
        JRBeanCollectionDataSource notaAccreditoTotaliCollectionDataSource = new JRBeanCollectionDataSource(notaAccreditoTotaleDataSources);

        // create report parameters
        Map<String, Object> parameters = createParameters();

        // add data to parameters
        parameters.put("notaAccreditoTitle", notaAccreditoTitleParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("riferimentoDocumento", riferimentoDocumento);
        parameters.put("note", notaAccredito.getNote());
        parameters.put("totaleImponibile", totaleImponibile);
        parameters.put("totaleIva", totaleIva);
        parameters.put("notaAccreditoTotDocumento", totaleImponibile.add(totaleIva).setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("notaAccreditoCollection", notaAccreditoCollectionDataSource);
        parameters.put("notaAccreditoRigheCollection", notaAccreditoRigheCollectionDataSource);
        parameters.put("notaAccreditoTotaliCollection", notaAccreditoTotaliCollectionDataSource);


        // create report
        return JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());
    }

    public byte[] generateFatturePerCommercianti(String from, String to, Set<Fattura> fatture) throws Exception{

        // retrieve the list of FatturaCommercianti
        List<FatturaCommercianteDataSource> fatturaCommercianteDataSource = getFattureCommercianti(fatture);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_FATTURE_COMMERCIANTI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fatturaCommercianteDataSource);

        // create report parameters
        Map<String, Object> parameters = createParameters();

        // add data to parameters
        parameters.put("from", from);
        parameters.put("to", to);
        parameters.put("fatturaCommercianteCollection", dataSource);

        // create report
        return JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());
    }

    public Map<AliquotaIva, BigDecimal> createFatturaTotaliImponibiliByIva(Fattura fattura) {

        Map<AliquotaIva, BigDecimal> ivaImponibileMap = new HashMap<>();
        Map<AliquotaIva, Set<DdtArticolo>> ivaDdtArticoliMap = new HashMap<>();

        /*Set<AliquotaIva> aliquoteIva = aliquotaIvaService.getAll();
        for(AliquotaIva aliquotaIva : aliquoteIva){
            ivaImponibileMap.put(aliquotaIva, BigDecimal.ZERO);
            ivaDdtArticoliMap.put(aliquotaIva, new HashSet<>());
        }*/

        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        if(fatturaDdts != null && !fatturaDdts.isEmpty()){
            // retrieve the list of all DdtArticolo
            Set<DdtArticolo> ddtArticoli = new HashSet<>();
            for(FatturaDdt fatturaDdt: fatturaDdts){
                Ddt ddt = fatturaDdt.getDdt();
                if(ddt != null){
                    ddtArticoli.addAll(ddt.getDdtArticoli());
                }
            }

            // group DdtArticolo by Iva
            if(!ddtArticoli.isEmpty()){
                ddtArticoli.stream().forEach(ddtArticolo -> {
                    Articolo articolo = ddtArticolo.getArticolo();
                    AliquotaIva iva = articolo.getAliquotaIva();

                    Set<DdtArticolo> ddtArticoliByIva = ivaDdtArticoliMap.getOrDefault(iva, new HashSet<>());
                    ddtArticoliByIva.add(ddtArticolo);

                    ivaDdtArticoliMap.put(iva, ddtArticoliByIva);
                });
            }

            // compute totaleImponibile for all AliquotaIva
            for (Map.Entry<AliquotaIva, Set<DdtArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
                AliquotaIva aliquotaIva = entry.getKey();
                BigDecimal totaleImponibile = new BigDecimal(0);

                Set<DdtArticolo> ddtArticoliByIva = entry.getValue();
                for(DdtArticolo ddtArticoloByIva : ddtArticoliByIva){
                    BigDecimal imponibile = ddtArticoloByIva.getImponibile() != null ? ddtArticoloByIva.getImponibile() : BigDecimal.ZERO;
                    totaleImponibile = totaleImponibile.add(imponibile);
                }
                ivaImponibileMap.put(aliquotaIva, totaleImponibile);

            }

        }
        return ivaImponibileMap;

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
