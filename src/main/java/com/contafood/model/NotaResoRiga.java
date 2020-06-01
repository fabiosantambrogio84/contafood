package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "nota_reso_riga")
public class NotaResoRiga implements Serializable {

    private static final long serialVersionUID = -8462905891304101780L;

    @EmbeddedId
    NotaResoRigaKey id;

    @ManyToOne
    @MapsId("id_nota_reso")
    @JoinColumn(name = "id_nota_reso")
    @JsonIgnoreProperties("id_nota_reso")
    private NotaReso notaReso;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

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

    @Column(name = "totale")
    private BigDecimal totale;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public NotaResoRigaKey getId() {
        return id;
    }

    public void setId(NotaResoRigaKey id) {
        this.id = id;
    }

    public NotaReso getNotaReso() {
        return notaReso;
    }

    public void setNotaReso(NotaReso notaReso) {
        this.notaReso = notaReso;
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

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
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

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
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
        result.append(", descrizione: " + descrizione);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", unitaMisura: " + unitaMisura);
        result.append(", quantita: " + quantita);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", aliquotaIva: " + aliquotaIva);
        result.append(", imponibile: " + imponibile);
        result.append(", totale: " + totale);
        result.append(", articolo: " + articolo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
