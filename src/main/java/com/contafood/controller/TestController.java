package com.contafood.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/download")
public class TestController {

    private static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    public TestController(){
    }

    @RequestMapping(method = GET)
    @CrossOrigin
    public ResponseEntity<Resource> download() throws Exception{

        // Fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream("/reports/test_2.jasper");

        // Compile the Jasper report from .jrxml to .japser
        //final JasperReport report = JasperCompileManager.compileReport(stream);

        //JRSaver.saveObject(report, "test.jasper");

        // Fetching the employees from the data source.
        //final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(employees);

        // Adding the additional parameters to the pdf.
        //final Map<String, Object> parameters = new HashMap<>();
        //parameters.put("createdBy", "javacodegeek.com");

        // Filling the report with the employee data and additional parameters information.
        //final JasperPrint print = JasperFillManager.fillReport(report, null);

        //File pdf = File.createTempFile("output.", ".pdf");

        JREmptyDataSource dataSource = new JREmptyDataSource();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", this.getClass().getResource("/reports/logo.png"));
        parameters.put("bollino", this.getClass().getResource("/reports/bollino.png"));

        byte[] reportBytes = JasperRunManager.runReportToPdf(stream, parameters, dataSource);

        //JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));

        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test_2.pdf");
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=test_2.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        //Path path = Paths.get(pdf.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(reportBytes.length)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }

}
