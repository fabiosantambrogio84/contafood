package com.contafood.service;

import com.contafood.exception.GenericException;
import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VFattura;
import com.contafood.repository.FatturaRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.repository.TipoPagamentoRepository;
import com.contafood.repository.views.VFatturaRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FatturaService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FatturaService.class);

    private final FatturaRepository fatturaRepository;
    private final FatturaDdtService fatturaDdtService;
    private final StatoFatturaService statoFatturaService;
    private final StatoDdtService statoDdtService;
    private final DdtService ddtService;
    private final TipoFatturaService tipoFatturaService;
    private final VFatturaRepository vFatturaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final SimpleDateFormat simpleDateFormat;

    @Autowired
    public FatturaService(final FatturaRepository fatturaRepository,
                          final FatturaDdtService fatturaDdtService,
                          final StatoFatturaService statoFatturaService,
                          final StatoDdtService statoDdtService,
                          final DdtService ddtService,
                          final TipoFatturaService tipoFatturaService,
                          final VFatturaRepository vFatturaRepository,
                          final PagamentoRepository pagamentoRepository,
                          final TipoPagamentoRepository tipoPagamentoRepository){
        this.fatturaRepository = fatturaRepository;
        this.fatturaDdtService = fatturaDdtService;
        this.statoFatturaService = statoFatturaService;
        this.statoDdtService = statoDdtService;
        this.ddtService = ddtService;
        this.tipoFatturaService = tipoFatturaService;
        this.vFatturaRepository = vFatturaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public List<VFattura> getAllByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo){
        LOGGER.info("Retrieving the list of 'fatture' filtered by request parameters");
        List<VFattura> fatture = vFatturaRepository.findByFilters(draw, start, length, sortOrders, dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);
        LOGGER.info("Retrieved {} 'fatture'", fatture.size());
        return fatture;
    }

    public Integer getCountByFilters(Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo){
        LOGGER.info("Retrieving the count of 'fatture' filtered by request parameters");
        Integer count = vFatturaRepository.countByFilters(dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);
        LOGGER.info("Retrieved {} 'fatture'", count);
        return count;
    }

    public List<VFattura> search(Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo){
        return vFatturaRepository.findByFilters(null, null, null, null, dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        /*
        List<Long> idTipiPagamento = new ArrayList<>();
        if(!StringUtils.isEmpty(idTipoPagamento)){
            Arrays.stream(idTipoPagamento.split(",")).mapToLong(Long::parseLong).forEach(idTipiPagamento::add);
        }

        Predicate<VFattura> isFatturaDataDaGreaterOrEquals = fattura -> {
            if(dataDa != null){
                return fattura.getData().compareTo(dataDa)>=0;
            }
            return true;
        };
        Predicate<VFattura> isFatturaDataALessOrEquals = fattura -> {
            if(dataA != null){
                return fattura.getData().compareTo(dataA)<=0;
            }
            return true;
        };
        Predicate<VFattura> isFatturaProgressivoEquals = fattura -> {
            if(progressivo != null){
                return fattura.getProgressivo().equals(progressivo);
            }
            return true;
        };
        Predicate<VFattura> isFatturaImportoEquals = fattura -> {
            if(importo != null){
                LOGGER.info("Importo {} - Fattura totale {} - Fattura totale float {}", importo, fattura.getTotale(), fattura.getTotale().floatValue());
                return importo.equals(fattura.getTotale().floatValue());
                //return fattura.getTotale().compareTo(new BigDecimal(importo).setScale(2, BigDecimal.ROUND_DOWN))==0;
            }
            return true;
        };
        Predicate<VFattura> isFatturaTipoPagamentoEquals = fattura -> {
            LOGGER.info("Filter by idTipoPagamento '{}'", idTipiPagamento);
            if(idTipiPagamento != null && !idTipiPagamento.isEmpty()){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    TipoPagamento tipoPagamento = fatturaCliente.getTipoPagamento();
                    if(tipoPagamento != null){
                        LOGGER.info("Cliente id '{}', TipoPagamento id '{}'", fatturaCliente.getId(), tipoPagamento.getId());
                        return idTipiPagamento.contains(tipoPagamento.getId());
                    }
                }
            }
            return true;
        };
        Predicate<VFattura> isFatturaClienteContains = fattura -> {
            if(cliente != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    return (fatturaCliente.getRagioneSociale().toLowerCase()).contains(cliente.toLowerCase());
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaAgenteEquals = fattura -> {
            if(idAgente != null){
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    Agente agente = fatturaCliente.getAgente();
                    if(agente != null){
                        return agente.getId().equals(Long.valueOf(idAgente));
                    }
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaDdtArticoloEquals = fattura -> {
            if(idArticolo != null){
                Set<DdtArticolo> ddtArticoli = getFatturaDdtArticoli(fattura.getId());
                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    return ddtArticoli.stream().filter(da -> da.getId() != null).map(DdtArticolo::getId).filter(daId -> daId.getArticoloId() != null && daId.getArticoloId().equals(Long.valueOf(idArticolo))).findFirst().isPresent();
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaStatoEquals = fattura -> {
            if(idStato != null){
                StatoFattura statoFattura = fattura.getStatoFattura();
                if(statoFattura != null){
                    return statoFattura.getId().equals(Long.valueOf(idStato));
                }
                return false;
            }
            return true;
        };
        Predicate<VFattura> isFatturaTipoEquals = fattura -> {
            if(idTipo != null){
                TipoFattura tipoFattura = fattura.getTipoFattura();
                if(tipoFattura != null){
                    return tipoFattura.getId().equals(Long.valueOf(idTipo));
                }
                return false;
            }
            return true;
        };

        return getAll().stream().filter(isFatturaDataDaGreaterOrEquals
                .and(isFatturaDataALessOrEquals)
                .and(isFatturaProgressivoEquals)
                .and(isFatturaImportoEquals)
                .and(isFatturaTipoPagamentoEquals)
                .and(isFatturaClienteContains)
                .and(isFatturaAgenteEquals)
                .and(isFatturaDdtArticoloEquals)
                .and(isFatturaStatoEquals)
                .and(isFatturaTipoEquals)).collect(Collectors.toSet());
        */
    }

    public Set<VFattura> getAll(){
        LOGGER.info("Retrieving the list of 'fatture vendita and fatture accompagnatorie'");
        Set<VFattura> fatture = vFatturaRepository.findAllByOrderByAnnoDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'fatture vendita and fatture accompagnatorie'", fatture.size());
        return fatture;
    }

    public Set<Fattura> getAllVendite(){
        return fatturaRepository.findAll();
    }

    public Fattura getOne(Long fatturaId){
        LOGGER.info("Retrieving 'fattura' '{}'", fatturaId);
        Fattura fattura = fatturaRepository.findById(fatturaId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'fattura' '{}'", fattura);
        return fattura;
    }

    public Map<String, Integer> getAnnoAndProgressivo(){
        Integer anno = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        Integer resultProgressivo = fatturaRepository.getLastProgressivoByAnnoContabile(anno);
        if(resultProgressivo != null){
            progressivo = resultProgressivo + 1;
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    public Set<Fattura> getFattureForReport(Date dateFrom, Date dateTo, Integer progressivoFrom, Integer annoFrom, Integer progressivoTo, Integer annoTo, String modalitaInvioFatture){
        Set<Fattura> fatture;

        Predicate<Fattura> isFatturaClienteModalitaInvioFatture = fattura -> {
            if(!StringUtils.isEmpty(modalitaInvioFatture)) {
                Cliente fatturaCliente = fattura.getCliente();
                if(fatturaCliente != null){
                    String clienteModalitaInvioFatture = fatturaCliente.getModalitaInvioFatture();
                    if(!StringUtils.isEmpty(clienteModalitaInvioFatture) && fatturaCliente.getModalitaInvioFatture().equalsIgnoreCase(modalitaInvioFatture)){
                        return true;
                    }
                }
                return false;
            }
            return true;
        };

        if(dateFrom != null && dateTo != null){
            fatture = fatturaRepository.findByDataGreaterThanEqualAndDataLessThanEqual(dateFrom, dateTo);
        } else {
            fatture = fatturaRepository.findByProgressivoGreaterThanEqualAndAnnoGreaterThanEqualAndProgressivoLessThanEqualAndAnnoLessThanEqual(progressivoFrom, annoFrom, progressivoTo, annoTo);
        }
        if(!fatture.isEmpty()){
            fatture = fatture.stream().filter(isFatturaClienteModalitaInvioFatture).collect(Collectors.toSet());
        }

        return fatture;
    }

    public Map<Cliente, List<Fattura>> getFattureByCliente(Date dateFrom, Date dateTo){
        LOGGER.info("Retrieving the list of fatture, grouped by cliente, with speditoAde 'false', dateFrom '{}' and dateTo '{}'", dateFrom, dateTo);

        Map<Cliente, List<Fattura>> fattureByCliente = new HashMap<>();

        //Predicate<Fattura> isFatturaSpeditoAdeFalse = fattura -> fattura.getSpeditoAde().equals(Boolean.FALSE);

        Predicate<Fattura> isFatturaDataDaGreaterOrEquals = fattura -> {
            if(dateFrom != null){
                return fattura.getData().compareTo(dateFrom)>=0;
            }
            return true;
        };
        Predicate<Fattura> isFatturaDataALessOrEquals = fattura -> {
            if(dateTo != null){
                return fattura.getData().compareTo(dateTo)<=0;
            }
            return true;
        };

        Set<Fattura> fatture = fatturaRepository.findAll().stream()
                .filter(isFatturaDataDaGreaterOrEquals
                .and(isFatturaDataALessOrEquals)).collect(Collectors.toSet());

        if(!fatture.isEmpty()){
            for(Fattura fattura : fatture){
                Cliente cliente = fattura.getCliente();

                List<Fattura> fattureList = fattureByCliente.getOrDefault(cliente, new ArrayList<>());
                fattureList.add(fattura);

                fattureByCliente.put(cliente, fattureList);
            }
        }

        LOGGER.info("Successfully retrieved the list of fatture grouped by cliente");

        return fattureByCliente;
    }

    @Transactional
    public Fattura create(Fattura fattura){
        LOGGER.info("Creating 'fattura'");

        checkExistsByAnnoAndProgressivoAndIdNot(fattura.getAnno(),fattura.getProgressivo(), -1L);

        fattura.setStatoFattura(statoFatturaService.getDaPagare());
        fattura.setTipoFattura(tipoFatturaService.getVendita());
        fattura.setSpeditoAde(false);
        fattura.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Fattura createdFattura = fatturaRepository.save(fattura);

        createdFattura.getFatturaDdts().forEach(fd -> {
            fd.getId().setFatturaId(createdFattura.getId());
            fd.getId().setUuid(UUID.randomUUID().toString());
            fatturaDdtService.create(fd);
        });

        // compute acconto
        computeTotaleAcconto(createdFattura);

        // compute stato
        computeStato(createdFattura);

        fatturaRepository.save(createdFattura);
        LOGGER.info("Created 'fattura' '{}'", createdFattura);

        setFatturaDdtsFatturato(createdFattura, true);
        return createdFattura;
    }

    @Transactional
    public List<Fattura> createBulk(Map<String, Object> body){
        LOGGER.info("Creating bulk 'fatture'...");
        List<Fattura> fattureToCreate = new ArrayList<>();
        List<Fattura> createdFatture = new ArrayList<>();

        Date data = Date.valueOf((String)body.get("data"));
        LOGGER.info("Retrieving 'ddt' with 'data' less or equal to '{}'", data);

        List<Ddt> ddts = ddtService.getAll().stream().filter(ddt -> {
            Boolean fatturato = ddt.getFatturato();
            if(data != null){
                if(ddt.getData().compareTo(data)<=0){
                    return fatturato == Boolean.FALSE;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        LOGGER.info("Retrieved {} 'ddt' with fatturato=false and data less or equal to {}", ddts.size(), data);

        Map<Long, Cliente> clientiMap = new HashMap<>();
        Map<Long, List<Ddt>> clientiDdtsMap = new HashMap<>();
        ddts.forEach(ddt -> {
            Cliente cliente = ddt.getCliente();
            if(cliente != null){
                clientiDdtsMap.computeIfAbsent(cliente.getId(), v -> new ArrayList<>());
                clientiDdtsMap.computeIfPresent(cliente.getId(), (key, value) -> {
                    value.add(ddt);
                    return value;
                });
                clientiMap.computeIfAbsent(cliente.getId(), v -> cliente);
                clientiMap.computeIfPresent(cliente.getId(), (key, value) -> cliente);
            }
        });

        LOGGER.info("Iterating on map with key=idCliente and value=List<Ddt>...");
        Map<String, Integer> annoProgressivoMap = getAnnoAndProgressivo();
        int anno = annoProgressivoMap.get("anno");
        int progressivo = annoProgressivoMap.get("progressivo");
        for (Map.Entry<Long, List<Ddt>> entry : clientiDdtsMap.entrySet()) {
            Long idCliente = entry.getKey();
            List<Ddt> ddtsCliente = entry.getValue();


            Fattura fattura = new Fattura();
            Set<FatturaDdt> fatturaDdts = new HashSet<>();

            fattura.setProgressivo(progressivo);
            fattura.setAnno(anno);
            Cliente cliente = clientiMap.get(idCliente);
            fattura.setCliente(cliente);
            fattura.setData(Date.valueOf(LocalDate.now()));

            BigDecimal totale = new BigDecimal(0);
            BigDecimal totaleAcconto = new BigDecimal(0);
            for(Ddt ddt:ddtsCliente){
                totale = totale.add(ddt.getTotale());
                totaleAcconto = totaleAcconto.add(ddt.getTotaleAcconto());
            }
            fattura.setTotale(Utils.roundPrice(totale));
            fattura.setTotaleAcconto(Utils.roundPrice(totaleAcconto));

            ddtsCliente.forEach(ddt -> {
                FatturaDdt fatturaDdt = new FatturaDdt();
                FatturaDdtKey fatturaDdtKey = new FatturaDdtKey();
                fatturaDdtKey.setDdtId(ddt.getId());

                fatturaDdt.setId(fatturaDdtKey);
                fatturaDdts.add(fatturaDdt);
            });
            fattura.setFatturaDdts(fatturaDdts);

            fattureToCreate.add(fattura);

            progressivo = progressivo + 1;
        }
        LOGGER.info("End of iteration on map with key=idCliente and value=List<Ddt>");

        LOGGER.info("Creating fatture...");
        Comparator<Fattura> comparator = Comparator.comparing(f -> {
            if(f.getCliente() != null){
                Cliente cliente = f.getCliente();
                if(cliente.getDittaIndividuale() || cliente.getPrivato()){
                    return cliente.getCognome() + " " + cliente.getNome();
                } else {
                    return cliente.getRagioneSociale();
                }
            }
            return "";
        });
        fattureToCreate.stream().sorted(comparator).forEach(f -> {
            create(f);
            createdFatture.add(f);
        });

        LOGGER.info("Fatture successfully created");

        LOGGER.info("Created {} bulk 'fatture'", createdFatture.size());
        return createdFatture;
    }

    /*
    @Transactional
    public Fattura update(Fattura fattura){
        LOGGER.info("Updating 'fattura'");
        checkExistsByAnnoAndProgressivoAndIdNot(fattura.getAnno(), fattura.getProgressivo(), fattura.getId());

        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fattura.setFatturaDdts(new HashSet<>());
        fatturaDdtService.deleteByFatturaId(fattura.getId());

        Fattura fatturaCurrent = fatturaRepository.findById(fattura.getId()).orElseThrow(ResourceNotFoundException::new);
        fattura.setStatoFattura(fatturaCurrent.getStatoFattura());
        fattura.setDataInserimento(fatturaCurrent.getDataInserimento());
        fattura.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Fattura updatedFattura = fatturaRepository.save(fattura);
        fatturaDdts.stream().forEach(fd -> {
            fd.getId().setFatturaId(updatedFattura.getId());
            fd.getId().setUuid(UUID.randomUUID().toString());
            fatturaDdtService.create(fd);
        });

        fatturaRepository.save(updatedFattura);
        LOGGER.info("Updated 'fattura' '{}'", updatedFattura);
        return updatedFattura;
    }
    */

    @Transactional
    public Fattura patch(Map<String,Object> patchFattura) throws Exception{
        LOGGER.info("Patching 'fattura'");

        Long id = Long.valueOf((Integer) patchFattura.get("id"));
        Fattura fattura = fatturaRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        for(String key : patchFattura.keySet()){
            Object value = patchFattura.get(key);
            switch (key) {
                case "id":
                    fattura.setId(Long.valueOf((Integer) value));
                    break;
                case "speditoAde":
                    if (value != null) {
                        fattura.setSpeditoAde((boolean) value);
                    } else {
                        fattura.setSpeditoAde(Boolean.FALSE);
                    }
                    break;
                case "progressivo":
                    fattura.setProgressivo((Integer) value);
                    break;
                case "anno":
                    fattura.setAnno((Integer) value);
                    break;
                case "data":
                    fattura.setData(new Date(simpleDateFormat.parse((String) value).getTime()));
                    break;
                case "note":
                    fattura.setNote((String) value);
                    break;
            }
        }
        checkExistsByAnnoAndProgressivoAndIdNot(fattura.getAnno(), fattura.getProgressivo(), fattura.getId());
        Fattura patchedFattura = fatturaRepository.save(fattura);

        LOGGER.info("Patched 'fattura' '{}'", patchedFattura);
        return patchedFattura;
    }

    @Transactional
    public void patchSpeditoAdeFattureByCliente(Map<Cliente, List<Fattura>> fattureByCliente, boolean speditoAde) throws Exception{
        LOGGER.info("Updating fatture setting speditoAde='{}'", speditoAde);

        if(fattureByCliente != null && !fattureByCliente.isEmpty()){
            for(Cliente cliente : fattureByCliente.keySet()){
                for(Fattura fattura : fattureByCliente.get(cliente)){
                    Map<String, Object> patchFattura = new HashMap<>();
                    patchFattura.put("id", fattura.getId().intValue());
                    patchFattura.put("speditoAde", speditoAde);

                    patch(patchFattura);
                }
            }
        }
        LOGGER.info("Successfully updated fatture setting speditoAde={}", speditoAde);
    }

    @Transactional
    public void delete(Long fatturaId){
        LOGGER.info("Deleting 'fattura' '{}'", fatturaId);

        Fattura fattura = fatturaRepository.findById(fatturaId).orElseThrow(ResourceNotFoundException::new);
        setFatturaDdtsFatturato(fattura, false);

        pagamentoRepository.deleteByFatturaId(fatturaId);
        fatturaDdtService.deleteByFatturaId(fatturaId);
        fatturaRepository.deleteById(fatturaId);
        LOGGER.info("Deleted 'fattura' '{}'", fatturaId);
    }

    public void checkStatoFatturaDaPagare(Integer idStato){
        StatoFattura statoFatturaDaPagare = statoFatturaService.getDaPagare();
        if(idStato != statoFatturaDaPagare.getId().intValue()){
            throw new GenericException("Stato diverso da '"+statoFatturaDaPagare.getDescrizione()+"'");
        }
    }

    public void checkTipoPagamentoRicevutaBancaria(Long idTipoPagamento){
        TipoPagamento tipoPagamento = tipoPagamentoRepository.findById(idTipoPagamento).orElseThrow(ResourceNotFoundException::new);
        String descrizione = tipoPagamento.getDescrizione().toLowerCase().replace(" ","");
        if(!descrizione.contains("ricevutabanc")){
            throw new GenericException("Tipo pagamento '"+tipoPagamento.getDescrizione()+"' non valido per RiBa");
        }
    }

    private void checkExistsByAnnoAndProgressivoAndIdNot(Integer anno, Integer progressivo, Long idFattura){
        Optional<Fattura> fattura = fatturaRepository.findByAnnoAndProgressivoAndIdNot(anno, progressivo, idFattura);
        if(fattura.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.FATTURA, anno, progressivo);
        }
    }

    private void computeStato(Fattura fattura){
        StatoDdt ddtStatoPagato = statoDdtService.getPagato();
        StatoDdt ddtStatoDaPagare = statoDdtService.getDaPagare();

        Set<Ddt> ddts = new HashSet<>();
        fattura.getFatturaDdts().forEach(fd -> {
            Long idDdt = fd.getId().getDdtId();
            ddts.add(ddtService.getOne(idDdt));
        });
        LOGGER.info("Fattura ddts size {}", ddts.size());
        int ddtsSize = ddts.size();
        Set<Ddt> ddtsPagati = ddts.stream().filter(d -> d.getStatoDdt().equals(ddtStatoPagato)).collect(Collectors.toSet());
        if(ddtsSize == ddtsPagati.size()){
            fattura.setStatoFattura(statoFatturaService.getPagata());
            return;
        }
        Set<Ddt> ddtsDaPagare = ddts.stream().filter(d -> d.getStatoDdt().equals(ddtStatoDaPagare)).collect(Collectors.toSet());
        if(ddtsSize == ddtsDaPagare.size()){
            fattura.setStatoFattura(statoFatturaService.getDaPagare());
            return;
        }
        fattura.setStatoFattura(statoFatturaService.getParzialmentePagata());
    }

    private void computeTotaleAcconto(Fattura fattura){
        LOGGER.info("Computing totaleAcconto...");

        BigDecimal totaleAcconto = BigDecimal.ZERO;

        Set<Ddt> ddts = new HashSet<>();
        fattura.getFatturaDdts().forEach(fd -> {
            Long idDdt = fd.getId().getDdtId();
            ddts.add(ddtService.getOne(idDdt));
        });
        LOGGER.info("Fattura ddts size {}", ddts.size());

        for(Ddt ddt: ddts){
            BigDecimal ddtTotaleAcconto = ddt.getTotaleAcconto();
            if(ddtTotaleAcconto == null){
                ddtTotaleAcconto = BigDecimal.ZERO;
            }
            totaleAcconto = totaleAcconto.add(ddtTotaleAcconto);
        }
        fattura.setTotaleAcconto(Utils.roundPrice(totaleAcconto));
    }

    private void setFatturaDdtsFatturato(Fattura fattura, boolean fatturato){
        LOGGER.info("Setting 'fatturato'={} on all ddts of 'fattura' '{}'", fatturato, fattura.getId());
        fattura.getFatturaDdts().forEach(fd -> {
            Long idDdt = fd.getId().getDdtId();
            Map<String,Object> patchDdt = new HashMap<>();
            patchDdt.put("id", idDdt.intValue());
            patchDdt.put("fatturato", fatturato);
            ddtService.patch(patchDdt);
        });
        LOGGER.info("Successfully set 'fatturato'={} on all ddts of 'fattura' '{}'", fatturato, fattura.getId());
    }
}