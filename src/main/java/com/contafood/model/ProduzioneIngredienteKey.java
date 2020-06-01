package com.contafood.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProduzioneIngredienteKey implements Serializable {

    private static final long serialVersionUID = -1264127502656708701L;

    @Column(name = "id_produzione")
    Long produzioneId;

    @Column(name = "id_ingrediente")
    Long ingredienteId;

    @Column(name = "uuid")
    String uuid;

    public ProduzioneIngredienteKey(){}

    public ProduzioneIngredienteKey(Long produzioneId, Long ingredienteId, String uuid){
        this.produzioneId = produzioneId;
        this.ingredienteId = ingredienteId;
        this.uuid = uuid;
    }

    public Long getProduzioneId() {
        return produzioneId;
    }

    public void setProduzioneId(Long produzioneId) {
        this.produzioneId = produzioneId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(produzioneId, ingredienteId, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProduzioneIngredienteKey that = (ProduzioneIngredienteKey) obj;
        return Objects.equals(produzioneId, that.produzioneId) &&
                Objects.equals(ingredienteId, that.ingredienteId) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("produzioneId: " + produzioneId);
        result.append(", ingredienteId: " + ingredienteId);
        result.append(", uuid: " + uuid);
        result.append("}");

        return result.toString();
    }
}
