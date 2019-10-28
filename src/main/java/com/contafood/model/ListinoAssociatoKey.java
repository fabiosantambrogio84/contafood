package com.contafood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode
@Getter
@Setter
//@Embeddable
public class ListinoAssociatoKey implements Serializable {

    //@Column(name = "id_cliente")
    Long clienteId;

    //@Column(name = "id_fornitore")
    Long fornitoreId;

    public ListinoAssociatoKey(){}

    public ListinoAssociatoKey(Long clienteId, Long fornitoreId){
        this.clienteId = clienteId;
        this.fornitoreId = fornitoreId;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("clienteId: " + clienteId);
        result.append(", fornitoreId: " + fornitoreId);
        result.append("}");

        return result.toString();
    }
}
