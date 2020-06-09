package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
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
    private final GiacenzaService giacenzaService;

    @Autowired
    public DdtService(final DdtRepository ddtRepository, final DdtArticoloService ddtArticoloService,
                      final StatoDdtService statoDdtService, final PagamentoRepository pagamentoRepository,
                      final GiacenzaService giacenzaService){
        this.ddtRepository = ddtRepository;
        this.ddtArticoloService = ddtArticoloService;
        this.statoDdtService = statoDdtService;
        this.pagamentoRepository = pagamentoRepository;
        this.giacenzaService = giacenzaService;
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

        LOGGER.info(ddt.getScannerLog());

        Ddt createdDdt = ddtRepository.save(ddt);

        // create 'ddt-articoli' and 'ddt-articoli-ordini-clienti'
        createdDdt.getDdtArticoli().stream().forEach(da -> {
            da.getId().setDdtId(createdDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);

            // compute 'giacenza'
            giacenzaService.computeGiacenza(da.getId().getArticoloId(), da.getLotto(), da.getScadenza(), da.getQuantita());
        });

        // update 'pezzi-da-evadere' and 'stato-ordine' on OrdineCliente
        ddtArticoloService.updateOrdineClienteFromCreateDdt(createdDdt.getId());

        // compute totali on Ddt
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

        LOGGER.info(ddt.getScannerLog());

        Ddt updatedDdt = ddtRepository.save(ddt);
        ddtArticoli.stream().forEach(da -> {
            da.getId().setDdtId(updatedDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);

            // compute 'giacenza'
            giacenzaService.computeGiacenza(da.getId().getArticoloId(), da.getLotto(), da.getScadenza(), da.getQuantita());
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

        Set<DdtArticolo> ddtArticoli = ddtArticoloService.findByDdtId(ddtId);

        // update 'pezzi-da-evadere' and 'stato-ordine' on OrdineCliente
        ddtArticoloService.updateOrdineClienteFromDeleteDdt(ddtId);

        pagamentoRepository.deleteByDdtId(ddtId);
        ddtArticoloService.deleteByDdtId(ddtId);
        ddtRepository.deleteById(ddtId);

        for (DdtArticolo ddtArticolo:ddtArticoli) {
            // compute 'giacenza'
            giacenzaService.computeGiacenza(ddtArticolo.getId().getArticoloId(), ddtArticolo.getLotto(), ddtArticolo.getScadenza(), ddtArticolo.getQuantita());
        }

        LOGGER.info("Deleted 'ddt' '{}'", ddtId);
    }

    private void checkExistsByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idDdt){
        Optional<Ddt> ddt = ddtRepository.findByAnnoContabileAndProgressivoAndIdNot(annoContabile, progressivo, idDdt);
        if(ddt.isPresent()){
            throw new ResourceAlreadyExistingException("ddt", annoContabile, progressivo);
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
    public Set<Pagamento> getDdtPagamentiByIdDdt(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
    }

}
