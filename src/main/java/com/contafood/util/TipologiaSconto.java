package com.contafood.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
