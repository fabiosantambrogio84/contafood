package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ricetta_ingrediente")
public class RicettaIngrediente implements Serializable {

    @EmbeddedId
    RicettaIngredienteKey id;

    @ManyToOne
    @MapsId("id_ricetta")
    @JoinColumn(name = "id_ricetta")
    @JsonIgnoreProperties("ricettaIngredienti")
    private Ricetta ricetta;

    @ManyToOne
    @MapsId("id_ingrediente")
    @JoinColumn(name = "id_ingrediente")
    @JsonIgnoreProperties("ricettaIngredienti")
    private Ingrediente ingrediente;

    @Column(name = "quantita")
    private Float quantita;

    public RicettaIngredienteKey getId() {
        return id;
    }

    public void setId(RicettaIngredienteKey id) {
        this.id = id;
    }

    public Ricetta getRicetta() {
        return ricetta;
    }

    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Float getQuantita() {
        return quantita;
    }

    public void setQuantita(Float quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricettaId: " + id.ricettaId);
        result.append(", ingredienteId: " + id.ingredienteId);
        result.append(", quantita: " + quantita);
        result.append("}");

        return result.toString();
    }
}
