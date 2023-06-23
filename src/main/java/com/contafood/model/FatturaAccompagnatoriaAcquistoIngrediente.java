package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "fattura_accom_acquisto_ingrediente")
public class FatturaAccompagnatoriaAcquistoIngrediente implements Serializable {

    private static final long serialVersionUID = -7655086381376925216L;

    @EmbeddedId
    FatturaAccompagnatoriaAcquistoIngredienteKey id;

    @ManyToOne
    @MapsId("id_fattura_accom_acquisto")
    @JoinColumn(name = "id_fattura_accom_acquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquistoIngredienti")
    private FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto;

    @ManyToOne
    @MapsId("id_ingrediente")
    @JoinColumn(name = "id_ingrediente")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquistoIngredienti")
    private Ingrediente ingrediente;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "numero_pezzi")
    private Integer numeroPezzi;

    @Column(name = "numero_pezzi_da_evadere")
    private Integer numeroPezziDaEvadere;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "costo")
    private BigDecimal costo;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public FatturaAccompagnatoriaAcquistoIngredienteKey getId() {
        return id;
    }

    public void setId(FatturaAccompagnatoriaAcquistoIngredienteKey id) {
        this.id = id;
    }

    public FatturaAccompagnatoriaAcquisto getFatturaAccompagnatoriaAcquisto() {
        return fatturaAccompagnatoriaAcquisto;
    }

    public void setFatturaAccompagnatoriaAcquisto(FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto) {
        this.fatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquisto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
    }

    public Integer getNumeroPezzi() {
        return numeroPezzi;
    }

    public void setNumeroPezzi(Integer numeroPezzi) {
        this.numeroPezzi = numeroPezzi;
    }

    public Integer getNumeroPezziDaEvadere() {
        return numeroPezziDaEvadere;
    }

    public void setNumeroPezziDaEvadere(Integer numeroPezziDaEvadere) {
        this.numeroPezziDaEvadere = numeroPezziDaEvadere;
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

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Timestamp getDataAggiornamento() {
        return dataAggiornamento;
    }

    public void setDataAggiornamento(Timestamp dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    @Override
    public String toString() {

        return "{" +
                "fatturaAccompagnatoriaAcquistoId: " + id.fatturaAccompagnatoriaAcquistoId +
                ", ingredienteId: " + id.ingredienteId +
                ", lotto: " + lotto +
                ", scadenza: " + scadenza +
                ", quantita: " + quantita +
                ", numeroPezzi: " + numeroPezzi +
                ", numeroPezziDaEvadere: " + numeroPezziDaEvadere +
                ", prezzo: " + prezzo +
                ", sconto: " + sconto +
                ", imponibile: " + imponibile +
                ", costo: " + costo +
                ", totale: " + totale +
                ", dataInserimento: " + dataInserimento +
                ", dataAggiornamento: " + dataAggiornamento +
                "}";
    }
}