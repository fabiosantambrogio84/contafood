package com.contafood.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "prezzo")
    private Float prezzo;

    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL)
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public Set<RicettaIngrediente> getRicettaIngredienti() {
        return ricettaIngredienti;
    }

    public void setRicettaIngredienti(Set<RicettaIngrediente> ricettaIngredienti) {
        this.ricettaIngredienti = ricettaIngredienti;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codice, descrizione, prezzo, ricettaIngredienti);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Ingrediente that = (Ingrediente) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(codice, that.codice) &&
                Objects.equals(descrizione, that.descrizione) &&
                Objects.equals(prezzo, that.prezzo) &&
                Objects.equals(ricettaIngredienti, that.ricettaIngredienti);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", prezzo: " + prezzo);
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
