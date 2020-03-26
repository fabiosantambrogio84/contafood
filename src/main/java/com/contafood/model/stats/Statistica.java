package com.contafood.model.stats;

import com.contafood.model.DdtArticolo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Statistica {

    private BigDecimal totaleVenduto;

    private BigDecimal totaleQuantitaVenduta;

    private Integer numeroRighe;

    private List<DdtArticolo> ddtArticoli;

    private List<StatisticaArticolo> statisticaArticoli;

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

    public List<DdtArticolo> getDdtArticoli() {
        return ddtArticoli;
    }

    public void setDdtArticoli(List<DdtArticolo> ddtArticoli) {
        this.ddtArticoli = ddtArticoli;
    }

    public List<StatisticaArticolo> getStatisticaArticoli() {
        return statisticaArticoli;
    }

    public void setStatisticaArticoli(List<StatisticaArticolo> statisticaArticoli) {
        this.statisticaArticoli = statisticaArticoli;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("totaleVenduto: " + totaleVenduto);
        result.append(", totaleQuantitaVenduta: " + totaleQuantitaVenduta);
        result.append(", numeroRighe: " + numeroRighe);
        result.append(", ddtArticoli: [");
        if(ddtArticoli != null && !ddtArticoli.isEmpty()){
            result.append(ddtArticoli.stream().map(a -> a.getId().getDdtId()+" - "+a.getId().getArticoloId()).collect(Collectors.joining(",")));
        }
        result.append("]");
        result.append(", statisticaArticoli: [");
        if(statisticaArticoli != null && !statisticaArticoli.isEmpty()){
            result.append(statisticaArticoli.stream().map(a -> a.getCodice()).collect(Collectors.joining(",")));
        }
        result.append("]");
        result.append("}");

        return result.toString();

    }
}
