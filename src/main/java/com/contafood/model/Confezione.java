package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(exclude = {"produzioni"})
@Entity
@Table(name = "confezione")
public class Confezione {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "peso")
    private Float peso;

    @OneToMany(mappedBy = "confezione")
    @JsonIgnore
    private List<Produzione> produzioni;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public List<Produzione> getProduzioni() {
        return produzioni;
    }

    public void setProduzioni(List<Produzione> produzioni) {
        this.produzioni = produzioni;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", tipo: " + tipo);
        result.append(", peso: " + peso);
        result.append("}");

        return result.toString();

    }
}
