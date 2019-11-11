package com.contafood.util;

import org.apache.commons.lang3.StringUtils;

public class LottoUtils {

    //public static final char LOTTO_SEPARATOR_CHAR = '.';

    public static String createLottoProduzione(String anno, Integer giorno, Integer numeroProgressivo){
        //String annoLetter = AnnoMapping.valueOf("Y_"+anno).getLetterMapping();

        StringBuilder sb = new StringBuilder();
        sb = sb.append(anno).append(giorno).append(StringUtils.leftPad(numeroProgressivo.toString(), 3, '0'));
        return sb.toString();
    }

}
