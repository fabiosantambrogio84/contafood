package com.contafood.util;

public class LottoUtils {

    public static final char LOTTO_SEPARATOR_CHAR = '.';

    public static String createLottoProduzione(Integer anno, Integer giorno, Integer numeroProgressivo){
        String annoLetter = AnnoMapping.valueOf("Y_"+anno).getLetterMapping();

        StringBuilder sb = new StringBuilder();
        sb = sb.append(annoLetter).append(LOTTO_SEPARATOR_CHAR).append(giorno).append(LOTTO_SEPARATOR_CHAR).append(numeroProgressivo);
        return sb.toString();
    }

}
