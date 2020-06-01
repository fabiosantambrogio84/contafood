package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produzione_confezione")
public class ProduzioneConfezione implements Serializable {

    private static final long serialVersionUID = -7611851738261520234L;

    @EmbeddedId
    ProduzioneConfezioneKey id;

    @ManyToOne
    @MapsId("id_produzione")
    @JoinColumn(name = "id_produzione")
    @JsonIgnoreProperties("produzioneConfezioni")
    private Produzione produzione;

    @ManyToOne
    @MapsId("id_confezione")
    @JoinColumn(name = "id_confezione")
    @JsonIgnoreProperties("produzioneConfezioni")
    private Confezione confezione;

    @Column(name = "num_confezioni")
    private Integer numConfezioni;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "peso")
    private Float peso;

    public ProduzioneConfezioneKey getId() {
        return id;
    }

    public void setId(ProduzioneConfezioneKey id) {
        this.id = id;
    }

    public Produzione getProduzione() {
        return produzione;
    }

    public void setProduzione(Produzione produzione) {
        this.produzione = produzione;
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

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("produzioneId: " + id.produzioneId);
        result.append(", confezioneId: " + id.confezioneId);
        result.append(", numConfezioni: " + numConfezioni);
        result.append(", lotto: " + lotto);
        result.append(", peso: " + peso);
        result.append("}");

        return result.toString();
    }
}
