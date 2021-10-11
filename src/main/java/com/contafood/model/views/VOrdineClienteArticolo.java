package com.contafood.model.views;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@EqualsAndHashCode()
@Entity
@Table(name = "v_ordine_cliente_articolo")
public class VOrdineClienteArticolo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_ordine_cliente")
    private Long idOrdineCliente;

    @Column(name = "data_consegna")
    private Date dataConsegna;

    @Column(name = "id_articolo")
    private Long idArticolo;

    @Column(name = "codice_articolo")
    private String codiceArticolo;

    @Column(name = "descrizione_articolo")
    private String descrizioneArticolo;

    @Column(name = "id_fornitore")
    private Long idFornitore;

    @Column(name = "num_ordinati")
    private Integer numeroPezziOrdinati;

    @Column(name = "num_da_evadere")
    private Integer numeroPezziDaEvadere;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdOrdineCliente() {
        return idOrdineCliente;
    }

    public void setIdOrdineCliente(Long idOrdineCliente) {
        this.idOrdineCliente = idOrdineCliente;
    }

    public Date getDataConsegna() {
        return dataConsegna;
    }

    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
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

    public Long getIdFornitore() {
        return idFornitore;
    }

    public void setIdFornitore(Long idFornitore) {
        this.idFornitore = idFornitore;
    }

    public Integer getNumeroPezziOrdinati() {
        return numeroPezziOrdinati;
    }

    public void setNumeroPezziOrdinati(Integer numeroPezziOrdinati) {
        this.numeroPezziOrdinati = numeroPezziOrdinati;
    }

    public Integer getNumeroPezziDaEvadere() {
        return numeroPezziDaEvadere;
    }

    public void setNumeroPezziDaEvadere(Integer numeroPezziDaEvadere) {
        this.numeroPezziDaEvadere = numeroPezziDaEvadere;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idOrdineCliente: " + idOrdineCliente);
        result.append(", dataConsegna: " + dataConsegna);
        result.append(", idArticolo: " + idArticolo);
        result.append(", codiceArticolo: " + codiceArticolo);
        result.append(", descrizioneArticolo: " + descrizioneArticolo);
        result.append(", idFornitore: " + idFornitore);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", numeroPezziDaEvadere: " + numeroPezziDaEvadere);
        result.append("}");

        return result.toString();
    }
}
