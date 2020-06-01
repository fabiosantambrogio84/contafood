package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ddt_articolo_ordine_cliente")
public class DdtArticoloOrdineCliente implements Serializable {

    private static final long serialVersionUID = 2898128805206300825L;

    @EmbeddedId
    DdtArticoloOrdineClienteKey id;

    @ManyToOne
    @JoinColumns( {
        @JoinColumn(name = "id_ddt", referencedColumnName = "id_ddt", insertable = false, updatable = false),
        @JoinColumn(name = "id_articolo", referencedColumnName = "id_articolo", insertable = false, updatable = false),
        @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    })
    @JsonIgnoreProperties("ddtArticoliOrdiniClienti")
    private DdtArticolo ddtArticolo;

    @ManyToOne
    @MapsId("id_ordine_cliente")
    @JoinColumn(name = "id_ordine_cliente")
    @JsonIgnoreProperties("ddtArticoliOrdiniClienti")
    private OrdineCliente ordineCliente;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public DdtArticoloOrdineClienteKey getId() {
        return id;
    }

    public void setId(DdtArticoloOrdineClienteKey id) {
        this.id = id;
    }

    public DdtArticolo getDdtArticolo() {
        return ddtArticolo;
    }

    public void setDdtArticolo(DdtArticolo ddtArticolo) {
        this.ddtArticolo = ddtArticolo;
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
        result.append("ddtId: " + id.ddtId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", uuid: " + id.uuid);
        result.append(", ordineClienteId: " + id.ordineClienteId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
