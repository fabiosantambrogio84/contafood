package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DdtArticoloKey implements Serializable {

    @Column(name = "id_ddt")
    Long ddtId;

    @Column(name = "id_articolo")
    Long articoloId;

    public DdtArticoloKey(){}

    public DdtArticoloKey(Long ddtId, Long articoloId){
        this.ddtId = ddtId;
        this.articoloId = articoloId;
    }

    public Long getDdtId() {
        return ddtId;
    }

    public void setDdtId(Long ddtId) {
        this.ddtId = ddtId;
    }

    public Long getArticoloId() {
        return articoloId;
    }

    public void setArticoloId(Long articoloId) {
        this.articoloId = articoloId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddtId, articoloId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DdtArticoloKey that = (DdtArticoloKey) obj;
        return Objects.equals(ddtId, that.ddtId) &&
                Objects.equals(articoloId, that.articoloId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtId: " + ddtId);
        result.append(", articoloId: " + articoloId);
        result.append("}");

        return result.toString();
    }
}
