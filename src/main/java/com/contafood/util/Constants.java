package com.contafood.util;

public interface Constants {

    String DEFAULT_FORNITORE = "URBANI GIUSEPPE";
    String DEFAULT_FORNITORE_INITIALS = "UR";

    String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";

    String HTTP_HEADER_PRAGMA_VALUE = "no-cache";
    String HTTP_HEADER_EXPIRES_VALUE = "0";
    String HTTP_HEADER_CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";

    String JASPER_PARAMETER_DDT_NOTA = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";
    String JASPER_PARAMETER_DDT_CAUSALE = "Vendita";
    String JASPER_REPORT_GIACENZE_INGREDIENTI = "/reports/giacenze_ingredienti.jasper";
    String JASPER_REPORT_DDT = "/reports/ddt.jasper";
    String JASPER_REPORT_ORDINI_AUTISTI = "/reports/ordini_autisti.jasper";
    String JASPER_REPORT_PAGAMENTI = "/reports/pagamenti.jasper";
}
