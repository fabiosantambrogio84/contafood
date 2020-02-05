package com.contafood.service;

import com.contafood.exception.DdtAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.repository.DdtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DdtService {

    private static Logger LOGGER = LoggerFactory.getLogger(DdtService.class);

    private final DdtRepository ddtRepository;
    private final DdtArticoloService ddtArticoloService;
    private final StatoDdtService statoDdtService;
    private final PagamentoService pagamentoService;

    @Autowired
    public DdtService(final DdtRepository ddtRepository, final DdtArticoloService ddtArticoloService, final StatoDdtService statoDdtService, final PagamentoService pagamentoService){
        this.ddtRepository = ddtRepository;
        this.ddtArticoloService = ddtArticoloService;
        this.statoDdtService = statoDdtService;
        this.pagamentoService = pagamentoService;
    }

    public Set<Ddt> getAll(){
        LOGGER.info("Retrieving the list of 'ddts'");
        Set<Ddt> ddts = ddtRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Ddt getOne(Long ddtId){
        LOGGER.info("Retrieving 'ddt' '{}'", ddtId);
        Ddt ddt = ddtRepository.findById(ddtId).orElseThrow(ResourceNotFoundException::new);
        LOGGER.info("Retrieved 'ddt' '{}'", ddt);
        return ddt;
    }

    public List<Pagamento> getDdtPagamenti(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        List<Pagamento> pagamenti = pagamentoService.getDdtPagamenti(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
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

        checkExistsByAnnoContabileAndProgressivo(ddt.getAnnoContabile(),ddt.getProgressivo());

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
        checkExistsByAnnoContabileAndProgressivo(ddt.getAnnoContabile(),ddt.getProgressivo());

        Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
        ddt.setDdtArticoli(new HashSet<>());
        ddtArticoloService.deleteByDdtId(ddt.getId());

        Ddt ddtCurrent = ddtRepository.findById(ddt.getId()).orElseThrow(ResourceNotFoundException::new);
        ddt.setStatoDdt(ddtCurrent.getStatoDdt());
        ddt.setDataInserimento(ddtCurrent.getDataInserimento());
        ddt.setDataAggiornamento(Timestamp.from(ZonedDateTime.now().toInstant()));

        Ddt updatedDdt = ddtRepository.save(ddt);
        ddtArticoli.stream().forEach(da -> {
            da.getId().setDdtId(updatedDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);
        });

        computeTotali(updatedDdt, updatedDdt.getDdtArticoli());

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
            } else if(key.equals("totaleAcconto")){
                if(value != null){
                    ddt.setTotaleAcconto(new BigDecimal(Float.parseFloat((String)value)));
                } else {
                    ddt.setTotaleAcconto(new BigDecimal(0));
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
        pagamentoService.deleteByDdtId(ddtId);
        ddtArticoloService.deleteByDdtId(ddtId);
        ddtRepository.deleteById(ddtId);
        LOGGER.info("Deleted 'ddt' '{}'", ddtId);
    }

    private void checkExistsByAnnoContabileAndProgressivo(Integer annoContabile, Integer progressivo){
        Optional<Ddt> ddt = ddtRepository.findByAnnoContabileAndProgressivo(annoContabile, progressivo);
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
        for (Map.Entry<AliquotaIva, Set<DdtArticolo>> entry : ivaDdtArticoliMap.entrySet()) {
            BigDecimal iva = entry.getKey().getValore();
            BigDecimal totaleByIva = new BigDecimal(0);
            Set<DdtArticolo> ddtArticoliByIva = entry.getValue();
            for(DdtArticolo ddtArticolo: ddtArticoliByIva){
                totaleImponibile = totaleImponibile.add(ddtArticolo.getImponibile());

                totaleCosto = totaleCosto.add(ddtArticolo.getCosto());

                BigDecimal partialIva = ddtArticolo.getImponibile().multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(ddtArticolo.getImponibile());
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ddt.setTotaleImponibile(totaleImponibile.setScale(2, RoundingMode.CEILING));
        ddt.setTotaleIva(totaleIva.setScale(2, RoundingMode.CEILING));
        ddt.setTotaleCosto(totaleCosto.setScale(2, RoundingMode.CEILING));
        ddt.setTotale(totale.setScale(2, RoundingMode.CEILING));
        ddt.setTotaleAcconto(new BigDecimal(0));
    }

}
