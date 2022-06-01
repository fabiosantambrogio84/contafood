create or replace view `v_produzione` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    `produzione_confezione`.`id_confezione` as `id_confezione`,
    `produzione`.`lotto` as `lotto`,
    `produzione`.`scadenza` as `scadenza`,
    `produzione_confezione`.`id_articolo` as `id_articolo`,
    `articolo`.`codice` as `codice_articolo`,
    `articolo`.`descrizione` as `descrizione_articolo`,
    `produzione_confezione`.`num_confezioni_prodotte` as `num_confezioni_prodotte`,
    produzione.quantita_totale as quantita,
    concat(ricetta.codice, ' ', ricetta.nome) as ricetta
from
    `produzione_confezione`
join `produzione` on
    `produzione_confezione`.`id_produzione` = `produzione`.`id`
join ricetta on
    produzione.id_ricetta = ricetta.id
left join `articolo` on
    `produzione_confezione`.`id_articolo` = `articolo`.`id`;


create or replace view `v_giacenza_articolo` as
select
    `giacenza_articolo`.`id_articolo` as `id_articolo`,
    concat(`articolo`.`codice`, ' ', coalesce(`articolo`.`descrizione`, '')) as `articolo`,
    articolo.prezzo_acquisto,
    articolo.prezzo_listino_base,
    sum(`giacenza_articolo`.`quantita`) as `quantita_tot`,
    null as quantita_kg,
    group_concat(`giacenza_articolo`.`id` separator ',') as `id_giacenze`,
    group_concat(`giacenza_articolo`.`lotto` separator ',') as `lotto_giacenze`,
    group_concat(`giacenza_articolo`.`scadenza` separator ',') as `scadenza_giacenze`,
    `articolo`.`attivo` as `attivo`,
    `articolo`.`id_fornitore` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    `giacenza_articolo`
join `articolo` on
    `giacenza_articolo`.`id_articolo` = `articolo`.`id`
left join `fornitore` on
    `articolo`.`id_fornitore` = `fornitore`.`id`
group by
    `giacenza_articolo`.`id_articolo`;
