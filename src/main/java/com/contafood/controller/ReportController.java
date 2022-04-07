package com.contafood.controller;

import com.contafood.exception.GenericException;
import com.contafood.service.ReportService;
import com.contafood.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/report")
public class ReportController {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    @Autowired
    public ReportController(final ReportService reportService){
        this.reportService = reportService;
    }

    @RequestMapping(method = GET, path = "/pdf", produces="application/pdf")
    @CrossOrigin
    public ResponseEntity<Resource> reportPdf(@RequestParam(name = "action") String action,
                                              @RequestParam(name = "dataDa", required = false) Date dataDa,
                                              @RequestParam(name = "dataA", required = false) Date dataA,
                                              @RequestParam(name = "numeroDa", required = false) String numeroDa,
                                              @RequestParam(name = "numeroA", required = false) String numeroA) throws Exception{

        LOGGER.info("Executing action '{}' for dataDa '{}', dataA '{}', numeroDa '{}', numeroA '{}'", action, dataDa, dataA, numeroDa, numeroA);

        reportService.checkRequestParams(dataDa, dataA, numeroDa, numeroA);

        Map<String, Object> report = new HashMap<>();

        try{
            if(action.equals("stampaFatture")){
                report = reportService.createReportPdfFatture(dataDa, dataA, numeroDa, numeroA, null);
            } else if(action.equals("stampaFattureCortesia")){
                report = reportService.createReportPdfFatture(dataDa, dataA, numeroDa, numeroA, "cartaceo");
            } else if(action.equals("stampaFattureCommercianti")){
                report = reportService.createReportPdfFattureCommercianti(dataDa, dataA, numeroDa, numeroA);
            }


        } catch(Exception e){
            LOGGER.error("Error creating report file. "+e.getMessage());
            throw e;
        }

        LOGGER.info("Successfully executed action '{}' for dataDa '{}', dataA '{}', numeroDa '{}', numeroA '{}'", action, dataDa, dataA, numeroDa, numeroA);

        byte[] zipContent = (byte[])report.get("content");

        ByteArrayResource resource = new ByteArrayResource(zipContent);

        return ResponseEntity.ok()
                .headers(ReportService.createHttpHeaders((String)report.get("fileName")))
                .contentLength(zipContent.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_PDF))
                .body(resource);

    }

    @RequestMapping(method = GET, path = "/email")
    @CrossOrigin
    public String reportEmail(@RequestParam(name = "action") String action,
                                              @RequestParam(name = "dataDa", required = false) Date dataDa,
                                              @RequestParam(name = "dataA", required = false) Date dataA,
                                              @RequestParam(name = "numeroDa", required = false) String numeroDa,
                                              @RequestParam(name = "numeroA", required = false) String numeroA) throws Exception{

        LOGGER.info("Executing action '{}' for dataDa '{}', dataA '{}', numeroDa '{}', numeroA '{}'", action, dataDa, dataA, numeroDa, numeroA);

        reportService.checkRequestParams(dataDa, dataA, numeroDa, numeroA);

        String response;

        try{
            response = reportService.createReportEmailFatture(action, dataDa, dataA, numeroDa, numeroA);
        } catch(Exception e){
            LOGGER.error("Error creating report file. "+e.getMessage());
            throw e;
        }

        LOGGER.info("Successfully executed action '{}' for dataDa '{}', dataA '{}', numeroDa '{}', numeroA '{}'", action, dataDa, dataA, numeroDa, numeroA);

        return response;
    }
}


