ALTER TABLE contafood.ordine_cliente_articolo ADD COLUMN id_ordine_fornitore int unsigned AFTER num_da_evadere;
ALTER TABLE contafood.ordine_fornitore_articolo ADD COLUMN id_ordini_clienti varchar(255) AFTER num_ordinati;

create or replace view `contafood`.`v_ordine_cliente_articolo` as
select
    uuid() as id,
    ordine_cliente.id as id_ordine_cliente,
    ordine_cliente.data_consegna,
    ordine_cliente_articolo.id_articolo,
    articolo.codice as codice_articolo,
    articolo.descrizione as descrizione_articolo,
    articolo.id_fornitore,
    ordine_cliente_articolo.num_ordinati,
    ordine_cliente_articolo.num_da_evadere
from contafood.ordine_cliente
join contafood.ordine_cliente_articolo on
    ordine_cliente.id = ordine_cliente_articolo.id_ordine_cliente
join contafood.articolo on
    ordine_cliente_articolo.id_articolo = articolo.id
join contafood.stato_ordine on
    ordine_cliente.id_stato_ordine = stato_ordine.id
where
    ordine_cliente_articolo.id_ordine_fornitore is null and
        stato_ordine.codice <> 'EVASO';

create or replace view `contafood`.`v_ordine_fornitore_articolo` as
select
    uuid() as id,
    articolo.id as id_articolo,
    articolo.codice as codice_articolo,
    articolo.descrizione as descrizione_articolo,
    ordine_fornitore_articolo.num_ordinati,
    ordine_fornitore_articolo.id_ordini_clienti
from
    contafood.ordine_fornitore_articolo
join contafood.articolo on
    ordine_fornitore_articolo.id_articolo = articolo.id;

