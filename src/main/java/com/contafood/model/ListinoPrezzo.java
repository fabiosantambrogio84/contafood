package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode
@Entity
@Table(name = "listino_prezzo")
public class ListinoPrezzo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listino;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "tipologia_variazione_prezzo")
    private String tipologiaVariazionePrezzo;

    @Column(name = "variazione_prezzo")
    private Float variazionePrezzo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Listino getListino() {
        return listino;
    }

    public void setListino(Listino listino) {
        this.listino = listino;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public String getTipologiaVariazionePrezzo() {
        return tipologiaVariazionePrezzo;
    }

    public void setTipologiaVariazionePrezzo(String tipologiaVariazionePrezzo) {
        this.tipologiaVariazionePrezzo = tipologiaVariazionePrezzo;
    }

    public Float getVariazionePrezzo() {
        return variazionePrezzo;
    }

    public void setVariazionePrezzo(Float variazionePrezzo) {
        this.variazionePrezzo = variazionePrezzo;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", listino: " + listino);
        result.append(", articolo: " + articolo);
        result.append(", prezzo: " + prezzo);
        result.append(", tipologiaVariazionePrezzo: " + tipologiaVariazionePrezzo);
        result.append(", variazionePrezzo: " + variazionePrezzo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
