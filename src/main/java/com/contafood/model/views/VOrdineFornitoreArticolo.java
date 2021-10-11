package com.contafood.model.views;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@EqualsAndHashCode()
@Entity
@Table(name = "v_ordine_fornitore_articolo")
public class VOrdineFornitoreArticolo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "codice_articolo")
    private String codiceArticolo;

    @Column(name = "descrizione_articolo")
    private String descrizioneArticolo;

    @Column(name = "num_ordinati")
    private Integer numeroPezziOrdinati;

    @Column(name = "id_ordini_clienti")
    private String idOrdiniClienti;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Long idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    public Integer getNumeroPezziOrdinati() {
        return numeroPezziOrdinati;
    }

    public void setNumeroPezziOrdinati(Integer numeroPezziOrdinati) {
        this.numeroPezziOrdinati = numeroPezziOrdinati;
    }

    public String getIdOrdiniClienti() {
        return idOrdiniClienti;
    }

    public void setIdOrdiniClienti(String idOrdiniClienti) {
        this.idOrdiniClienti = idOrdiniClienti;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idArticolo: " + idArticolo);
        result.append(", codiceArticolo: " + codiceArticolo);
        result.append(", descrizioneArticolo: " + descrizioneArticolo);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", idOrdiniClienti: " + idOrdiniClienti);
        result.append("}");

        return result.toString();
    }
}
