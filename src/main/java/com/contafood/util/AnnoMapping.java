package com.contafood.util;

public enum AnnoMapping {

    Y_2019("A"),
    Y_2020("B"),
    Y_2021("C"),
    Y_2022("D"),
    Y_2023("E"),
    Y_2024("F"),
    Y_2025("G"),
    Y_2026("H"),
    Y_2027("I"),
    Y_2028("J"),
    Y_2029("K"),
    Y_2030("L"),
    Y_2031("M"),
    Y_2032("N"),
    Y_2033("O"),
    Y_2034("P"),
    Y_2035("Q"),
    Y_2036("R"),
    Y_2037("S"),
    Y_2038("T"),
    Y_2039("U"),
    Y_2040("V"),
    Y_2041("W"),
    Y_2042("X"),
    Y_2043("Y"),
    Y_2044("Z");

    private String letterMapping;

    AnnoMapping(String letterMapping) {
        this.letterMapping = letterMapping;
    }

    public String getLetterMapping() {
        return letterMapping;
    }
}
