package com.contafood.service;

import com.contafood.exception.ResourceAlreadyExistingException;
import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.*;
import com.contafood.model.beans.DdtRicercaLotto;
import com.contafood.model.views.VDdt;
import com.contafood.repository.DdtRepository;
import com.contafood.repository.PagamentoRepository;
import com.contafood.repository.views.VDdtRepository;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DdtService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DdtService.class);

    private final DdtRepository ddtRepository;
    private final DdtArticoloService ddtArticoloService;
    private final StatoDdtService statoDdtService;
    private final PagamentoRepository pagamentoRepository;
    private final GiacenzaArticoloService giacenzaArticoloService;
    private final VDdtRepository vDdtRepository;

    @Autowired
    public DdtService(final DdtRepository ddtRepository, final DdtArticoloService ddtArticoloService,
                      final StatoDdtService statoDdtService, final PagamentoRepository pagamentoRepository,
                      final GiacenzaArticoloService giacenzaArticoloService,
                      final VDdtRepository vDdtRepository){
        this.ddtRepository = ddtRepository;
        this.ddtArticoloService = ddtArticoloService;
        this.statoDdtService = statoDdtService;
        this.pagamentoRepository = pagamentoRepository;
        this.giacenzaArticoloService = giacenzaArticoloService;
        this.vDdtRepository = vDdtRepository;
    }

    public Set<Ddt> getAll(){
        LOGGER.info("Retrieving the list of 'ddts'");
        Set<Ddt> ddts = ddtRepository.findAllByOrderByAnnoContabileDescProgressivoDesc();
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Set<DdtRicercaLotto> getAllByLotto(String lotto){
        LOGGER.info("Retrieving the list of 'ddts' filtered by 'lotto' '{}'", lotto);
        Set<DdtRicercaLotto> ddts = ddtRepository.findAllByLotto(lotto);
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public List<VDdt> getAllByFilters(Date dataDa, Date dataA, Integer progressivo, Integer idCliente, String cliente, Integer idAgente, Integer idAutista, Integer idStato, Boolean pagato, Boolean fatturato, Float importo, Integer idTipoPagamento, Integer idArticolo){
        LOGGER.info("Retrieving the list of 'ddts' filtered by request paramters");
        List<VDdt> ddts = vDdtRepository.findByFilter(dataDa, dataA, progressivo, idCliente, cliente, idAgente, idAutista, idStato, pagato, fatturato, importo, idTipoPagamento, idArticolo);
        LOGGER.info("Retrieved {} 'ddts'", ddts.size());
        return ddts;
    }

    public Ddt getOne(Long ddtId){
        LOGGER.info("Retrieving 'ddt' '{}'", ddtId);
        Ddt ddt = ddtRepository.findById(ddtId).orElseThrow(ResourceNotFoundException::new);

        // filter DdtArticoli with quantity not null and prezzo not null
        ddt = filterDdtArticoli(ddt);

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
        Integer progressivo = getProgressivo(annoContabile);
        HashMap<String, Integer> result = new HashMap<>();
        result.put("annoContabile", annoContabile);
        result.put("progressivo", progressivo);

        return result;
    }

    public String getProgressiviDuplicates(){
        String result = "";
        List<Integer> progressivi = ddtRepository.getProgressiviDuplicates();
        if(progressivi != null && !progressivi.isEmpty()){
            result = progressivi.stream().map(Object::toString).collect(Collectors.joining(","));
        }
        return result;
    }

    private Integer getProgressivo(Integer annoContabile){
        Integer progressivo = 1;
        Integer resultProgressivo = ddtRepository.getLastProgressivoByAnnoContabile(annoContabile);
        if(resultProgressivo != null){
            progressivo = resultProgressivo + 1;
        }
        return progressivo;
    }

    @Transactional
    public Ddt create(Ddt ddt){
        LOGGER.info("Creating 'ddt'");

        Integer progressivo = ddt.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ddt.getAnnoContabile());
            ddt.setProgressivo(progressivo);
        }

        checkExistsByAnnoContabileAndProgressivoAndIdNot(ddt.getAnnoContabile(),ddt.getProgressivo(), -1L);

        ddt.setStatoDdt(statoDdtService.getDaPagare());
        ddt.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));

        LOGGER.info(ddt.getScannerLog());

        Ddt createdDdt = ddtRepository.save(ddt);

        // create 'ddt-articoli'
        createdDdt.getDdtArticoli().stream().forEach(da -> {
            da.getId().setDdtId(createdDdt.getId());
            da.getId().setUuid(UUID.randomUUID().toString());
            ddtArticoloService.create(da);

            // compute 'giacenza articolo'
            giacenzaArticoloService.computeGiacenza(da.getId().getArticoloId(), da.getLotto(), da.getScadenza(), da.getQuantita(), Resource.DDT);
        });

        // update 'pezzi-da-evadere' and 'stato-ordine' on OrdineCliente
        //ddtArticoloService.updateOrdineClienteFromCreateDdt(createdDdt.getId());

        // compute totali on Ddt
        computeTotali(createdDdt, createdDdt.getDdtArticoli());

        ddtRepository.save(createdDdt);
        LOGGER.info("Created 'ddt' '{}'", createdDdt);
        return createdDdt;
    }

    @Transactional
    public Ddt update(Ddt ddt){
        LOGGER.info("Updating 'ddt'");

        Integer progressivo = ddt.getProgressivo();
        if(progressivo == null){
            progressivo = getProgressivo(ddt.getAnnoContabile());
            ddt.setProgressivo(progressivo);
        }

        checkExistsByAnnoContabileAndProgressivoAndIdNot(ddt.getAnnoContabile(),ddt.getProgressivo(), ddt.getId());

        Boolean modificaGiacenze = ddt.getModificaGiacenze();

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

            if(modificaGiacenze != null && modificaGiacenze.equals(Boolean.TRUE)){
                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(da.getId().getArticoloId(), da.getLotto(), da.getScadenza(), da.getQuantita(), Resource.DDT);
            }
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
    public void delete(Long ddtId, Boolean modificaGiacenze){
        LOGGER.info("Deleting 'ddt' '{}' ('modificaGiacenze={}')", ddtId, modificaGiacenze);

        Set<DdtArticolo> ddtArticoli = ddtArticoloService.findByDdtId(ddtId);

        // update 'pezzi-da-evadere' and 'stato-ordine' on OrdineCliente
        ddtArticoloService.updateOrdineClienteFromDeleteDdt(ddtId);

        pagamentoRepository.deleteByDdtId(ddtId);
        ddtArticoloService.deleteByDdtId(ddtId);
        ddtRepository.deleteById(ddtId);

        if(modificaGiacenze.equals(Boolean.TRUE)){
            for (DdtArticolo ddtArticolo:ddtArticoli) {
                // compute 'giacenza articolo'
                giacenzaArticoloService.computeGiacenza(ddtArticolo.getId().getArticoloId(), ddtArticolo.getLotto(), ddtArticolo.getScadenza(), ddtArticolo.getQuantita(), Resource.DDT);
            }
        }
        LOGGER.info("Deleted 'ddt' '{}'", ddtId);
    }

    private void checkExistsByAnnoContabileAndProgressivoAndIdNot(Integer annoContabile, Integer progressivo, Long idDdt){
        Optional<Ddt> ddt = ddtRepository.findByAnnoContabileAndProgressivoAndIdNot(annoContabile, progressivo, idDdt);
        if(ddt.isPresent()){
            throw new ResourceAlreadyExistingException(Resource.DDT, annoContabile, progressivo);
        }
    }

    private void computeTotali(Ddt ddt, Set<DdtArticolo> ddtArticoli){
        Map<AliquotaIva, Set<DdtArticolo>> ivaDdtArticoliMap = new HashMap<>();
        ddtArticoli.stream().filter(da -> da.getQuantita() != null && da.getQuantita() != 0 && da.getPrezzo() != null).forEach(da -> {
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
                BigDecimal imponibile = ddtArticolo.getImponibile() != null ? ddtArticolo.getImponibile() : BigDecimal.ZERO;
                BigDecimal costo = ddtArticolo.getCosto() != null ? ddtArticolo.getCosto() : BigDecimal.ZERO;

                totaleImponibile = totaleImponibile.add(imponibile);
                totaleCosto = totaleCosto.add(costo);
                totaleQuantita = totaleQuantita + (ddtArticolo.getQuantita() != null ? ddtArticolo.getQuantita() : 0f);

                BigDecimal partialIva = imponibile.multiply(iva.divide(new BigDecimal(100)));
                totaleIva = totaleIva.add(partialIva);

                totaleByIva = totaleByIva.add(imponibile);
            }
            totale = totale.add(totaleByIva.add(totaleByIva.multiply(iva.divide(new BigDecimal(100)))));
        }
        ddt.setTotaleImponibile(Utils.roundPrice(totaleImponibile));
        ddt.setTotaleIva(Utils.roundPrice(totaleIva));
        ddt.setTotaleCosto(Utils.roundPrice(totaleCosto));
        ddt.setTotale(Utils.roundPrice(totale));
        ddt.setTotaleAcconto(new BigDecimal(0));
        ddt.setTotaleQuantita(Utils.roundPrice(new BigDecimal(totaleQuantita)));
    }

    private Ddt filterDdtArticoli(Ddt ddt){
        Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli().stream().filter(da -> da.getQuantita() != null && da.getQuantita() != 0 && da.getPrezzo() != null).collect(Collectors.toSet());
        ddt.setDdtArticoli(ddtArticoli);
        return ddt;
    }

    // PAGAMENTI
    public Set<Pagamento> getDdtPagamentiByIdDdt(Long ddtId){
        LOGGER.info("Retrieving 'pagamenti' of 'ddt' '{}'", ddtId);
        Set<Pagamento> pagamenti = pagamentoRepository.findByDdtIdOrderByDataDesc(ddtId);
        LOGGER.info("Retrieved {} 'pagamenti' of 'ddt' '{}'", pagamenti.size(), ddtId);
        return pagamenti;
    }

}
