package com.contafood.util;

import java.util.Arrays;
import java.util.List;

public interface Constants {

    String DEFAULT_ENCODING = "UTF-8";

    String DEFAULT_FORNITORE = "URBANI GIUSEPPE";
    String DEFAULT_FORNITORE_INITIALS = "UR";

    String ZONE_ID_EUROPE_ROME = "Europe/Rome";

    String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
    String MEDIA_TYPE_APPLICATION_XML = "application/xml";
    String MEDIA_TYPE_APPLICATION_ZIP = "application/zip";
    String MEDIA_TYPE_APPLICATION_TXT = "application/text";

    String HTTP_HEADER_PRAGMA_VALUE = "no-cache";
    String HTTP_HEADER_EXPIRES_VALUE = "0";
    String HTTP_HEADER_CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";

    String EMAIL_INVIATA_OK = "Y";

    String JASPER_PARAMETER_DDT_NOTA = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";
    String JASPER_PARAMETER_FATTURA_NOTA = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";
    String JASPER_PARAMETER_FATTURA_ACCOMPAGNATORIA_NOTA = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";
    String JASPER_PARAMETER_RICEVUTA_PRIVATO_NOTA = "Assolve obblighi art.62,comm 1, del D.L. 24.01.12, n.1, convertito, con modificazioni, dalla legge 24.03.12, n.27. Contributo CONAI assolto dove dovuto";
    String JASPER_REPORT_GIACENZE_INGREDIENTI = "/reports/giacenze_ingredienti.jasper";
    String JASPER_REPORT_DDTS = "/reports/ddts.jasper";
    String JASPER_REPORT_DDT = "/reports/ddt.jasper";
    String JASPER_REPORT_ORDINI_AUTISTI = "/reports/ordini_autisti.jasper";
    String JASPER_REPORT_PAGAMENTI = "/reports/pagamenti.jasper";
    String JASPER_REPORT_NOTE_ACCREDITO = "/reports/note_accredito.jasper";
    String JASPER_REPORT_NOTA_ACCREDITO = "/reports/nota_accredito.jasper";
    String JASPER_REPORT_FATTURE = "/reports/fatture.jasper";
    String JASPER_REPORT_FATTURA = "/reports/fattura.jasper";
    String JASPER_REPORT_FATTURA_ACCOMPAGNATORIA = "/reports/fattura_accompagnatoria.jasper";
    String JASPER_REPORT_NOTA_RESO = "/reports/nota_reso.jasper";
    String JASPER_REPORT_RICEVUTE_PRIVATI = "/reports/ricevute_privati.jasper";
    String JASPER_REPORT_RICEVUTA_PRIVATO = "/reports/ricevuta_privato.jasper";
    String JASPER_REPORT_FATTURE_COMMERCIANTI = "/reports/fatture_commercianti.jasper";
    String JASPER_REPORT_ORDINE_FORNITORE = "/reports/ordine_fornitore.jasper";

    List<Character> BARCODE_ALLOWED_CHARS = Arrays.asList('L', 'A', 'M', 'G', 'X');
    String BARCODE_REGEXP = "^.{start}(.{length})";
}
