package com.contafood.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
@Data
public class OrdineClienteAggregate {

    private Long idArticolo;

    private String articolo;

    private BigDecimal prezzoListinoBase;

    private Integer numeroPezziOrdinati;

    private Integer numeroPezziDaEvadere;

    private Integer numeroPezziEvasi;

    private String idsOrdiniClienti;

    private String codiciOrdiniClienti;

    private String note;

    private String idsDdts;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("idArticolo: " + idArticolo);
        result.append(", articolo: " + articolo);
        result.append(", prezzoListinoBase: " + prezzoListinoBase);
        result.append(", numeroPezziOrdinati: " + numeroPezziOrdinati);
        result.append(", numeroPezziDaEvadere: " + numeroPezziDaEvadere);
        result.append(", numeroPezziEvasi: " + numeroPezziEvasi);
        result.append(", idsOrdiniClienti: " + idsOrdiniClienti);
        result.append(", codiciOrdiniClienti: " + codiciOrdiniClienti);
        result.append(", note: " + note);
        result.append(", idsDdts: " + idsDdts);
        result.append("}");

        return result.toString();
    }
}
