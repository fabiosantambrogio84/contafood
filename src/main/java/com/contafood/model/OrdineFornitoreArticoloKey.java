package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrdineFornitoreArticoloKey implements Serializable {

    @Column(name = "id_ordine_fornitore")
    Long ordineFornitoreId;

    @Column(name = "id_articolo")
    Long articoloId;

    public OrdineFornitoreArticoloKey(){}

    public OrdineFornitoreArticoloKey(Long ordineFornitoreId, Long articoloId){
        this.ordineFornitoreId = ordineFornitoreId;
        this.articoloId = articoloId;
    }

    public Long getOrdineFornitoreId() {
        return ordineFornitoreId;
    }

    public void setOrdineFornitoreId(Long ordineFornitoreId) {
        this.ordineFornitoreId = ordineFornitoreId;
    }

    public Long getArticoloId() {
        return articoloId;
    }

    public void setArticoloId(Long articoloId) {
        this.articoloId = articoloId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordineFornitoreId, articoloId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final OrdineFornitoreArticoloKey that = (OrdineFornitoreArticoloKey) obj;
        return Objects.equals(ordineFornitoreId, that.ordineFornitoreId) &&
                Objects.equals(articoloId, that.articoloId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ordineFornitoreId: " + ordineFornitoreId);
        result.append(", articoloId: " + articoloId);
        result.append("}");

        return result.toString();
    }
}
