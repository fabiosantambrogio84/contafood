package com.contafood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "punto_consegna")
public class PuntoConsegna {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @Column(name = "nome")
    private String nome;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "localita")
    private String localita;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cap")
    private String cap;

    @Column(name = "codice_conad")
    private String codiceConad;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", indirizzo: " + indirizzo);
        result.append(", localita: " + localita);
        result.append(", provincia: " + provincia);
        result.append(", cap: " + cap);
        result.append(", codiceConad: " + codiceConad);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
