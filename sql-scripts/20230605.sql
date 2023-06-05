create or replace view `v_documento_acquisto` as
select
    concat('da_',ddt_acquisto.id) as id,
    'DDT acquisto' as tipo_documento,
    ddt_acquisto.id as id_documento,
    ddt_acquisto.numero as num_documento,
    ddt_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    ddt_acquisto.id_stato,
    stato_ddt.codice as stato,
    ddt_acquisto.totale_imponibile,
    ddt_acquisto.totale_iva,
    ddt_acquisto.totale,
    ddt_acquisto.totale_acconto,
    case
        when ddt_acquisto.fatturato = 1 then 1
        else 0
        end as fatturato
from ddt_acquisto
         join fornitore on
        ddt_acquisto.id_fornitore = fornitore.id
         left join stato_ddt on
        ddt_acquisto.id_stato = stato_ddt.id
union all
select
    concat('fa_',fattura_acquisto.id) as id,
    'Fattura acquisto' as tipo_documento,
    fattura_acquisto.id as id_documento,
    fattura_acquisto.numero as num_documento,
    fattura_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    fattura_acquisto.id_stato,
    stato_fattura.codice as stato,
    fattura_acquisto.totale_imponibile,
    fattura_acquisto.totale_iva,
    fattura_acquisto.totale,
    fattura_acquisto.totale_acconto,
    1 as fatturato
from fattura_acquisto
         join fornitore on
        fattura_acquisto.id_fornitore = fornitore.id
         left join stato_fattura on
        fattura_acquisto.id_stato = stato_fattura.id
union all
select
    concat('faa_',fattura_accom_acquisto.id) as id,
    'Fattura accompagnatoria acquisto' as tipo_documento,
    fattura_accom_acquisto.id as id_documento,
    fattura_accom_acquisto.numero as num_documento,
    fattura_accom_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    fattura_accom_acquisto.id_stato,
    stato_fattura.codice as stato,
    fattura_accom_acquisto.totale_imponibile,
    fattura_accom_acquisto.totale_iva,
    fattura_accom_acquisto.totale,
    fattura_accom_acquisto.totale_acconto,
    1 as fatturato
from fattura_accom_acquisto
         join fornitore on
        fattura_accom_acquisto.id_fornitore = fornitore.id
         left join stato_fattura on
        fattura_accom_acquisto.id_stato = stato_fattura.id
;
