package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.model.reports.*;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.service.StampaService;
import com.contafood.util.Constants;
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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/stampe")
public class StampaController {

    private static Logger LOGGER = LoggerFactory.getLogger(StampaController.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
    public ResponseEntity<Resource> printDdts(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'ddt' with ids {}", ids);

        // retrieve the list of Ddt
        List<DdtDataSource> ddts = stampaService.getDdtDataSources(ids);

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

        // retrieve the Ddt
        Ddt ddt = stampaService.getDdt(idDdt);
        PuntoConsegna puntoConsegna = ddt.getPuntoConsegna();
        Cliente cliente = ddt.getCliente();

        // create DdtDataSource
        List<DdtDataSource> ddtDataSources = new ArrayList<>();
        ddtDataSources.add(stampaService.getDdtDataSource(ddt));

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
        List<DdtArticoloDataSource> ddtArticoloDataSources = stampaService.getDdtArticoliDataSource(ddt);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_DDT);

        // create report datasource for Ddt
        JRBeanCollectionDataSource ddtCollectionDataSource = new JRBeanCollectionDataSource(ddtDataSources);

        // create report datasource for DdtArticoli
        JRBeanCollectionDataSource ddtArticoliCollectionDataSource = new JRBeanCollectionDataSource(ddtArticoloDataSources);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

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
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ddt' with id '{}'", idDdt);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ddt-"+idDdt+".pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/ordini-autisti")
    @CrossOrigin
    public ResponseEntity<Resource> printOrdiniAutisti(
            @RequestParam(name = "idAutista") Long idAutista,
            @RequestParam(name = "dataConsegna") Date dataConsegna,
            @RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'ordini-clienti' with ids {} of 'autista' {} and 'dataConsegna' {}", ids, idAutista, dataConsegna);

        // retrieve Autista with id 'idAutista'
        Autista autista = stampaService.getAutista(idAutista);
        String autistaLabel = "";
        if(autista != null){
            autistaLabel = autista.getNome()+" "+autista.getCognome();
        }

        // retrieve the list of OrdiniClienti
        List<OrdineAutistaDataSource> ordineAutistaDataSources = stampaService.getOrdiniAutista(ids);

        // fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream(Constants.JASPER_REPORT_ORDINI_AUTISTI);

        // create report datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ordineAutistaDataSources);

        // create report parameters
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("autista", autistaLabel);
        parameters.put("dataConsegna", simpleDateFormat.format(dataConsegna));
        parameters.put("ordineAutistaCollection", dataSource);

        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'ordini-clienti' with ids {} of 'autista' {} and 'dataConsegna' {}", ids, idAutista, dataConsegna);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("ordini-clienti-autista.pdf"))
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/note-accredito")
    @CrossOrigin
    public ResponseEntity<Resource> printNoteAccredito(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'note accredito' with ids {}", ids);

        // retrieve the list of NoteAccredito
        List<NotaAccreditoDataSource> noteAccredito = stampaService.getNotaAccreditoDataSources(ids);

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
    public ResponseEntity<Resource> printFatture(@RequestParam(name = "ids") String ids) throws Exception{
        LOGGER.info("Creating pdf for 'fatture' with ids {}", ids);

        // retrieve the list of Fatture
        List<FatturaDataSource> fatture = stampaService.getFatturaDataSources(ids);

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
                sb.append(fornitore.getRagioneSociale()+"\n");
            }
            if(!StringUtils.isEmpty(fornitore.getIndirizzo())){
                sb.append(fornitore.getIndirizzo()+"\n");
            }
            if(!StringUtils.isEmpty(fornitore.getCap())){
                sb.append(fornitore.getCap()+" ");
            }
            if(!StringUtils.isEmpty(fornitore.getCitta())){
                sb.append(fornitore.getCitta()+" ");
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
        parameters.put("notaResoTotDocumento", totaleImponibile.add(totaleIva).setScale(2, RoundingMode.HALF_DOWN));
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
            if(!StringUtils.isEmpty(cliente.getNome())){
                sb.append(cliente.getNome());
            }
            if(!StringUtils.isEmpty(cliente.getCognome())){
                sb.append(" "+cliente.getCognome()+"\n");
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
        parameters.put("ricevutaPrivatoTotImponibile", ricevutaPrivato.getTotaleImponibile().setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("ricevutaPrivatoTotIva", ricevutaPrivato.getTotaleIva().setScale(2, RoundingMode.HALF_DOWN));
        parameters.put("ricevutaPrivatoTotDocumento", ricevutaPrivato.getTotale().setScale(2, RoundingMode.HALF_DOWN));
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

    public static void main(String[] args) throws Exception{

        /*
        PuntoConsegna puntoConsegna = new PuntoConsegna();
        puntoConsegna.setNome("VISENTIN macelleria");
        puntoConsegna.setIndirizzo("via V. Veneto, 28");
        puntoConsegna.setCap("37052");
        puntoConsegna.setLocalita("CASALEONE");
        puntoConsegna.setProvincia("VR");

        Cliente cliente = new Cliente();
        cliente.setRagioneSociale("VISENTIN F.LLI SRL Macelleria");
        cliente.setIndirizzo("Via V. Veneto, 28");
        cliente.setCap("37052");
        cliente.setCitta("CASALEONE");
        cliente.setProvincia("VR");

        Ddt ddt = new Ddt();
        ddt.setProgressivo(1);
        ddt.setAnnoContabile(2020);
        ddt.setPuntoConsegna(puntoConsegna);
        ddt.setTipoTrasporto("mittente");
        ddt.setNumeroColli(1);
        ddt.setDataTrasporto(new Date(LocalDate.now().toEpochDay()));
        ddt.setOraTrasporto(new Time(LocalTime.now().getNano()));
        ddt.setTotaleImponibile(new BigDecimal(10.18d));
        ddt.setTotaleIva(new BigDecimal(1.02d));
        ddt.setTotale(new BigDecimal(11.2d));

        String ddtTitleParam = ddt.getProgressivo()+"/"+ddt.getAnnoContabile();
        String puntoConsegnaParam = puntoConsegna.getNome()+"\n"+puntoConsegna.getIndirizzo()+"\n"+puntoConsegna.getCap()+" "+puntoConsegna.getLocalita()+" "+puntoConsegna.getProvincia();
        String destinatarioParam = cliente.getRagioneSociale()+"\n"+cliente.getIndirizzo()+"\n"+cliente.getCap()+" "+cliente.getCitta()+" "+cliente.getProvincia();

        String notaParam = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";

        */
        List<OrdineAutistaDataSource> ordineAutistaDataSources = new ArrayList<>();

        OrdineAutistaDataSource ordineAutistaDataSource = new OrdineAutistaDataSource();
        ordineAutistaDataSource.setCodiceOrdine("1/2020");
        ordineAutistaDataSource.setCliente("BERTOLASO MAURIZIO & C. SNC");

        List<OrdineAutistaArticoloDataSource> ordineAutistaArticoloDataSources = new ArrayList<>();
        OrdineAutistaArticoloDataSource ordineAutistaArticoloDataSource = new OrdineAutistaArticoloDataSource();
        ordineAutistaArticoloDataSource.setArticolo("Articolo 1");
        ordineAutistaArticoloDataSource.setNumeroPezzi(4);
        OrdineAutistaArticoloDataSource ordineAutistaArticoloDataSource2 = new OrdineAutistaArticoloDataSource();
        ordineAutistaArticoloDataSource2.setArticolo("Articolo 2");
        ordineAutistaArticoloDataSource2.setNumeroPezzi(6);
        ordineAutistaArticoloDataSources.add(ordineAutistaArticoloDataSource);
        ordineAutistaArticoloDataSources.add(ordineAutistaArticoloDataSource2);

        ordineAutistaDataSource.setOrdineAutistaArticoloDataSources(ordineAutistaArticoloDataSources);

        OrdineAutistaDataSource ordineAutistaDataSource2 = new OrdineAutistaDataSource();
        ordineAutistaDataSource2.setCodiceOrdine("2/2020");
        ordineAutistaDataSource2.setCliente("RIZZOTTO AUGUSTO");

        List<OrdineAutistaArticoloDataSource> ordineAutistaArticoloDataSources2 = new ArrayList<>();
        OrdineAutistaArticoloDataSource ordineAutistaArticoloDataSource3 = new OrdineAutistaArticoloDataSource();
        ordineAutistaArticoloDataSource3.setArticolo("Articolo 3");
        ordineAutistaArticoloDataSource3.setNumeroPezzi(5);
        OrdineAutistaArticoloDataSource ordineAutistaArticoloDataSource4 = new OrdineAutistaArticoloDataSource();
        ordineAutistaArticoloDataSource4.setArticolo("Articolo 4");
        ordineAutistaArticoloDataSource4.setNumeroPezzi(9);
        ordineAutistaArticoloDataSources2.add(ordineAutistaArticoloDataSource3);
        ordineAutistaArticoloDataSources2.add(ordineAutistaArticoloDataSource4);

        ordineAutistaDataSource2.setOrdineAutistaArticoloDataSources(ordineAutistaArticoloDataSources2);

        ordineAutistaDataSources.add(ordineAutistaDataSource);
        ordineAutistaDataSources.add(ordineAutistaDataSource2);


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ordineAutistaDataSources);

        final InputStream stream = OrdineAutistaArticoloDataSource.class.getResourceAsStream(Constants.JASPER_REPORT_ORDINI_AUTISTI);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", VGiacenzaIngrediente.class.getResource("/reports/logo.png"));
        parameters.put("bollino", VGiacenzaIngrediente.class.getResource("/reports/bollino.png"));
        parameters.put("autista", "ELIA URBANI");
        parameters.put("dataConsegna", "22/09/2020");
        parameters.put("ordineAutistaCollection", dataSource);
        /*parameters.put("ordineAutistaArticoliCollection", notaParam);
        parameters.put("ddtTrasportoTipo", ddt.getTipoTrasporto());
        parameters.put("ddtTrasportoDataOra", ddt.getDataTrasporto()+" "+ddt.getOraTrasporto());
        parameters.put("ddtNumeroColli", ddt.getNumeroColli());
        parameters.put("ddtTotImponibile", ddt.getTotaleImponibile());
        parameters.put("ddtTotIva", ddt.getTotaleIva());
        parameters.put("ddtTotDocumento", ddt.getTotale());*/

        //parameters.put("CollectionBeanParam", dataSource);

        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\FabioAndreaSantambro\\Desktop\\report1.pdf")) {
            fos.write(reportBytes);
        }
    }

}


