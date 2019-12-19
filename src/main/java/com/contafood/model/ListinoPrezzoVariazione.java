package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "listino_prezzo_variazione")
public class ListinoPrezzoVariazione {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipologia_variazione_prezzo")
    private String tipologiaVariazionePrezzo;

    @Column(name = "variazione_prezzo")
    private Float variazionePrezzo;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listino;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getDataAggiornamento() {
        return dataAggiornamento;
    }

    public void setDataAggiornamento(Timestamp dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
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

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", tipologiaVariazionePrezzo: " + tipologiaVariazionePrezzo);
        result.append(", variazionePrezzo: " + variazionePrezzo);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
