package com.contafood.util.enumeration;

public enum Resource {

    CLIENTE("cliente"),
    DDT("ddt"),
    DDT_ACQUISTO("ddt acquisto"),
    FATTURA_ACCOMPAGNATORIA("fattura accompagnatoria"),
    FORNITORE("fornitore"),
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
