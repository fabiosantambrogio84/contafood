package com.contafood.model.reports;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.math.BigDecimal;
import java.util.List;

public class FatturaCommercianteDataSource {

    private String data;

    private String numero;

    private String ragioneSociale;

    private String indirizzo;

    private String citta;

    private String partitaIva;

    private String codiceFiscale;

    private BigDecimal totale;

    private List<FatturaCommercianteTotaleDataSource> fatturaCommercianteTotaleDataSources;

    private JRBeanCollectionDataSource fatturaCommercianteTotaliDataSource;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public List<FatturaCommercianteTotaleDataSource> getFatturaCommercianteTotaleDataSources() {
        return fatturaCommercianteTotaleDataSources;
    }

    public void setFatturaCommercianteTotaleDataSources(List<FatturaCommercianteTotaleDataSource> fatturaCommercianteTotaleDataSources) {
        this.fatturaCommercianteTotaleDataSources = fatturaCommercianteTotaleDataSources;
    }

    public JRBeanCollectionDataSource getFatturaCommercianteTotaliDataSource() {
        return new JRBeanCollectionDataSource(fatturaCommercianteTotaleDataSources, false);
    }

}
