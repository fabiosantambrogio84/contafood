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
@Table(name = "v_produzione")
public class VProduzione {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_produzione")
    private Long idProduzione;

    @Column(name = "codice_produzione")
    private String codiceProduzione;

    @Column(name = "data_produzione")
    private Date dataProduzione;

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

    @Column(name = "num_confezioni_prodotte")
    private Integer numConfezioniProdotte;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "ricetta")
    private String ricetta;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idProduzione: " + idProduzione);
        result.append(", codiceProduzione: " + codiceProduzione);
        result.append(", dataProduzione: " + dataProduzione);
        result.append(", idConfezione: " + idConfezione);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", idArticolo: " + idArticolo);
        result.append(", codiceArticolo: " + codiceArticolo);
        result.append(", descrizioneArticolo: " + descrizioneArticolo);
        result.append(", numConfezioniProdotte: " + numConfezioniProdotte);
        result.append(", quantita: " + quantita);
        result.append(", ricetta: " + ricetta);
        result.append("}");

        return result.toString();
    }
}
