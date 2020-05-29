package com.contafood.util.enumeration;

import java.util.*;

public enum StatisticaOpzione {

    MOSTRA_DETTAGLIO("Mostra dettaglio", 0),
    RAGGRUPPA_DETTAGLIO("Raggruppa dettaglio", 1);

    private String label;

    private int ordine;

    StatisticaOpzione(String label, int ordine) {
        this.label = label;
        this.ordine = ordine;
    }

    public String getLabel() {
        return label;
    }

    public int getOrdine() {
        return ordine;
    }

    public static List<Map<String, Object>> getAll(){
        List<Map<String, Object>> returningList = new ArrayList<>();
        Arrays.stream(StatisticaOpzione.values()).sorted(Comparator.comparingInt(StatisticaOpzione::getOrdine)).forEach(s -> {
            Map<String, Object> statisticaOpzioneMap = new HashMap<>();
            statisticaOpzioneMap.put("codice", s);
            statisticaOpzioneMap.put("label", s.getLabel());
            returningList.add(statisticaOpzioneMap);
        });
        return returningList;
    }

}
