package com.contafood.service;

import com.contafood.exception.GenericException;
import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.views.VFattura;
import com.contafood.repository.FatturaRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.repository.TipoPagamentoRepository;
import com.contafood.repository.views.VFatturaRepository;
import com.contafood.util.Constants;
import com.contafood.util.enumeration.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    private static Logger LOGGER = LoggerFactory.getLogger(FatturaService.class);

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
        List<VFattura> fatture = vFatturaRepository.findByAnnoOrderByProgressivoDesc(anno);
        if(fatture != null && !fatture.isEmpty()){
            Optional<VFattura> lastFattura = fatture.stream().findFirst();
            if(lastFattura.isPresent()){
                progressivo = lastFattura.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("anno", anno);
        result.put("progressivo", progressivo);

        return result;
    }

    public Set<Pagamento> getFatturaDdtPagamenti(Long idFattura){
        Set<Pagamento> pagamenti = new HashSet<>();
        Fattura fattura = fatturaRepository.findById(idFattura).orElseThrow(ResourceNotFoundException::new);
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fatturaDdts.forEach(fd -> {
            pagamenti.addAll(fd.getDdt().getDdtPagamenti());
        });
        return pagamenti;
    }

    public Set<DdtArticolo> getFatturaDdtArticoli(Long idFattura){
        Set<DdtArticolo> ddtArticoli = new HashSet<>();
        Fattura fattura = fatturaRepository.findById(idFattura).orElseThrow(ResourceNotFoundException::new);
        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();
        fatturaDdts.forEach(fd -> {
            ddtArticoli.addAll(fd.getDdt().getDdtArticoli());
        });
        return ddtArticoli;
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

        if(fatture != null && !fatture.isEmpty()){
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

        createdFattura.getFatturaDdts().stream().forEach(fd -> {
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
        List<Fattura> createdFatture = new ArrayList<>();

        Date data = Date.valueOf((String)body.get("data"));
        LOGGER.info("Retrieving 'ddt' with 'data' less or equal to '{}'", data);

        List<Ddt> ddts = ddtService.getAll().stream().filter(ddt -> {
            Boolean fatturato = ddt.getFatturato();
            if(data != null){
                if(ddt.getData().compareTo(data)<=0){
                    return fatturato != null && fatturato == Boolean.FALSE;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        LOGGER.info("Retrieved {} 'ddt' with fatturato=false and data less or equal to {}", ddts.size(), data);

        Map<Long, List<Ddt>> clientiDdtsMap = new HashMap<>();
        ddts.forEach(ddt -> {
            Cliente cliente = ddt.getCliente();
            if(cliente != null){
                clientiDdtsMap.computeIfAbsent(cliente.getId(), v -> new ArrayList<Ddt>());
                clientiDdtsMap.computeIfPresent(cliente.getId(), (key, value) -> {
                    value.add(ddt);
                    return value;
                });
            }
        });

        LOGGER.info("Iterating on map with key=idCliente and value=List<Ddt>...");
        for (Map.Entry<Long, List<Ddt>> entry : clientiDdtsMap.entrySet()) {
            Long idCliente = entry.getKey();
            List<Ddt> ddtsCliente = entry.getValue();

            Map<String, Integer> annoProgressivoMap = getAnnoAndProgressivo();
            Fattura fattura = new Fattura();
            Set<FatturaDdt> fatturaDdts = new HashSet<>();

            fattura.setProgressivo(annoProgressivoMap.get("progressivo"));
            fattura.setAnno(annoProgressivoMap.get("anno"));
            Cliente cliente = new Cliente();
            cliente.setId(idCliente);
            fattura.setCliente(cliente);
            fattura.setData(Date.valueOf(LocalDate.now()));

            BigDecimal totale = new BigDecimal(0);
            BigDecimal totaleAcconto = new BigDecimal(0);
            for(Ddt ddt:ddtsCliente){
                totale = totale.add(ddt.getTotale());
                totaleAcconto = totaleAcconto.add(ddt.getTotaleAcconto());
            }
            fattura.setTotale(totale);
            fattura.setTotaleAcconto(totaleAcconto);

            ddtsCliente.forEach(ddt -> {
                FatturaDdt fatturaDdt = new FatturaDdt();
                FatturaDdtKey fatturaDdtKey = new FatturaDdtKey();
                fatturaDdtKey.setDdtId(ddt.getId());

                fatturaDdt.setId(fatturaDdtKey);
                fatturaDdts.add(fatturaDdt);
            });
            fattura.setFatturaDdts(fatturaDdts);

            create(fattura);
        }
        LOGGER.info("End of iteration on map with key=idCliente and value=List<Ddt>");

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
            if(key.equals("id")){
                fattura.setId(Long.valueOf((Integer)value));
            } else if(key.equals("speditoAde")){
                if(value != null){
                    fattura.setSpeditoAde((boolean)value);
                } else {
                    fattura.setSpeditoAde(Boolean.FALSE);
                }
            } else if(key.equals("progressivo")){
                fattura.setProgressivo((Integer)value);
            } else if(key.equals("anno")){
                fattura.setAnno((Integer)value);
            } else if(key.equals("data")){
                fattura.setData(new Date(simpleDateFormat.parse((String) value).getTime()));
            } else if(key.equals("note")){
                fattura.setNote((String)value);
            }
        }
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
        if(!descrizione.contains("ricevutabancaria")){
            throw new GenericException("Tipo pagamento '"+tipoPagamento.getDescrizione()+"' non valido per RiBa");
        }
    }

    /*public TipoFattura getTipoFatturaVendita(){
        return tipoFatturaService.getVendita();
    }*/

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
        fattura.setTotaleAcconto(totaleAcconto);

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

    public static HttpHeaders createHttpHeaders(String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        headers.add(HttpHeaders.CACHE_CONTROL, Constants.HTTP_HEADER_CACHE_CONTROL_VALUE);
        headers.add(HttpHeaders.PRAGMA, Constants.HTTP_HEADER_PRAGMA_VALUE);
        headers.add(HttpHeaders.EXPIRES, Constants.HTTP_HEADER_EXPIRES_VALUE);
        return headers;
    }

}
