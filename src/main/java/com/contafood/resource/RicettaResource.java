package com.contafood.resource;

import com.contafood.model.Ricetta;
import com.contafood.model.RicettaIngrediente;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RicettaResource {

    private Ricetta ricetta;

    private String costoOrarioPreparazione;

    public Ricetta getRicetta() {
        return ricetta;
    }

    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    public String getCostoOrarioPreparazione() {
        return costoOrarioPreparazione;
    }

    public void setCostoOrarioPreparazione(String costoOrarioPreparazione) {
        this.costoOrarioPreparazione = costoOrarioPreparazione;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricetta: " + ricetta.toString());
        result.append(", costoOrarioPreparazione: " + costoOrarioPreparazione);
        result.append("}");

        return result.toString();
    }
}
