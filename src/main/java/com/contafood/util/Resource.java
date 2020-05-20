package com.contafood.util;

public enum Resource {

    FORNITORE("fornitore"),
    CLIENTE("cliente"),
    DDT("ddt"),
    NOTA_ACCREDITO("nota accredito"),
    NOTA_RESO("nota reso");

    private String label;

    Resource(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
