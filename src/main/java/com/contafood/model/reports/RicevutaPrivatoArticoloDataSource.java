package com.contafood.model.reports;

import java.math.BigDecimal;

public class RicevutaPrivatoArticoloDataSource {

    private String codiceArticolo;

    private String descrizioneArticolo;

    private String lotto;

    private String udm;

    private Float quantita;

    private BigDecimal prezzo;

    private BigDecimal sconto;

    private BigDecimal imponibile;

    private Integer iva;

    private BigDecimal prezzoConIva;

    private BigDecimal totale;

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public String getUdm() {
        return udm;
    }

    public void setUdm(String udm) {
        this.udm = udm;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public BigDecimal getSconto() {
        return sconto;
    }

    public void setSconto(BigDecimal sconto) {
        this.sconto = sconto;
    }

    public BigDecimal getImponibile() {
        return imponibile;
    }

    public void setImponibile(BigDecimal imponibile) {
        this.imponibile = imponibile;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public BigDecimal getPrezzoConIva() {
        return prezzoConIva;
    }

    public void setPrezzoConIva(BigDecimal prezzoConIva) {
        this.prezzoConIva = prezzoConIva;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }
}
