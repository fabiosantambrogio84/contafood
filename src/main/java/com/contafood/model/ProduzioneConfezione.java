package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "produzione_confezione")
public class ProduzioneConfezione implements Serializable {

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("produzioneId: " + id.produzioneId);
        result.append(", confezioneId: " + id.confezioneId);
        result.append(", numConfezioni: " + numConfezioni);
        result.append("}");

        return result.toString();
    }
}
