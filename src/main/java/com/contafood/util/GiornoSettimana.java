package com.contafood.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public enum GiornoSettimana {

    LUNEDI("Lunedi", 1),
    MARTEDI("Martedi", 2),
    MERCOLEDI("Mercoledi", 3),
    GIOVEDI("Giovedi", 4),
    VENERDI("Venerdi", 5),
    SABATO("Sabato", 6),
    DOMENICA("Domenica", 7);

    private String label;

    private Integer value;

    GiornoSettimana(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue(){
        return value;
    }

    public static List<String> labels(){
        return Arrays.asList(GiornoSettimana.values()).stream().map(p -> p.getLabel()).collect(Collectors.toList());
    }

    public static List<HashMap> giorni(){
        List<HashMap> giorni = new ArrayList<>();
        Arrays.stream(GiornoSettimana.values()).forEach(gs -> {
            HashMap<Integer, String> giorno = new HashMap<>();
            giorno.put(gs.getValue(), gs.getLabel());
            giorni.add(giorno);
        });
        return giorni;
    }

    public static Integer getValueByLabel(String label){
        return Arrays.stream(GiornoSettimana.values()).filter(gs -> gs.getLabel().equals(label)).map(gsf -> gsf.getValue()).findFirst().get();
    }
}
