package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"produzioneIngredienti", "produzioneConfezioni"})
@Entity
@Table(name = "produzione")
public class Produzione {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice")
    private Integer codice;

    @Column(name = "data_produzione")
    private Date dataProduzione;

    @Column(name = "tipologia")
    private String tipologia;

    @ManyToOne
    @JoinColumn(name="id_ricetta")
    private Ricetta ricetta;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private CategoriaRicetta categoria;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "lotto_anno")
    @JsonIgnoreProperties
    private Integer lottoAnno;

    @Column(name = "lotto_giorno")
    @JsonIgnoreProperties
    private Integer lottoGiorno;

    @Column(name = "lotto_numero_progressivo")
    @JsonIgnoreProperties
    private Integer lottoNumeroProgressivo;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "tempo_impiegato")
    private Float tempoImpiegato;

    @Column(name = "quantita_totale")
    private Float quantitaTotale;

    @Column(name = "numero_confezioni")
    private Integer numeroConfezioni;

    @Column(name = "scopo")
    private String scopo;

    @Column(name = "film_chiusura")
    private String filmChiusura;

    @Column(name = "lotto_film_chiusura")
    private String lottoFilmChiusura;

    @Column(name = "barcode_ean_13")
    private String barcodeEan13;

    @Column(name = "barcode_ean_128")
    private String barcodeEan128;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @OneToMany(mappedBy = "produzione")
    @JsonIgnoreProperties("produzione")
    private Set<ProduzioneIngrediente> produzioneIngredienti = new HashSet<>();

    @OneToMany(mappedBy = "produzione")
    @JsonIgnoreProperties("produzione")
    private Set<ProduzioneConfezione> produzioneConfezioni = new HashSet<>();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: ").append(id);
        result.append(", codice: ").append(codice);
        result.append(", dataProduzione: ").append(dataProduzione);
        result.append(", tipologia: ").append(tipologia);
        result.append(", ricetta: ").append(ricetta);
        result.append(", categoria: ").append(categoria);
        result.append(", lotto: ").append(lotto);
        result.append(", lottoAnno: ").append(lottoAnno);
        result.append(", lottoGiorno: ").append(lottoGiorno);
        result.append(", lottoNumeroProgressivo: ").append(lottoNumeroProgressivo);
        result.append(", scadenza: ").append(scadenza);
        result.append(", tempoImpiegato: ").append(tempoImpiegato);
        result.append(", quantitaTotale: ").append(quantitaTotale);
        result.append(", numeroConfezioni: ").append(numeroConfezioni);
        result.append(", scopo: ").append(scopo);
        result.append(", filmChiusura: ").append(filmChiusura);
        result.append(", lottoFilmChiusura: ").append(lottoFilmChiusura);
        result.append(", barcodeEan13: ").append(barcodeEan13);
        result.append(", barcodeEan128: ").append(barcodeEan128);
        result.append(", dataInserimento: ").append(dataInserimento);
        result.append(", dataAggiornamento: ").append(dataAggiornamento);
        result.append(", ingredienti: [");
        for(ProduzioneIngrediente produzioneIngrediente: produzioneIngredienti){
            result.append("{");
            result.append(produzioneIngrediente.toString());
            result.append("}");
        }
        result.append("]");
        result.append(", confezioni: [");
        for(ProduzioneConfezione produzioneConfezione: produzioneConfezioni){
            result.append("{");
            result.append(produzioneConfezione.toString());
            result.append("}");
        }
        result.append("]");
        result.append("}");

        return result.toString();
    }
}
