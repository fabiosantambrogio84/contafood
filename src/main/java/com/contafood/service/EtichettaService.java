package com.contafood.service;

import com.contafood.exception.ResourceNotFoundException;
import com.contafood.model.Etichetta;
import com.contafood.repository.EtichettaRepository;
import com.contafood.util.Constants;
import com.contafood.util.Utils;
import com.contafood.util.enumeration.LabelPlaceholder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EtichettaService {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final EtichettaRepository etichettaRepository;

    public EtichettaService(final EtichettaRepository etichettaRepository){
        this.etichettaRepository = etichettaRepository;
    }

    public Map<String, String> generate(Long idProduzione, String articolo, String ingredienti, String ingredienti2, String conservazione, String valoriNutrizionali, Date dataConsumazione, String lotto, Double peso, String disposizioniComune, String footer, MultipartFile barcodeEan13File, MultipartFile barcodeEan128File) throws Exception{

        final Map<String, String> result = new HashMap<>();

        final InputStream templateAsInputStream = this.getClass().getResourceAsStream(Constants.LABEL_TEMPLATE);
        String template = Utils.convertInputStream(templateAsInputStream);

        template = template.replace(LabelPlaceholder.ARTICOLO.getPlaceholder(), articolo);
        template = template.replace(LabelPlaceholder.INGREDIENTI.getPlaceholder(), ingredienti);
        template = template.replace(LabelPlaceholder.INGREDIENTI_2.getPlaceholder(), ingredienti2);
        template = template.replace(LabelPlaceholder.CONSERVAZIONE.getPlaceholder(), conservazione);
        template = template.replace(LabelPlaceholder.VALORI_NUTRIZIONALI.getPlaceholder(), valoriNutrizionali);
        template = template.replace(LabelPlaceholder.CONSUMAZIONE.getPlaceholder(), createConsumazione(dataConsumazione, lotto, peso));
        template = template.replace(LabelPlaceholder.BARCODE_EAN_13.getPlaceholder(), createBarcodeImgSrc(barcodeEan13File, 90, 40));
        template = template.replace(LabelPlaceholder.BARCODE_EAN_128.getPlaceholder(), createBarcodeImgSrc(barcodeEan128File, 150, 50));
        template = template.replace(LabelPlaceholder.DISPOSIZIONI_COMUNE.getPlaceholder(), disposizioniComune);
        template = template.replace(LabelPlaceholder.FOOTER.getPlaceholder(), footer);

        byte[] htmlBytes = template.getBytes(StandardCharsets.UTF_8);
        String html = new String(htmlBytes, StandardCharsets.UTF_8);
        String filename = createFilename(articolo);
        String uuid = UUID.randomUUID().toString();

        Etichetta etichetta = new Etichetta();
        etichetta.setUuid(uuid);
        etichetta.setArticolo(articolo);
        etichetta.setLotto(lotto);
        etichetta.setPeso(new BigDecimal(peso));
        etichetta.setFilename(filename);
        etichetta.setHtml(html);
        etichetta.setDataInserimento(Timestamp.from(ZonedDateTime.now().toInstant()));
        etichettaRepository.save(etichetta);

        result.put("filename", filename);
        result.put("uuid", uuid);

        return result;
    }

    public Etichetta get(String uuid){
        return etichettaRepository.findById(uuid).orElseThrow(ResourceNotFoundException::new);
    }

    public void delete(String uuid) {
        try{
            etichettaRepository.deleteById(uuid);
        } catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public List<Etichetta> getEtichetteToDelete(Integer days){
        int finalDays = days != null ? days : 1;
        List<Etichetta> etichette = (List<Etichetta>) etichettaRepository.findAll();
        etichette = etichette.stream().filter(e -> e.getDataInserimento().compareTo(Date.valueOf(LocalDate.now().minusDays(finalDays)))<=0).collect(Collectors.toList());
        return etichette;
    }

    private String createFilename(String articolo){
        return "label_"+ articolo.replaceAll(" ", "_").toLowerCase() +".html";
    }

    private String createConsumazione(Date dataConsumazione, String lotto, Double peso){
        return "Da consumarsi preferibilmente entro il: " +
                simpleDateFormat.format(dataConsumazione) +
                "<br/>Lotto: " +
                lotto +
                "<br/>Peso: " +
                peso +
                "g";
    }

    private String createBarcodeImgSrc(MultipartFile file, int widthPixels, int heightPixels) throws Exception{

        String imgSrc = "data:image/jpeg;base64,";

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        Image resultingImage = originalImage.getScaledInstance(widthPixels, heightPixels, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(widthPixels, heightPixels, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(outputImage, "jpg", byteArrayOutputStream);

        imgSrc += Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

        return imgSrc;
    }

    //public static void main(String[] args) {

        /*
        String articolo = "Patate al forno";
        String ingredienti = "Ingredienti: patate novelle, olio di semi di girasole, sale, salvia.";
        String ingredienti2 = "Può contenere tracce di: glutine, crostacei, pesce, latte, sedano, solfiti, molluschi, uova, soia.";
        String conservazione = "Conservare a 4°C. Dopo l’apertura consumare entro 4 giorni.";
        String valoriNutrizionali = "Valori nutrizionali medi su 100 grammi di prodotto: Energia: 576kj – 138 kcal, Grassi: 6,8g, di cui saturi: 0g, Carboidrati: 16,8g, di cui zuccheri: 1,1g, Proteine: 2,3g, Sale: 0,8g.";
        Date dataConsumazione = new Date(System.currentTimeMillis());
        String lotto = "221597";
        Double peso = 300D;
        String disposizioniComune = "Verifica le disposizioni del tuo comune";
        String footer = "Prodotto e confezionato da:<br/> URBANI GIUSEPPE<br/>Via 11 Settembre, 17 SAN GIOVANNI ILARIONE (VR)<br/>TEL. 045/6550993 CEL. 328/4694654 www.urbanialimentari.com";

        EtichettaService etichettaService = new EtichettaService();

        Map<String, String> result = etichettaService.generate(articolo, ingredienti, ingredienti2, conservazione, valoriNutrizionali, dataConsumazione, lotto, peso, disposizioniComune, footer, null, null);

        File outputFile = new File("C:\\temp\\"+filename);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(label);
        }

        try{
            File img = new File("C:\\temp\\barcode_ORIG.jpg");
            BufferedImage originalImage = ImageIO.read(img);

            Image resultingImage = originalImage.getScaledInstance(90, 50, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(90, 50, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //File outputfile = new File("C:\\temp\\barcode_NEW.jpg");
            ImageIO.write(outputImage, "jpg", byteArrayOutputStream);

            Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

        } catch(Exception e){
            e.printStackTrace();
        }
        */
    //}
}
