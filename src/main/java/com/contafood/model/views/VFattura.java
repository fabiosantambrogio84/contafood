package com.contafood.model.views;

import com.contafood.model.Cliente;
import com.contafood.model.StatoFattura;
import com.contafood.model.TipoFattura;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode()
@Data
@Entity
@Table(name = "v_fattura")
public class VFattura {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "progressivo")
    private Integer progressivo;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "data")
    private Date data;

    @Column(name="id_tipo")
    private Long idTipo;

    @Column(name="tipo_codice")
    private String tipoCodice;

    @Column(name="id_cliente")
    private Long idCliente;

    @Column(name="cliente")
    private String cliente;

    @Column(name="cliente_email")
    private String clienteEmail;

    @Column(name="id_tipo_pagamento")
    private Long idTipoPagamento;

    @Column(name="id_agente")
    private Long idAgente;

    @Column(name="agente")
    private String agente;

    @Column(name="id_stato")
    private Long idStato;

    @Column(name="stato_codice")
    private String statoCodice;

    @Column(name = "spedito_ade")
    private Boolean speditoAde;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "note")
    private String note;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Column(name = "data_aggiornamento")
    private Timestamp dataAggiornamento;

    @Column(name = "id_articoli")
    private String idArticoli;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", progressivo: " + progressivo);
        result.append(", anno: " + anno);
        result.append(", data: " + data);
        result.append(", idTipo: " + idTipo);
        result.append(", tipoCodice: " + tipoCodice);
        result.append(", idCliente: " + idCliente);
        result.append(", cliente: " + cliente);
        result.append(", clienteEmail: " + clienteEmail);
        result.append(", idTipoPagamento: " + idTipoPagamento);
        result.append(", idAgente: " + idAgente);
        result.append(", agente: " + agente);
        result.append(", stato: " + idStato);
        result.append(", statoCodice: " + statoCodice);
        result.append(", speditoAde: " + speditoAde);
        result.append(", totaleAcconto: " + totaleAcconto);
        result.append(", totale: " + totale);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append(", dataAggiornamento: " + dataAggiornamento);
        result.append("}");

        return result.toString();
    }
}
