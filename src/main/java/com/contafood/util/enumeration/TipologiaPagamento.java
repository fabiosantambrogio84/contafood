package com.contafood.util.enumeration;

public enum TipologiaPagamento {

    DDT("Ddt"),
    DDT_ACQUISTO("Ddt acquisto"),
    NOTA_ACCREDITO("Nota accredito"),
    NOTA_RESO_FORNITORE("Nota reso fornitore"),
    FATTURA("Fattura"),
    FATTURA_ACCOMPAGNATORIA("Fattura accompagnatoria"),
    FATTURA_ACQUISTO("Fattura acquisto"),
    RICEVUTA_PRIVATO("Ricevuta a privato"),
    FATTURA_ACCOMPAGNATORIA_ACQUISTO("Fattura accompagnatoria acquisto");

    private String label;

    TipologiaPagamento(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
