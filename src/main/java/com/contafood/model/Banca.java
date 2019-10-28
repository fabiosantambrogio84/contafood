package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(exclude = {"clienti"})
@Getter
@Setter
@Entity
@Table(name = "banca")
public class Banca {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "abi")
    private String abi;

    @Column(name = "cab")
    private String cab;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @OneToMany(mappedBy = "banca")
    @JsonIgnore
    private List<Cliente> clienti;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", abi: " + abi);
        result.append(", cab: " + cab);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();

    }
}
