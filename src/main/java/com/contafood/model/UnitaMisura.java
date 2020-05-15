package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"articoli", "notaAccreditoInfo"})
@Entity
@Table(name = "unita_misura")
public class UnitaMisura {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "etichetta")
    private String etichetta;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @OneToMany(mappedBy = "unitaMisura")
    @JsonIgnore
    private List<Articolo> articoli;

    @OneToMany(mappedBy = "unitaMisura")
    @JsonIgnore
    private List<NotaAccreditoRiga> notaAccreditoRiga;

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

    public String getEtichetta() {
        return etichetta;
    }

    public void setEtichetta(String etichetta) {
        this.etichetta = etichetta;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public List<Articolo> getArticoli() {
        return articoli;
    }

    public void setArticoli(List<Articolo> articoli) {
        this.articoli = articoli;
    }

    public List<NotaAccreditoRiga> getNotaAccreditoRiga() {
        return notaAccreditoRiga;
    }

    public void setNotaAccreditoRiga(List<NotaAccreditoRiga> notaAccreditoRiga) {
        this.notaAccreditoRiga = notaAccreditoRiga;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", etichetta: " + etichetta);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
