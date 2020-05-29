package com.contafood.util.enumeration;

public enum TipologiaSconto {

    ARTICOLO("Articolo"),
    FORNITORE("Fornitore");

    private String label;

    TipologiaSconto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
