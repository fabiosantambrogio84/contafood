package com.contafood.util.enumeration;

import java.util.*;

public enum CleanupAction {

    DELETE_ORDINI_CLIENTI("Elimina ordini clienti evasi"),
    DELETE_ETICHETTE("Elimina etichette");

    private final String label;

    CleanupAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<Map<String, String>> getActions(){
        List<Map<String, String>> actions = new ArrayList<>();
        Arrays.stream(CleanupAction.values()).forEach(ca -> {
            Map<String, String> action = new HashMap<>();
            action.put("action", ca.name());
            action.put("label", ca.getLabel());
            actions.add(action);
        });

        return actions;
    }
}