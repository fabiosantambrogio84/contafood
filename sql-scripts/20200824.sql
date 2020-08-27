
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
        produzione_confezione.lotto,
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


