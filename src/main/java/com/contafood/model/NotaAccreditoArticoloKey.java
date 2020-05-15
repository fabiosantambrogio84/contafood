package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotaAccreditoArticoloKey implements Serializable {

    @Column(name = "id_nota_accredito")
    Long notaAccreditoId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    public NotaAccreditoArticoloKey(){}

    public NotaAccreditoArticoloKey(Long notaAccreditoId, Long articoloId, String uuid){
        this.notaAccreditoId = notaAccreditoId;
        this.articoloId = articoloId;
        this.uuid = uuid;
    }

    public Long getNotaAccreditoId() {
        return notaAccreditoId;
    }

    public void setNotaAccreditoId(Long notaAccreditoId) {
        this.notaAccreditoId = notaAccreditoId;
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
        return Objects.hash(notaAccreditoId, articoloId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotaAccreditoArticoloKey that = (NotaAccreditoArticoloKey) obj;
        return Objects.equals(notaAccreditoId, that.notaAccreditoId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaAccreditoId: " + notaAccreditoId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
