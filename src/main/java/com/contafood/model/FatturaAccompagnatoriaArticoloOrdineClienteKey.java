package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FatturaAccompagnatoriaArticoloOrdineClienteKey implements Serializable {

    private static final long serialVersionUID = 7029170314907580384L;

    @Column(name = "id_fattura_accom")
    Long fatturaAccompagnatoriaId;

    @Column(name = "id_articolo")
    Long articoloId;

    @Column(name = "uuid")
    String uuid;

    @Column(name = "id_ordine_cliente")
    Long ordineClienteId;

    public FatturaAccompagnatoriaArticoloOrdineClienteKey(){}

    public FatturaAccompagnatoriaArticoloOrdineClienteKey(Long fatturaAccompagnatoriaId, Long articoloId, String uuid, Long ordineClienteId){
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
        this.articoloId = articoloId;
        this.uuid = uuid;
        this.ordineClienteId = ordineClienteId;
    }

    public Long getFatturaAccompagnatoriaId() {
        return fatturaAccompagnatoriaId;
    }

    public void setFatturaAccompagnatoriaId(Long fatturaAccompagnatoriaId) {
        this.fatturaAccompagnatoriaId = fatturaAccompagnatoriaId;
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
        return Objects.hash(fatturaAccompagnatoriaId, articoloId, uuid, ordineClienteId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FatturaAccompagnatoriaArticoloOrdineClienteKey that = (FatturaAccompagnatoriaArticoloOrdineClienteKey) obj;
        return Objects.equals(fatturaAccompagnatoriaId, that.fatturaAccompagnatoriaId) &&
                Objects.equals(articoloId, that.articoloId) && Objects.equals(uuid, that.uuid) && Objects.equals(ordineClienteId, that.ordineClienteId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("fatturaAccompagnatoriaId: " + fatturaAccompagnatoriaId);
        result.append(", articoloId: " + articoloId);
        result.append(", uuid: " + uuid);
        result.append(", ordineClienteId: " + ordineClienteId);
        result.append("}");

        return result.toString();
    }
}
