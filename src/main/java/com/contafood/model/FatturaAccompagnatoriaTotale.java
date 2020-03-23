package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "fattura_accom_totale")
public class FatturaAccompagnatoriaTotale implements Serializable {

    @EmbeddedId
    FatturaAccompagnatoriaTotaleKey id;

    @ManyToOne
    @MapsId("id_fattura_accom")
    @JoinColumn(name = "id_fattura_accom")
    @JsonIgnoreProperties("fatturaAccompagnatoriaArticoli")
    private FatturaAccompagnatoria fatturaAccompagnatoria;

    @ManyToOne
    @MapsId("id_aliquota_iva")
    @JoinColumn(name = "id_aliquota_iva")
    @JsonIgnoreProperties("fatturaAccompagnatoriaAliquoteIva")
    private AliquotaIva aliquotaIva;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public FatturaAccompagnatoriaTotaleKey getId() {
        return id;
    }

    public void setId(FatturaAccompagnatoriaTotaleKey id) {
        this.id = id;
    }

    public FatturaAccompagnatoria getFatturaAccompagnatoria() {
        return fatturaAccompagnatoria;
    }

    public void setFatturaAccompagnatoria(FatturaAccompagnatoria fatturaAccompagnatoria) {
        this.fatturaAccompagnatoria = fatturaAccompagnatoria;
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
        result.append("fatturaAccompagnatoriaId: " + id.fatturaAccompagnatoriaId);
        result.append(", aliquotaIvaId: " + id.aliquotaIvaId);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totaleIva: " + totaleIva);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
