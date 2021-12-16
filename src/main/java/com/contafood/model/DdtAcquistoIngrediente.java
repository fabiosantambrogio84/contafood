package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ddt_acquisto_ingrediente")
public class DdtAcquistoIngrediente implements Serializable {

    private static final long serialVersionUID = 2911599584132134195L;

    @EmbeddedId
    DdtAcquistoIngredienteKey id;

    @ManyToOne
    @MapsId("id_ddt_acquisto")
    @JoinColumn(name = "id_ddt_acquisto")
    @JsonIgnoreProperties("ddtAcquistoIngredienti")
    private DdtAcquisto ddtAcquisto;

    @ManyToOne
    @MapsId("id_ingrediente")
    @JoinColumn(name = "id_ingrediente")
    @JsonIgnoreProperties("ddtAcquistoIngredienti")
    private Ingrediente ingrediente;

    @Column(name = "lotto")
    private String lotto;

    @Column(name = "data_scadenza")
    private Date dataScadenza;

    @Column(name = "quantita")
    private Float quantita;

    @Column(name = "numero_pezzi")
    private Integer numeroPezzi;

    @Column(name = "prezzo")
    private BigDecimal prezzo;

    @Column(name = "sconto")
    private BigDecimal sconto;

    @Column(name = "imponibile")
    private BigDecimal imponibile;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtAcquistoId: " + id.ddtAcquistoId);
        result.append(", ingredienteId: " + id.ingredienteId);
        result.append(", lotto: " + lotto);
        result.append(", dataScadenza: " + dataScadenza);
        result.append(", quantita: " + quantita);
        result.append(", numeroPezzi: " + numeroPezzi);
        result.append(", prezzo: " + prezzo);
        result.append(", sconto: " + sconto);
        result.append(", imponibile: " + imponibile);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
