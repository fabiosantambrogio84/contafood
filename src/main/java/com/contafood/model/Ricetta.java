package com.contafood.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "tempo_preparazione")
    private String tempoPreparazione;

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

    @OneToMany(mappedBy = "ricetta", cascade = CascadeType.ALL)
    private Set<RicettaIngrediente> ricettaIngredienti;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(String tempoPreparazione) {
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

    @Override
    public int hashCode() {
        return Objects.hash(id, codice, nome, categoria, tempoPreparazione, numeroPorzioni, costoIngredienti, costoPreparazione, costoTotale, preparazione, allergeni, valoriNutrizionali, note, ricettaIngredienti);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Ricetta that = (Ricetta) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(codice, that.codice) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(categoria, that.categoria) &&
                Objects.equals(tempoPreparazione, that.tempoPreparazione) &&
                Objects.equals(numeroPorzioni, that.numeroPorzioni) &&
                Objects.equals(costoIngredienti, that.costoIngredienti) &&
                Objects.equals(costoPreparazione, that.costoPreparazione) &&
                Objects.equals(costoTotale, that.costoTotale) &&
                Objects.equals(preparazione, that.preparazione) &&
                Objects.equals(allergeni, that.allergeni) &&
                Objects.equals(valoriNutrizionali, that.valoriNutrizionali) &&
                Objects.equals(note, that.note) &&
                Objects.equals(ricettaIngredienti, that.ricettaIngredienti);
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
        result.append(", ricettaIngredienti: [");
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
