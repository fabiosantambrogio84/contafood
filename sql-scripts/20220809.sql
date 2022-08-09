alter table contafood.produzione add column tipologia varchar(255) NOT NULL DEFAULT 'STANDARD' after data_produzione;

ALTER TABLE produzione_confezione ADD COLUMN id_ingrediente int(10) unsigned;
ALTER TABLE produzione_confezione ADD CONSTRAINT fk_prod_conf_ingr FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`);

create or replace view `v_produzione` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    produzione.tipologia,
    `produzione_confezione`.`id_confezione` as `id_confezione`,
    `produzione`.`lotto` as `lotto`,
    `produzione`.`scadenza` as `scadenza`,
    `produzione_confezione`.`id_articolo` as `id_articolo`,
    `articolo`.`codice` as `codice_articolo`,
    `articolo`.`descrizione` as `descrizione_articolo`,
    `produzione_confezione`.`id_ingrediente` as `id_ingrediente`,
    `ingrediente`.`codice` as `codice_ingrediente`,
    `ingrediente`.`descrizione` as `descrizione_ingrediente`,
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
    `produzione_confezione`.`id_articolo` = `articolo`.`id`
left join ingrediente on
    produzione_confezione.id_ingrediente = ingrediente.id
;
