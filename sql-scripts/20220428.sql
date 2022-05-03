ALTER TABLE contafood.causale ADD COLUMN predefinito bit(1) NOT NULL DEFAULT b'0' after descrizione;

update contafood.causale set predefinito=1 where lower(descrizione)='reso merce';

create or replace view contafood.v_ddt as
select
    ddt.id, ddt.anno_contabile, ddt.progressivo, ddt.data,
    ddt.id_cliente, cliente.ragione_sociale cliente, cliente.email cliente_email,
    cliente.id_agente, CONCAT(agente.nome, " ", agente.cognome) agente,
    ddt.id_autista,
    ddt.id_stato, stato_ddt.codice,
    ddt.fatturato,
    ddt.totale_acconto, ddt.totale, ddt.totale_imponibile, ddt.totale_costo, ddt.totale_iva
from ddt
join stato_ddt on
ddt.id_stato = stato_ddt.id
join cliente on
ddt.id_cliente = cliente.id
left join agente on
cliente.id_agente = agente.id
;

create or replace view contafood.v_ddt_last as
select ddt.id, ddt.id_autista
from contafood.ddt
order by coalesce(data_aggiornamento,data_inserimento) desc
    limit 1
;
