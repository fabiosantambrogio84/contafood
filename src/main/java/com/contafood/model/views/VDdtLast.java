package com.contafood.model.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode()
@Entity
@Table(name = "v_ddt_last")
public class VDdtLast {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_autista")
    private Long idAutista;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", idAutista: " + idAutista);
        result.append("}");

        return result.toString();
    }
}
