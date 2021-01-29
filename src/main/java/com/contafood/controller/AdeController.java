package com.contafood.controller;

import com.contafood.exception.ZipNoResourceFoundException;
import com.contafood.model.Cliente;
import com.contafood.model.Fattura;
import com.contafood.model.FatturaAccompagnatoria;
import com.contafood.model.NotaAccredito;
import com.contafood.service.AdeService;
import com.contafood.service.FatturaAccompagnatoriaService;
import com.contafood.service.FatturaService;
import com.contafood.service.NotaAccreditoService;
import com.contafood.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static com.contafood.util.Constants.DEFAULT_ENCODING;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/export-ade")
public class AdeController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdeController.class);

    private final FatturaService fatturaService;
    private final FatturaAccompagnatoriaService fatturaAccompagnatoriaService;
    private final NotaAccreditoService notaAccreditoService;
    private final AdeService adeService;

    @Autowired
    public AdeController(final FatturaService fatturaService,
                         final FatturaAccompagnatoriaService fatturaAccompagnatoriaService,
                         final NotaAccreditoService notaAccreditoService,
                         final AdeService adeService){
        this.fatturaService = fatturaService;
        this.fatturaAccompagnatoriaService = fatturaAccompagnatoriaService;
        this.notaAccreditoService = notaAccreditoService;
        this.adeService = adeService;
    }

    @RequestMapping(method = GET, produces="application/zip")
    @CrossOrigin
    public ResponseEntity<Resource> exportAde(@RequestParam(name = "tipo") String tipo,
                                              @RequestParam(name = "dataDa") Date dataDa,
                                              @RequestParam(name = "dataA") Date dataA) throws Exception{

        LOGGER.info("Creating zip file for tipo '{}', dataDa '{}' and dataA '{}'", tipo, dataDa, dataA);

        Map<String, Object> result;
        String zipFilename;
        byte[] zipContent;

        try{
            switch(tipo){
                case "fatture":
                    Map<Cliente, List<Fattura>> fattureByCliente = fatturaService.getFattureByCliente(dataDa, dataA);
                    if(fattureByCliente == null || fattureByCliente.isEmpty()){
                        throw new ZipNoResourceFoundException(com.contafood.util.enumeration.Resource.FATTURA);
                    }
                    result = adeService.createFattureZipFile(fattureByCliente);

                    // set SpeditoAde=true
                    fatturaService.patchSpeditoAdeFattureByCliente(fattureByCliente, Boolean.TRUE);

                    break;

                case "fatture-accompagnatorie":
                    Map<Cliente, List<FatturaAccompagnatoria>> fattureAccompagnatorieByCliente = fatturaAccompagnatoriaService.getFattureAccompagnatorieByCliente(dataDa, dataA);
                    if(fattureAccompagnatorieByCliente == null || fattureAccompagnatorieByCliente.isEmpty()){
                        throw new ZipNoResourceFoundException(com.contafood.util.enumeration.Resource.FATTURA_ACCOMPAGNATORIA);
                    }
                    result = adeService.createFattureAccompagnatorieZipFile(fattureAccompagnatorieByCliente);

                    // set SpeditoAde=true
                    fatturaAccompagnatoriaService.patchSpeditoAdeFattureAccompagnatorieByCliente(fattureAccompagnatorieByCliente, Boolean.TRUE);

                    break;

                case "note-accredito":
                    Map<Cliente, List<NotaAccredito>> noteAccreditoByCliente = notaAccreditoService.getNoteAccreditoByCliente(dataDa, dataA);
                    if(noteAccreditoByCliente == null || noteAccreditoByCliente.isEmpty()){
                        throw new ZipNoResourceFoundException(com.contafood.util.enumeration.Resource.NOTA_ACCREDITO);
                    }
                    result = adeService.createNoteAccreditoZipFile(noteAccreditoByCliente);

                    // set SpeditoAde=true
                    notaAccreditoService.patchSpeditoAdeNoteAccreditoByCliente(noteAccreditoByCliente, Boolean.TRUE);

                    break;

                default:
                    throw new RuntimeException("Tipo '"+tipo+"' not expected");
            }

            if(result == null || result.isEmpty()){
                throw new RuntimeException("Result is empty");
            }

        } catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Error creating zip file. "+e.getMessage());
            throw e;
        }

        zipFilename = (String)result.get("fileName");
        zipContent = (byte[])result.get("content");

        LOGGER.info("Successfully created zip file for tipo '{}', dataDa '{}' and dataA '{}'", tipo, dataDa, dataA);

        ByteArrayResource resource = new ByteArrayResource(zipContent);

        return ResponseEntity.ok()
                .headers(AdeService.createHttpHeaders(zipFilename))
                .contentLength(zipContent.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_ZIP))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/fatture/{idFattura}")
    @CrossOrigin
    public ResponseEntity<Resource> exportAdeFattura(@PathVariable final Long idFattura) throws Exception{
        LOGGER.info("Creating xml file for 'fattura' with id '{}'", idFattura);

        Fattura fattura = fatturaService.getOne(idFattura);
        Map<String, String> xmlResultMap = adeService.createFatturaXmlFile(fattura);
        String xmlFilename = xmlResultMap.get("fileName");
        String xmlContent = xmlResultMap.get("content");

        byte[] xmlContentByteArray = xmlContent.getBytes(Charset.forName(DEFAULT_ENCODING));
        ByteArrayResource resource = new ByteArrayResource(xmlContentByteArray);

        LOGGER.info("Successfully create xml for 'fattura' with id '{}'", idFattura);

        return ResponseEntity.ok()
                .headers(AdeService.createHttpHeaders(xmlFilename))
                .contentLength(xmlContentByteArray.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_XML))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/fatture-accompagnatorie/{idFatturaAccompagnatoria}")
    @CrossOrigin
    public ResponseEntity<Resource> exportAdeFatturaAccompagnatoria(@PathVariable final Long idFatturaAccompagnatoria) throws Exception{
        LOGGER.info("Creating xml file for 'fatturaAccompgnatoria' with id '{}'", idFatturaAccompagnatoria);

        FatturaAccompagnatoria fatturaAccompagnatoria = fatturaAccompagnatoriaService.getOne(idFatturaAccompagnatoria);

        Map<String, String> xmlResultMap = adeService.createFatturaAccompagnatoriaXmlFile(fatturaAccompagnatoria);
        String xmlFilename = xmlResultMap.get("fileName");
        String xmlContent = xmlResultMap.get("content");

        byte[] xmlContentByteArray = xmlContent.getBytes(Charset.forName(DEFAULT_ENCODING));
        ByteArrayResource resource = new ByteArrayResource(xmlContentByteArray);

        LOGGER.info("Successfully create xml for 'fatturaAccompgnatoria' with id '{}'", idFatturaAccompagnatoria);

        return ResponseEntity.ok()
                .headers(AdeService.createHttpHeaders(xmlFilename))
                .contentLength(xmlContentByteArray.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_XML))
                .body(resource);
    }

    @RequestMapping(method = GET, path = "/note-accredito/{idNotaAccredito}")
    @CrossOrigin
    public ResponseEntity<Resource> exportAdeNotaAccredito(@PathVariable final Long idNotaAccredito) throws Exception{
        LOGGER.info("Creating xml file for 'notaAccredito' with id '{}'", idNotaAccredito);

        NotaAccredito notaAccredito = notaAccreditoService.getOne(idNotaAccredito);

        Map<String, String> xmlResultMap = adeService.createNotaAccreditoXmlFile(notaAccredito);
        String xmlFilename = xmlResultMap.get("fileName");
        String xmlContent = xmlResultMap.get("content");

        byte[] xmlContentByteArray = xmlContent.getBytes(Charset.forName(DEFAULT_ENCODING));
        ByteArrayResource resource = new ByteArrayResource(xmlContentByteArray);

        LOGGER.info("Successfully create xml for 'notaAccredito' with id '{}'", idNotaAccredito);

        return ResponseEntity.ok()
                .headers(AdeService.createHttpHeaders(xmlFilename))
                .contentLength(xmlContentByteArray.length)
                .contentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_APPLICATION_XML))
                .body(resource);
    }

}


