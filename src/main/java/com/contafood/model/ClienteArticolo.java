package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@Entity
@Table(name = "cliente_articolo")
public class ClienteArticolo implements Serializable {

    private static final long serialVersionUID = -6921233882488253317L;

    @EmbeddedId
    ClienteArticoloKey id;

    @ManyToOne
    @MapsId("id_cliente")
    @JoinColumn(name = "id_cliente")
    @JsonIgnoreProperties("clienteArticoli")
    private Cliente cliente;

    @ManyToOne
    @MapsId("id_articolo")
    @JoinColumn(name = "id_articolo")
    @JsonIgnoreProperties("clienteArticoli")
    private Articolo articolo;

    public ClienteArticoloKey getId() {
        return id;
    }

    public void setId(ClienteArticoloKey id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("clienteId: " + id.clienteId);
        result.append(", articoloId: " + id.articoloId);
        result.append("}");

        return result.toString();
    }
}
