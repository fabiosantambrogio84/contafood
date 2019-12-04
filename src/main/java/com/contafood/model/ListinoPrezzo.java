package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "listino_prezzo")
public class ListinoPrezzo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listino;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Listino getListino() {
        return listino;
    }

    public void setListino(Listino listino) {
        this.listino = listino;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
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
        result.append(", listino: " + listino);
        result.append(", articolo: " + articolo);
        result.append(", prezzo: " + prezzo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
