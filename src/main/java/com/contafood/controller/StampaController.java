package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.model.reports.*;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.service.StampaService;
import com.contafood.util.Constants;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.Provincia;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path="/stampe")
@SuppressWarnings("unused")
public class StampaController {

    private final static Logger LOGGER = LoggerFactory.getLogger(StampaController.class);

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final StampaService stampaService;

    @Autowired
    public StampaController(final StampaService stampaService){
        this.stampaService = stampaService;
        // https://www.youtube.com/watch?v=fZtnoQpPzaw
        // https://www.qualogy.com/techblog/java-web/creating-report-with-list-containing-list-using-jasper-report
    }

    @RequestMapping(method = GET, path = "/giacenze-ingredienti")
    @CrossOrigin
    public ResponseEntity<Resource> printGiacenzeIngredienti(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'giacenze-ingredienti' with ids {}", ids);

        // retrieve the list of GiacenzeIngredienti
        List<VGiacenzaIngrediente> giacenzeIngredienti = stampaService.getGiacenzeIngredienti(ids);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_GIACENZE_INGREDIENTI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(giacenzeIngredienti);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("CollectionBeanParam", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'giacenze-ingredienti'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("giacenze-ingredienti.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/pagamenti")
    @CrossOrigin
    public ResponseEntity<Resource> printPagamenti(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'pagamenti' with ids {}", ids);

        // retrieve the list of Pagamenti
        List<PagamentoDataSource> pagamenti = stampaService.getPagamenti(ids);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_PAGAMENTI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pagamenti);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("pagamentiCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'pagamenti'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("pagamenti.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ddts")
    @CrossOrigin
    public ResponseEntity<Resource> printDdts(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                              @RequestParam(name = "dataA", required = false) Date dataA,
                                              @RequestParam(name = "progressivo", required = false) Integer progressivo,
                                              @RequestParam(name = "importo", required = false) Float importo,
                                              @RequestParam(name = "tipoPagamento", required = false) Integer idTipoPagamento,
                                              @RequestParam(name = "cliente", required = false) String cliente,
                                              @RequestParam(name = "agente", required = false) Integer idAgente,
                                              @RequestParam(name = "autista", required = false) Integer idAutista,
                                              @RequestParam(name = "articolo", required = false) Integer idArticolo,
                                              @RequestParam(name = "stato", required = false) Integer idStato,
                                              @RequestParam(name = "pagato", required = false) Boolean pagato,
                                              @RequestParam(name = "idCliente", required = false) Integer idCliente,
                                              @RequestParam(name = "fatturato", required = false) Boolean fatturato) throws Exception{
        LOGGER.info("Creating pdf for list of 'ddt'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, autista {}, articolo {}, stato {}, pagato {}, idCliente {}, fatturato {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idAutista, idArticolo, idStato, pagato, idCliente, fatturato);

        // retrieve the list of Ddt
        List<DdtDataSource> ddts = stampaService.getDdtDataSources(dataDa, dataA, progressivo, idCliente, cliente, idAgente, idAutista, idStato, pagato, fatturato, importo, idTipoPagamento, idArticolo);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_DDTS);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ddts);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        BigDecimal totaleDaPagare = new BigDecimal(0);

        for(DdtDataSource ddt: ddts){
            totaleAcconto = totaleAcconto.add(ddt.getAcconto());
            totale = totale.add(ddt.getTotale());
            totaleDaPagare = totaleDaPagare.add(ddt.getTotaleDaPagare());
        }

        // add data to parameters
        parameters.put("totaleAcconto", totaleAcconto);
        parameters.put("totale", totale);
        parameters.put("totaleDaPagare", totaleDaPagare);
        parameters.put("ddtsCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ddts'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("elenco_ddt.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ddts/{idDdt}")
    @CrossOrigin
    public ResponseEntity<Resource> printDdt(@PathVariable final Long idDdt) throws Exception{
        LOGGER.info("Creating pdf for 'ddt' with id '{}'", idDdt);

        // create report
        byte[] reportBytes = stampaService.generateDdt(idDdt);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ddt' with id '{}'", idDdt);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ddt-"+idDdt+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = POST, path = "/ddts/selected", produces=Constants.MEDIA_TYPE_APPLICATION_ZIP)
    @CrossOrigin
    public ResponseEntity<Resource> printDdtsSelected(@RequestBody final Long[] ddts) throws Exception{
        LOGGER.info("Creating pdf merging pdfs of selected 'ddt'");

        String fileName = "ddts-"+ dateTimeFormatter.format(LocalDateTime.now())+".pdf";
        byte[] content;

        if(ddts == null || ddts.length == 0){
            throw new RuntimeException("No DDT to print");
        }

        content = stampaService.generateSingleDdt(ddts);

        LOGGER.info("Successfully create pdf for checked 'ddts'");

        ByteArrayResource resource = new ByteArrayResource(content);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders(fileName))
                .contentLength(content.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ordini-autisti")
    @CrossOrigin
    public ResponseEntity<Resource> printOrdiniAutisti(
            @RequestParam(name = "idAutista") Long idAutista,
            @RequestParam(name = "dataConsegnaDa") Date dataConsegnaDa,
            @RequestParam(name = "dataConsegnaA") Date dataConsegnaA) throws Exception{
        LOGGER.info("Creating pdf for 'ordini-clienti' of 'autista' {}, 'dataConsegnaDa' {} and 'dataConsegnaA' {}", idAutista, dataConsegnaDa, dataConsegnaA);

        // retrieve Autista with id 'idAutista'
        Autista autista = stampaService.getAutista(idAutista);
        String autistaLabel = "";
        if(autista != null){
            autistaLabel = autista.getNome()+" "+autista.getCognome();
        }

        // retrieve the list of OrdiniClienti
        List<OrdineAutistaDataSource> ordineAutistaDataSources = stampaService.getOrdiniAutista(idAutista, dataConsegnaDa, dataConsegnaA);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_ORDINI_AUTISTI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ordineAutistaDataSources);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("autista", autistaLabel);
        parameters.put("dataConsegnaDa", simpleDateFormat.format(dataConsegnaDa));
        parameters.put("dataConsegnaA", simpleDateFormat.format(dataConsegnaA));
        parameters.put("ordineAutistaCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ordini-clienti' of 'autista' {}, 'dataConsegnaDa' {} and 'dataConsegnaA' {}", idAutista, dataConsegnaDa, dataConsegnaA);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ordini-clienti-autista.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/note-accredito")
    @CrossOrigin
    public ResponseEntity<Resource> printNoteAccredito(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                                       @RequestParam(name = "dataA", required = false) Date dataA,
                                                       @RequestParam(name = "progressivo", required = false) Integer progressivo,
                                                       @RequestParam(name = "importo", required = false) Float importo,
                                                       @RequestParam(name = "cliente", required = false) String cliente,
                                                       @RequestParam(name = "agente", required = false) Integer idAgente,
                                                       @RequestParam(name = "articolo", required = false) Integer idArticolo,
                                                       @RequestParam(name = "stato", required = false) Integer idStato) throws Exception{
        LOGGER.info("Creating pdf for list of 'note accredito'");

        // retrieve the list of NoteAccredito
        List<NotaAccreditoDataSource> noteAccredito = stampaService.getNotaAccreditoDataSources(dataDa, dataA, progressivo, importo, cliente, idAgente, idArticolo, idStato);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_NOTE_ACCREDITO);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(noteAccredito);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        BigDecimal totaleDaPagare = new BigDecimal(0);

        for(NotaAccreditoDataSource notaAccredito: noteAccredito){
            totaleAcconto = totaleAcconto.add(notaAccredito.getAcconto());
            totale = totale.add(notaAccredito.getTotale());
            totaleDaPagare = totaleDaPagare.add(notaAccredito.getTotaleDaPagare());
        }

        // add data to parameters
        parameters.put("totaleAcconto", totaleAcconto);
        parameters.put("totale", totale);
        parameters.put("totaleDaPagare", totaleDaPagare);
        parameters.put("noteAccreditoCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'note accredito'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("elenco_note_accredito.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/note-accredito/{idNotaAccredito}")
    @CrossOrigin
    public ResponseEntity<Resource> printNotaAccredito(@PathVariable final Long idNotaAccredito) throws Exception{
        LOGGER.info("Creating pdf for 'nota accredito' with id '{}'", idNotaAccredito);

        // create report
        byte[] reportBytes = stampaService.generateNotaAccredito(idNotaAccredito);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'nota accredito' with id '{}'", idNotaAccredito);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("nota-accredito-"+idNotaAccredito+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/fatture")
    @CrossOrigin
    public ResponseEntity<Resource> printFatture(@RequestParam(name = "dataDa", required = false) Date dataDa,
                                                 @RequestParam(name = "dataA", required = false) Date dataA,
                                                 @RequestParam(name = "progressivo", required = false) Integer progressivo,
                                                 @RequestParam(name = "importo", required = false) Float importo,
                                                 @RequestParam(name = "tipoPagamento", required = false) String idTipoPagamento,
                                                 @RequestParam(name = "cliente", required = false) String cliente,
                                                 @RequestParam(name = "agente", required = false) Integer idAgente,
                                                 @RequestParam(name = "articolo", required = false) Integer idArticolo,
                                                 @RequestParam(name = "stato", required = false) Integer idStato,
                                                 @RequestParam(name = "tipo", required = false) Integer idTipo) throws Exception{
        LOGGER.info("Creating pdf for list of 'fatture'");
        LOGGER.info("Request params: dataDa {}, dataA {}, progressivo {}, importo {}, tipoPagamento {}, cliente {}, agente {}, articolo {}, stato {}, tipo {}",
                dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        // retrieve the list of Fatture
        List<FatturaDataSource> fatture = stampaService.getFatturaDataSources(dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_FATTURE);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fatture);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        BigDecimal totaleDaPagare = new BigDecimal(0);

        for(FatturaDataSource fattura: fatture){
            totaleAcconto = totaleAcconto.add(fattura.getAcconto());
            totale = totale.add(fattura.getTotale());
            totaleDaPagare = totaleDaPagare.add(fattura.getTotaleDaPagare());
        }

        // add data to parameters
        parameters.put("totaleAcconto", totaleAcconto);
        parameters.put("totale", totale);
        parameters.put("totaleDaPagare", totaleDaPagare);
        parameters.put("fattureCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'fatture'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("elenco_fatture.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/fatture/{idFattura}")
    @CrossOrigin
    public ResponseEntity<Resource> printFattura(@PathVariable final Long idFattura) throws Exception{
        LOGGER.info("Creating pdf for 'fattura' with id '{}'", idFattura);

        byte[] reportBytes = stampaService.generateFattura(idFattura);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'fattura' with id '{}'", idFattura);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("fattura-"+idFattura+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/fatture-accompagnatorie/{idFatturaAccompagnatoria}")
    @CrossOrigin
    public ResponseEntity<Resource> printFatturaAccompagnatoria(@PathVariable final Long idFatturaAccompagnatoria) throws Exception{
        LOGGER.info("Creating pdf for 'fattura accompagnatoria' with id '{}'", idFatturaAccompagnatoria);

        byte[] reportBytes = stampaService.generateFatturaAccompagnatoria(idFatturaAccompagnatoria);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'fattura accompagnatoria' with id '{}'", idFatturaAccompagnatoria);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("fattura-accompagnatoria-"+idFatturaAccompagnatoria+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/note-reso/{idNotaReso}")
    @CrossOrigin
    public ResponseEntity<Resource> printNotaReso(@PathVariable final Long idNotaReso) throws Exception{
        LOGGER.info("Creating pdf for 'nota reso' with id '{}'", idNotaReso);

        // retrieve the NotaReso
        NotaReso notaReso = stampaService.getNotaReso(idNotaReso);
        Fornitore fornitore = notaReso.getFornitore();

        // create data parameters
        String notaResoTitleParam = notaReso.getProgressivo()+"/"+notaReso.getAnno()+" del "+simpleDateFormat.format(notaReso.getData());
        String destinatarioParam = "";

        // create data parameters for Fornitore
        if(fornitore != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(fornitore.getRagioneSociale())){
                sb.append(fornitore.getRagioneSociale()).append("\n");
            }
            if(!StringUtils.isEmpty(fornitore.getIndirizzo())){
                sb.append(fornitore.getIndirizzo()).append("\n");
            }
            if(!StringUtils.isEmpty(fornitore.getCap())){
                sb.append(fornitore.getCap()).append(" ");
            }
            if(!StringUtils.isEmpty(fornitore.getCitta())){
                sb.append(fornitore.getCitta()).append(" ");
            }
            if(!StringUtils.isEmpty(fornitore.getProvincia())){
                sb.append(fornitore.getProvincia());
            }

            destinatarioParam = sb.toString();
        }

        // create NotaResoDataSource
        List<NotaResoDataSource> notaResoDataSources = new ArrayList<>();
        notaResoDataSources.add(stampaService.getNotaResoDataSource(notaReso));

        // create list of NotaResoRigheDataSource from NotaResoRiga
        List<NotaResoRigaDataSource> notaResoRigaDataSources = stampaService.getNotaResoRigheDataSource(notaReso);

        // create list of NotaResoTotaliDataSource from NotaResoTotale
        List<NotaResoTotaleDataSource> notaResoTotaleDataSources = stampaService.getNotaResoTotaliDataSource(notaReso);

        BigDecimal totaleImponibile = new BigDecimal(0);
        BigDecimal totaleIva = new BigDecimal(0);

        for(NotaResoTotaleDataSource notaResoTotale: notaResoTotaleDataSources){
            totaleImponibile = totaleImponibile.add(notaResoTotale.getTotaleImponibile());
            totaleIva = totaleIva.add(notaResoTotale.getTotaleIva());
        }

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_NOTA_RESO);

        // create report datasource for NotaReso
        JRBeanCollectionDataSource notaResoCollectionDataSource = new JRBeanCollectionDataSource(notaResoDataSources);

        // create report datasource for NotaResoRighe
        JRBeanCollectionDataSource notaResoRigheCollectionDataSource = new JRBeanCollectionDataSource(notaResoRigaDataSources);

        // create report datasource for NotaResoTotali
        JRBeanCollectionDataSource notaResoTotaliCollectionDataSource = new JRBeanCollectionDataSource(notaResoTotaleDataSources);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("notaResoTitle", notaResoTitleParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("note", notaReso.getNote());
        parameters.put("totaleImponibile", totaleImponibile);
        parameters.put("totaleIva", totaleIva);
        parameters.put("notaResoTotDocumento", Utils.roundPrice(totaleImponibile.add(totaleIva)));
        parameters.put("notaResoCollection", notaResoCollectionDataSource);
        parameters.put("notaResoRigheCollection", notaResoRigheCollectionDataSource);
        parameters.put("notaResoTotaliCollection", notaResoTotaliCollectionDataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'nota reso' with id '{}'", idNotaReso);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("nota-reso-"+idNotaReso+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ricevute-privati")
    @CrossOrigin
    public ResponseEntity<Resource> printRicevutePrivati(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'ricevute privati' with ids {}", ids);

        // retrieve the list of RicevutePrivato
        List<RicevutaPrivatoDataSource> ricevutePrivato = stampaService.getRicevutaPrivatoDataSources(ids);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_RICEVUTE_PRIVATI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ricevutePrivato);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        BigDecimal totaleAcconto = new BigDecimal(0);
        BigDecimal totale = new BigDecimal(0);
        BigDecimal totaleDaPagare = new BigDecimal(0);

        for(RicevutaPrivatoDataSource ricevutaPrivato: ricevutePrivato){
            totaleAcconto = totaleAcconto.add(ricevutaPrivato.getAcconto());
            totale = totale.add(ricevutaPrivato.getTotale());
            totaleDaPagare = totaleDaPagare.add(ricevutaPrivato.getTotaleDaPagare());
        }

        // add data to parameters
        parameters.put("totaleAcconto", totaleAcconto);
        parameters.put("totale", totale);
        parameters.put("totaleDaPagare", totaleDaPagare);
        parameters.put("ricevutePrivatoCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ricevute privati'");

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("elenco_ricevute_privati.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ricevute-privati/{idRicevutaPrivato}")
    @CrossOrigin
    public ResponseEntity<Resource> printRicevutaPrivato(@PathVariable final Long idRicevutaPrivato) throws Exception{
        LOGGER.info("Creating pdf for 'ricevuta privato' with id '{}'", idRicevutaPrivato);

        // retrieve the RicevutaPrivato
        RicevutaPrivato ricevutaPrivato = stampaService.getRicevutaPrivato(idRicevutaPrivato);
        PuntoConsegna puntoConsegna = ricevutaPrivato.getPuntoConsegna();
        Cliente cliente = ricevutaPrivato.getCliente();

        // create RicevutaPrivatoDataSource
        List<RicevutaPrivatoDataSource> ricevutaPrivatoDataSources = new ArrayList<>();
        ricevutaPrivatoDataSources.add(stampaService.getRicevutaPrivatoDataSource(ricevutaPrivato));

        // create data parameters
        String ricevutaPrivatoTitleParam = ricevutaPrivato.getProgressivo()+"/"+ricevutaPrivato.getAnno()+" del "+simpleDateFormat.format(ricevutaPrivato.getData());
        String puntoConsegnaParam = "";
        String destinatarioParam = "";

        // create data parameters for PuntoConsegna
        if(puntoConsegna != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(puntoConsegna.getNome())){
                sb.append(puntoConsegna.getNome()).append("\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getIndirizzo())){
                sb.append(puntoConsegna.getIndirizzo()).append("\n");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getCap())){
                sb.append(puntoConsegna.getCap()).append(" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getLocalita())){
                sb.append(puntoConsegna.getLocalita()).append(" ");
            }
            if(!StringUtils.isEmpty(puntoConsegna.getProvincia())){
                sb.append("(").append(Provincia.getByLabel(puntoConsegna.getProvincia()).getSigla()).append(")");
            }

            puntoConsegnaParam = sb.toString();
        }

        // create data parameters for Cliente
        if(cliente != null){
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(cliente.getNome())){
                sb.append(cliente.getNome());
            }
            if(!StringUtils.isEmpty(cliente.getCognome())){
                sb.append(" ").append(cliente.getCognome()).append("\n");
            }
            if(!StringUtils.isEmpty(cliente.getIndirizzo())){
                sb.append(cliente.getIndirizzo()).append("\n");
            }
            if(!StringUtils.isEmpty(cliente.getCap())){
                sb.append(cliente.getCap()).append(" ");
            }
            if(!StringUtils.isEmpty(cliente.getCitta())){
                sb.append(cliente.getCitta()).append(" ");
            }
            if(!StringUtils.isEmpty(cliente.getProvincia())){
                sb.append("(").append(Provincia.getByLabel(cliente.getProvincia()).getSigla()).append(")");
            }

            destinatarioParam = sb.toString();
        }

        // create 'ricevutaPrivatoTrasportoDataOra' param
        String ricevutaPrivatoTrasportoDataOraParam = simpleDateFormat.format(ricevutaPrivato.getDataTrasporto())+" "+ricevutaPrivato.getOraTrasporto();

        // create list of RicevutaPrivatoArticoloDataSource from RicevutaPrivatoArticolo
        List<RicevutaPrivatoArticoloDataSource> ricevutaPrivatoArticoloDataSources = stampaService.getRicevutaPrivatoArticoliDataSource(ricevutaPrivato);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_RICEVUTA_PRIVATO);

        // create report datasource for RicevutaPrivato
        JRBeanCollectionDataSource ricevutaPrivatoCollectionDataSource = new JRBeanCollectionDataSource(ricevutaPrivatoDataSources);

        // create report datasource for RicevutaPrivatoArticoli
        JRBeanCollectionDataSource ricevutaPrivatoArticoliCollectionDataSource = new JRBeanCollectionDataSource(ricevutaPrivatoArticoloDataSources);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("ricevutaPrivatoTitle", ricevutaPrivatoTitleParam);
        parameters.put("puntoConsegna", puntoConsegnaParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("note", ricevutaPrivato.getNote());
        parameters.put("trasportatore", ricevutaPrivato.getTrasportatore());
        parameters.put("nota", Constants.JASPER_PARAMETER_RICEVUTA_PRIVATO_NOTA);
        parameters.put("ricevutaPrivatoTrasportoTipo", ricevutaPrivato.getTipoTrasporto());
        parameters.put("ricevutaPrivatoTrasportoDataOra", ricevutaPrivatoTrasportoDataOraParam);
        parameters.put("ricevutaPrivatoNumeroColli", ricevutaPrivato.getNumeroColli());
        parameters.put("ricevutaPrivatoTotImponibile", Utils.roundPrice(ricevutaPrivato.getTotaleImponibile()));
        parameters.put("ricevutaPrivatoTotIva", Utils.roundPrice(ricevutaPrivato.getTotaleIva()));
        parameters.put("ricevutaPrivatoTotDocumento", Utils.roundPrice(ricevutaPrivato.getTotale()));
        parameters.put("ricevutaPrivatoArticoliCollection", ricevutaPrivatoArticoliCollectionDataSource);
        parameters.put("ricevutaPrivatoCollection", ricevutaPrivatoCollectionDataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ricevuta privato' with id '{}'", idRicevutaPrivato);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ricevuta-privato-"+idRicevutaPrivato+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ordini-fornitori/{idOrdineFornitore}")
    @CrossOrigin
    public ResponseEntity<Resource> printOrdineFornitore(@PathVariable final Long idOrdineFornitore) throws Exception{
        LOGGER.info("Creating pdf for 'ordine-fornitore' with id '{}'", idOrdineFornitore);

        // create report
        byte[] reportBytes = stampaService.generateOrdineFornitore(idOrdineFornitore);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ordine-fornitore' with id '{}'", idOrdineFornitore);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ordine-fornitore-"+idOrdineFornitore+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/listini/{idListino}")
    @CrossOrigin
    public ResponseEntity<Resource> printListino(@PathVariable final Long idListino,
                                                 @RequestParam(name = "orderBy", required = false) String orderBy) throws Exception{
        LOGGER.info("Creating pdf for 'listino' with id '{}' order by '{}'", idListino, orderBy);

        // create report
        byte[] reportBytes = stampaService.generateListino(idListino, orderBy);

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'listino' with id '{}' order by '{}'", idListino, orderBy);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("listino-"+idListino+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }
}


