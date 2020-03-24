package com.contafood.util;

import java.util.*;
import java.util.stream.Collectors;

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

    public static List<Map<StatisticaOpzione, String>> getAll(){
        List<Map<StatisticaOpzione, String>> returningList = new ArrayList<>();
        Arrays.stream(StatisticaOpzione.values()).sorted(Comparator.comparingInt(StatisticaOpzione::getOrdine)).forEach(s -> {
            Map<StatisticaOpzione, String> statisticaOpzioneMap = new HashMap<>();
            statisticaOpzioneMap.put(s, s.getLabel());
            returningList.add(statisticaOpzioneMap);
        });
        return returningList;
    }

}
