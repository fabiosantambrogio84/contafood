package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DdtArticoloKey implements Serializable {

    private static final long serialVersionUID = -3173007831154184314L;

    @Column(name = "id_ddt")
    Long ddtId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public DdtArticoloKey(){}

    public DdtArticoloKey(Long ddtId, Long articoloId, String uuid){
        this.ddtId = ddtId;
        this.articoloId = articoloId;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddtId, articoloId, uuid);
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
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtId: " + ddtId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
