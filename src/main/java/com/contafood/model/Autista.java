package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"telefonate"})
@Entity
@Table(name = "autista")
public class Autista {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "attivo")
    private Boolean attivo;

    @OneToMany(mappedBy = "autista")
    @JsonIgnore
    private List<Telefonata> telefonate;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", nome: " + nome);
        result.append(", cognome: " + cognome);
        result.append(", telefono: " + telefono);
        result.append("}");

        return result.toString();

    }
}
