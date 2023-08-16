alter table produzione add column `barcode_ean_13` varchar(255) DEFAULT NULL after lotto_film_chiusura;
alter table produzione add column `barcode_ean_128` varchar(255) DEFAULT NULL after barcode_ean_13;

create or replace view `v_produzione` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    `produzione`.`tipologia` as `tipologia`,
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
    `produzione`.`quantita_totale` as `quantita`,
    concat(`ricetta`.`codice`, ' ', `ricetta`.`nome`) as `ricetta`,
    `produzione`.barcode_ean_13,
    `produzione`.barcode_ean_128
from
    ((((`produzione_confezione`
        join `produzione` on
            ((`produzione_confezione`.`id_produzione` = `produzione`.`id`)))
        join `ricetta` on
            ((`produzione`.`id_ricetta` = `ricetta`.`id`)))
        left join `articolo` on
            ((`produzione_confezione`.`id_articolo` = `articolo`.`id`)))
        left join `ingrediente` on
        ((`produzione_confezione`.`id_ingrediente` = `ingrediente`.`id`)));

CREATE TABLE contafood.etichetta (
    uuid varchar(255) not null,
    articolo varchar(255),
    lotto varchar(255),
    peso decimal(10,2),
    filename varchar(255),
    html text,
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;


create or replace view `v_produzione_etichetta` as
select
    produzione.id,
    produzione.lotto,
    produzione.scadenza,
    ricetta.nome as articolo,
    GROUP_CONCAT(DISTINCT ingrediente.descrizione ORDER BY ingrediente.descrizione asc SEPARATOR ',') as ingredienti
from contafood.produzione
         join ricetta on
        produzione.id_ricetta = ricetta.id
         left join produzione_ingrediente on
        produzione.id = produzione_ingrediente.id_produzione
         left join ingrediente on
        produzione_ingrediente.id_ingrediente = ingrediente.id
group by
    produzione.id,
    produzione.lotto,
    produzione.scadenza,
    ricetta.nome
;