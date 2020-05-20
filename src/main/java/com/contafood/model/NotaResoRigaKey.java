package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NotaResoRigaKey implements Serializable {

    @Column(name = "id_nota_reso")
    Long notaResoId;

    @Column(name = "uuid")
    String uuid;

    public NotaResoRigaKey(){}

    public NotaResoRigaKey(Long notaResoId, String uuid){
        this.notaResoId = notaResoId;
        this.uuid = uuid;
    }

    public Long getNotaResoId() {
        return notaResoId;
    }

    public void setNotaResoId(Long notaResoId) {
        this.notaResoId = notaResoId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaResoId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotaResoRigaKey that = (NotaResoRigaKey) obj;
        return Objects.equals(notaResoId, that.notaResoId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("notaResoId: " + notaResoId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
