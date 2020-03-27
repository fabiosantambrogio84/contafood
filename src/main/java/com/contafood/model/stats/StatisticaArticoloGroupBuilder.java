package com.contafood.model.stats;

import java.math.BigDecimal;

public class StatisticaArticoloGroupBuilder {

    private String codice;

    private String descrizione;

    private Integer numeroRighe;

    private BigDecimal totaleVenduto;

    private BigDecimal totaleQuantitaVenduta;

    private BigDecimal totaleVendutoMedio;

    public StatisticaArticoloGroupBuilder setCodice(String codice) {
        this.codice = codice;
        return this;
    }

    public StatisticaArticoloGroupBuilder setDescrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public StatisticaArticoloGroupBuilder setNumeroRighe(Integer numeroRighe) {
        this.numeroRighe = numeroRighe;
        return this;
    }

    public StatisticaArticoloGroupBuilder setTotaleVenduto(BigDecimal totaleVenduto) {
        this.totaleVenduto = totaleVenduto;
        return this;
    }

    public StatisticaArticoloGroupBuilder setTotaleQuantitaVenduta(BigDecimal totaleQuantitaVenduta) {
        this.totaleQuantitaVenduta = totaleQuantitaVenduta;
        return this;
    }

    public StatisticaArticoloGroupBuilder setTotaleVendutoMedio(BigDecimal totaleVendutoMedio) {
        this.totaleVendutoMedio = totaleVendutoMedio;
        return this;
    }

    public StatisticaArticoloGroup build(){
        StatisticaArticoloGroup statisticaArticoloGroup = new StatisticaArticoloGroup();
        statisticaArticoloGroup.setCodice(this.codice);
        statisticaArticoloGroup.setDescrizione(this.descrizione);
        statisticaArticoloGroup.setNumeroRighe(this.numeroRighe);
        statisticaArticoloGroup.setTotaleQuantitaVenduta(this.totaleQuantitaVenduta);
        statisticaArticoloGroup.setTotaleVenduto(this.totaleVenduto);
        statisticaArticoloGroup.setTotaleVendutoMedio(this.totaleVendutoMedio);

        return statisticaArticoloGroup;
    }
}
