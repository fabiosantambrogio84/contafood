package com.contafood.model.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@EqualsAndHashCode()
@Entity
@Table(name = "v_produzione")
public class VProduzione {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_produzione")
    private Long idProduzione;

    @Column(name = "codice_produzione")
    private Integer codiceProduzione;

    @Column(name = "data_produzione")
    private Date dataProduzione;

    @Column(name = "tipologia")
    private String tipologia;

    @Column(name = "id_confezione")
    private Long idConfezione;

    @Column(name = "lotto")
    private String lotto;

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

    @Column(name = "num_confezioni_prodotte")
    private Integer numConfezioniProdotte;

    @Column(name = "quantita")
    private BigDecimal quantita;

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
                ", lotto: " + lotto +
                ", scadenza: " + scadenza +
                ", idArticolo: " + idArticolo +
                ", codiceArticolo: " + codiceArticolo +
                ", descrizioneArticolo: " + descrizioneArticolo +
                ", idIngrediente: " + idIngrediente +
                ", codiceIngrediente: " + codiceIngrediente +
                ", descrizioneIngrediente: " + descrizioneIngrediente +
                ", numConfezioniProdotte: " + numConfezioniProdotte +
                ", quantita: " + quantita +
                ", ricetta: " + ricetta +
                "}";
    }
}
