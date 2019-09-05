package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"produzioneIngredienti"})
@Entity
@Table(name = "produzione")
public class Produzione {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private String codice;

    @ManyToOne
    @JoinColumn(name="id_ricetta")
    private Ricetta ricetta;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private CategoriaRicetta categoria;

    @ManyToOne
    @JoinColumn(name="id_confezione")
    private Confezione confezione;

    @Column(name = "num_confezioni")
    private Integer numConfezioni;

    @OneToMany(mappedBy = "produzione")
    @JsonIgnoreProperties("produzione")
    private Set<ProduzioneIngrediente> produzioneIngredienti = new HashSet<>();

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

    public Ricetta getRicetta() {
        return ricetta;
    }

    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }

    public CategoriaRicetta getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRicetta categoria) {
        this.categoria = categoria;
    }

    public Confezione getConfezione() {
        return confezione;
    }

    public void setConfezione(Confezione confezione) {
        this.confezione = confezione;
    }

    public Integer getNumConfezioni() {
        return numConfezioni;
    }

    public void setNumConfezioni(Integer numConfezioni) {
        this.numConfezioni = numConfezioni;
    }

    public Set<ProduzioneIngrediente> getProduzioneIngredienti() {
        return produzioneIngredienti;
    }

    public void setProduzioneIngredienti(Set<ProduzioneIngrediente> produzioneIngredienti) {
        this.produzioneIngredienti = produzioneIngredienti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", codice: " + codice);
        result.append(", ricetta: " + ricetta);
        result.append(", categoria: " + categoria);
        result.append(", confezione: " + confezione);
        result.append(", numConfezioni: " + numConfezioni);
        result.append(", ingredienti: [");
        for(ProduzioneIngrediente produzioneIngrediente: produzioneIngredienti){
            result.append("{");
            result.append(produzioneIngrediente.toString());
            result.append("}");
        }
        result.append("]");
        result.append("}");

        return result.toString();
    }
}
