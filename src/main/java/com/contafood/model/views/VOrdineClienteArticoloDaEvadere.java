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
@Table(name = "v_ordine_cliente_articolo_da_evadere")
public class VOrdineClienteArticoloDaEvadere {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_ordine_cliente")
    private Integer idOrdineCliente;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "progressivo")
    private Integer progressivo;

    @Column(name = "anno_contabile")
    private Integer annoContabile;

    @Column(name = "data")
    private Date data;

    @Column(name = "id_articolo")
    private Integer idArticolo;

    @Column(name = "codice_articolo")
    private String codiceArticolo;

    @Column(name = "descrizione_articolo")
    private String descrizioneArticolo;

    @Column(name = "num_ordinati")
    private Integer numOrdinati;

    @Column(name = "num_da_evadere")
    private Integer numDaEvadere;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", idOrdineCliente: " + idOrdineCliente +
                ", descrizione: " + descrizione +
                ", progressivo: " + progressivo +
                ", annoContabile: " + annoContabile +
                ", data: " + data +
                ", idArticolo: " + idArticolo +
                ", codiceArticolo: " + codiceArticolo +
                ", descrizioneArticolo: " + descrizioneArticolo +
                ", numOrdinati: " + numOrdinati +
                ", numDaEvadere: " + numDaEvadere +
                ", idCliente: " + idCliente +
                "}";
    }
}
