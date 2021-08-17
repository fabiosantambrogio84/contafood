package com.contafood.service;

import com.contafood.model.*;
import com.contafood.properties.AdeExportProperties;
import com.contafood.service.jpa.NativeQueryService;
import com.contafood.util.AdeConstants;
import com.contafood.util.Constants;
import com.contafood.util.enumeration.Provincia;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class AdeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdeService.class);

    private final NativeQueryService nativeQueryService;
    private final StampaService stampaService;


    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final String baseDirectory;

    @Autowired
    public AdeService(final NativeQueryService nativeQueryService,
                      final StampaService stampaService,
                      final AdeExportProperties adeExportProperties
                      ){
        this.nativeQueryService = nativeQueryService;
        this.stampaService = stampaService;
        this.baseDirectory = adeExportProperties.getBaseDirectory();
    }

    public Map<String, Object> createFattureZipFile(Map<Cliente, List<Fattura>> fattureByCliente) throws Exception{

        Map<String, Object> result;

        LOGGER.info("Start creating ZIP file for fatture...");

        Integer idExport = createFattureXmlFiles(fattureByCliente);

        result = createZipFile(idExport, "export_fatture_elettroniche");

        LOGGER.info("Successfully created ZIP file for fatture");

        return result;
    }

    public Map<String, Object> createFattureAccompagnatorieZipFile(Map<Cliente, List<FatturaAccompagnatoria>> fattureAccompagnatorieByCliente) throws Exception{

        Map<String, Object> result;

        LOGGER.info("Start creating ZIP file for fatture accompagnatorie...");

        Integer idExport = createFattureAccompagnatorieXmlFiles(fattureAccompagnatorieByCliente);

        result = createZipFile(idExport, "export_fatture_accompagnatorie_elettroniche");

        LOGGER.info("Successfully created ZIP file for fatture accompagnatorie");

        return result;
    }

    public Map<String, Object> createNoteAccreditoZipFile(Map<Cliente, List<NotaAccredito>> noteAccreditoByCliente) throws Exception{

        Map<String, Object> result;

        LOGGER.info("Start creating ZIP file for note accredito...");

        Integer idExport = createNoteAccreditoXmlFiles(noteAccreditoByCliente);

        result = createZipFile(idExport, "export_note_accredito_elettroniche");

        LOGGER.info("Successfully created ZIP file for note accredito");

        return result;
    }

    public Map<String, String> createFatturaXmlFile(Fattura fattura) throws Exception{

        Map<String, String> result = new HashMap<>();

        Cliente cliente = fattura.getCliente();

        // get progressivo file
        Integer idProgressivo = nativeQueryService.getAdeNextId("xml_file");

        // create xml file name
        String fileName = AdeConstants.PAESE + AdeConstants.CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(idProgressivo), 5, '0') + ".xml";

        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

        // create xml root element
        xmlStreamWriter.writeStartElement("p", "FatturaElettronica", "");
        xmlStreamWriter.writeAttribute("xmlns", "", "ds", "http://www.w3.org/2000/09/xmldsig#");
        xmlStreamWriter.writeAttribute("xmlns", "", "p", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2");
        xmlStreamWriter.writeAttribute("versione", "FPR12");

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeStartElement("FatturaElettronicaHeader");

        // create xml node 'DatiTrasmissione'
        createNodeDatiTrasmissione(xmlStreamWriter, cliente, idProgressivo);

        // create xml node 'CedentePrestatore'
        createNodeCedentePrestatore(xmlStreamWriter);

        // create xml node 'CessionarioCommittente'
        createNodeCessionarioCommittente(xmlStreamWriter, cliente);

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeEndElement();

        // create xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeStartElement("FatturaElettronicaBody");

        Set<FatturaDdt> fatturaDdts = fattura.getFatturaDdts();

        createNodeBodyDatiGenerali(xmlStreamWriter, fattura, fatturaDdts);
        createNodeBodyDatiBeniServizi(xmlStreamWriter, fattura, fatturaDdts);
        createNodeBodyDatiPagamento(xmlStreamWriter, fattura);
        createNodeBodyAllegati(xmlStreamWriter, fattura);

        // close xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.flush();
        xmlStreamWriter.close();

        String xmlString = stringWriter.getBuffer().toString();
        stringWriter.close();

        xmlString = transformToPrettyPrint(xmlString);

        result.put("fileName", fileName);
        result.put("content", xmlString);

        return result;
    }

    public Map<String, String> createFatturaAccompagnatoriaXmlFile(FatturaAccompagnatoria fatturaAccompagnatoria) throws Exception{

        Map<String, String> result = new HashMap<>();

        Cliente cliente = fatturaAccompagnatoria.getCliente();

        // get progressivo file
        Integer idProgressivo = nativeQueryService.getAdeNextId("xml_file");

        // create xml file name
        String fileName = AdeConstants.PAESE + AdeConstants.CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(idProgressivo), 5, '0') + ".xml";

        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

        // create xml root element
        xmlStreamWriter.writeStartElement("p", "FatturaElettronica", "");
        xmlStreamWriter.writeAttribute("xmlns", "", "ds", "http://www.w3.org/2000/09/xmldsig#");
        xmlStreamWriter.writeAttribute("xmlns", "", "p", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2");
        xmlStreamWriter.writeAttribute("versione", "FPR12");

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeStartElement("FatturaElettronicaHeader");

        // create xml node 'DatiTrasmissione'
        createNodeDatiTrasmissione(xmlStreamWriter, cliente, idProgressivo);

        // create xml node 'CedentePrestatore'
        createNodeCedentePrestatore(xmlStreamWriter);

        // create xml node 'CessionarioCommittente'
        createNodeCessionarioCommittente(xmlStreamWriter, cliente);

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeEndElement();

        // create xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeStartElement("FatturaElettronicaBody");

        Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli = fatturaAccompagnatoria.getFatturaAccompagnatoriaArticoli();

        createNodeBodyDatiGenerali(xmlStreamWriter, fatturaAccompagnatoria, fatturaAccompagnatoriaArticoli);
        createNodeBodyDatiBeniServizi(xmlStreamWriter, fatturaAccompagnatoriaArticoli, fatturaAccompagnatoria.getFatturaAccompagnatoriaTotali());
        createNodeBodyDatiPagamento(xmlStreamWriter, fatturaAccompagnatoria);
        createNodeBodyAllegati(xmlStreamWriter, fatturaAccompagnatoria);

        // close xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.flush();
        xmlStreamWriter.close();

        String xmlString = stringWriter.getBuffer().toString();
        stringWriter.close();

        xmlString = transformToPrettyPrint(xmlString);

        result.put("fileName", fileName);
        result.put("content", xmlString);

        return result;
    }

    public Map<String, String> createNotaAccreditoXmlFile(NotaAccredito notaAccredito) throws Exception{

        Map<String, String> result = new HashMap<>();

        Cliente cliente = notaAccredito.getCliente();

        // get progressivo file
        Integer idProgressivo = nativeQueryService.getAdeNextId("xml_file");

        // create xml file name
        String fileName = AdeConstants.PAESE + AdeConstants.CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(idProgressivo), 5, '0') + ".xml";

        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

        // create xml root element
        xmlStreamWriter.writeStartElement("p", "FatturaElettronica", "");
        xmlStreamWriter.writeAttribute("xmlns", "", "ds", "http://www.w3.org/2000/09/xmldsig#");
        xmlStreamWriter.writeAttribute("xmlns", "", "p", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2");
        xmlStreamWriter.writeAttribute("versione", "FPR12");

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeStartElement("FatturaElettronicaHeader");

        // create xml node 'DatiTrasmissione'
        createNodeDatiTrasmissione(xmlStreamWriter, cliente, idProgressivo);

        // create xml node 'CedentePrestatore'
        createNodeCedentePrestatore(xmlStreamWriter);

        // create xml node 'CessionarioCommittente'
        createNodeCessionarioCommittente(xmlStreamWriter, cliente);

        // create xml node 'FatturaElettronicaHeader'
        xmlStreamWriter.writeEndElement();

        // create xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeStartElement("FatturaElettronicaBody");

        Set<NotaAccreditoRiga> notaAccreditoRighe = notaAccredito.getNotaAccreditoRighe();

        createNodeBodyDatiGenerali(xmlStreamWriter, notaAccredito, notaAccreditoRighe);
        createNodeBodyDatiBeniServizi(xmlStreamWriter, notaAccredito, notaAccreditoRighe, notaAccredito.getNotaAccreditoTotali());
        createNodeBodyDatiPagamento(xmlStreamWriter, notaAccredito);
        createNodeBodyAllegati(xmlStreamWriter, notaAccredito);

        // close xml node 'FatturaElettronicaBody'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.flush();
        xmlStreamWriter.close();

        String xmlString = stringWriter.getBuffer().toString();
        stringWriter.close();

        xmlString = transformToPrettyPrint(xmlString);

        result.put("fileName", fileName);
        result.put("content", xmlString);

        return result;
    }

    private Map<String, Object> createZipFile(Integer idExport, String fileNamePrefix) throws Exception{

        Map<String, Object> result = new HashMap<>();
        String resultFilePath = "";
        String resultFileName = "";
        byte[] zipFile;

        String destinationPath = baseDirectory + AdeConstants.FILE_SEPARATOR + idExport;
        Path path = Paths.get(destinationPath);

        long maxSize = AdeConstants.MAX_ZIP_FILE_SIZE;
        long currentSize = 0;
        int index = 1;

        Map<Integer, List<Path>> filesGrouped = new LinkedHashMap<>();
        filesGrouped.put(index, new ArrayList<>());

        // create a map containing xml files to insert into zip file
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
            for (Path file: directoryStream) {
                long fileSize = file.toFile().length();

                if((currentSize + fileSize) < maxSize){
                    currentSize = currentSize + fileSize;

                    List<Path> filesByIndex = filesGrouped.getOrDefault(index, new ArrayList<>());
                    filesByIndex.add(file);
                    filesGrouped.put(index, filesByIndex);

                } else{
                    currentSize = fileSize;
                    index = index + 1;
                    List<Path> filesByIndex = new ArrayList<>();
                    filesByIndex.add(file);
                    filesGrouped.put(index, filesByIndex);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error creating xml files map");
            throw e;
        }

        if(!filesGrouped.isEmpty()){

            // create a file zip for every key of the map
            for(Map.Entry<Integer, List<Path>> entry : filesGrouped.entrySet()){

                // create the progressivo for the zip file
                Integer progressivoZip = nativeQueryService.getAdeNextId("zip_file");

                // create zip file name
                String fileName = AdeConstants.PAESE + AdeConstants.CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(progressivoZip), 5, '0')+".zip";

                // retrieve the list of xml file to insert into zip file
                List<Path> filesToZip = entry.getValue();

                try(FileOutputStream fos = new FileOutputStream(path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + fileName);
                    ZipOutputStream zipOut = new ZipOutputStream(fos)){

                    for(Path fileToZip : filesToZip){
                        try(FileInputStream fis = new FileInputStream(fileToZip.toFile())){
                            ZipEntry zipEntry = new ZipEntry(fileToZip.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while((length = fis.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                        }
                    }

                    // delete xml files inserted into zip file
                    //removeFiles(filesToZip, new ArrayList<>());

                } catch(Exception e){
                    e.printStackTrace();
                    LOGGER.error("Error creating zip files");
                    throw e;
                }
            }

            // create the zip file containing all the zip files
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyyMMdd_HHmmss");

            resultFileName = fileNamePrefix+"_"+sdf.format(new Date(System.currentTimeMillis()))+".zip";
            resultFilePath = path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + resultFileName;

            try(FileOutputStream fos = new FileOutputStream(resultFilePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){

                for (Path file: directoryStream) {
                    if(file.getFileName().toString().endsWith("zip") && !file.getFileName().toString().startsWith(fileNamePrefix)){
                        try(FileInputStream fis = new FileInputStream(file.toFile())){
                            ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while((length = fis.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                        }
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
                LOGGER.error("Error creating final zip file");
                throw e;
            }
        }

        zipFile = Files.readAllBytes(Paths.get(resultFilePath));

        // remove all zip files
        removeFileOrDirectory(path);

        result.put("fileName", resultFileName);
        result.put("content", zipFile);

        return result;
    }

    private Integer createFattureXmlFiles(Map<Cliente, List<Fattura>> fattureByCliente) throws Exception{
        LOGGER.info("Start creating xml files for fatture...");
        Integer idExport;

        try{
            checkAndCreateDirectory(baseDirectory);

            // retrieve id for the exportation
            idExport = nativeQueryService.getAdeNextId("export");

            LOGGER.info("AdeExport: id={}", idExport);

            // create, if not exists, the destination folder
            String destinationPath = baseDirectory + AdeConstants.FILE_SEPARATOR + idExport;
            Path path = Paths.get(destinationPath);

            checkAndCreateDirectory(destinationPath);

            // create an xml file for every Cliente and Fattura
            if(fattureByCliente != null && !fattureByCliente.isEmpty()){
                for(Cliente cliente : fattureByCliente.keySet()){
                    // retrieve the list of Fattura associate to the current Cliente
                    List<Fattura> fatture = fattureByCliente.get(cliente);

                    if(fatture != null && !fatture.isEmpty()){
                        for(Fattura fattura : fatture){

                            Map<String, String> result =  createFatturaXmlFile(fattura);

                            File file = new File(path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + result.get("fileName"));
                            file.setReadable(true, false);
                            file.setWritable(true, false);
                            try (PrintWriter out = new PrintWriter(file)) {
                                out.println(result.get("content"));
                            }

                        }
                    }
                }
            }

        } catch(Exception e){
            LOGGER.error("Error creating xml files for fatture");
            e.printStackTrace();
            throw e;
        }
        return idExport;
    }

    private Integer createFattureAccompagnatorieXmlFiles(Map<Cliente, List<FatturaAccompagnatoria>> fattureAccompagnatorieByCliente) throws Exception{
        LOGGER.info("Start creating xml files for fatture accompagnatorie...");
        Integer idExport;

        try{
            checkAndCreateDirectory(baseDirectory);

            // retrieve id for the exportation
            idExport = nativeQueryService.getAdeNextId("export");

            LOGGER.info("AdeExport: id={}", idExport);

            // create, if not exists, the destination folder
            String destinationPath = baseDirectory + AdeConstants.FILE_SEPARATOR + idExport;
            Path path = Paths.get(destinationPath);

            checkAndCreateDirectory(destinationPath);

            // create an xml file for every Cliente and Fattura accompagnatoria
            if(fattureAccompagnatorieByCliente != null && !fattureAccompagnatorieByCliente.isEmpty()){
                for(Cliente cliente : fattureAccompagnatorieByCliente.keySet()){
                    // retrieve the list of FatturaAccompagnatoria associate to the current Cliente
                    List<FatturaAccompagnatoria> fattureAccompagnatorie = fattureAccompagnatorieByCliente.get(cliente);

                    if(fattureAccompagnatorie != null && !fattureAccompagnatorie.isEmpty()){
                        for(FatturaAccompagnatoria fatturaAccompagnatoria : fattureAccompagnatorie){

                            Map<String, String> result =  createFatturaAccompagnatoriaXmlFile(fatturaAccompagnatoria);

                            File file = new File(path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + result.get("fileName"));
                            file.setReadable(true, false);
                            file.setWritable(true, false);
                            try (PrintWriter out = new PrintWriter(file)) {
                                out.println(result.get("content"));
                            }

                        }
                    }
                }
            }

        } catch(Exception e){
            LOGGER.error("Error creating xml files for fatture");
            e.printStackTrace();
            throw e;
        }
        return idExport;
    }

    private Integer createNoteAccreditoXmlFiles(Map<Cliente, List<NotaAccredito>> noteAccreditoByCliente) throws Exception{
        LOGGER.info("Start creating xml files for note accredito...");
        Integer idExport;

        try{
            checkAndCreateDirectory(baseDirectory);

            // retrieve id for the exportation
            idExport = nativeQueryService.getAdeNextId("export");

            LOGGER.info("AdeExport: id={}", idExport);

            // create, if not exists, the destination folder
            String destinationPath = baseDirectory + AdeConstants.FILE_SEPARATOR + idExport;
            Path path = Paths.get(destinationPath);

            checkAndCreateDirectory(destinationPath);

            // create an xml file for every Cliente and Fattura accompagnatoria
            if(noteAccreditoByCliente != null && !noteAccreditoByCliente.isEmpty()){
                for(Cliente cliente : noteAccreditoByCliente.keySet()){
                    // retrieve the list of NoteAccredito associate to the current Cliente
                    List<NotaAccredito> noteAccredito = noteAccreditoByCliente.get(cliente);

                    if(noteAccredito != null && !noteAccredito.isEmpty()){
                        for(NotaAccredito notaAccredito : noteAccredito){

                            Map<String, String> result =  createNotaAccreditoXmlFile(notaAccredito);

                            File file = new File(path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + result.get("fileName"));
                            file.setReadable(true, false);
                            file.setWritable(true, false);
                            try (PrintWriter out = new PrintWriter(file)) {
                                out.println(result.get("content"));
                            }

                        }
                    }
                }
            }

        } catch(Exception e){
            LOGGER.error("Error creating xml files for fatture");
            e.printStackTrace();
            throw e;
        }
        return idExport;
    }

    private void createNodeDatiTrasmissione(XMLStreamWriter xmlStreamWriter, Cliente cliente, Integer idProgressivo) throws Exception{
        xmlStreamWriter.writeStartElement("DatiTrasmissione");

        // create node 'IdTrasmittente' 
        xmlStreamWriter.writeStartElement("IdTrasmittente");

        // create node 'IdPaese' 
        xmlStreamWriter.writeStartElement("IdPaese");
        xmlStreamWriter.writeCharacters(AdeConstants.PAESE);
        xmlStreamWriter.writeEndElement();

        // create node 'IdCodice' 
        xmlStreamWriter.writeStartElement("IdCodice");
        xmlStreamWriter.writeCharacters(AdeConstants.CODICE_FISCALE);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'IdTrasmittente' 
        xmlStreamWriter.writeEndElement();

        // create node 'ProgressivoInvio' 
        String numProg_s = String.valueOf(idProgressivo);
        xmlStreamWriter.writeStartElement("ProgressivoInvio");
        xmlStreamWriter.writeCharacters(StringUtils.leftPad(numProg_s, 5, '0'));
        xmlStreamWriter.writeEndElement();

        // create node 'FormatoTrasmissione' 
        xmlStreamWriter.writeStartElement("FormatoTrasmissione");
        xmlStreamWriter.writeCharacters(AdeConstants.FORMATO_TRASMISSIONE);
        xmlStreamWriter.writeEndElement();

        // create node 'CodiceDestinatario' 
        String codiceUnivocoSdi = cliente.getCodiceUnivocoSdi();
        xmlStreamWriter.writeStartElement("CodiceDestinatario");
        if(codiceUnivocoSdi == null || codiceUnivocoSdi.equals("")){
            codiceUnivocoSdi = AdeConstants.DEFAULT_CODICE_DESTINATARIO;
        }
        xmlStreamWriter.writeCharacters(codiceUnivocoSdi);
        xmlStreamWriter.writeEndElement();

        // create node 'ContattiTrasmittente' 
        //xmlStreamWriter.writeStartElement("ContattiTrasmittente");

        // create node 'Telefono' 
        //xmlStreamWriter.writeStartElement("Telefono");
        //xmlStreamWriter.writeCharacters(TELEFONO);
        //xmlStreamWriter.writeEndElement();

        // create node 'Email' 
        //xmlStreamWriter.writeStartElement("Email");
        //xmlStreamWriter.writeCharacters(EMAIL);
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'ContattiTrasmittente' 
        //xmlStreamWriter.writeEndElement();

        // create node 'PECDestinatario' 
        String email = cliente.getEmailPec();
        if(email != null && !email.equals("")){
            xmlStreamWriter.writeStartElement("PECDestinatario");
            xmlStreamWriter.writeCharacters(email);
            xmlStreamWriter.writeEndElement();
        }

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeCedentePrestatore(XMLStreamWriter xmlStreamWriter) throws Exception{
        xmlStreamWriter.writeStartElement("CedentePrestatore");

        // create node 'DatiAnagrafici' 
        xmlStreamWriter.writeStartElement("DatiAnagrafici");

        // create node 'IdFiscaleIVA' 
        xmlStreamWriter.writeStartElement("IdFiscaleIVA");

        // create node 'IdPaese' 
        xmlStreamWriter.writeStartElement("IdPaese");
        xmlStreamWriter.writeCharacters(AdeConstants.PAESE);
        xmlStreamWriter.writeEndElement();

        // create node 'IdCodice' 
        xmlStreamWriter.writeStartElement("IdCodice");
        xmlStreamWriter.writeCharacters(AdeConstants.PARTITA_IVA);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'IdFiscaleIVA' 
        xmlStreamWriter.writeEndElement();

        // create node 'CodiceFiscale' 
        xmlStreamWriter.writeStartElement("CodiceFiscale");
        xmlStreamWriter.writeCharacters(AdeConstants.CODICE_FISCALE);
        xmlStreamWriter.writeEndElement();

        // create node 'Anagrafica' 
        xmlStreamWriter.writeStartElement("Anagrafica");

        // create node 'Denominazione' 
        xmlStreamWriter.writeStartElement("Denominazione");
        xmlStreamWriter.writeCharacters(AdeConstants.RAGIONE_SOCIALE);
        xmlStreamWriter.writeEndElement();

        // create node 'Nome' 
        //xmlStreamWriter.writeStartElement("Nome");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'Cognome' 
        //xmlStreamWriter.writeStartElement("Cognome");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'Titolo' 
        //xmlStreamWriter.writeStartElement("Titolo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'CodEORI' 
        //xmlStreamWriter.writeStartElement("CodEORI");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'Anagrafica' 
        xmlStreamWriter.writeEndElement();

        // create node 'AlboProfessionale' 
        //xmlStreamWriter.writeStartElement("AlboProfessionale");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'ProvinciaAlbo' 
        //xmlStreamWriter.writeStartElement("ProvinciaAlbo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'NumeroIscrizioneAlbo' 
        //xmlStreamWriter.writeStartElement("NumeroIscrizioneAlbo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'DataIscrizioneAlbo' 
        //xmlStreamWriter.writeStartElement("DataIscrizioneAlbo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'RegimeFiscale' 
        xmlStreamWriter.writeStartElement("RegimeFiscale");
        xmlStreamWriter.writeCharacters(AdeConstants.REGIME_FISCALE);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiAnagrafici' 
        xmlStreamWriter.writeEndElement();

        // create node 'Sede' 
        xmlStreamWriter.writeStartElement("Sede");

        // create node 'Indirizzo' 
        xmlStreamWriter.writeStartElement("Indirizzo");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_INDIRIZZO);
        xmlStreamWriter.writeEndElement();

        // create node 'NumeroCivico' 
        xmlStreamWriter.writeStartElement("NumeroCivico");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_NUMERO_CIVICO);
        xmlStreamWriter.writeEndElement();

        // create node 'CAP' 
        xmlStreamWriter.writeStartElement("CAP");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_CAP);
        xmlStreamWriter.writeEndElement();

        // create node 'Comune' 
        xmlStreamWriter.writeStartElement("Comune");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_COMUNE);
        xmlStreamWriter.writeEndElement();

        // create node 'Provincia' 
        xmlStreamWriter.writeStartElement("Provincia");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_PROVINCIA);
        xmlStreamWriter.writeEndElement();

        // create node 'Nazione' 
        xmlStreamWriter.writeStartElement("Nazione");
        xmlStreamWriter.writeCharacters(AdeConstants.SEDE_NAZIONE);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'Sede' 
        xmlStreamWriter.writeEndElement();

        // create node 'Contatti' 
        xmlStreamWriter.writeStartElement("Contatti");

        // create node 'Telefono' 
        xmlStreamWriter.writeStartElement("Telefono");
        xmlStreamWriter.writeCharacters(AdeConstants.TELEFONO);
        xmlStreamWriter.writeEndElement();

        // create node 'Email' 
        xmlStreamWriter.writeStartElement("Email");
        xmlStreamWriter.writeCharacters(AdeConstants.EMAIL);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'Contatti' 
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeCessionarioCommittente(XMLStreamWriter xmlStreamWriter, Cliente cliente) throws Exception{
        xmlStreamWriter.writeStartElement("CessionarioCommittente");

        // create node 'DatiAnagrafici' 
        xmlStreamWriter.writeStartElement("DatiAnagrafici");

        // create node 'IdFiscaleIVA' 
        String partitaIva = cliente.getPartitaIva();
        if(partitaIva != null && !partitaIva.equals("")){
            xmlStreamWriter.writeStartElement("IdFiscaleIVA");

            // create node 'IdPaese' 
            xmlStreamWriter.writeStartElement("IdPaese");
            xmlStreamWriter.writeCharacters(AdeConstants.PAESE);
            xmlStreamWriter.writeEndElement();

            // create node 'IdCodice' 
            xmlStreamWriter.writeStartElement("IdCodice");
            xmlStreamWriter.writeCharacters(cliente.getPartitaIva());
            xmlStreamWriter.writeEndElement();

            // Chiudo il nodo 'IdFiscaleIVA' 
            xmlStreamWriter.writeEndElement();
        }

        // create node 'CodiceFiscale' 
        xmlStreamWriter.writeStartElement("CodiceFiscale");
        xmlStreamWriter.writeCharacters(cliente.getCodiceFiscale());
        xmlStreamWriter.writeEndElement();

        // create node 'Anagrafica' 
        xmlStreamWriter.writeStartElement("Anagrafica");

        // create node 'Denominazione' 
        String ragioneSociale = cliente.getRagioneSociale();
        if(ragioneSociale == null || ragioneSociale.equals("")){
            ragioneSociale = cliente.getRagioneSociale2();
        }
        if(ragioneSociale == null || ragioneSociale.equals("")){
            ragioneSociale = cliente.getNome() + " " + cliente.getCognome();
        }
        xmlStreamWriter.writeStartElement("Denominazione");
        xmlStreamWriter.writeCharacters(ragioneSociale);
        xmlStreamWriter.writeEndElement();

        // create node 'Nome' 
        //xmlStreamWriter.writeStartElement("Nome");
        //xmlStreamWriter.writeCharacters(cliente.getNome());
        //xmlStreamWriter.writeEndElement();

        // create node 'Cognome' 
        //xmlStreamWriter.writeStartElement("Cognome");
        //xmlStreamWriter.writeCharacters(cliente.getCognome());
        //xmlStreamWriter.writeEndElement();

        // create node 'Titolo' 
        //xmlStreamWriter.writeStartElement("Titolo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'CodEORI' 
        //xmlStreamWriter.writeStartElement("CodEORI");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'Anagrafica' 
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiAnagrafici' 
        xmlStreamWriter.writeEndElement();

        // create node 'Sede' 
        xmlStreamWriter.writeStartElement("Sede");

        // create node 'Indirizzo' 
        xmlStreamWriter.writeStartElement("Indirizzo");
        xmlStreamWriter.writeCharacters(cliente.getIndirizzo());
        xmlStreamWriter.writeEndElement();

        // create node 'CAP' 
        xmlStreamWriter.writeStartElement("CAP");
        xmlStreamWriter.writeCharacters(cliente.getCap());
        xmlStreamWriter.writeEndElement();

        // create node 'Comune' 
        xmlStreamWriter.writeStartElement("Comune");
        xmlStreamWriter.writeCharacters(cliente.getCitta());
        xmlStreamWriter.writeEndElement();

        // create node 'Provincia' 
        xmlStreamWriter.writeStartElement("Provincia");
        xmlStreamWriter.writeCharacters(Provincia.getByLabel(cliente.getProvincia()).getSigla());
        xmlStreamWriter.writeEndElement();

        // create node 'Nazione' 
        xmlStreamWriter.writeStartElement("Nazione");
        xmlStreamWriter.writeCharacters(AdeConstants.PAESE);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'Sede' 
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiGenerali(XMLStreamWriter xmlStreamWriter, Fattura fattura, Set<FatturaDdt> fatturaDdts) throws Exception{
        xmlStreamWriter.writeStartElement("DatiGenerali");

        // create node 'DatiGeneraliDocumento' 
        xmlStreamWriter.writeStartElement("DatiGeneraliDocumento");

        // create node 'TipoDocumento' 
        xmlStreamWriter.writeStartElement("TipoDocumento");
        xmlStreamWriter.writeCharacters(AdeConstants.TIPO_DOCUMENTO);
        xmlStreamWriter.writeEndElement();

        // create node 'Divisa' 
        xmlStreamWriter.writeStartElement("Divisa");
        xmlStreamWriter.writeCharacters(AdeConstants.DIVISA);
        xmlStreamWriter.writeEndElement();

        // create node 'Data' 
        Date dataFattura = fattura.getData();
        String dataFattura_s = "";
        if(dataFattura != null){
            dataFattura_s = sdf.format(dataFattura);
        }
        xmlStreamWriter.writeStartElement("Data");
        xmlStreamWriter.writeCharacters(dataFattura_s);
        xmlStreamWriter.writeEndElement();

        // create node 'Numero' 
        Integer numeroProgr = fattura.getProgressivo();
        String numero = "";
        if(numeroProgr != null){
            numero = String.valueOf(numeroProgr);
        }
        xmlStreamWriter.writeStartElement("Numero");
        xmlStreamWriter.writeCharacters(numero);
        xmlStreamWriter.writeEndElement();

        // create node 'DatiRitenuta' 
        //xmlStreamWriter.writeStartElement("DatiRitenuta");

        // create node 'TipoRitenuta' 
        //xmlStreamWriter.writeStartElement("TipoRitenuta");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'ImportoRitenuta' 
        //xmlStreamWriter.writeStartElement("ImportoRitenuta");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'AliquotaRitenuta' 
        //xmlStreamWriter.writeStartElement("AliquotaRitenuta");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'CausalePagamento' 
        //xmlStreamWriter.writeStartElement("CausalePagamento");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiRitenuta' 
        //xmlStreamWriter.writeEndElement();

        // create node 'DatiBollo' 
        //xmlStreamWriter.writeStartElement("DatiBollo");

        // create node 'BolloVirtuale' 
        //xmlStreamWriter.writeStartElement("BolloVirtuale");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'ImportoBollo' 
        //xmlStreamWriter.writeStartElement("ImportoBollo");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiBollo' 
        //xmlStreamWriter.writeEndElement();

        // create node 'DatiCassaPrevidenziale' 
        //xmlStreamWriter.writeStartElement("DatiCassaPrevidenziale");

        // create node 'TipoCassa' 
        //xmlStreamWriter.writeStartElement("TipoCassa");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'AlCassa' 
        //xmlStreamWriter.writeStartElement("AlCassa");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'ImportoContributoCassa' 
        //xmlStreamWriter.writeStartElement("ImportoContributoCassa");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'ImponibileCassa' 
        //xmlStreamWriter.writeStartElement("ImponibileCassa");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'AliquotaIVA' 
        //xmlStreamWriter.writeStartElement("AliquotaIVA");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'Ritenuta' 
        //xmlStreamWriter.writeStartElement("Ritenuta");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'Natura' 
        //xmlStreamWriter.writeStartElement("Natura");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'RiferimentoAmministrazione' 
        //xmlStreamWriter.writeStartElement("RiferimentoAmministrazione");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiCassaPrevidenziale' 
        //xmlStreamWriter.writeEndElement();

        // create node 'ScontoMaggiorazione' 

        // create node 'ImportoTotaleDocumento' 
        BigDecimal totaleFattura = fattura.getTotale();
        String totaleFattura_s;
        if(totaleFattura != null){
            totaleFattura_s = totaleFattura.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
            xmlStreamWriter.writeStartElement("ImportoTotaleDocumento");
            xmlStreamWriter.writeCharacters(totaleFattura_s);
            xmlStreamWriter.writeEndElement();
        }

        // create node 'Arrotondamento' 
        //xmlStreamWriter.writeStartElement("Arrotondamento");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // create node 'Causale' 
        // Se la lunghezza è maggiore di 200 devo creare un nuovo nodo contenente i successivi 200 caratteri 
        String causale = "vendita";
        int casualeLength = causale.length();
        if(casualeLength < 200){
            xmlStreamWriter.writeStartElement("Causale");
            xmlStreamWriter.writeCharacters(causale.toUpperCase());
            xmlStreamWriter.writeEndElement();
        }else{
            String[] tokens = Iterables.toArray(Splitter.fixedLength(4).split(causale), String.class);
            if(tokens != null && tokens.length > 0){
                for (String token : tokens) {
                    xmlStreamWriter.writeStartElement("Causale");
                    xmlStreamWriter.writeCharacters(token.toUpperCase());
                    xmlStreamWriter.writeEndElement();
                }
            }
        }
        //else{
            //xmlStreamWriter.writeStartElement("Causale");
            //xmlStreamWriter.writeCharacters("");
            //xmlStreamWriter.writeEndElement();
        //}

        // create node 'Art73' 
        //xmlStreamWriter.writeStartElement("Art73");
        //xmlStreamWriter.writeCharacters("");
        //xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DatiGeneraliDocumento' 
        xmlStreamWriter.writeEndElement();

//		// create node 'DatiOrdineAcquisto' 
//		xmlStreamWriter.writeStartElement("DatiOrdineAcquisto");
//
//		// create node 'RiferimentoNumeroLinea' 
//		xmlStreamWriter.writeStartElement("RiferimentoNumeroLinea");
//		xmlStreamWriter.writeCharacters("1");
//		xmlStreamWriter.writeEndElement();
//
//		// create node 'IdDocumento' 
//		xmlStreamWriter.writeStartElement("IdDocumento");
//		xmlStreamWriter.writeCharacters("66666");
//		xmlStreamWriter.writeEndElement();
//
//		// create node 'NumItem' 
//		xmlStreamWriter.writeStartElement("NumItem");
//		xmlStreamWriter.writeCharacters("1");
//		xmlStreamWriter.writeEndElement();
//
//		// Chiudo il nodo 'DatiOrdineAcquisto' 
//		xmlStreamWriter.writeEndElement();

        // create node 'DatiContratto' 
        // create node 'DatiConvenzione' 
        // create node 'DatiRicezione' 
        // create node 'DatiFattureCollegate' 
        // create node 'DatiSAL' 

        int numLineaIndex = 1;

        // Creo i nodi 'DatiDDT' 
        if(fatturaDdts != null && !fatturaDdts.isEmpty()){
            for(FatturaDdt fatturaDdt : fatturaDdts){
            
                Ddt ddt = fatturaDdt.getDdt();
                
                xmlStreamWriter.writeStartElement("DatiDDT");

                // create node 'NumeroDDT' 
                xmlStreamWriter.writeStartElement("NumeroDDT");
                Integer numProgr = ddt.getProgressivo();
                String numProgr_s = "";
                if(numProgr != null){
                    numProgr_s = String.valueOf(numProgr);
                }
                xmlStreamWriter.writeCharacters(numProgr_s);
                xmlStreamWriter.writeEndElement();

                // create node 'DataDDT' 
                xmlStreamWriter.writeStartElement("DataDDT");
                Date data = ddt.getData();
                String data_s = "";
                if(data != null){
                    data_s = sdf.format(data);
                }
                xmlStreamWriter.writeCharacters(data_s);
                xmlStreamWriter.writeEndElement();

                // Creo i nodi 'RiferimentoNumeroLinea' 
                Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();
                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    for(int i=0; i<ddtArticoli.size(); i++){
                        xmlStreamWriter.writeStartElement("RiferimentoNumeroLinea");
                        xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                        xmlStreamWriter.writeEndElement();

                        numLineaIndex = numLineaIndex + 1;
                    }

                }

                // Chiudo il nodo 'DatiDDT' 
                xmlStreamWriter.writeEndElement();
            }
        }


        // create node 'DatiTrasporto' 
//		xmlStreamWriter.writeStartElement("DatiTrasporto");
//
//		// create node 'DatiAnagraficiVettore' 
//		xmlStreamWriter.writeStartElement("DatiAnagraficiVettore");
//
//		// create node 'IdFiscaleIVA' 
//		xmlStreamWriter.writeStartElement("IdFiscaleIVA");
//
//		// create node 'IdPaese' 
//		xmlStreamWriter.writeStartElement("IdPaese");
//		xmlStreamWriter.writeCharacters("IT");
//		xmlStreamWriter.writeEndElement();
//
//		// create node 'IdCodice' 
//		xmlStreamWriter.writeStartElement("IdCodice");
//		xmlStreamWriter.writeCharacters("24681012141");
//		xmlStreamWriter.writeEndElement();
//
//		// Chiudo il nodo 'IdFiscaleIVA' 
//		xmlStreamWriter.writeEndElement();
//
//		// create node 'Anagrafica' 
//		xmlStreamWriter.writeStartElement("Anagrafica");
//
//		// create node 'Denominazione' 
//		xmlStreamWriter.writeStartElement("Denominazione");
//		xmlStreamWriter.writeCharacters("Trasporto spa");
//		xmlStreamWriter.writeEndElement();
//
//		// Chiudo il nodo 'Anagrafica' 
//		xmlStreamWriter.writeEndElement();
//
//		// Chiudo il nodo 'DatiAnagraficiVettore' 
//		xmlStreamWriter.writeEndElement();
//
//		// Chiudo il nodo 'DatiTrasporto' 
//		xmlStreamWriter.writeEndElement();

        // create node 'FatturaPrincipale' 

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiGenerali(XMLStreamWriter xmlStreamWriter, FatturaAccompagnatoria fatturaAccompagnatoria, Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli) throws Exception{
        xmlStreamWriter.writeStartElement("DatiGenerali");

        // create node 'DatiGeneraliDocumento'
        xmlStreamWriter.writeStartElement("DatiGeneraliDocumento");

        // create node 'TipoDocumento'
        xmlStreamWriter.writeStartElement("TipoDocumento");
        xmlStreamWriter.writeCharacters(AdeConstants.TIPO_DOCUMENTO);
        xmlStreamWriter.writeEndElement();

        // create node 'Divisa'
        xmlStreamWriter.writeStartElement("Divisa");
        xmlStreamWriter.writeCharacters(AdeConstants.DIVISA);
        xmlStreamWriter.writeEndElement();

        // create node 'Data'
        Date dataFattura = fatturaAccompagnatoria.getData();
        String dataFattura_s = "";
        if(dataFattura != null){
            dataFattura_s = sdf.format(dataFattura);
        }
        xmlStreamWriter.writeStartElement("Data");
        xmlStreamWriter.writeCharacters(dataFattura_s);
        xmlStreamWriter.writeEndElement();

        // create node 'Numero'
        Integer numeroProgr = fatturaAccompagnatoria.getProgressivo();
        String numero = "";
        if(numeroProgr != null){
            numero = String.valueOf(numeroProgr);
        }
        xmlStreamWriter.writeStartElement("Numero");
        xmlStreamWriter.writeCharacters(numero);
        xmlStreamWriter.writeEndElement();

        // create node 'ImportoTotaleDocumento'
        BigDecimal totaleFattura = fatturaAccompagnatoria.getTotale();
        String totaleFattura_s;
        if(totaleFattura != null){
            totaleFattura_s = totaleFattura.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
            xmlStreamWriter.writeStartElement("ImportoTotaleDocumento");
            xmlStreamWriter.writeCharacters(totaleFattura_s);
            xmlStreamWriter.writeEndElement();
        }

        // create node 'Causale'
        // Se la lunghezza è maggiore di 200 devo creare un nuovo nodo contenente i successivi 200 caratteri
        String causale = "vendita";
        int casualeLength = causale.length();
        if(casualeLength < 200){
            xmlStreamWriter.writeStartElement("Causale");
            xmlStreamWriter.writeCharacters(causale.toUpperCase());
            xmlStreamWriter.writeEndElement();
        }else{
            String[] tokens = Iterables.toArray(Splitter.fixedLength(4).split(causale), String.class);
            if(tokens != null && tokens.length > 0){
                for (String token : tokens) {
                    xmlStreamWriter.writeStartElement("Causale");
                    xmlStreamWriter.writeCharacters(token.toUpperCase());
                    xmlStreamWriter.writeEndElement();
                }
            }
        }

        // Chiudo il nodo 'DatiGeneraliDocumento'
        xmlStreamWriter.writeEndElement();

        int numLineaIndex = 1;

        xmlStreamWriter.writeStartElement("DatiDDT");

        // create node 'NumeroDDT'
        xmlStreamWriter.writeStartElement("NumeroDDT");
        Integer numProgr = fatturaAccompagnatoria.getProgressivo();
        String numProgr_s = "";
        if(numProgr != null){
            numProgr_s = String.valueOf(numProgr);
        }
        xmlStreamWriter.writeCharacters(numProgr_s);
        xmlStreamWriter.writeEndElement();

        // create node 'DataDDT'
        xmlStreamWriter.writeStartElement("DataDDT");
        Date data = fatturaAccompagnatoria.getData();
        String data_s = "";
        if(data != null){
            data_s = sdf.format(data);
        }
        xmlStreamWriter.writeCharacters(data_s);
        xmlStreamWriter.writeEndElement();

        // Creo i nodi 'RiferimentoNumeroLinea'
        if(fatturaAccompagnatoriaArticoli != null && !fatturaAccompagnatoriaArticoli.isEmpty()){
            for(int i=0; i<fatturaAccompagnatoriaArticoli.size(); i++){
                xmlStreamWriter.writeStartElement("RiferimentoNumeroLinea");
                xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                xmlStreamWriter.writeEndElement();

                numLineaIndex = numLineaIndex + 1;
            }
        }

        // Chiudo il nodo 'DatiDDT'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiGenerali(XMLStreamWriter xmlStreamWriter, NotaAccredito notaAccredito, Set<NotaAccreditoRiga> notaAccreditoRighe) throws Exception{
        xmlStreamWriter.writeStartElement("DatiGenerali");

        // create node 'DatiGeneraliDocumento'
        xmlStreamWriter.writeStartElement("DatiGeneraliDocumento");

        // create node 'TipoDocumento'
        xmlStreamWriter.writeStartElement("TipoDocumento");
        xmlStreamWriter.writeCharacters(AdeConstants.TIPO_DOCUMENTO_NOTA_ACCREDITO);
        xmlStreamWriter.writeEndElement();

        // create node 'Divisa'
        xmlStreamWriter.writeStartElement("Divisa");
        xmlStreamWriter.writeCharacters(AdeConstants.DIVISA);
        xmlStreamWriter.writeEndElement();

        // create node 'Data'
        Date dataFattura = notaAccredito.getData();
        String dataFattura_s = "";
        if(dataFattura != null){
            dataFattura_s = sdf.format(dataFattura);
        }
        xmlStreamWriter.writeStartElement("Data");
        xmlStreamWriter.writeCharacters(dataFattura_s);
        xmlStreamWriter.writeEndElement();

        // create node 'Numero'
        Integer numeroProgr = notaAccredito.getProgressivo();
        String numero = "";
        if(numeroProgr != null){
            numero = String.valueOf(numeroProgr);
        }
        xmlStreamWriter.writeStartElement("Numero");
        xmlStreamWriter.writeCharacters(numero);
        xmlStreamWriter.writeEndElement();

        // create node 'ImportoTotaleDocumento'
        BigDecimal totaleNotaAccredito = notaAccredito.getTotale();
        String totaleNotaAccredito_s;
        if(totaleNotaAccredito != null){
            totaleNotaAccredito_s = totaleNotaAccredito.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
            xmlStreamWriter.writeStartElement("ImportoTotaleDocumento");
            xmlStreamWriter.writeCharacters(totaleNotaAccredito_s);
            xmlStreamWriter.writeEndElement();
        }

        // create node 'Causale'
        // Se la lunghezza è maggiore di 200 devo creare un nuovo nodo contenente i successivi 200 caratteri
        String causale = notaAccredito.getCausale() != null ? notaAccredito.getCausale().getDescrizione() : "vendita";
        if(causale != null && !causale.isEmpty()){
            int casualeLength = causale.length();
            if(casualeLength < 200){
                xmlStreamWriter.writeStartElement("Causale");
                xmlStreamWriter.writeCharacters(causale.toUpperCase());
                xmlStreamWriter.writeEndElement();
            }else{
                String[] tokens = Iterables.toArray(Splitter.fixedLength(4).split(causale), String.class);
                if(tokens != null && tokens.length > 0){
                    for (String token : tokens) {
                        xmlStreamWriter.writeStartElement("Causale");
                        xmlStreamWriter.writeCharacters(token.toUpperCase());
                        xmlStreamWriter.writeEndElement();
                    }
                }
            }
        }

        // Chiudo il nodo 'DatiGeneraliDocumento'
        xmlStreamWriter.writeEndElement();

        int numLineaIndex = 1;

        xmlStreamWriter.writeStartElement("DatiDDT");

        // create node 'NumeroDDT'
        xmlStreamWriter.writeStartElement("NumeroDDT");
        Integer numProgr = notaAccredito.getProgressivo();
        String numProgr_s = "";
        if(numProgr != null){
            numProgr_s = String.valueOf(numProgr);
        }
        xmlStreamWriter.writeCharacters(numProgr_s);
        xmlStreamWriter.writeEndElement();

        // create node 'DataDDT'
        xmlStreamWriter.writeStartElement("DataDDT");
        Date data = notaAccredito.getData();
        String data_s = "";
        if(data != null){
            data_s = sdf.format(data);
        }
        xmlStreamWriter.writeCharacters(data_s);
        xmlStreamWriter.writeEndElement();

        // Creo i nodi 'RiferimentoNumeroLinea'
        if(notaAccreditoRighe != null && !notaAccreditoRighe.isEmpty()){
            for(int i=0; i<notaAccreditoRighe.size(); i++){
                xmlStreamWriter.writeStartElement("RiferimentoNumeroLinea");
                xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                xmlStreamWriter.writeEndElement();

                numLineaIndex = numLineaIndex + 1;
            }
        }

        // Chiudo il nodo 'DatiDDT'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiBeniServizi(XMLStreamWriter xmlStreamWriter, Fattura fattura, Set<FatturaDdt> fatturaDdts) throws Exception{
        xmlStreamWriter.writeStartElement("DatiBeniServizi");

        int numLineaIndex = 1;

        // Creo i nodi 'DettaglioLinee' 
        if(fatturaDdts != null && !fatturaDdts.isEmpty()){
            for(FatturaDdt fatturaDdt : fatturaDdts){
                Ddt ddt = fatturaDdt.getDdt();
                Set<DdtArticolo> ddtArticoli = ddt.getDdtArticoli();

                if(ddtArticoli != null && !ddtArticoli.isEmpty()){
                    for(DdtArticolo ddtArticolo : ddtArticoli){
                        Articolo articolo = ddtArticolo.getArticolo();
                        UnitaMisura unitaMisura = null;
                        AliquotaIva aliquotaIva = null;
                        if(articolo != null){
                            unitaMisura = articolo.getUnitaMisura();
                            aliquotaIva = articolo.getAliquotaIva();
                        }

                        // create node 'DettaglioLinee' 
                        xmlStreamWriter.writeStartElement("DettaglioLinee");

                        // create node 'NumeroLinea' 
                        xmlStreamWriter.writeStartElement("NumeroLinea");
                        xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                        xmlStreamWriter.writeEndElement();

                        // create node 'Descrizione' 
                        xmlStreamWriter.writeStartElement("Descrizione");
                        xmlStreamWriter.writeCharacters(articolo != null ? articolo.getDescrizione() : "");
                        xmlStreamWriter.writeEndElement();

                        // create node 'Quantita' 
                        xmlStreamWriter.writeStartElement("Quantita");
                        Float quantita = ddtArticolo.getQuantita();
                        String quantita_s = "";
                        if(quantita != null){
                            quantita_s = quantita.toString();
                        }
                        if(quantita_s.endsWith(".0")){
                            quantita_s += "00";
                        }
                        xmlStreamWriter.writeCharacters(quantita_s);
                        xmlStreamWriter.writeEndElement();

                        // create node 'UnitaMisura' 
                        xmlStreamWriter.writeStartElement("UnitaMisura");
                        xmlStreamWriter.writeCharacters(unitaMisura != null ? unitaMisura.getEtichetta() : "");
                        xmlStreamWriter.writeEndElement();

                        // create node 'PrezzoUnitario' 
                        xmlStreamWriter.writeStartElement("PrezzoUnitario");
                        BigDecimal prezzo = ddtArticolo.getPrezzo();
                        String prezzo_s = "";
                        if(prezzo != null){
                            prezzo_s = prezzo.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        xmlStreamWriter.writeCharacters(prezzo_s);
                        xmlStreamWriter.writeEndElement();

                        // create node 'Sconto' 
                        BigDecimal sconto = ddtArticolo.getSconto();
                        String sconto_s = "";
                        if(sconto != null && sconto.compareTo(BigDecimal.ZERO) != 0){
                            sconto_s = sconto.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        if(!sconto_s.equals("")){
                            xmlStreamWriter.writeStartElement("ScontoMaggiorazione");

                            // create node 'Tipo' 
                            xmlStreamWriter.writeStartElement("Tipo");
                            xmlStreamWriter.writeCharacters("SC");
                            xmlStreamWriter.writeEndElement();

                            // create node 'Percentuale' 
                            xmlStreamWriter.writeStartElement("Percentuale");
                            xmlStreamWriter.writeCharacters(sconto_s);
                            xmlStreamWriter.writeEndElement();

                            // create node 'Importo' 
//								xmlStreamWriter.writeStartElement("Importo");
//								xmlStreamWriter.writeCharacters(sconto_s);
//								xmlStreamWriter.writeEndElement();

                            // Chiudo il nodo 'ScontoMaggiorazione' 
                            xmlStreamWriter.writeEndElement();
                        }

                        // create node 'PrezzoTotale' 
                        xmlStreamWriter.writeStartElement("PrezzoTotale");
                        BigDecimal prezzoTemp = ddtArticolo.getPrezzo().setScale(2, RoundingMode.HALF_DOWN);
                        if(sconto != null && sconto.compareTo(BigDecimal.ZERO) != 0){
                            BigDecimal scontoValue = prezzoTemp.multiply(sconto.divide(new BigDecimal(100)));
                            prezzoTemp = prezzoTemp.subtract(scontoValue);
                        }
                        BigDecimal prezzoTotale = prezzoTemp.multiply(BigDecimal.valueOf(ddtArticolo.getQuantita()));
                        String prezzoTotale_s = "";
                        if(prezzoTotale != null){
                            prezzoTotale_s = prezzoTotale.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        xmlStreamWriter.writeCharacters(prezzoTotale_s);
                        xmlStreamWriter.writeEndElement();

                        // create node 'AliquotaIVA' 
                        xmlStreamWriter.writeStartElement("AliquotaIVA");
                        Integer iva = aliquotaIva != null ? aliquotaIva.getValore().intValue() : null;
                        String iva_s = "";
                        if(iva != null){
                            iva_s = iva + ".00";
                        }
                        xmlStreamWriter.writeCharacters(iva_s);
                        xmlStreamWriter.writeEndElement();

                        // Chiudo il nodo 'DettaglioLinee' 
                        xmlStreamWriter.writeEndElement();

                        numLineaIndex = numLineaIndex + 1;
                    }

                }
            }
        }

        // Creo i nodi 'DatiRiepilogo' 
        Map<AliquotaIva, BigDecimal> totaliImponibiliByIva = stampaService.createFatturaTotaliImponibiliByIva(fattura);
        if(totaliImponibiliByIva != null && !totaliImponibiliByIva.isEmpty()){
            for(Map.Entry<AliquotaIva, BigDecimal> totaleImponibileByIva : totaliImponibiliByIva.entrySet()){
                BigDecimal iva = totaleImponibileByIva.getKey().getValore();
                BigDecimal imponibile = totaleImponibileByIva.getValue();
                BigDecimal imposta = null;

                // create node 'DatiRiepilogo' 
                xmlStreamWriter.writeStartElement("DatiRiepilogo");

                // create node 'AliquotaIVA' 
                xmlStreamWriter.writeStartElement("AliquotaIVA");
                String iva_s = "";
                if(iva != null){
                    iva_s = iva.toString();
                    if(!iva_s.endsWith(".00")){
                        iva_s += ".00";
                    }
                }
                xmlStreamWriter.writeCharacters(iva_s);
                xmlStreamWriter.writeEndElement();

                // if IVA=0 create node 'Natura'
                if(iva != null && iva.compareTo(BigDecimal.ZERO) == 0){
                    xmlStreamWriter.writeStartElement("Natura");
                    xmlStreamWriter.writeCharacters(AdeConstants.NATURA_IVA_ZERO);
                    xmlStreamWriter.writeEndElement();
                }

                // create node 'ImponibileImporto' 
                xmlStreamWriter.writeStartElement("ImponibileImporto");
                String imp_s = "";
                if(imponibile != null){

                    // Calcolo l'imposta 
                    imposta = imponibile.multiply(iva.divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);

                    if(imponibile != null){
                        imp_s = imponibile.toString();
                        if(imp_s.endsWith("0")){
                            imp_s = StringUtils.substringBeforeLast(imp_s, "0");
                        }
                    }
                }
                xmlStreamWriter.writeCharacters(imp_s);
                xmlStreamWriter.writeEndElement();

                // create node 'Imposta' 
                xmlStreamWriter.writeStartElement("Imposta");
                String imposta_s = "";
                if(imposta != null){
                    imposta_s = imposta.toString();
                }
                xmlStreamWriter.writeCharacters(imposta_s);
                xmlStreamWriter.writeEndElement();

                // close node 'DatiRiepilogo'
                xmlStreamWriter.writeEndElement();
            }
        }

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiBeniServizi(XMLStreamWriter xmlStreamWriter, Set<FatturaAccompagnatoriaArticolo> fatturaAccompagnatoriaArticoli, Set<FatturaAccompagnatoriaTotale> fatturaAccompagnatoriaTotali) throws Exception{
        xmlStreamWriter.writeStartElement("DatiBeniServizi");

        int numLineaIndex = 1;

        // Creo i nodi 'DettaglioLinee'
        if(fatturaAccompagnatoriaArticoli != null && !fatturaAccompagnatoriaArticoli.isEmpty()){
            for(FatturaAccompagnatoriaArticolo fatturaAccompagnatoriaArticolo : fatturaAccompagnatoriaArticoli){

                Articolo articolo = fatturaAccompagnatoriaArticolo.getArticolo();
                UnitaMisura unitaMisura = null;
                AliquotaIva aliquotaIva = null;
                if(articolo != null){
                    unitaMisura = articolo.getUnitaMisura();
                    aliquotaIva = articolo.getAliquotaIva();
                }

                // create node 'DettaglioLinee'
                xmlStreamWriter.writeStartElement("DettaglioLinee");

                // create node 'NumeroLinea'
                xmlStreamWriter.writeStartElement("NumeroLinea");
                xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                xmlStreamWriter.writeEndElement();

                // create node 'Descrizione'
                xmlStreamWriter.writeStartElement("Descrizione");
                xmlStreamWriter.writeCharacters(articolo != null ? articolo.getDescrizione() : "");
                xmlStreamWriter.writeEndElement();

                // create node 'Quantita'
                xmlStreamWriter.writeStartElement("Quantita");
                Float quantita = fatturaAccompagnatoriaArticolo.getQuantita();
                String quantita_s = "";
                if(quantita != null){
                    quantita_s = quantita.toString();
                }
                xmlStreamWriter.writeCharacters(quantita_s);
                xmlStreamWriter.writeEndElement();

                // create node 'UnitaMisura'
                xmlStreamWriter.writeStartElement("UnitaMisura");
                xmlStreamWriter.writeCharacters(unitaMisura != null ? unitaMisura.getEtichetta() : "");
                xmlStreamWriter.writeEndElement();

                // create node 'PrezzoUnitario'
                xmlStreamWriter.writeStartElement("PrezzoUnitario");
                BigDecimal prezzo = fatturaAccompagnatoriaArticolo.getPrezzo();
                String prezzo_s = "";
                if(prezzo != null){
                    prezzo_s = prezzo.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                xmlStreamWriter.writeCharacters(prezzo_s);
                xmlStreamWriter.writeEndElement();

                // create node 'Sconto'
                BigDecimal sconto = fatturaAccompagnatoriaArticolo.getSconto();
                String sconto_s = "";
                if(sconto != null && sconto.compareTo(BigDecimal.ZERO) != 0){
                    sconto_s = sconto.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                if(!sconto_s.equals("")){
                    xmlStreamWriter.writeStartElement("ScontoMaggiorazione");

                    // create node 'Tipo'
                    xmlStreamWriter.writeStartElement("Tipo");
                    xmlStreamWriter.writeCharacters("SC");
                    xmlStreamWriter.writeEndElement();

                    // create node 'Percentuale'
                    xmlStreamWriter.writeStartElement("Percentuale");
                    xmlStreamWriter.writeCharacters(sconto_s);
                    xmlStreamWriter.writeEndElement();

                    // create node 'Importo'
//								xmlStreamWriter.writeStartElement("Importo");
//								xmlStreamWriter.writeCharacters(sconto_s);
//								xmlStreamWriter.writeEndElement();

                    // Chiudo il nodo 'ScontoMaggiorazione'
                    xmlStreamWriter.writeEndElement();
                }

                // create node 'PrezzoTotale'
                xmlStreamWriter.writeStartElement("PrezzoTotale");
                BigDecimal prezzoTotale = fatturaAccompagnatoriaArticolo.getTotale();
                String prezzoTotale_s = "";
                if(prezzoTotale != null){
                    prezzoTotale_s = prezzoTotale.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                xmlStreamWriter.writeCharacters(prezzoTotale_s);
                xmlStreamWriter.writeEndElement();

                // create node 'AliquotaIVA'
                xmlStreamWriter.writeStartElement("AliquotaIVA");
                Integer iva = aliquotaIva != null ? aliquotaIva.getValore().intValue() : null;
                String iva_s = "";
                if(iva != null){
                    iva_s = iva + ".00";
                }
                xmlStreamWriter.writeCharacters(iva_s);
                xmlStreamWriter.writeEndElement();

                // Chiudo il nodo 'DettaglioLinee'
                xmlStreamWriter.writeEndElement();

                numLineaIndex = numLineaIndex + 1;
            }

        }

        // Creo i nodi 'DatiRiepilogo'
        if(fatturaAccompagnatoriaTotali != null && !fatturaAccompagnatoriaTotali.isEmpty()){
            for(FatturaAccompagnatoriaTotale fatturaAccompagnatoriaTotale : fatturaAccompagnatoriaTotali){
                BigDecimal iva = fatturaAccompagnatoriaTotale.getAliquotaIva().getValore();
                BigDecimal imponibile = fatturaAccompagnatoriaTotale.getTotaleImponibile();
                BigDecimal imposta = fatturaAccompagnatoriaTotale.getTotaleIva();

                // create node 'DatiRiepilogo'
                xmlStreamWriter.writeStartElement("DatiRiepilogo");

                // create node 'AliquotaIVA'
                xmlStreamWriter.writeStartElement("AliquotaIVA");
                String iva_s = "";
                if(iva != null){
                    iva_s = iva.toString();
                    if(!iva_s.endsWith(".00")){
                        iva_s += ".00";
                    }
                }
                xmlStreamWriter.writeCharacters(iva_s);
                xmlStreamWriter.writeEndElement();

                // create node 'ImponibileImporto'
                xmlStreamWriter.writeStartElement("ImponibileImporto");
                String imp_s = "";
                if(imponibile != null){
                    imp_s = imponibile.toString();
                }
                xmlStreamWriter.writeCharacters(imp_s);
                xmlStreamWriter.writeEndElement();

                // create node 'Imposta'
                xmlStreamWriter.writeStartElement("Imposta");
                String imposta_s = "";
                if(imposta != null){
                    imposta_s = imposta.toString();
                }
                xmlStreamWriter.writeCharacters(imposta_s);
                xmlStreamWriter.writeEndElement();

                // close node 'DatiRiepilogo'
                xmlStreamWriter.writeEndElement();

            }
        }

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiBeniServizi(XMLStreamWriter xmlStreamWriter, NotaAccredito notaAccredito, Set<NotaAccreditoRiga> notaAccreditoRighe, Set<NotaAccreditoTotale> notaAccreditoTotali) throws Exception{
        xmlStreamWriter.writeStartElement("DatiBeniServizi");

        int numLineaIndex = 1;

        // Creo i nodi 'DettaglioLinee'
        if(notaAccreditoRighe != null && !notaAccreditoRighe.isEmpty()){
            for(NotaAccreditoRiga notaAccreditoRiga : notaAccreditoRighe){

                Articolo articolo = notaAccreditoRiga.getArticolo();
                UnitaMisura unitaMisura = null;
                if(articolo != null){
                    unitaMisura = articolo.getUnitaMisura();
                }
                AliquotaIva aliquotaIva = notaAccreditoRiga.getAliquotaIva();

                // create node 'DettaglioLinee'
                xmlStreamWriter.writeStartElement("DettaglioLinee");

                // create node 'NumeroLinea'
                xmlStreamWriter.writeStartElement("NumeroLinea");
                xmlStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
                xmlStreamWriter.writeEndElement();

                // create node 'Descrizione'
                xmlStreamWriter.writeStartElement("Descrizione");
                xmlStreamWriter.writeCharacters(notaAccreditoRiga.getDescrizione());
                xmlStreamWriter.writeEndElement();

                // create node 'Quantita'
                xmlStreamWriter.writeStartElement("Quantita");
                Float quantita = notaAccreditoRiga.getQuantita();
                String quantita_s = "";
                if(quantita != null){
                    quantita_s = quantita.toString();
                }
                if(quantita_s.endsWith(".0")){
                    quantita_s += "0";
                }
                xmlStreamWriter.writeCharacters(quantita_s);
                xmlStreamWriter.writeEndElement();

                // create node 'UnitaMisura'
                xmlStreamWriter.writeStartElement("UnitaMisura");
                xmlStreamWriter.writeCharacters(unitaMisura != null ? unitaMisura.getEtichetta() : "");
                xmlStreamWriter.writeEndElement();

                // create node 'PrezzoUnitario'
                xmlStreamWriter.writeStartElement("PrezzoUnitario");
                BigDecimal prezzo = notaAccreditoRiga.getPrezzo();
                String prezzo_s = "";
                if(prezzo != null){
                    prezzo_s = prezzo.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                xmlStreamWriter.writeCharacters(prezzo_s);
                xmlStreamWriter.writeEndElement();

                // create node 'Sconto'
                BigDecimal sconto = notaAccreditoRiga.getSconto();
                String sconto_s = "";
                if(sconto != null && sconto.compareTo(BigDecimal.ZERO) != 0){
                    sconto_s = sconto.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                if(!sconto_s.equals("")){
                    xmlStreamWriter.writeStartElement("ScontoMaggiorazione");

                    // create node 'Tipo'
                    xmlStreamWriter.writeStartElement("Tipo");
                    xmlStreamWriter.writeCharacters("SC");
                    xmlStreamWriter.writeEndElement();

                    // create node 'Percentuale'
                    xmlStreamWriter.writeStartElement("Percentuale");
                    xmlStreamWriter.writeCharacters(sconto_s);
                    xmlStreamWriter.writeEndElement();

                    // create node 'Importo'
//								xmlStreamWriter.writeStartElement("Importo");
//								xmlStreamWriter.writeCharacters(sconto_s);
//								xmlStreamWriter.writeEndElement();

                    // Chiudo il nodo 'ScontoMaggiorazione'
                    xmlStreamWriter.writeEndElement();
                }

                // create node 'PrezzoTotale'
                xmlStreamWriter.writeStartElement("PrezzoTotale");
                BigDecimal prezzoTotale = notaAccreditoRiga.getTotale();
                String prezzoTotale_s = "";
                if(prezzoTotale != null){
                    prezzoTotale_s = prezzoTotale.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
                }
                xmlStreamWriter.writeCharacters(prezzoTotale_s);
                xmlStreamWriter.writeEndElement();

                // create node 'AliquotaIVA'
                xmlStreamWriter.writeStartElement("AliquotaIVA");
                Integer iva = aliquotaIva != null ? aliquotaIva.getValore().intValue() : null;
                String iva_s = "";
                if(iva != null){
                    iva_s = iva + ".00";
                }
                xmlStreamWriter.writeCharacters(iva_s);
                xmlStreamWriter.writeEndElement();

                // Chiudo il nodo 'DettaglioLinee'
                xmlStreamWriter.writeEndElement();

                numLineaIndex = numLineaIndex + 1;
            }

        }

        // Creo i nodi 'DatiRiepilogo'
        if(notaAccreditoTotali != null && !notaAccreditoTotali.isEmpty()){
            for(NotaAccreditoTotale notaAccreditoTotale : notaAccreditoTotali){
                BigDecimal iva = notaAccreditoTotale.getAliquotaIva().getValore();
                BigDecimal imponibile = notaAccreditoTotale.getTotaleImponibile();
                BigDecimal imposta = notaAccreditoTotale.getTotaleIva();

                // create node 'DatiRiepilogo'
                xmlStreamWriter.writeStartElement("DatiRiepilogo");

                // create node 'AliquotaIVA'
                xmlStreamWriter.writeStartElement("AliquotaIVA");
                String iva_s = "";
                if(iva != null){
                    iva_s = iva.toString();
                    if(!iva_s.endsWith(".00")){
                        iva_s += ".00";
                    }
                }
                xmlStreamWriter.writeCharacters(iva_s);
                xmlStreamWriter.writeEndElement();

                // if IVA=0 create node 'Natura'
                if(iva != null && iva.compareTo(BigDecimal.ZERO) == 0){
                    xmlStreamWriter.writeStartElement("Natura");
                    xmlStreamWriter.writeCharacters(AdeConstants.NATURA_IVA_ZERO);
                    xmlStreamWriter.writeEndElement();
                }

                // create node 'ImponibileImporto'
                xmlStreamWriter.writeStartElement("ImponibileImporto");
                String imp_s = "";
                if(imponibile != null){
                    imp_s = imponibile.toString();
                }
                xmlStreamWriter.writeCharacters(imp_s);
                xmlStreamWriter.writeEndElement();

                // create node 'Imposta'
                xmlStreamWriter.writeStartElement("Imposta");
                String imposta_s = "";
                if(imposta != null){
                    imposta_s = imposta.toString();
                }
                xmlStreamWriter.writeCharacters(imposta_s);
                xmlStreamWriter.writeEndElement();

                // close node 'DatiRiepilogo'
                xmlStreamWriter.writeEndElement();

            }
        }

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiPagamento(XMLStreamWriter xmlStreamWriter, Fattura fattura) throws Exception{
        xmlStreamWriter.writeStartElement("DatiPagamento");

        // create node 'CondizioniPagamento' 
        xmlStreamWriter.writeStartElement("CondizioniPagamento");
        xmlStreamWriter.writeCharacters(AdeConstants.COND_PAGAMENTO_COMPLETO);
        xmlStreamWriter.writeEndElement();

        // create node 'DettaglioPagamento' 
        xmlStreamWriter.writeStartElement("DettaglioPagamento");

        // create node 'ModalitaPagamento' 
        String modalitaPagamento = "";
        Cliente cliente = fattura.getCliente();
        if(cliente != null){
            TipoPagamento tipoPagamento = cliente.getTipoPagamento();
            String descrizionePagamento = tipoPagamento.getDescrizione();
            if(descrizionePagamento != null && !descrizionePagamento.equals("")){
                if(descrizionePagamento.toLowerCase().contains("contanti")){
                    modalitaPagamento = "MP01";
                } else if(descrizionePagamento.toLowerCase().contains("bonifico")){
                    modalitaPagamento = "MP05";
                } else if(descrizionePagamento.toLowerCase().contains("ricevuta bancaria")){
                    modalitaPagamento = "MP12";
                }
            }
        }
        xmlStreamWriter.writeStartElement("ModalitaPagamento");
        xmlStreamWriter.writeCharacters(modalitaPagamento);
        xmlStreamWriter.writeEndElement();

        // create node 'ImportoPagamento' 
        BigDecimal totaleFattura = fattura.getTotale();
        String totaleFattura_s = "";
        if(totaleFattura != null){
            totaleFattura_s = totaleFattura.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
        }
        xmlStreamWriter.writeStartElement("ImportoPagamento");
        xmlStreamWriter.writeCharacters(totaleFattura_s);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DettaglioPagamento' 
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiPagamento(XMLStreamWriter xmlStreamWriter, FatturaAccompagnatoria fatturaAccompagnatoria) throws Exception{
        xmlStreamWriter.writeStartElement("DatiPagamento");

        // create node 'CondizioniPagamento'
        xmlStreamWriter.writeStartElement("CondizioniPagamento");
        xmlStreamWriter.writeCharacters(AdeConstants.COND_PAGAMENTO_COMPLETO);
        xmlStreamWriter.writeEndElement();

        // create node 'DettaglioPagamento'
        xmlStreamWriter.writeStartElement("DettaglioPagamento");

        // create node 'ModalitaPagamento'
        String modalitaPagamento = "";
        Cliente cliente = fatturaAccompagnatoria.getCliente();
        if(cliente != null){
            TipoPagamento tipoPagamento = cliente.getTipoPagamento();
            String descrizionePagamento = tipoPagamento.getDescrizione();
            if(descrizionePagamento != null && !descrizionePagamento.equals("")){
                if(descrizionePagamento.toLowerCase().contains("contanti")){
                    modalitaPagamento = "MP01";
                } else if(descrizionePagamento.toLowerCase().contains("bonifico")){
                    modalitaPagamento = "MP05";
                } else if(descrizionePagamento.toLowerCase().contains("ricevuta bancaria")){
                    modalitaPagamento = "MP12";
                }
            }
        }
        xmlStreamWriter.writeStartElement("ModalitaPagamento");
        xmlStreamWriter.writeCharacters(modalitaPagamento);
        xmlStreamWriter.writeEndElement();

        // create node 'ImportoPagamento'
        BigDecimal totaleFattura = fatturaAccompagnatoria.getTotale();
        String totaleFattura_s = "";
        if(totaleFattura != null){
            totaleFattura_s = totaleFattura.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
        }
        xmlStreamWriter.writeStartElement("ImportoPagamento");
        xmlStreamWriter.writeCharacters(totaleFattura_s);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DettaglioPagamento'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyDatiPagamento(XMLStreamWriter xmlStreamWriter, NotaAccredito notaAccredito) throws Exception{
        xmlStreamWriter.writeStartElement("DatiPagamento");

        // create node 'CondizioniPagamento'
        xmlStreamWriter.writeStartElement("CondizioniPagamento");
        xmlStreamWriter.writeCharacters(AdeConstants.COND_PAGAMENTO_COMPLETO);
        xmlStreamWriter.writeEndElement();

        // create node 'DettaglioPagamento'
        xmlStreamWriter.writeStartElement("DettaglioPagamento");

        // create node 'ModalitaPagamento'
        String modalitaPagamento = "";
        Cliente cliente = notaAccredito.getCliente();
        if(cliente != null){
            TipoPagamento tipoPagamento = cliente.getTipoPagamento();
            String descrizionePagamento = tipoPagamento.getDescrizione();
            if(descrizionePagamento != null && !descrizionePagamento.equals("")){
                if(descrizionePagamento.toLowerCase().contains("contanti")){
                    modalitaPagamento = "MP01";
                } else if(descrizionePagamento.toLowerCase().contains("bonifico")){
                    modalitaPagamento = "MP05";
                } else if(descrizionePagamento.toLowerCase().contains("ricevuta bancaria")){
                    modalitaPagamento = "MP12";
                }
            }
        }
        xmlStreamWriter.writeStartElement("ModalitaPagamento");
        xmlStreamWriter.writeCharacters(modalitaPagamento);
        xmlStreamWriter.writeEndElement();

        // create node 'ImportoPagamento'
        BigDecimal totaleFattura = notaAccredito.getTotale();
        String totaleFattura_s = "";
        if(totaleFattura != null){
            totaleFattura_s = totaleFattura.setScale(2, RoundingMode.HALF_DOWN).toPlainString();
        }
        xmlStreamWriter.writeStartElement("ImportoPagamento");
        xmlStreamWriter.writeCharacters(totaleFattura_s);
        xmlStreamWriter.writeEndElement();

        // Chiudo il nodo 'DettaglioPagamento'
        xmlStreamWriter.writeEndElement();

        xmlStreamWriter.writeEndElement();
    }

    private void createNodeBodyAllegati(XMLStreamWriter xmlStreamWriter, Fattura fattura) {

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos)){
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yy");

            // create attachment name
            String nomeAttachment = "fattura_"+fattura.getProgressivo() + "/" + sdf.format(fattura.getData())+".pdf";

            // create fattura pdf
            byte[] pdfBytes = stampaService.generateFattura(fattura.getId());

            // create zip containing the pdf
            ZipEntry entry = new ZipEntry(nomeAttachment);
            zos.putNextEntry(entry);
            zos.write(pdfBytes);
            zos.closeEntry();

            // encode pdf file in Base64
            String encodedPdf = new String(Base64.getEncoder().encode(baos.toByteArray()));

            xmlStreamWriter.writeStartElement("Allegati");

            // create node 'NomeAttachment' 
            xmlStreamWriter.writeStartElement("NomeAttachment");
            xmlStreamWriter.writeCharacters(nomeAttachment + ".zip");
            xmlStreamWriter.writeEndElement();

            // create node 'AlgoritmoCompressione' 
            xmlStreamWriter.writeStartElement("AlgoritmoCompressione");
            xmlStreamWriter.writeCharacters("ZIP");
            xmlStreamWriter.writeEndElement();

            // create node 'FormatoAttachment' 
            xmlStreamWriter.writeStartElement("FormatoAttachment");
            xmlStreamWriter.writeCharacters("PDF");
            xmlStreamWriter.writeEndElement();

            // create node 'Attachment' 
            xmlStreamWriter.writeStartElement("Attachment");
            xmlStreamWriter.writeCharacters(encodedPdf);
            xmlStreamWriter.writeEndElement();

            xmlStreamWriter.writeEndElement();


        } catch(Exception e){
            LOGGER.error("Error creating tag 'Allegati' for 'fattura' "+fattura.getId()+"'", e);
        }
    }

    private void createNodeBodyAllegati(XMLStreamWriter xmlStreamWriter, FatturaAccompagnatoria fatturaAccompagnatoria) {

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos)){
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yy");

            // create attachment name
            String nomeAttachment = "fattura_accompagnatoria"+fatturaAccompagnatoria.getProgressivo() + "/" + sdf.format(fatturaAccompagnatoria.getData())+".pdf";

            // create fattura accompagnatoria pdf
            byte[] pdfBytes = stampaService.generateFatturaAccompagnatoria(fatturaAccompagnatoria.getId());

            // create zip containing the pdf
            ZipEntry entry = new ZipEntry(nomeAttachment);
            zos.putNextEntry(entry);
            zos.write(pdfBytes);
            zos.closeEntry();

            // encode pdf file in Base64
            String encodedPdf = new String(Base64.getEncoder().encode(baos.toByteArray()));

            xmlStreamWriter.writeStartElement("Allegati");

            // create node 'NomeAttachment'
            xmlStreamWriter.writeStartElement("NomeAttachment");
            xmlStreamWriter.writeCharacters(nomeAttachment + ".zip");
            xmlStreamWriter.writeEndElement();

            // create node 'AlgoritmoCompressione'
            xmlStreamWriter.writeStartElement("AlgoritmoCompressione");
            xmlStreamWriter.writeCharacters("ZIP");
            xmlStreamWriter.writeEndElement();

            // create node 'FormatoAttachment'
            xmlStreamWriter.writeStartElement("FormatoAttachment");
            xmlStreamWriter.writeCharacters("PDF");
            xmlStreamWriter.writeEndElement();

            // create node 'Attachment'
            xmlStreamWriter.writeStartElement("Attachment");
            xmlStreamWriter.writeCharacters(encodedPdf);
            xmlStreamWriter.writeEndElement();

            xmlStreamWriter.writeEndElement();


        } catch(Exception e){
            LOGGER.error("Error creating tag 'Allegati' for 'fattura accompagnatoria' "+fatturaAccompagnatoria.getId()+"'", e);
        }
    }

    private void createNodeBodyAllegati(XMLStreamWriter xmlStreamWriter, NotaAccredito notaAccredito) {

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos)){
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yy");

            // create attachment name
            String nomeAttachment = "nota_accredito"+notaAccredito.getProgressivo() + "/" + sdf.format(notaAccredito.getData())+".pdf";

            // create fattura accompagnatoria pdf
            byte[] pdfBytes = stampaService.generateNotaAccredito(notaAccredito.getId());

            // create zip containing the pdf
            ZipEntry entry = new ZipEntry(nomeAttachment);
            zos.putNextEntry(entry);
            zos.write(pdfBytes);
            zos.closeEntry();

            // encode pdf file in Base64
            String encodedPdf = new String(Base64.getEncoder().encode(baos.toByteArray()));

            xmlStreamWriter.writeStartElement("Allegati");

            // create node 'NomeAttachment'
            xmlStreamWriter.writeStartElement("NomeAttachment");
            xmlStreamWriter.writeCharacters(nomeAttachment + ".zip");
            xmlStreamWriter.writeEndElement();

            // create node 'AlgoritmoCompressione'
            xmlStreamWriter.writeStartElement("AlgoritmoCompressione");
            xmlStreamWriter.writeCharacters("ZIP");
            xmlStreamWriter.writeEndElement();

            // create node 'FormatoAttachment'
            xmlStreamWriter.writeStartElement("FormatoAttachment");
            xmlStreamWriter.writeCharacters("PDF");
            xmlStreamWriter.writeEndElement();

            // create node 'Attachment'
            xmlStreamWriter.writeStartElement("Attachment");
            xmlStreamWriter.writeCharacters(encodedPdf);
            xmlStreamWriter.writeEndElement();

            xmlStreamWriter.writeEndElement();


        } catch(Exception e){
            LOGGER.error("Error creating tag 'Allegati' for 'nota_accredito'' "+notaAccredito.getId()+"'", e);
        }
    }

    private void checkAndCreateDirectory(String directory) throws Exception{
        Path path = Paths.get(directory);
        if(!Files.exists(path)){
            Files.createDirectory(path);
            LOGGER.info("AdeExport: successfully created folder '{}'", path.toAbsolutePath());
        }
    }

    /*private void removeFiles(List<Path> paths, List<String> fileNamesToExclude){
        for (Path path : paths) {
            String filename = path.getFileName().toString();
            if (!fileNamesToExclude.isEmpty() && !fileNamesToExclude.contains(filename)) {
                removeFileOrDirectory(path);
            }
        }
    }*/

    private void removeFileOrDirectory(Path path){
        try{
            File file = path.toFile();
            if(file.isDirectory()){
                FileUtils.deleteDirectory(file);
            } else{
                file.delete();
            }

        } catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error deleting file '{}'", path.toAbsolutePath());
        }

    }

    private String transformToPrettyPrint(String xml) throws Exception{
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Writer out = new StringWriter();
        t.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
        return out.toString();
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
