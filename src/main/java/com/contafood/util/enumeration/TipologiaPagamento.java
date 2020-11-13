package com.contafood.util.enumeration;

public enum TipologiaPagamento {

    DDT("Ddt"),
    NOTA_ACCREDITO("Nota accredito"),
    NOTA_RESO_FORNITORE("Nota reso fornitore"),
    FATTURA("Fattura"),
    FATTURA_ACCOMPAGNATORIA("Fattura accompagnatoria"),
    RICEVUTA_PRIVATO("Ricevuta a privato");

    private String label;

    TipologiaPagamento(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
