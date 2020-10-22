package com.contafood.controller;

import com.contafood.model.*;
import com.contafood.model.reports.*;
import com.contafood.model.views.VGiacenzaIngrediente;
import com.contafood.service.StampaService;
import com.contafood.util.Constants;
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
public class StampeController {

    private static Logger LOGGER = LoggerFactory.getLogger(StampeController.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final StampaService stampaService;

    @Autowired
    public StampeController(final StampaService stampaService){
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
                sb.append(puntoConsegna.getProvincia());
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
        parameters.put("nota", Constants.JASPER_PARAMETER_DDT_NOTA);
        parameters.put("ddtTrasportoTipo", ddt.getTipoTrasporto());
        parameters.put("ddtTrasportoDataOra", ddtTrasportoDataOraParam);
        parameters.put("ddtNumeroColli", ddt.getNumeroColli());
        parameters.put("ddtTotImponibile", ddt.getTotaleImponibile().setScale(2, RoundingMode.CEILING));
        parameters.put("ddtTotIva", ddt.getTotaleIva().setScale(2, RoundingMode.CEILING));
        parameters.put("ddtTotDocumento", ddt.getTotale().setScale(2, RoundingMode.CEILING));
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

    @RequestMapping(method = GET, path = "/note-accredito/{idNotaAccredito}")
    @CrossOrigin
    public ResponseEntity<Resource> printNotaAccredito(@PathVariable final Long idNotaAccredito) throws Exception{
        LOGGER.info("Creating pdf for 'nota accredito' with id '{}'", idNotaAccredito);

        // retrieve the NotaAccredito
        NotaAccredito notaAccredito = stampaService.getNotaAccredito(idNotaAccredito);
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
        notaAccreditoDataSources.add(stampaService.getNotaAccreditoDataSource(notaAccredito));

        // create list of NotaAccreditoRigheDataSource from NotaAccreditoRiga
        List<NotaAccreditoRigaDataSource> notaAccreditoRigaDataSources = stampaService.getNotaAccreditoRigheDataSource(notaAccredito);

        // create list of NotaAccreditoTotaliDataSource from NotaAccreditoTotale
        List<NotaAccreditoTotaleDataSource> notaAccreditoTotaleDataSources = stampaService.getNotaAccreditoTotaliDataSource(notaAccredito);

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
        Map<String, Object> parameters = stampaService.createParameters();

        // add data to parameters
        parameters.put("notaAccreditoTitle", notaAccreditoTitleParam);
        parameters.put("destinatario", destinatarioParam);
        parameters.put("riferimentoDocumento", riferimentoDocumento);
        parameters.put("totaleImponibile", totaleImponibile);
        parameters.put("totaleIva", totaleIva);
        parameters.put("notaAccreditoTotDocumento", notaAccredito.getTotale().setScale(2, RoundingMode.CEILING));
        parameters.put("notaAccreditoCollection", notaAccreditoCollectionDataSource);
        parameters.put("notaAccreditoRigheCollection", notaAccreditoRigheCollectionDataSource);
        parameters.put("notaAccreditoTotaliCollection", notaAccreditoTotaliCollectionDataSource);


        // create report
        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, new JREmptyDataSource());

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        LOGGER.info("Successfully create pdf for 'nota accredito' with id '{}'", idNotaAccredito);

        return ResponseEntity.ok()
                .headers(StampaService.createHttpHeaders("nota-accredito-"+idNotaAccredito+".pdf"))
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


