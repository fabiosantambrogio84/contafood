package com.contafood.model.stats;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Statistica {

    private BigDecimal totaleVenduto;

    private BigDecimal totaleQuantitaVenduta;

    private Integer numeroRighe;

    private List<StatisticaArticolo> statisticaArticoli;

    private List<StatisticaArticoloGroup> statisticaArticoloGroups;

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

    public Integer getNumeroRighe() {
        return numeroRighe;
    }

    public void setNumeroRighe(Integer numeroRighe) {
        this.numeroRighe = numeroRighe;
    }

    public List<StatisticaArticolo> getStatisticaArticoli() {
        return statisticaArticoli;
    }

    public void setStatisticaArticoli(List<StatisticaArticolo> statisticaArticoli) {
        this.statisticaArticoli = statisticaArticoli;
    }

    public List<StatisticaArticoloGroup> getStatisticaArticoloGroups() {
        return statisticaArticoloGroups;
    }

    public void setStatisticaArticoloGroups(List<StatisticaArticoloGroup> statisticaArticoloGroups) {
        this.statisticaArticoloGroups = statisticaArticoloGroups;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("totaleVenduto: " + totaleVenduto);
        result.append(", totaleQuantitaVenduta: " + totaleQuantitaVenduta);
        result.append(", numeroRighe: " + numeroRighe);
        result.append(", statisticaArticoli: [");
        if(statisticaArticoli != null && !statisticaArticoli.isEmpty()){
            result.append(statisticaArticoli.stream().map(a -> a.getTipologia()+"-"+a.getProgressivo()+"-"+a.getCodice()+"-"+a.getTotale()).collect(Collectors.joining(", ")));
        }
        result.append("]");
        result.append(", statisticaArticoloGroups: [");
        if(statisticaArticoloGroups != null && !statisticaArticoloGroups.isEmpty()){
            result.append(statisticaArticoloGroups.stream().map(a -> a.getCodice()).collect(Collectors.joining(", ")));
        }
        result.append("]");
        result.append("}");

        return result.toString();

    }
}
