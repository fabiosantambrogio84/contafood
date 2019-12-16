package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"listiniAssociati", "listiniPrezzi", "listiniPrezziVariazioni"})
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

    @Column(name = "tipologia_variazione_prezzo")
    private String tipologiaVariazionePrezzo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoPrezzo> listiniPrezzi;

    @OneToMany(mappedBy = "listino")
    private List<ListinoPrezzoVariazione> listiniPrezziVariazioni;

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

    public String getTipologiaVariazionePrezzo() {
        return tipologiaVariazionePrezzo;
    }

    public void setTipologiaVariazionePrezzo(String tipologiaVariazionePrezzo) {
        this.tipologiaVariazionePrezzo = tipologiaVariazionePrezzo;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", tipologia: " + tipologia);
        result.append(", tipologiaVariazionePrezzo: " + tipologiaVariazionePrezzo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
