package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrdineClienteArticoloKey implements Serializable {

    @Column(name = "id_ordine_cliente")
    Long ordineClienteId;

    @Column(name = "id_articolo")
    Long articoloId;

    public OrdineClienteArticoloKey(){}

    public OrdineClienteArticoloKey(Long ordineClienteId, Long articoloId){
        this.ordineClienteId = ordineClienteId;
        this.articoloId = articoloId;
    }

    public Long getOrdineClienteId() {
        return ordineClienteId;
    }

    public void setOrdineClienteId(Long ordineClienteId) {
        this.ordineClienteId = ordineClienteId;
    }

    public Long getArticoloId() {
        return articoloId;
    }

    public void setArticoloId(Long articoloId) {
        this.articoloId = articoloId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordineClienteId, articoloId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final OrdineClienteArticoloKey that = (OrdineClienteArticoloKey) obj;
        return Objects.equals(ordineClienteId, that.ordineClienteId) &&
                Objects.equals(articoloId, that.articoloId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ordineClienteId: " + ordineClienteId);
        result.append(", articoloId: " + articoloId);
        result.append("}");

        return result.toString();
    }
}
