package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ricevuta_privato_ordine_cliente")
public class RicevutaPrivatoArticoloOrdineCliente implements Serializable {

    private static final long serialVersionUID = -6162317307499583467L;

    @EmbeddedId
    RicevutaPrivatoArticoloOrdineClienteKey id;

    @ManyToOne
    @JoinColumns( {
        @JoinColumn(name = "id_ricevuta_privato", referencedColumnName = "id_ricevuta_privato", insertable = false, updatable = false),
        @JoinColumn(name = "id_articolo", referencedColumnName = "id_articolo", insertable = false, updatable = false),
        @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    })
    @JsonIgnoreProperties("ricevutaPrivatoArticoliOrdiniClienti")
    private RicevutaPrivatoArticolo ricevutaPrivatoArticolo;

    @ManyToOne
    @MapsId("id_ordine_cliente")
    @JoinColumn(name = "id_ordine_cliente")
    @JsonIgnoreProperties("ricevutaPrivatoArticoliOrdiniClienti")
    private OrdineCliente ordineCliente;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public RicevutaPrivatoArticoloOrdineClienteKey getId() {
        return id;
    }

    public void setId(RicevutaPrivatoArticoloOrdineClienteKey id) {
        this.id = id;
    }

    public RicevutaPrivatoArticolo getRicevutaPrivatoArticolo() {
        return ricevutaPrivatoArticolo;
    }

    public void setRicevutaPrivatoArticolo(RicevutaPrivatoArticolo ricevutaPrivatoArticolo) {
        this.ricevutaPrivatoArticolo = ricevutaPrivatoArticolo;
    }

    public OrdineCliente getOrdineCliente() {
        return ordineCliente;
    }

    public void setOrdineCliente(OrdineCliente ordineCliente) {
        this.ordineCliente = ordineCliente;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricevutaPrivatoId: " + id.ricevutaPrivatoId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", uuid: " + id.uuid);
        result.append(", ordineClienteId: " + id.ordineClienteId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
