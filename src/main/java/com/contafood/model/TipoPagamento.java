package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"clienti"})
@Getter
@Setter
@Entity
@Table(name = "tipo_pagamento")
public class TipoPagamento {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "scadenza_giorni")
    private Integer scadenzaGiorni;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @OneToMany(mappedBy = "tipoPagamento")
    @JsonIgnore
    private List<Cliente> clienti;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", descrizione: " + descrizione);
        result.append(", scadenzaGiorni: " + scadenzaGiorni);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
