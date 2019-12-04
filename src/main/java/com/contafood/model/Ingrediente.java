package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "ricettaIngredienti")
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
    private BigDecimal prezzo;

    @Column(name = "unita_di_misura")
    private String unitaDiMisura;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "ingrediente")
    @JsonIgnore
    Set<RicettaIngrediente> ricettaIngredienti = new HashSet<>();

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

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public String getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public void setUnitaDiMisura(String unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
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
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", descrizione: " + descrizione);
        result.append(", prezzo: " + prezzo);
        result.append(", unitaDiMisura: " + unitaDiMisura);
        result.append(", fornitore: " + fornitore);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", note: " + note);
        result.append("}");

        return result.toString();

    }
}
