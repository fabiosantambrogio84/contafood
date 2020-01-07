package com.contafood.util;

public enum TipologiaOrdine {

    CLIENTI("Clienti"),
    FORNITORI("Fornitori"),
    AUTISTI("Autisti");

    private String label;

    TipologiaOrdine(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
