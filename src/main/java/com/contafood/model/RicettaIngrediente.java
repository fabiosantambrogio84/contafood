package com.contafood.model;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.awt.print.Book;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ricetta_ingrediente")
public class RicettaIngrediente implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_ricetta")
    private Ricetta ricetta;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_ingrediente")
    private Ingrediente ingrediente;

    @Column(name = "quantita")
    private Float quantita;

    public RicettaIngrediente(Ricetta ricetta, Ingrediente ingrediente) {
        this.ricetta = ricetta;
        this.ingrediente = ingrediente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ricetta.hashCode(), ingrediente.hashCode(), quantita);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RicettaIngrediente that = (RicettaIngrediente) obj;
        return Objects.equals(ricetta, that.ricetta) &&
                Objects.equals(ingrediente, that.ingrediente) &&
                Objects.equals(quantita, that.quantita);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricettaId: " + (ricetta != null ? ricetta.getId() : ""));
        result.append(", ingredienteId: " + (ingrediente != null ? ingrediente.getId() : ""));
        result.append(", quantita: " + quantita);
        result.append("}");

        return result.toString();
    }
}
