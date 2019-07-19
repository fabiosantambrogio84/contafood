package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RicettaIngredienteKey implements Serializable {

    @Column(name = "id_ricetta")
    Long ricettaId;

    @Column(name = "id_ingrediente")
    Long ingredienteId;

    public RicettaIngredienteKey(){}

    public RicettaIngredienteKey(Long ricettaId, Long ingredienteId){
        this.ricettaId = ricettaId;
        this.ingredienteId = ingredienteId;
    }

    public Long getRicettaId() {
        return ricettaId;
    }

    public void setRicettaId(Long ricettaId) {
        this.ricettaId = ricettaId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ricettaId, ingredienteId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RicettaIngredienteKey that = (RicettaIngredienteKey) obj;
        return Objects.equals(ricettaId, that.ricettaId) &&
                Objects.equals(ingredienteId, that.ingredienteId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricettaId: " + ricettaId);
        result.append(", ingredienteId: " + ingredienteId);
        result.append("}");

        return result.toString();
    }
}
