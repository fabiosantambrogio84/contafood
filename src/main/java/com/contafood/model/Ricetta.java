package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"ricettaIngredienti"})
@Entity
@Table(name = "ricetta")
public class Ricetta {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private CategoriaRicetta categoria;

    @Column(name = "tempo_preparazione")
    private Integer tempoPreparazione;

    @Column(name = "numero_porzioni")
    private Integer numeroPorzioni;

    @Column(name = "costo_ingredienti")
    private Float costoIngredienti;

    @Column(name = "costo_preparazione")
    private Float costoPreparazione;

    @Column(name = "costo_totale")
    private Float costoTotale;

    @Column(name = "preparazione")
    private String preparazione;

    @Column(name = "allergeni")
    private String allergeni;

    @Column(name = "valori_nutrizionali")
    private String valoriNutrizionali;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "ricetta")
    @JsonIgnoreProperties("ricetta")
    private Set<RicettaIngrediente> ricettaIngredienti = new HashSet<>();

    @JsonIgnore
    private Float costoOrarioPreparazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaRicetta getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRicetta categoria) {
        this.categoria = categoria;
    }

    public Integer getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(Integer tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public Integer getNumeroPorzioni() {
        return numeroPorzioni;
    }

    public void setNumeroPorzioni(Integer numeroPorzioni) {
        this.numeroPorzioni = numeroPorzioni;
    }

    public Float getCostoIngredienti() {
        return costoIngredienti;
    }

    public void setCostoIngredienti(Float costoIngredienti) {
        this.costoIngredienti = costoIngredienti;
    }

    public Float getCostoPreparazione() {
        return costoPreparazione;
    }

    public void setCostoPreparazione(Float costoPreparazione) {
        this.costoPreparazione = costoPreparazione;
    }

    public Float getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(Float costoTotale) {
        this.costoTotale = costoTotale;
    }

    public String getPreparazione() {
        return preparazione;
    }

    public void setPreparazione(String preparazione) {
        this.preparazione = preparazione;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public void setAllergeni(String allergeni) {
        this.allergeni = allergeni;
    }

    public String getValoriNutrizionali() {
        return valoriNutrizionali;
    }

    public void setValoriNutrizionali(String valoriNutrizionali) {
        this.valoriNutrizionali = valoriNutrizionali;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<RicettaIngrediente> getRicettaIngredienti() {
        return ricettaIngredienti;
    }

    public void setRicettaIngredienti(Set<RicettaIngrediente> ricettaIngredienti) {
        this.ricettaIngredienti = ricettaIngredienti;
    }

    public Float getCostoOrarioPreparazione() {
        return costoOrarioPreparazione;
    }

    public void setCostoOrarioPreparazione(Float costoOrarioPreparazione) {
        this.costoOrarioPreparazione = costoOrarioPreparazione;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", nome: " + nome);
        result.append(", categoria: " + categoria);
        result.append(", tempoPreparazione: " + tempoPreparazione);
        result.append(", numeroPorzioni: " + numeroPorzioni);
        result.append(", costoIngredienti: " + costoIngredienti);
        result.append(", costoPreparazione: " + costoPreparazione);
        result.append(", costoTotale: " + costoTotale);
        result.append(", preparazione: " + preparazione);
        result.append(", allergeni: " + allergeni);
        result.append(", valoriNutrizionali: " + valoriNutrizionali);
        result.append(", note: " + note);
        result.append(", costoOrarioPreparazione: " + costoOrarioPreparazione);
        result.append(", ingredienti: [");
        for(RicettaIngrediente ricettaIngrediente: ricettaIngredienti){
            result.append("{");
            result.append(ricettaIngrediente.toString());
            result.append("}");
        }
        result.append("]");
        result.append("}");

        return result.toString();
    }
}
