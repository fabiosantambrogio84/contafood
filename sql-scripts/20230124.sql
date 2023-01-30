create or replace view `v_fattura_articolo` as
select
    fattura.id as id_fattura,
    fattura.id_tipo,
    GROUP_CONCAT(distinct ddt_articolo.id_articolo) as id_articoli
from contafood.fattura
         join contafood.fattura_ddt on
        fattura_ddt.id_fattura = fattura.id
         join contafood.ddt_articolo on
        ddt_articolo.id_ddt = fattura_ddt.id_ddt
group by fattura.id, fattura.id_tipo
union all
select
    fattura_accom.id as id_fattura,
    fattura_accom.id_tipo,
    GROUP_CONCAT(distinct fattura_accom_articolo.id_articolo) as id_articoli
from contafood.fattura_accom
         join contafood.fattura_accom_articolo on
        fattura_accom_articolo.id_fattura_accom = fattura_accom.id
group by fattura_accom.id, fattura_accom.id_tipo
;



create or replace view `v_fattura` as
select
    `fattura`.`id` as `id`,
    `fattura`.`progressivo` as `progressivo`,
    `fattura`.`anno` as `anno`,
    `fattura`.`data` as `data`,
    `fattura`.`id_tipo` as `id_tipo`,
    tipo_fattura.codice as tipo_codice,
    `fattura`.`id_cliente` as `id_cliente`,
    cliente.ragione_sociale as cliente,
    cliente.email as cliente_email,
    cliente.id_tipo_pagamento,
    cliente.id_agente,
    concat(agente.nome,' ',agente.cognome) as agente,
    `fattura`.`id_stato` as `id_stato`,
    stato_fattura.codice  as stato_codice,
    `fattura`.`spedito_ade` as `spedito_ade`,
    `fattura`.`totale_acconto` as `totale_acconto`,
    `fattura`.`totale` as `totale`,
    `fattura`.`note` as `note`,
    `fattura`.`data_inserimento` as `data_inserimento`,
    `fattura`.`data_aggiornamento` as `data_aggiornamento`,
    v_fattura_articolo.id_articoli
from contafood.fattura
         join contafood.tipo_fattura on
        fattura.id_tipo = tipo_fattura.id
         join contafood.stato_fattura on
        fattura.id_stato = stato_fattura.id
         join contafood.cliente on
        fattura.id_cliente = cliente.id
         left join contafood.agente on
        cliente.id_agente = agente.id
         join contafood.v_fattura_articolo on
            v_fattura_articolo.id_tipo = fattura.id_tipo and
            v_fattura_articolo.id_fattura = fattura.id
union all
select
    `fattura_accom`.`id` as `id`,
    `fattura_accom`.`progressivo` as `progressivo`,
    `fattura_accom`.`anno` as `anno`,
    `fattura_accom`.`data` as `data`,
    `fattura_accom`.`id_tipo` as `id_tipo`,
    tipo_fattura.codice as tipo_codice,
    `fattura_accom`.`id_cliente` as `id_cliente`,
    cliente.ragione_sociale as cliente,
    cliente.email as cliente_email,
    cliente.id_tipo_pagamento,
    cliente.id_agente,
    concat(agente.nome,' ',agente.cognome) as agente,
    `fattura_accom`.`id_stato` as `id_stato`,
    stato_fattura.codice  as stato_codice,
    `fattura_accom`.`spedito_ade` as `spedito_ade`,
    `fattura_accom`.`totale_acconto` as `totale_acconto`,
    `fattura_accom`.`totale` as `totale`,
    `fattura_accom`.`note` as `note`,
    `fattura_accom`.`data_inserimento` as `data_inserimento`,
    `fattura_accom`.`data_aggiornamento` as `data_aggiornamento`,
    v_fattura_articolo.id_articoli
from contafood.fattura_accom
         join contafood.tipo_fattura on
        fattura_accom.id_tipo = tipo_fattura.id
         join contafood.stato_fattura on
        fattura_accom.id_stato = stato_fattura.id
         join contafood.cliente on
        fattura_accom.id_cliente = cliente.id
         left join contafood.agente on
        cliente.id_agente = agente.id
         join contafood.v_fattura_articolo on
            v_fattura_articolo.id_tipo = fattura_accom.id_tipo and
            v_fattura_articolo.id_fattura = fattura_accom.id
;