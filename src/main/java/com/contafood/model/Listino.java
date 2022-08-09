package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"listiniAssociati", "listiniPrezzi", "listiniPrezziVariazioni", "clienti"})
@Entity
@Table(name = "listino")
public class Listino {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipologia")
    private String tipologia;

    @Column(name = "blocca_prezzi")
    private Boolean bloccaPrezzi;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoPrezzo> listiniPrezzi;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoPrezzoVariazione> listiniPrezziVariazioni;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<Cliente> clienti;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: ").append(id);
        result.append(", nome: ").append(nome);
        result.append(", tipologia: ").append(tipologia);
        result.append(", bloccaPrezzi: ").append(bloccaPrezzi);
        result.append(", note: ").append(note);
        result.append(", dataInserimento: ").append(dataInserimento);
        result.append(", dataAggiornamento: ").append(dataAggiornamento);
        result.append("}");

        return result.toString();

    }
}
