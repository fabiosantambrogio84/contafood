alter table fattura_accom add column `totale_imponibile` decimal(10,2) DEFAULT NULL after trasportatore;
alter table fattura_accom add column `totale_iva` decimal(10,2) DEFAULT NULL after totale_acconto;

create or replace
algorithm = UNDEFINED view `v_fattura` as
select
    `fattura`.`id` as `id`,
    `fattura`.`progressivo` as `progressivo`,
    `fattura`.`anno` as `anno`,
    `fattura`.`data` as `data`,
    `fattura`.`id_tipo` as `id_tipo`,
    `tipo_fattura`.`codice` as `tipo_codice`,
    `fattura`.`id_cliente` as `id_cliente`,
    `cliente`.`ragione_sociale` as `cliente`,
    `cliente`.`email` as `cliente_email`,
    `cliente`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `cliente`.`id_agente` as `id_agente`,
    concat(`agente`.`nome`, ' ', `agente`.`cognome`) as `agente`,
    `fattura`.`id_stato` as `id_stato`,
    `stato_fattura`.`codice` as `stato_codice`,
    `fattura`.`spedito_ade` as `spedito_ade`,
       fattura.totale_imponibile,
       fattura.totale_iva,
    `fattura`.`totale_acconto` as `totale_acconto`,
    `fattura`.`totale` as `totale`,
    `fattura`.`note` as `note`,
    `fattura`.`data_inserimento` as `data_inserimento`,
    `fattura`.`data_aggiornamento` as `data_aggiornamento`,
    `v_fattura_articolo`.`id_articoli` as `id_articoli`
from
    (((((`fattura`
        join `tipo_fattura` on
            ((`fattura`.`id_tipo` = `tipo_fattura`.`id`)))
        join `stato_fattura` on
            ((`fattura`.`id_stato` = `stato_fattura`.`id`)))
        join `cliente` on
            ((`fattura`.`id_cliente` = `cliente`.`id`)))
        left join `agente` on
            ((`cliente`.`id_agente` = `agente`.`id`)))
        join `v_fattura_articolo` on
        (((`v_fattura_articolo`.`id_tipo` = `fattura`.`id_tipo`)
            and (`v_fattura_articolo`.`id_fattura` = `fattura`.`id`))))
union all
select
    `fattura_accom`.`id` as `id`,
    `fattura_accom`.`progressivo` as `progressivo`,
    `fattura_accom`.`anno` as `anno`,
    `fattura_accom`.`data` as `data`,
    `fattura_accom`.`id_tipo` as `id_tipo`,
    `tipo_fattura`.`codice` as `tipo_codice`,
    `fattura_accom`.`id_cliente` as `id_cliente`,
    `cliente`.`ragione_sociale` as `cliente`,
    `cliente`.`email` as `cliente_email`,
    `cliente`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `cliente`.`id_agente` as `id_agente`,
    concat(`agente`.`nome`, ' ', `agente`.`cognome`) as `agente`,
    `fattura_accom`.`id_stato` as `id_stato`,
    `stato_fattura`.`codice` as `stato_codice`,
    `fattura_accom`.`spedito_ade` as `spedito_ade`,
    fattura_accom.totale_imponibile,
    fattura_accom.totale_iva,
    `fattura_accom`.`totale_acconto` as `totale_acconto`,
    `fattura_accom`.`totale` as `totale`,
    `fattura_accom`.`note` as `note`,
    `fattura_accom`.`data_inserimento` as `data_inserimento`,
    `fattura_accom`.`data_aggiornamento` as `data_aggiornamento`,
    `v_fattura_articolo`.`id_articoli` as `id_articoli`
from
    (((((`fattura_accom`
        join `tipo_fattura` on
            ((`fattura_accom`.`id_tipo` = `tipo_fattura`.`id`)))
        join `stato_fattura` on
            ((`fattura_accom`.`id_stato` = `stato_fattura`.`id`)))
        join `cliente` on
            ((`fattura_accom`.`id_cliente` = `cliente`.`id`)))
        left join `agente` on
            ((`cliente`.`id_agente` = `agente`.`id`)))
        join `v_fattura_articolo` on
        (((`v_fattura_articolo`.`id_tipo` = `fattura_accom`.`id_tipo`)
            and (`v_fattura_articolo`.`id_fattura` = `fattura_accom`.`id`))));

UPDATE contafood.fattura
INNER JOIN (
    select
    fattura.id,
    round(sum(ddt.totale_imponibile),2) as totale_imponibile,
    round(sum(ddt.totale_iva),2) as totale_iva
    from contafood.fattura
    join contafood.fattura_ddt on
    fattura_ddt.id_fattura = fattura.id
    join contafood.ddt on
    fattura_ddt.id_ddt = ddt.id
    where fattura.totale_imponibile is null
    group by
    fattura.id
) f2 ON
    fattura.id = f2.id
SET
    fattura.totale_imponibile = f2.totale_imponibile,
    fattura.totale_iva = f2.totale_iva
where
    fattura.totale_imponibile is null;

update contafood.fattura_accom
inner join (
    select
    fattura_accom_totale.id_fattura_accom,
    round(sum(fattura_accom_totale.totale_imponibile), 2) as totale_imponibile,
    round(sum(fattura_accom_totale.totale_iva), 2) as totale_iva
    from contafood.fattura_accom_totale
    group by
    fattura_accom_totale.id_fattura_accom
)fa2 on
    fattura_accom.id = fa2.id_fattura_accom
set
    fattura_accom.totale_imponibile = fa2.totale_imponibile,
    fattura_accom.totale_iva = fa2.totale_iva;
