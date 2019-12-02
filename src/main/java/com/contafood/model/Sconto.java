package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "sconto")
public class Sconto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @JoinColumn(name="tipologia")
    private String tipologia;

    @ManyToOne
    @JoinColumn(name="id_fornitore")
    private Fornitore fornitore;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @Column(name = "valore")
    private Float valore;

    @Column(name = "data_dal")
    private Date dataDal;

    @Column(name = "data_al")
    private Date dataAl;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public Float getValore() {
        return valore;
    }

    public void setValore(Float valore) {
        this.valore = valore;
    }

    public Date getDataDal() {
        return dataDal;
    }

    public void setDataDal(Date dataDal) {
        this.dataDal = dataDal;
    }

    public Date getDataAl() {
        return dataAl;
    }

    public void setDataAl(Date dataAl) {
        this.dataAl = dataAl;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", cliente: " + cliente);
        result.append(", tipologia: " + tipologia);
        result.append(", fornitore: " + fornitore);
        result.append(", articolo: " + articolo);
        result.append(", valore: " + valore);
        result.append(", dataDal: " + dataDal);
        result.append(", dataAl: " + dataAl);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
