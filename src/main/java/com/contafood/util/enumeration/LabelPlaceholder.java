package com.contafood.util.enumeration;

public enum LabelPlaceholder {

    ARTICOLO("${articolo}"),
    INGREDIENTI("${ingredienti}"),
    INGREDIENTI_2("${ingredienti2}"),
    CONSERVAZIONE("${conservazione}"),
    VALORI_NUTRIZIONALI("${valoriNutrizionali}"),
    CONSUMAZIONE("${consumazione}"),
    BARCODE_EAN_13("${barcodeEan13}"),
    BARCODE_EAN_128("${barcodeEan128}"),
    DISPOSIZIONI_COMUNE("${disposizioniComune}"),
    FOOTER("${footer}");

    private String placeholder;

    LabelPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
