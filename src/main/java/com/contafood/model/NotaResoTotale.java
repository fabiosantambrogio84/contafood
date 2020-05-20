package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "nota_reso_totale")
public class NotaResoTotale implements Serializable {

    @EmbeddedId
    NotaResoTotaleKey id;

    @ManyToOne
    @MapsId("id_nota_reso")
    @JoinColumn(name = "id_nota_reso")
    @JsonIgnoreProperties("notaResoTotali")
    private NotaReso notaReso;

    @ManyToOne
    @MapsId("id_aliquota_iva")
    @JoinColumn(name = "id_aliquota_iva")
    @JsonIgnoreProperties("notaResoAliquoteIva")
    private AliquotaIva aliquotaIva;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public NotaResoTotaleKey getId() {
        return id;
    }

    public void setId(NotaResoTotaleKey id) {
        this.id = id;
    }

    public NotaReso getNotaReso() {
        return notaReso;
    }

    public void setNotaReso(NotaReso notaReso) {
        this.notaReso = notaReso;
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
        result.append("notaResoId: " + id.notaResoId);
        result.append(", aliquotaIvaId: " + id.aliquotaIvaId);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totaleIva: " + totaleIva);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
