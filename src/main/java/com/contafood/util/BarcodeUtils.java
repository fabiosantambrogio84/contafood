package com.contafood.util;

import com.contafood.exception.BarcodeMaskParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarcodeUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(BarcodeUtils.class);

    public static String createRegexpLotto(String barcodeMask){
        String mask = barcodeMask.toUpperCase();

        if(!Utils.stringContainsOnlyCertainCharacters(mask)){
            LOGGER.error("La maschera barcode può contenere solo i caratteri X, L, A, M, G");
            throw new BarcodeMaskParsingException("La maschera barcode può contenere solo i caratteri X, L, A, M, G");
        }

        String lottoRegexp;

        int lottoStart = mask.indexOf('L');
        if(lottoStart == -1){
            LOGGER.error("La maschera barcode non contiene il carattere L");
            throw new BarcodeMaskParsingException("La maschera barcode non contiene il carattere L");
        }

        int lottoLength = 0;
        for(int i=lottoStart; i<mask.length(); i++){
            char ch = mask.charAt(i);
            if(ch == 'L'){
                lottoLength += 1;
            }
        }
        lottoRegexp = Constants.BARCODE_REGEXP.replace("start", String.valueOf(lottoStart)).replace("length", String.valueOf(lottoLength));

        if(lottoRegexp == null){
            LOGGER.error("Errore nella gestione della maschera barcode");
            throw new BarcodeMaskParsingException("Errore nella gestione della maschera barcode");
        }

        return lottoRegexp;
    }

    public static String createRegexpDataScadenza(String barcodeMask){
        String mask = barcodeMask.toUpperCase();

        if(!Utils.stringContainsOnlyCertainCharacters(mask)){
            LOGGER.error("La maschera barcode può contenere solo i caratteri X, L, A, M, G");
            throw new BarcodeMaskParsingException("La maschera barcode può contenere solo i caratteri X, L, A, M, G");
        }

        String dataScadenzaRegexp;

        int dataScadenzaStart = -1;
        int dataScadenzaStartYear = mask.indexOf("XA");
        int dataScadenzaStartDay = mask.indexOf("XG");

        if(dataScadenzaStartYear != -1){
            dataScadenzaStart = dataScadenzaStartYear;
        } else if(dataScadenzaStartDay != -1){
            dataScadenzaStart = dataScadenzaStartDay;
        }

        if(dataScadenzaStart == -1){
            throw new RuntimeException("");
        }

        int dataScadenzaLength = 0;
        for(int i=dataScadenzaStart; i<mask.length(); i++){
            char ch = mask.charAt(i);
            if(ch == 'A' || ch == 'M' || ch == 'G'){
                dataScadenzaLength += 1;
            }
        }
        dataScadenzaRegexp = Constants.BARCODE_REGEXP.replace("start", String.valueOf(dataScadenzaStart)).replace("length", String.valueOf(dataScadenzaLength));

        if(dataScadenzaRegexp == null){
            LOGGER.error("Errore nella gestione della maschera barcode");
            throw new BarcodeMaskParsingException("Errore nella gestione della maschera barcode");
        }

        return dataScadenzaRegexp;
    }

}
