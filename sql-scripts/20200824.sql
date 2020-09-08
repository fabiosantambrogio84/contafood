
ALTER TABLE produzione_confezione ADD COLUMN id_articolo int(10) unsigned;
ALTER TABLE produzione_confezione ADD CONSTRAINT fk_prod_conf_art FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`);


ALTER TABLE contafood.produzione_confezione CHANGE peso num_confezioni_prodotte INT NULL;
ALTER TABLE contafood.produzione_confezione MODIFY COLUMN num_confezioni_prodotte INT NULL;


DROP VIEW IF EXISTS v_produzione;

CREATE VIEW `v_produzione` AS
    select
        uuid() as `id`,
    	produzione.id as id_produzione,
    	produzione.codice as codice_produzione,
        produzione.data_produzione,
        produzione_confezione.id_confezione,
        produzione.lotto,
        produzione.scadenza,
        produzione_confezione.id_articolo,
        articolo.codice as codice_articolo,
        articolo.descrizione as descrizione_articolo,
        produzione_confezione.num_confezioni_prodotte
    from
        contafood.produzione_confezione
    join contafood.produzione on
        produzione_confezione.id_produzione = produzione.id
    left join contafood.articolo on
        produzione_confezione.id_articolo = articolo.id
;

ALTER TABLE produzione_confezione ADD COLUMN lotto_produzione varchar(100) after lotto;

create or replace view contafood.v_giacenza_ingrediente as
select
	id_ingrediente,
	concat(ingrediente.codice,' ',coalesce(ingrediente.descrizione,'')) ingrediente,
	sum(quantita) quantita_tot,
	GROUP_CONCAT(giacenza_ingrediente.id) id_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.lotto) lotto_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.scadenza) scadenza_giacenze,
	ingrediente.attivo,
	unita_misura.etichetta udm,
	ingrediente.id_fornitore,
	fornitore.ragione_sociale fornitore
from
	contafood.giacenza_ingrediente
join contafood.ingrediente on
	giacenza_ingrediente.id_ingrediente = ingrediente.id
left join contafood.unita_misura on
    ingrediente.id_unita_misura = unita_misura.id
left join contafood.fornitore on
	ingrediente.id_fornitore = fornitore.id
group by
	id_ingrediente
;