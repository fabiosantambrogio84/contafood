package com.contafood.model.reports;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;

public class OrdineAutistaDataSource {

    private String codiceOrdine;

    private String cliente;

    private String puntoConsegna;

    private List<OrdineAutistaArticoloDataSource> ordineAutistaArticoloDataSources;

    private JRBeanCollectionDataSource ordineAutistaArticoliDataSource;

    public String getCodiceOrdine() {
        return codiceOrdine;
    }

    public void setCodiceOrdine(String codiceOrdine) {
        this.codiceOrdine = codiceOrdine;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getPuntoConsegna() {
        return puntoConsegna;
    }

    public void setPuntoConsegna(String puntoConsegna) {
        this.puntoConsegna = puntoConsegna;
    }

    public List<OrdineAutistaArticoloDataSource> getOrdineAutistaArticoloDataSources() {
        return ordineAutistaArticoloDataSources;
    }

    public void setOrdineAutistaArticoloDataSources(List<OrdineAutistaArticoloDataSource> ordineAutistaArticoloDataSources) {
        this.ordineAutistaArticoloDataSources = ordineAutistaArticoloDataSources;
    }

    public JRBeanCollectionDataSource getOrdineAutistaArticoliDataSource() {
        return new JRBeanCollectionDataSource(ordineAutistaArticoloDataSources, false);
    }
}
