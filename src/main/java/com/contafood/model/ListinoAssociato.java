package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "listino_associato")
public class ListinoAssociato implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listino;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public Listino getListino() {
        return listino;
    }

    public void setListino(Listino listino) {
        this.listino = listino;
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
        result.append("id: " + id);
        result.append(", cliente: " + cliente);
        result.append(", fornitore: " + fornitore);
        result.append(", listino: " + listino);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
