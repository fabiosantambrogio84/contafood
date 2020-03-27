package com.contafood.model.stats;

import java.math.BigDecimal;

public class StatisticaArticolo {

    private String tipologia;

    private Long idArticolo;

    private Integer progressivo;

    private String codice;

    private String descrizione;

    private String lotto;

    private Float quantita;

    private BigDecimal prezzo;

    private BigDecimal totale;

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

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

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
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

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("tipologia: " + tipologia);
        result.append("idArticolo: " + idArticolo);
        result.append(", progressivo: " + progressivo);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", lotto: " + lotto);
        result.append(", quantita: " + quantita);
        result.append(", prezzo: " + prezzo);
        result.append(", totale: " + totale);
        result.append("}");

        return result.toString();

    }
}
