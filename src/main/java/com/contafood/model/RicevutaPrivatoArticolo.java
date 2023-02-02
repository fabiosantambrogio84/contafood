package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"ricevutaPrivatoArticoliOrdiniClienti"})
@Entity
@Table(name = "ricevuta_privato_articolo")
public class RicevutaPrivatoArticolo implements Serializable {

    private static final long serialVersionUID = 802397943709735004L;

    @EmbeddedId
    RicevutaPrivatoArticoloKey id;

    @ManyToOne
    @MapsId("id_ricevuta_privato")
    @JoinColumn(name = "id_ricevuta_privato")
    @JsonIgnoreProperties("ricevutaPrivatoArticoli")
    private RicevutaPrivato ricevutaPrivato;

    @ManyToOne
    @MapsId("id_articolo")
    @JoinColumn(name = "id_articolo")
    @JsonIgnoreProperties("ricevutaPrivatoArticoli")
    private Articolo articolo;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "scadenza")
    private Date scadenza;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "numero_pezzi")
    private Integer numeroPezzi;

    @Column(name = "numero_pezzi_da_evadere")
    private Integer numeroPezziDaEvadere;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "prezzo_iva")
    private BigDecimal prezzoIva;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "costo")
    private BigDecimal costo;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Transient
    private List<Long> idOrdiniClienti;

    @OneToMany(mappedBy = "ricevutaPrivatoArticolo")
    @JsonIgnoreProperties("ricevutaPrivatoArticolo")
    private Set<RicevutaPrivatoArticoloOrdineCliente> ricevutaPrivatoArticoliOrdiniClienti = new HashSet<>();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ricevutaPrivatoId: " + id.ricevutaPrivatoId);
        result.append(", articoloId: " + id.articoloId);
        result.append(", lotto: " + lotto);
        result.append(", scadenza: " + scadenza);
        result.append(", quantita: " + quantita);
        result.append(", numeroPezzi: " + numeroPezzi);
        result.append(", numeroPezziDaEvadere: " + numeroPezziDaEvadere);
        result.append(", prezzo: " + prezzo);
        result.append(", prezzoIva: " + prezzoIva);
        result.append(", sconto: " + sconto);
        result.append(", imponibile: " + imponibile);
        result.append(", costo: " + costo);
        result.append(", totale: " + totale);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append(", idOrdiniClienti: " + idOrdiniClienti);
        result.append("}");

        return result.toString();
    }
}
