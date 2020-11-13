package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@EqualsAndHashCode
@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data")
    private Date data;

    @Column(name = "tipologia")
    private String tipologia;

    @ManyToOne
    @JoinColumn(name="id_tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name="id_ddt")
    private Ddt ddt;

    @ManyToOne
    @JoinColumn(name="id_nota_accredito")
    private NotaAccredito notaAccredito;

    @ManyToOne
    @JoinColumn(name="id_nota_reso")
    private NotaReso notaReso;

    @ManyToOne
    @JoinColumn(name="id_ricevuta_privato")
    private RicevutaPrivato ricevutaPrivato;

    @ManyToOne
    @JoinColumn(name="id_fattura")
    private Fattura fattura;

    @ManyToOne
    @JoinColumn(name="id_fattura_accom")
    private FatturaAccompagnatoria fatturaAccompagnatoria;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "importo")
    private BigDecimal importo;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Ddt getDdt() {
        return ddt;
    }

    public void setDdt(Ddt ddt) {
        this.ddt = ddt;
    }

    public NotaAccredito getNotaAccredito() {
        return notaAccredito;
    }

    public void setNotaAccredito(NotaAccredito notaAccredito) {
        this.notaAccredito = notaAccredito;
    }

    public NotaReso getNotaReso() {
        return notaReso;
    }

    public void setNotaReso(NotaReso notaReso) {
        this.notaReso = notaReso;
    }

    public RicevutaPrivato getRicevutaPrivato() {
        return ricevutaPrivato;
    }

    public void setRicevutaPrivato(RicevutaPrivato ricevutaPrivato) {
        this.ricevutaPrivato = ricevutaPrivato;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public FatturaAccompagnatoria getFatturaAccompagnatoria() {
        return fatturaAccompagnatoria;
    }

    public void setFatturaAccompagnatoria(FatturaAccompagnatoria fatturaAccompagnatoria) {
        this.fatturaAccompagnatoria = fatturaAccompagnatoria;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        result.append("id: " + id);
        result.append(", data: " + data);
        result.append(", tipologia: " + tipologia);
        result.append(", descrizione: " + descrizione);
        result.append(", importo: " + importo);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }

}
