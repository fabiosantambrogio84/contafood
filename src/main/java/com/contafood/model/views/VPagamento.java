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
@Table(name = "v_pagamento")
public class VPagamento {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private Date data;

    @Column(name = "tipologia")
    private String tipologia;

    @Column(name = "id_tipo_pagamento")
    private Long idTipoPagamento;

    @Column(name = "tipo_pagamento")
    private String tipoPagamento;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "note")
    private String note;

    @Column(name = "importo")
    private BigDecimal importo;

    @Column(name = "id_resource")
    private Long idResource;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "id_fornitore")
    private Long idFornitore;

    @Column(name = "fornitore")
    private String fornitore;

}
