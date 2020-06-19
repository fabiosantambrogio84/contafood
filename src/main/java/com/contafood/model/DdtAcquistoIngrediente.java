package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "ddt_acquisto_ingrediente")
public class DdtAcquistoIngrediente implements Serializable {

    private static final long serialVersionUID = 2911599584132134195L;

    @EmbeddedId
    DdtAcquistoIngredienteKey id;

    @ManyToOne
    @MapsId("id_ddt_acquisto")
    @JoinColumn(name = "id_ddt_acquisto")
    @JsonIgnoreProperties("ddtAcquistoIngredienti")
    private DdtAcquisto ddtAcquisto;

    @ManyToOne
    @MapsId("id_ingrediente")
    @JoinColumn(name = "id_ingrediente")
    @JsonIgnoreProperties("ddtAcquistoIngredienti")
    private Ingrediente ingrediente;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "data_scadenza")
    private Date dataScadenza;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public DdtAcquistoIngredienteKey getId() {
        return id;
    }

    public void setId(DdtAcquistoIngredienteKey id) {
        this.id = id;
    }

    public DdtAcquisto getDdtAcquisto() {
        return ddtAcquisto;
    }

    public void setDdtAcquisto(DdtAcquisto ddtAcquisto) {
        this.ddtAcquisto = ddtAcquisto;
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

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
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
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtAcquistoId: " + id.ddtAcquistoId);
        result.append(", ingredienteId: " + id.ingredienteId);
        result.append(", lotto: " + lotto);
        result.append(", dataScadenza: " + dataScadenza);
        result.append(", quantita: " + quantita);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", imponibile: " + imponibile);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
