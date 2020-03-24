package com.contafood.util;

import java.util.*;

public enum StatisticaPeriodo {

    ANNO("Anno corrente", 0),
    MESE("Mese corrente", 1),
    SETTIMANA("Settimana corrente", 2);

    private String label;

    private int ordine;

    StatisticaPeriodo(String label, int ordine) {
        this.label = label;
        this.ordine = ordine;
    }

    public String getLabel() {
        return label;
    }

    public int getOrdine() {
        return ordine;
    }

    public static List<Map<StatisticaPeriodo, String>> getAll(){
        List<Map<StatisticaPeriodo, String>> returningList = new ArrayList<>();
        Arrays.stream(StatisticaPeriodo.values()).sorted(Comparator.comparingInt(StatisticaPeriodo::getOrdine)).forEach(s -> {
            Map<StatisticaPeriodo, String> statisticaPeriodoMap = new HashMap<>();
            statisticaPeriodoMap.put(s, s.getLabel());
            returningList.add(statisticaPeriodoMap);
        });
        return returningList;
    }
}
