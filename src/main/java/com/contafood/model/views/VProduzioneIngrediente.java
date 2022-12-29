package com.contafood.model.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@EqualsAndHashCode()
@Entity
@Table(name = "v_produzione_ingrediente")
public class VProduzioneIngrediente {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_produzione")
    private Long idProduzione;

    @Column(name = "codice_produzione")
    private String codiceProduzione;

    @Column(name = "data_produzione")
    private Date dataProduzione;

    @Column(name = "tipologia")
    private String tipologia;

    @Column(name = "lotto_produzione")
    private String lottoProduzione;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "lotto_ingrediente")
    private String lottoIngrediente;

    @Column(name = "id_ingrediente")
    private Long idIngrediente;

    @Column(name = "codice_ingrediente")
    private String codiceIngrediente;

    @Column(name = "descrizione_ingrediente")
    private String descrizioneIngrediente;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "ricetta")
    private String ricetta;

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", idProduzione: " + idProduzione +
                ", codiceProduzione: " + codiceProduzione +
                ", dataProduzione: " + dataProduzione +
                ", tipologia: " + tipologia +
                ", lottoProduzione: " + lottoProduzione +
                ", scadenza: " + scadenza +
                ", lottoIngrediente: " + lottoIngrediente +
                ", idIngrediente: " + idIngrediente +
                ", codiceIngrediente: " + codiceIngrediente +
                ", descrizioneIngrediente: " + descrizioneIngrediente +
                ", quantita: " + quantita +
                ", ricetta: " + ricetta +
                "}";
    }
}
