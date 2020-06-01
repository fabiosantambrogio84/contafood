package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "produzione_ingrediente")
public class ProduzioneIngrediente implements Serializable {

    private static final long serialVersionUID = -6513863375941482733L;

    @EmbeddedId
    ProduzioneIngredienteKey id;

    @ManyToOne
    @MapsId("id_produzione")
    @JoinColumn(name = "id_produzione")
    @JsonIgnoreProperties("produzioneIngredienti")
    private Produzione produzione;

    @ManyToOne
    @MapsId("id_ingrediente")
    @JoinColumn(name = "id_ingrediente")
    @JsonIgnoreProperties("produzioneIngredienti")
    private Ingrediente ingrediente;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "quantita")
    private Float quantita;

    public ProduzioneIngredienteKey getId() {
        return id;
    }

    public void setId(ProduzioneIngredienteKey id) {
        this.id = id;
    }

    public Produzione getProduzione() {
        return produzione;
    }

    public void setProduzione(Produzione produzione) {
        this.produzione = produzione;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
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
        result.append("produzioneId: " + id.produzioneId);
        result.append(", ingredienteId: " + id.ingredienteId);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", quantita: " + quantita);
        result.append("}");

        return result.toString();
    }
}
