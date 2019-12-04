package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(exclude = {"ricettaIngredienti", "produzioni"})
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

    @Column(name = "peso_totale")
    private Float pesoTotale;

    @Column(name = "scadenza_giorni")
    private Integer scadenzaGiorni;

    @Column(name = "costo_ingredienti")
    private BigDecimal costoIngredienti;

    @Column(name = "costo_preparazione")
    private BigDecimal costoPreparazione;

    @Column(name = "costo_totale")
    private BigDecimal costoTotale;

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

    @OneToMany(mappedBy = "ricetta")
    @JsonIgnore
    private List<Produzione> produzioni;

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

    public Float getPesoTotale() {
        return pesoTotale;
    }

    public void setPesoTotale(Float pesoTotale) {
        this.pesoTotale = pesoTotale;
    }

    public Integer getScadenzaGiorni(){return scadenzaGiorni;}

    public void setScadenzaGiorni(Integer scadenzaGiorni){
        this.scadenzaGiorni = scadenzaGiorni;
    }

    public BigDecimal getCostoIngredienti() {
        return costoIngredienti;
    }

    public void setCostoIngredienti(BigDecimal costoIngredienti) {
        this.costoIngredienti = costoIngredienti;
    }

    public BigDecimal getCostoPreparazione() {
        return costoPreparazione;
    }

    public void setCostoPreparazione(BigDecimal costoPreparazione) {
        this.costoPreparazione = costoPreparazione;
    }

    public BigDecimal getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(BigDecimal costoTotale) {
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

    public List<Produzione> getProduzioni() {
        return produzioni;
    }

    public void setProduzioni(List<Produzione> produzioni) {
        this.produzioni = produzioni;
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
        result.append(", pesoTotale: " + pesoTotale);
        result.append(", scadenzaGiorni: " + scadenzaGiorni);
        result.append(", costoIngredienti: " + costoIngredienti);
        result.append(", costoPreparazione: " + costoPreparazione);
        result.append(", costoTotale: " + costoTotale);
        result.append(", preparazione: " + preparazione);
        result.append(", allergeni: " + allergeni);
        result.append(", valoriNutrizionali: " + valoriNutrizionali);
        result.append(", note: " + note);
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
