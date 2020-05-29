package com.contafood.model.views;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode()
@Entity
@Table(name = "v_ordine_cliente_stats_month")
public class VOrdineClienteStatsMonth {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_punto_consegna")
    private Long idPuntoConsegna;

    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "codice")
    private String codice;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "prezzo_listino_base")
    private BigDecimal prezzoListinoBase;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdPuntoConsegna() {
        return idPuntoConsegna;
    }

    public void setIdPuntoConsegna(Long idPuntoConsegna) {
        this.idPuntoConsegna = idPuntoConsegna;
    }

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzoListinoBase() {
        return prezzoListinoBase;
    }

    public void setPrezzoListinoBase(BigDecimal prezzoListinoBase) {
        this.prezzoListinoBase = prezzoListinoBase;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idCliente: " + idCliente);
        result.append(", idPuntoConsegna: " + idPuntoConsegna);
        result.append(", idArticolo: " + idArticolo);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", prezzoListinoBase: " + prezzoListinoBase);
        result.append("}");

        return result.toString();
    }
}
