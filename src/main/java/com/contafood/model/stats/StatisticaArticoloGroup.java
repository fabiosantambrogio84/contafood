package com.contafood.model.stats;

import java.math.BigDecimal;

public class StatisticaArticoloGroup {

    private String codice;

    private String descrizione;

    private Integer numeroRighe;

    private BigDecimal totaleVenduto;

    private BigDecimal totaleQuantitaVenduta;

    private BigDecimal totaleVendutoMedio;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getNumeroRighe() {
        return numeroRighe;
    }

    public void setNumeroRighe(Integer numeroRighe) {
        this.numeroRighe = numeroRighe;
    }

    public BigDecimal getTotaleVenduto() {
        return totaleVenduto;
    }

    public void setTotaleVenduto(BigDecimal totaleVenduto) {
        this.totaleVenduto = totaleVenduto;
    }

    public BigDecimal getTotaleQuantitaVenduta() {
        return totaleQuantitaVenduta;
    }

    public void setTotaleQuantitaVenduta(BigDecimal totaleQuantitaVenduta) {
        this.totaleQuantitaVenduta = totaleQuantitaVenduta;
    }

    public BigDecimal getTotaleVendutoMedio() {
        return totaleVendutoMedio;
    }

    public void setTotaleVendutoMedio(BigDecimal totaleVendutoMedio) {
        this.totaleVendutoMedio = totaleVendutoMedio;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", totaleVenduto: " + totaleVenduto);
        result.append(", totaleQuantitaVenduta: " + totaleQuantitaVenduta);
        result.append(", numeroRighe: " + numeroRighe);
        result.append(", totaleVendutoMedio: " + totaleVendutoMedio);
        result.append("}");

        return result.toString();

    }
}
