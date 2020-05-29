package com.contafood.util.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipologiaTrasportoDdt {

    MITTENTE("Mittente", 2),
    DESTINATARIO("Destinatario", 1),
    VETTORE("Vettore", 3);

    private String label;

    private Integer order;

    TipologiaTrasportoDdt(String label, Integer order) {
        this.label = label;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public Integer getOrder() {
        return order;
    }

    public static List<String> labels(){
        return Arrays.asList(TipologiaTrasportoDdt.values()).stream().map(ttd -> ttd.getLabel()).collect(Collectors.toList());
    }
}
