package com.contafood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode
@Getter
@Setter
@Entity
//@Table(name = "listino_associato")
public class ListinoAssociato implements Serializable {

    @EmbeddedId
    ListinoAssociatoKey id;

    //@ManyToOne
    //@MapsId("id_cliente")
    //@JoinColumn(name = "id_cliente")
    //@JsonIgnoreProperties("produzioneConfezioni")
    //private Cliente cliente;

    //@ManyToOne
    //@MapsId("id_fornitore")
    //@JoinColumn(name = "id_fornitore")
    //@JsonIgnoreProperties("produzioneConfezioni")
    //private Fornitore fornitore;

    //@Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("clienteId: " + id.clienteId);
        result.append(", fornitoreId: " + id.fornitoreId);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
