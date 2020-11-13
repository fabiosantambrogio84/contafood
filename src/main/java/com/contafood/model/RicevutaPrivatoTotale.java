package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "ricevuta_privato_totale")
public class RicevutaPrivatoTotale implements Serializable {

    private static final long serialVersionUID = -7394974050388950156L;

    @EmbeddedId
    RicevutaPrivatoTotaleKey id;

    @ManyToOne
    @MapsId("id_ricevuta_privato")
    @JoinColumn(name = "id_ricevuta_privato")
    @JsonIgnoreProperties("ricevutaPrivatoTotali")
    private RicevutaPrivato ricevutaPrivato;

    @ManyToOne
    @MapsId("id_aliquota_iva")
    @JoinColumn(name = "id_aliquota_iva")
    @JsonIgnoreProperties("ricevutaPrivatoAliquoteIva")
    private AliquotaIva aliquotaIva;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public RicevutaPrivatoTotaleKey getId() {
        return id;
    }

    public void setId(RicevutaPrivatoTotaleKey id) {
        this.id = id;
    }

    public RicevutaPrivato getRicevutaPrivato() {
        return ricevutaPrivato;
    }

    public void setRicevutaPrivato(RicevutaPrivato ricevutaPrivato) {
        this.ricevutaPrivato = ricevutaPrivato;
    }

    public AliquotaIva getAliquotaIva() {
        return aliquotaIva;
    }

    public void setAliquotaIva(AliquotaIva aliquotaIva) {
        this.aliquotaIva = aliquotaIva;
    }

    public BigDecimal getTotaleIva() {
        return totaleIva;
    }

    public void setTotaleIva(BigDecimal totaleIva) {
        this.totaleIva = totaleIva;
    }

    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
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
        result.append("ricevutaPrivatoId: " + id.ricevutaPrivatoId);
        result.append(", aliquotaIvaId: " + id.aliquotaIvaId);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totaleIva: " + totaleIva);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
