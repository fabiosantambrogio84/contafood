package com.contafood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"listini"})
@Getter
@Setter
@Entity
@Table(name = "listino")
public class Listino {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @ManyToOne
    @JoinColumn(name="id_listino")
    private Listino listinoRiferimento;

    @OneToMany(mappedBy="listinoRiferimento")
    private List<Listino> listini;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
