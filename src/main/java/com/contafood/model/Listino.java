package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"listini", "listiniAssociati", "listiniPrezzi"})
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

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listinoRiferimento;

    @OneToMany(mappedBy="listinoRiferimento")
    @JsonIgnore
    private List<Listino> listini;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoAssociato> listiniAssociati;

    @OneToMany(mappedBy = "listino")
    @JsonIgnore
    private List<ListinoPrezzo> listiniPrezzi;

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

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Listino getListinoRiferimento() {
        return listinoRiferimento;
    }

    public void setListinoRiferimento(Listino listinoRiferimento) {
        this.listinoRiferimento = listinoRiferimento;
    }

    public List<Listino> getListini() {
        return listini;
    }

    public void setListini(List<Listino> listini) {
        this.listini = listini;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", tipologia: " + tipologia);
        result.append(", listinoRiferimento: " + listinoRiferimento);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
