package com.contafood.service;

import com.contafood.exception.DdtAlreadyExistingException;
import com.contafood.exception.DdtPagamentoExceedingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.DdtRepository;
import com.contafood.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtService.class);

    private final DdtRepository ddtRepository;
    private final DdtArticoloService ddtArticoloService;
    private final StatoDdtService statoDdtService;
    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public DdtService(final DdtRepository ddtRepository, final DdtArticoloService ddtArticoloService, final StatoDdtService statoDdtService, final PagamentoRepository pagamentoRepository){
        this.ddtRepository = ddtRepository;
        this.ddtArticoloService = ddtArticoloService;
        this.statoDdtService = statoDdtService;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Set<Ddt> getAll(){
        LOGGER.info("Retrieving the list of 'ddts'");
        Set<Ddt> ddts = ddtRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Set<Ddt> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'ddts' filtered by 'lotto' '{}'", lotto);
        Set<Ddt> ddts = ddtRepository.findAllByLotto(lotto);
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Ddt getOne(Long ddtId){
        LOGGER.info("Retrieving 'ddt' '{}'", ddtId);
        Ddt ddt = ddtRepository.findById(ddtId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ddt' '{}'", ddt);
        return ddt;
    }

    public List<Ddt> getByDataGreaterThanEqual(Date data){
        LOGGER.info("Retrieving 'ddt' with 'data' greater or equals to '{}'", data);
        List<Ddt> ddts = ddtRepository.findByDataGreaterThanEqualOrderByProgressivoDesc(data);
        LOGGER.info("Retrieved {} 'ddt' with 'data' greater or equals to '{}'", ddts.size(), data);
        return ddts;
    }

    public Map<String, Integer> getAnnoContabileAndProgressivo(){
        Integer annoContabile = ZonedDateTime.now().getYear();
        Integer progressivo = 1;
        List<Ddt> ddts = ddtRepository.findByAnnoContabileOrderByProgressivoDesc(annoContabile);
        if(ddts != null && !ddts.isEmpty()){
            Optional<Ddt> lastDdt = ddts.stream().findFirst();
            if(lastDdt.isPresent()){
                progressivo = lastDdt.get().getProgressivo() + 1;
            }
        }
        HashMap<String, Integer> result = new HashMap<>();
        result.put("annoContabile", annoContabile);
        result.put("progressivo", progressivo);

        return result;
    }

    @Transactional
    public Ddt create(Ddt ddt){
        LOGGER.info("Creating 'ddt'");

        checkExistsByAnnoContabileAndProgressivoAndIdNot(ddt.getAnnoContabile(),ddt.getProgressivo(), Long.valueOf(-1));

        ddt.setStatoDdt(statoDdtService.getDaPagare());

        ddt.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Ddt createdDdt = ddtRepository.save(ddt);

        createdDdt.getDdtArticoli().stream().forEach(da -> {
            da.getId().setDdtId(createdDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);
        });

        computeTotali(createdDdt, createdDdt.getDdtArticoli());

        ddtRepository.save(createdDdt);
        LOGGER.info("Created 'ddt' '{}'", createdDdt);
        return createdDdt;
    }

    @Transactional
    public Ddt update(Ddt ddt){
        LOGGER.info("Updating 'ddt'");
        checkExistsByAnnoContabileAndProgressivoAndIdNot(ddt.getAnnoContabile(),ddt.getProgressivo(), ddt.getId());

        Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
        ddt.setDdtArticoli(new HashSet<>());
        ddtArticoloService.deleteByDdtId(ddt.getId());

        Ddt ddtCurrent = ddtRepository.findById(ddt.getId()).orElseThrow(ResourceNotFoundException::new);
        ddt.setAutista(ddtCurrent.getAutista());
        ddt.setStatoDdt(ddtCurrent.getStatoDdt());
        ddt.setDataInserimento(ddtCurrent.getDataInserimento());
        ddt.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Ddt updatedDdt = ddtRepository.save(ddt);
        ddtArticoli.stream().forEach(da -> {
            da.getId().setDdtId(updatedDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);
        });

        computeTotali(updatedDdt, ddtArticoli);

        ddtRepository.save(updatedDdt);
        LOGGER.info("Updated 'ddt' '{}'", updatedDdt);
        return updatedDdt;
    }

    @Transactional
    public Ddt patch(Map<String,Object> patchDdt){
        LOGGER.info("Patching 'ddt'");

        Long id = Long.valueOf((Integer) patchDdt.get("id"));
        Ddt ddt = ddtRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        patchDdt.forEach((key, value) -> {
            if(key.equals("id")){
                ddt.setId(Long.valueOf((Integer)value));
            } else if(key.equals("idAutista")){
                if(value != null){
                    Autista autista = new Autista();
                    autista.setId(Long.valueOf((Integer)value));
                    ddt.setAutista(autista);
                } else {
                    ddt.setAutista(null);
                }
            } else if(key.equals("fatturato")){
                if(value != null){
                    ddt.setFatturato((Boolean)value);
                }
            }
        });
        Ddt patchedDdt = ddtRepository.save(ddt);

        LOGGER.info("Patched 'ddt' '{}'", patchedDdt);
        return patchedDdt;
    }

    @Transactional
    public void delete(Long ddtId){
        LOGGER.info("Deleting 'ddt' '{}'", ddtId);
        pagamentoRepository.deleteByDdtId(ddtId);
        ddtArticoloService.deleteByDdtId(ddtId);
        ddtRepository.deleteById(ddtId);
        LOGGER.info("Deleted 'ddt' '{}'", ddtId);
    }

    private void checkExistsByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idDdt){
        Optional<Ddt> ddt = ddtRepository.findByAnnoContabileAndProgressivoAndIdNot(annoContabile, progressivo, idDdt);
        if(ddt.isPresent()){
            throw new DdtAlreadyExistingException(annoContabile, progressivo);
        }
    }

    private void computeTotali(Ddt ddt, Set<DdtArticolo> ddtArticoli){
        Map<AliquotaIva, Set<DdtArticolo>> ivaDdtArticoliMap = new HashMap<>();
        ddtArticoli.stream().forEach(da -> {
            Articolo articolo = ddtArticoloService.getArticolo(da);
            AliquotaIva iva = articolo.getAliquotaIva();
            Set<DdtArticolo> ddtArticoliByIva;
            if(ivaDdtArticoliMap.containsKey(iva)){
                ddtArticoliByIva = ivaDdtArticoliMap.get(iva);
            } else {
                ddtArticoliByIva = new HashSet<>();
            }
            ddtArticoliByIva.add(da);
            ivaDdtArticoliMap.put(iva, ddtArticoliByIva);
        });
        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);
        BigDecimal totaleCosto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        Float totaleQuantita = 0f;
        for (Map.Entry<AliquotaIva, Set<DdtArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<DdtArticolo> ddtArticoliByIva = entry.getValue();
            for(DdtArticolo ddtArticolo: ddtArticoliByIva){
                totaleImponibile = totaleImponibile.add(ddtArticolo.getImponibile());
                totaleCosto = totaleCosto.add(ddtArticolo.getCosto());
                totaleQuantita = totaleQuantita + ddtArticolo.getQuantita();

                BigDecimal partialIva = ddtArticolo.getImponibile().multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(ddtArticolo.getImponibile());
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ddt.setTotaleImponibile(totaleImponibile.setScale(2, RoundingMode.HALF_DOWN));
        ddt.setTotaleIva(totaleIva.setScale(2, RoundingMode.HALF_DOWN));
        ddt.setTotaleCosto(totaleCosto.setScale(2, RoundingMode.HALF_DOWN));
        ddt.setTotale(totale.setScale(2, RoundingMode.HALF_DOWN));
        ddt.setTotaleAcconto(new BigDecimal(0));
        ddt.setTotaleQuantita(new BigDecimal(totaleQuantita).setScale(2, RoundingMode.HALF_DOWN));
    }

    // PAGAMENTI
    public Set<Pagamento> getDdtPagamenti(){
        LOGGER.info("Retrieving 'pagamenti'");
        Set<Pagamento> pagamenti = pagamentoRepository.findAllByOrderByDataDesc();
        LOGGER.info("Retrieved {} 'pagamenti'", pagamenti.size());
        return pagamenti;
    }

    public List<Pagamento> getDdtPagamentiByIdDdt(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        List<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
    }

    public Pagamento getDdtPagamento(Long pagamentoId){
        LOGGER.info("Retrieving 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'pagamento' '{}'", pagamento);
        return pagamento;
    }

    @Transactional
    public Pagamento createDdtPagamento(Pagamento pagamento){
        LOGGER.info("Creating 'pagamento'");

        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }

        Ddt ddt = ddtRepository.findById(pagamento.getDdt().getId()).orElseThrow(ResourceNotFoundException::new);
        BigDecimal totaleAcconto = ddt.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        BigDecimal totale = ddt.getTotale();
        if(totale == null){
            totale = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = totaleAcconto.add(importo).setScale(2, RoundingMode.HALF_DOWN);
        if(newTotaleAcconto.compareTo(totale) == 1){
            LOGGER.error("The 'importo' '{}' sum to '{}' is greater than the DDT 'totale' '{}' (idDdt={})", importo, totaleAcconto, totale, pagamento.getDdt().getId());
            throw new DdtPagamentoExceedingException();
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

        LOGGER.info("Updating 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
        ddt.setTotaleAcconto(newTotaleAcconto);
        computeStato(ddt);
        ddtRepository.save(ddt);
        LOGGER.info("Updated 'totaleAcconto' of 'ddt' '{}'", ddt.getId());
        return createdPagamento;
    }

    @Transactional
    public void deleteDdtPagamento(Long pagamentoId){
        LOGGER.info("Deleting 'pagamento' '{}'", pagamentoId);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(ResourceNotFoundException::new);
        BigDecimal importo = pagamento.getImporto();
        if(importo == null){
            importo = new BigDecimal(0);
        }
        Ddt ddt = pagamento.getDdt();
        BigDecimal totaleAcconto = ddt.getTotaleAcconto();
        if(totaleAcconto == null){
            totaleAcconto = new BigDecimal(0);
        }
        BigDecimal newTotaleAcconto = totaleAcconto.subtract(importo).setScale(2, RoundingMode.HALF_DOWN);
        ddt.setTotaleAcconto(newTotaleAcconto);
        computeStato(ddt);
        ddtRepository.save(ddt);
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
}
