package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DdtAcquistoArticoloKey implements Serializable {

    private static final long serialVersionUID = 5425281527222066388L;

    @Column(name = "id_ddt_acquisto")
    Long ddtAcquistoId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public DdtAcquistoArticoloKey(){}

    public DdtAcquistoArticoloKey(Long ddtAcquistoId, Long articoloId, String uuid){
        this.ddtAcquistoId = ddtAcquistoId;
        this.articoloId = articoloId;
        this.uuid = uuid;
    }

    public Long getDdtAcquistoId() {
        return ddtAcquistoId;
    }

    public void setDdtAcquistoId(Long ddtAcquistoId) {
        this.ddtAcquistoId = ddtAcquistoId;
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
        return Objects.hash(ddtAcquistoId, articoloId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DdtAcquistoArticoloKey that = (DdtAcquistoArticoloKey) obj;
        return Objects.equals(ddtAcquistoId, that.ddtAcquistoId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtAcquistoId: " + ddtAcquistoId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
