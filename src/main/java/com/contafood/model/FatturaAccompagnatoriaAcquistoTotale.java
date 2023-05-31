package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "fattura_accom_acquisto_totale")
public class FatturaAccompagnatoriaAcquistoTotale implements Serializable {

    private static final long serialVersionUID = -7398697454368778848L;

    @EmbeddedId
    FatturaAccompagnatoriaAcquistoTotaleKey id;

    @ManyToOne
    @MapsId("id_fattura_accom_acquisto")
    @JoinColumn(name = "id_fattura_accom_acquisto")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquistoTotali")
    private FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto;

    @ManyToOne
    @MapsId("id_aliquota_iva")
    @JoinColumn(name = "id_aliquota_iva")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAcquistoAliquoteIva")
    private AliquotaIva aliquotaIva;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public FatturaAccompagnatoriaAcquistoTotaleKey getId() {
        return id;
    }

    public void setId(FatturaAccompagnatoriaAcquistoTotaleKey id) {
        this.id = id;
    }

    public FatturaAccompagnatoriaAcquisto getFatturaAccompagnatoriaAcquisto() {
        return fatturaAccompagnatoriaAcquisto;
    }

    public void setFatturaAccompagnatoriaAcquisto(FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto) {
        this.fatturaAccompagnatoriaAcquisto = fatturaAccompagnatoriaAcquisto;
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

        return "{" +
                "fatturaAccompagnatoriaAcquistoId: " + id.fatturaAccompagnatoriaAcquistoId +
                ", aliquotaIvaId: " + id.aliquotaIvaId +
                ", totaleImponibile: " + totaleImponibile +
                ", totaleIva: " + totaleIva +
                ", dataInserimento: " + dataInserimento +
                ", dataAggiornamento: " + dataAggiornamento +
                "}";
    }
}