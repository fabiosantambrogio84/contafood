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
@Table(name = "v_produzione_confezione")
public class VProduzioneConfezione {

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

    @Column(name = "id_confezione")
    private Long idConfezione;

    @Column(name = "num_confezioni")
    private Integer numConfezioni;

    @Column(name = "num_confezioni_prodotte")
    private Integer numConfezioniProdotte;

    @Column(name = "lotto_confezione")
    private String lottoConfezione;

    @Column(name = "tipo_confezione")
    private String tipoConfezione;

    @Column(name = "lotto_produzione")
    private String lottoProduzione;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "codice_articolo")
    private String codiceArticolo;

    @Column(name = "descrizione_articolo")
    private String descrizioneArticolo;

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
                ", idConfezione: " + idConfezione +
                ", numConfezioni: " + numConfezioni +
                ", numConfezioniProdotte: " + numConfezioniProdotte +
                ", lottoConfezione: " + lottoConfezione +
                ", tipoConfezione: " + tipoConfezione +
                ", lottoProduzione: " + lottoProduzione +
                ", scadenza: " + scadenza +
                ", idArticolo: " + idArticolo +
                ", codiceArticolo: " + codiceArticolo +
                ", descrizioneArticolo: " + descrizioneArticolo +
                ", idIngrediente: " + idIngrediente +
                ", codiceIngrediente: " + codiceIngrediente +
                ", descrizioneIngrediente: " + descrizioneIngrediente +
                ", quantita: " + quantita +
                ", ricetta: " + ricetta +
                "}";
    }
}
