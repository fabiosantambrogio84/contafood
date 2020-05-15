package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotaAccreditoInfoKey implements Serializable {

    @Column(name = "id_nota_accredito")
    Long notaAccreditoId;

    @Column(name = "uuid")
    String uuid;

    public NotaAccreditoInfoKey(){}

    public NotaAccreditoInfoKey(Long notaAccreditoId, String uuid){
        this.notaAccreditoId = notaAccreditoId;
        this.uuid = uuid;
    }

    public Long getNotaAccreditoId() {
        return notaAccreditoId;
    }

    public void setNotaAccreditoId(Long notaAccreditoId) {
        this.notaAccreditoId = notaAccreditoId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaAccreditoId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotaAccreditoInfoKey that = (NotaAccreditoInfoKey) obj;
        return Objects.equals(notaAccreditoId, that.notaAccreditoId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaAccreditoId: " + notaAccreditoId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
