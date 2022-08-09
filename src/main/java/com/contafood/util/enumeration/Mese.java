package com.contafood.util.enumeration;

import java.util.Arrays;

public enum Mese {

    M_0("Gennaio"),
    M_1("Febbraio"),
    M_2("Marzo"),
    M_3("Aprile"),
    M_4("Maggio"),
    M_5("Giugno"),
    M_6("Luglio"),
    M_7("Agosto"),
    M_8("Settembre"),
    M_9("Ottobre"),
    M_10("Novembre"),
    M_11("Dicembre");

    private String label;

    Mese(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Mese get(int month){
        return Arrays.stream(Mese.values()).filter(m -> m.name().equals("M_"+month)).findFirst().get();
    }
}
