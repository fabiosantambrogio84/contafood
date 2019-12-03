package com.contafood.util;

public enum TipologiaListinoPrezzoVariazione {

    PERCENTUALE("%"),
    EURO("€");
    private String label;

    TipologiaListinoPrezzoVariazione(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
