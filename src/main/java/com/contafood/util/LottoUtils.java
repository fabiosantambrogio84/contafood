package com.contafood.util;

import org.apache.commons.lang3.StringUtils;

public class LottoUtils {

    public static String createLottoProduzione(String anno, Integer codice){
        StringBuilder sb = new StringBuilder();
        sb = sb.append(anno).append(StringUtils.leftPad(codice.toString(), 3, '0'));
        return sb.toString();
    }

}
