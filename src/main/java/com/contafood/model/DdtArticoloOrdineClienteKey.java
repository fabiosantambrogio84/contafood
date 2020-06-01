package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DdtArticoloOrdineClienteKey implements Serializable {

    private static final long serialVersionUID = 7720584464700246830L;

    @Column(name = "id_ddt")
    Long ddtId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    @Column(name = "id_ordine_cliente")
    Long ordineClienteId;

    public DdtArticoloOrdineClienteKey(){}

    public DdtArticoloOrdineClienteKey(Long ddtId, Long articoloId, String uuid, Long ordineClienteId){
        this.ddtId = ddtId;
        this.articoloId = articoloId;
        this.uuid = uuid;
        this.ordineClienteId = ordineClienteId;
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

    public Long getOrdineClienteId() {
        return ordineClienteId;
    }

    public void setOrdineClienteId(Long ordineClienteId) {
        this.ordineClienteId = ordineClienteId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddtId, articoloId, uuid, ordineClienteId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DdtArticoloOrdineClienteKey that = (DdtArticoloOrdineClienteKey) obj;
        return Objects.equals(ddtId, that.ddtId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid) && Objects.equals(ordineClienteId, that.ordineClienteId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("ddtId: " + ddtId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append(", ordineClienteId: " + ordineClienteId);
        result.append("}");

        return result.toString();
    }
}
