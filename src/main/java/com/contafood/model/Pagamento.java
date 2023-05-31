package com.contafood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data")
    private Date data;

    @Column(name = "tipologia")
    private String tipologia;

    @ManyToOne
    @JoinColumn(name="id_tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name="id_ddt")
    private Ddt ddt;

    @ManyToOne
    @JoinColumn(name="id_ddt_acquisto")
    private DdtAcquisto ddtAcquisto;

    @ManyToOne
    @JoinColumn(name="id_nota_accredito")
    private NotaAccredito notaAccredito;

    @ManyToOne
    @JoinColumn(name="id_nota_reso")
    private NotaReso notaReso;

    @ManyToOne
    @JoinColumn(name="id_ricevuta_privato")
    private RicevutaPrivato ricevutaPrivato;

    @ManyToOne
    @JoinColumn(name="id_fattura")
    private Fattura fattura;

    @ManyToOne
    @JoinColumn(name="id_fattura_accom")
    private FatturaAccompagnatoria fatturaAccompagnatoria;

    @ManyToOne
    @JoinColumn(name="id_fattura_acquisto")
    private FatturaAcquisto fatturaAcquisto;

    @ManyToOne
    @JoinColumn(name="id_fattura_accom_acquisto")
    private FatturaAccompagnatoriaAcquisto fatturaAccompagnatoriaAcquisto;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "importo")
    private BigDecimal importo;

    @ManyToOne
    @JoinColumn(name="id_pagamento_aggregato")
    private PagamentoAggregato pagamentoAggregato;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Override
    public String toString() {

        return "{" +
                "id: " + id +
                ", data: " + data +
                ", tipologia: " + tipologia +
                ", descrizione: " + descrizione +
                ", importo: " + importo +
                ", note: " + note +
                ", dataInserimento: " + dataInserimento +
                "}";
    }
}
