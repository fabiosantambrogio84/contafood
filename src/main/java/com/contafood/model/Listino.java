package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
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

    public List<ListinoAssociato> getListiniAssociati() {
        return listiniAssociati;
    }

    public void setListiniAssociati(List<ListinoAssociato> listiniAssociati) {
        this.listiniAssociati = listiniAssociati;
    }

    public List<ListinoPrezzo> getListiniPrezzi() {
        return listiniPrezzi;
    }

    public void setListiniPrezzi(List<ListinoPrezzo> listiniPrezzi) {
        this.listiniPrezzi = listiniPrezzi;
    }

    public List<ListinoPrezzoVariazione> getListiniPrezziVariazioni() {
        return listiniPrezziVariazioni;
    }

    public void setListiniPrezziVariazioni(List<ListinoPrezzoVariazione> listiniPrezziVariazioni) {
        this.listiniPrezziVariazioni = listiniPrezziVariazioni;
    }

    public List<Cliente> getClienti() {
        return clienti;
    }

    public void setClienti(List<Cliente> clienti) {
        this.clienti = clienti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", tipologia: " + tipologia);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();

    }
}
