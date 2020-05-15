package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "nota_accredito_riga")
public class NotaAccreditoRiga implements Serializable {

    @EmbeddedId
    NotaAccreditoRigaKey id;

    @ManyToOne
    @MapsId("id_nota_accredito")
    @JoinColumn(name = "id_nota_accredito")
    @JsonIgnoreProperties("notaAccreditoInfo")
    private NotaAccredito notaAccredito;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "lotto")
    private String lotto;

    @ManyToOne
    @JoinColumn(name="id_unita_misura")
    private UnitaMisura unitaMisura;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @ManyToOne
    @JoinColumn(name="id_aliquota_iva")
    private AliquotaIva aliquotaIva;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public NotaAccreditoRigaKey getId() {
        return id;
    }

    public void setId(NotaAccreditoRigaKey id) {
        this.id = id;
    }

    public NotaAccredito getNotaAccredito() {
        return notaAccredito;
    }

    public void setNotaAccredito(NotaAccredito notaAccredito) {
        this.notaAccredito = notaAccredito;
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

    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(UnitaMisura unitaMisura) {
        this.unitaMisura = unitaMisura;
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

    public AliquotaIva getAliquotaIva() {
        return aliquotaIva;
    }

    public void setAliquotaIva(AliquotaIva aliquotaIva) {
        this.aliquotaIva = aliquotaIva;
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
        result.append("notaAccreditoId: " + id.notaAccreditoId);
        result.append(", descrizione: " + descrizione);
        result.append(", lotto: " + lotto);
        result.append(", unitaMisura: " + unitaMisura);
        result.append(", quantita: " + quantita);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", aliquotaIva: " + aliquotaIva);
        result.append(", imponibile: " + imponibile);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
