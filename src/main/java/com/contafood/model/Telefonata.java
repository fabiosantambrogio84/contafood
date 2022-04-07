package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Data
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

    @ManyToOne
    @JoinColumn(name="id_autista")
    private Autista autista;

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

    @Column(name = "giorno_consegna")
    private String giornoConsegna;

    @Column(name = "giorno_consegna_ordinale")
    private Integer giornoConsegnaOrdinale;

    @Column(name = "ora")
    private Time ora;

    @Column(name = "ora_consegna")
    private Time oraConsegna;

    @Column(name = "eseguito")
    private Boolean eseguito;

    @Column(name = "data_esecuzione")
    private Timestamp dataEsecuzione;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "telefonata")
    @JsonIgnore
    private List<OrdineCliente> ordiniClienti;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", cliente: " + cliente);
        result.append(", puntoConsegna: " + puntoConsegna);
        result.append(", autista: " + autista);
        result.append(", telefono: " + telefono);
        result.append(", telefono 2: " + telefonoTwo);
        result.append(", telefono 3: " + telefonoThree);
        result.append(", giorno: " + giorno);
        result.append(", giornoOrdinale: " + giornoOrdinale);
        result.append(", giornoConsegna: " + giornoConsegna);
        result.append(", giornoConsegnaOrdinale: " + giornoConsegnaOrdinale);
        result.append(", ora: " + ora);
        result.append(", oraConsegna: " + oraConsegna);
        result.append(", eseguito: " + eseguito);
        result.append(", dataEsecuzione: " + dataEsecuzione);
        result.append(", note: " + note);
        result.append(", ordiniClienti: " + ordiniClienti);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
