package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "ddt_articolo")
public class DdtArticolo implements Serializable {

    @EmbeddedId
    DdtArticoloKey id;

    @ManyToOne
    @MapsId("id_ddt")
    @JoinColumn(name = "id_ddt")
    @JsonIgnoreProperties("ddtArticoli")
    private Ddt ddt;

    @ManyToOne
    @MapsId("id_articolo")
    @JoinColumn(name = "id_articolo")
    @JsonIgnoreProperties("ddtArticoli")
    private Articolo articolo;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "numero_pezzi")
    private Integer numeroPezzi;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public DdtArticoloKey getId() {
        return id;
    }

    public void setId(DdtArticoloKey id) {
        this.id = id;
    }

    public Ddt getDdt() {
        return ddt;
    }

    public void setDdt(Ddt ddt) {
        this.ddt = ddt;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
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
        result.append("ddtId: " + id.ddtId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", quantita: " + quantita);
        result.append(", numeroPezzi: " + numeroPezzi);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
