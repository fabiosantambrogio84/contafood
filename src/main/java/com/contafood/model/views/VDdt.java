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
@Table(name = "v_ddt")
public class VDdt {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "anno_contabile")
    private Integer annoContabile;

    @Column(name = "progressivo")
    private Integer progressivo;

    @Column(name = "data")
    private Date data;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "cliente_email")
    private String clienteEmail;

    @Column(name = "id_agente")
    private Long idAgente;

    @Column(name = "agente")
    private String agente;

    @Column(name = "id_autista")
    private Long idAutista;

    @Column(name = "id_stato")
    private Long idStato;

    @Column(name = "stato")
    private String stato;

    @Column(name = "fatturato")
    private Boolean fatturato;

    @Column(name = "totale_acconto")
    private BigDecimal totaleAcconto;

    @Column(name = "totale")
    private BigDecimal totale;

    @Column(name = "totale_imponibile")
    private BigDecimal totaleImponibile;

    @Column(name = "totale_costo")
    private BigDecimal totaleCosto;

    @Column(name = "totale_iva")
    private BigDecimal totaleIva;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", annoContabile: " + annoContabile);
        result.append(", progressivo: " + progressivo);
        result.append(", data: " + data);
        result.append(", idCliente: " + idCliente);
        result.append(", cliente: " + cliente);
        result.append(", clienteEmail: " + clienteEmail);
        result.append(", idAgente: " + idAgente);
        result.append(", agente: " + agente);
        result.append(", idAutista: " + idAutista);
        result.append(", idStato: " + idStato);
        result.append(", stato: " + stato);
        result.append(", fatturato: " + fatturato);
        result.append(", totaleAcconto: " + totaleAcconto);
        result.append(", totale: " + totale);
        result.append(", totaleImponibile: " + totaleImponibile);
        result.append(", totaleCosto: " + totaleCosto);
        result.append(", totaleIva: " + totaleIva);
        result.append("}");

        return result.toString();
    }
}
