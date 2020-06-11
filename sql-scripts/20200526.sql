DROP VIEW IF EXISTS v_ordine_cliente_stats_week;
DROP VIEW IF EXISTS v_ordine_cliente_stats_month;
DROP TABLE IF EXISTS ddt_articolo_ordine_cliente;
DROP TABLE IF EXISTS giacenza;

CREATE VIEW `v_ordine_cliente_stats_week` AS
    select
        uuid() as id,
        id_cliente,
        id_punto_consegna,
        ddt_articolo.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        ddt
    join ddt_articolo on
        ddt.id = ddt_articolo.id_ddt
    join articolo on
        ddt_articolo.id_articolo = articolo.id
    WHERE
        ddt.data >= now() - INTERVAL DAYOFWEEK(now())+6 day AND
        ddt.data < now() - INTERVAL DAYOFWEEK(now())-1 day
    union
    select
        uuid() as id,
        id_cliente,
        id_punto_consegna,
        fattura_accom_articolo.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        fattura_accom
    join fattura_accom_articolo on
        fattura_accom.id = fattura_accom_articolo.id_fattura_accom
    join articolo on
        fattura_accom_articolo.id_articolo = articolo.id
    WHERE
        fattura_accom.data >= now() - INTERVAL DAYOFWEEK(now())+6 day AND
        fattura_accom.data < now() - INTERVAL DAYOFWEEK(now())-1 day
    union
    select
        uuid() as id,
        id_cliente,
        -1 id_punto_consegna,
        nota_accredito_riga.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        nota_accredito
    join nota_accredito_riga on
        nota_accredito.id = nota_accredito_riga.id_nota_accredito
    join articolo on
        nota_accredito_riga.id_articolo = articolo.id
    WHERE
        nota_accredito.data >= now() - INTERVAL DAYOFWEEK(now())+6 day AND
        nota_accredito.data < now() - INTERVAL DAYOFWEEK(now())-1 day
;

CREATE VIEW `v_ordine_cliente_stats_month` AS
    select
        uuid() as id,
        id_cliente,
        id_punto_consegna,
        ddt_articolo.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        ddt
    join ddt_articolo on
        ddt.id = ddt_articolo.id_ddt
    join articolo on
        ddt_articolo.id_articolo = articolo.id
    WHERE
        YEAR(ddt.data) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH) AND
       	MONTH(ddt.data) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)
    union
    select
        uuid() as id,
        id_cliente,
        id_punto_consegna,
        fattura_accom_articolo.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        fattura_accom
    join fattura_accom_articolo on
        fattura_accom.id = fattura_accom_articolo.id_fattura_accom
    join articolo on
        fattura_accom_articolo.id_articolo = articolo.id
    WHERE
        YEAR(fattura_accom.data) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH) AND
       	MONTH(fattura_accom.data) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)
    union
    select
        uuid() as id,
        id_cliente,
        -1 id_punto_consegna,
        nota_accredito_riga.id_articolo,
        articolo.codice,
        articolo.descrizione,
        articolo.prezzo_listino_base
    FROM
        nota_accredito
    join nota_accredito_riga on
        nota_accredito.id = nota_accredito_riga.id_nota_accredito
    join articolo on
        nota_accredito_riga.id_articolo = articolo.id
    WHERE
        YEAR(nota_accredito.data) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH) AND
       	MONTH(nota_accredito.data) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)
;

ALTER TABLE contafood.ordine_cliente COLLATE=latin1_general_cs;
ALTER TABLE contafood.ordine_cliente_articolo COLLATE=latin1_general_cs;
ALTER TABLE contafood.stato_ordine COLLATE=latin1_general_cs;
ALTER TABLE contafood.agente COLLATE=latin1_general_cs;
ALTER TABLE contafood.cliente COLLATE=latin1_general_cs;
ALTER TABLE contafood.autista COLLATE=latin1_general_cs;
ALTER TABLE contafood.punto_consegna COLLATE=latin1_general_cs;
ALTER TABLE contafood.unita_misura COLLATE=latin1_general_cs;

ALTER TABLE contafood.ddt_articolo MODIFY COLUMN uuid varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT '' NOT NULL;


ALTER TABLE ddt_articolo ADD COLUMN numero_pezzi_da_evadere int(10) AFTER numero_pezzi;

CREATE TABLE `ddt_articolo_ordine_cliente` (
	id_ddt int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	id_ordine_cliente int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id_ddt, id_articolo, uuid, id_ordine_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `giacenza` (
	id int(10) unsigned auto_increment,
	id_articolo int(10) unsigned,
	lotto varchar(100),
	scadenza date,
	quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_giacenza_articolo` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- 16/06/2020
ALTER TABLE giacenza ADD COLUMN id_ricetta int(10) unsigned AFTER id_articolo;
ALTER TABLE giacenza ADD COLUMN codice_articolo_ricetta varchar(100) AFTER id_ricetta;
ALTER TABLE giacenza ADD CONSTRAINT fk_giacenza_ricetta FOREIGN KEY (`id_ricetta`) REFERENCES `ricetta` (`id`);
