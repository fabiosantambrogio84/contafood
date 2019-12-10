package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "telefonata")
public class Telefonata {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_punto_consegna")
    private PuntoConsegna puntoConsegna;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "telefono_2")
    private String telefonoTwo;

    @Column(name = "telefono_3")
    private String telefonoThree;

    @Column(name = "giorno")
    private String giorno;

    @Column(name = "giorno_ordinale")
    private Integer giornoOrdinale;

    @Column(name = "ora")
    private Integer ora;

    @Column(name = "note")
    private String note;

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

    public PuntoConsegna getPuntoConsegna() {
        return puntoConsegna;
    }

    public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
        this.puntoConsegna = puntoConsegna;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoTwo() {
        return telefonoTwo;
    }

    public void setTelefonoTwo(String telefonoTwo) {
        this.telefonoTwo = telefonoTwo;
    }

    public String getTelefonoThree() {
        return telefonoThree;
    }

    public void setTelefonoThree(String telefonoThree) {
        this.telefonoThree = telefonoThree;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public Integer getGiornoOrdinale() {
        return giornoOrdinale;
    }

    public void setGiornoOrdinale(Integer giornoOrdinale) {
        this.giornoOrdinale = giornoOrdinale;
    }

    public Integer getOra() {
        return ora;
    }

    public void setOra(Integer ora) {
        this.ora = ora;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        result.append(", puntoConsegna: " + puntoConsegna);
        result.append(", telefono: " + telefono);
        result.append(", telefono 2: " + telefonoTwo);
        result.append(", telefono 3: " + telefonoThree);
        result.append(", giorno: " + giorno);
        result.append(", giornoOrdinale: " + giornoOrdinale);
        result.append(", ora: " + ora);
        result.append(", note: " + note);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
