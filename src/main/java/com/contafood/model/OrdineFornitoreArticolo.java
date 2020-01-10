package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ordine_fornitore_articolo")
public class OrdineFornitoreArticolo implements Serializable {

    @EmbeddedId
    OrdineFornitoreArticoloKey id;

    @ManyToOne
    @MapsId("id_ordine_fornitore")
    @JoinColumn(name = "id_ordine_fornitore")
    @JsonIgnoreProperties("ordineFornitoreArticoli")
    private OrdineFornitore ordineFornitore;

    @ManyToOne
    @MapsId("id_articolo")
    @JoinColumn(name = "id_articolo")
    @JsonIgnoreProperties("ordineClienteArticoli")
    private Articolo articolo;

    @Column(name = "num_ordinati")
    private Integer numeroPezziOrdinati;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    public OrdineFornitoreArticoloKey getId() {
        return id;
    }

    public void setId(OrdineFornitoreArticoloKey id) {
        this.id = id;
    }

    public OrdineFornitore getOrdineFornitore() {
        return ordineFornitore;
    }

    public void setOrdineFornitore(OrdineFornitore ordineFornitore) {
        this.ordineFornitore = ordineFornitore;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public Integer getNumeroPezziOrdinati() {
        return numeroPezziOrdinati;
    }

    public void setNumeroPezziOrdinati(Integer numeroPezziOrdinati) {
        this.numeroPezziOrdinati = numeroPezziOrdinati;
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
        result.append("ordineFornitoreId: " + id.ordineFornitoreId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
