DROP VIEW IF EXISTS v_ordine_cliente_stats_week;
DROP VIEW IF EXISTS v_ordine_cliente_stats_month;
DROP TABLE IF EXISTS ddt_articolo_ordine_cliente;
DROP TABLE IF EXISTS fattura_accom_articolo_ordine_cliente;
DROP TABLE IF EXISTS giacenzaArticolo;

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
-- ALTER TABLE giacenza ADD COLUMN id_ricetta int(10) unsigned AFTER id_articolo;
-- ALTER TABLE giacenza ADD COLUMN codice_articolo_ricetta varchar(100) AFTER id_ricetta;
-- ALTER TABLE giacenza ADD CONSTRAINT fk_giacenza_ricetta FOREIGN KEY (`id_ricetta`) REFERENCES `ricetta` (`id`);

-- 17/06/2020
DROP TABLE IF EXISTS giacenza;
DROP TABLE IF EXISTS giacenza_articolo;
DROP TABLE IF EXISTS giacenza_ingrediente;
DROP TABLE IF EXISTS tipo_fornitore;
DROP TABLE IF EXISTS ddt_acquisto_ingrediente;

CREATE TABLE `giacenza_articolo` (
	id int(10) unsigned auto_increment,
	id_articolo int(10) unsigned,
	lotto varchar(100),
	scadenza date,
	quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_giacenza_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `giacenza_ingrediente` (
	id int(10) unsigned auto_increment,
	id_ingrediente int(10) unsigned,
	lotto varchar(100),
	scadenza date,
	quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_giacenza_ingrediente_ing` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `tipo_fornitore` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `codice` varchar(100),
  `descrizione` varchar(255),
  `ordine` int(10),
  `data_inserimento` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

ALTER TABLE fornitore ADD COLUMN id_tipo int(10) unsigned AFTER id;
ALTER TABLE fornitore ADD CONSTRAINT fk_fornitore_tipo FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fornitore` (`id`);

INSERT INTO contafood.tipo_fornitore(codice, descrizione, ordine, data_inserimento)
VALUES('FORNITORE_ARTICOLI', 'Fornitore articoli', 0, CURRENT_TIMESTAMP);

INSERT INTO contafood.tipo_fornitore(codice, descrizione, ordine, data_inserimento)
VALUES('FORNITORE_INGREDIENTI', 'Fornitore ingredienti', 1, CURRENT_TIMESTAMP);

UPDATE contafood.fornitore SET id_tipo = 1;


CREATE TABLE `ddt_acquisto_ingrediente` (
  `id_ddt_acquisto` int(10) unsigned NOT NULL DEFAULT '0',
  `id_ingrediente` int(10) unsigned NOT NULL DEFAULT '0',
  `uuid` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '',
  `lotto` varchar(100) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT NULL,
  `data_scadenza` date DEFAULT NULL,
  `quantita` decimal(10,3) DEFAULT NULL,
  `prezzo` decimal(10,3) DEFAULT NULL,
  `sconto` decimal(10,3) DEFAULT NULL,
  `imponibile` decimal(10,3) DEFAULT NULL,
  `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_aggiornamento` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_ddt_acquisto`,`id_ingrediente`,`uuid`),
  CONSTRAINT `fk_ddt_acq_ingrediente_ingr` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`),
  CONSTRAINT `fk_ddt_acq_ingrediente_ddt_acq` FOREIGN KEY (`id_ddt_acquisto`) REFERENCES `ddt_acquisto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

ALTER TABLE ingrediente ADD COLUMN id_aliquota_iva int(10) unsigned AFTER id_fornitore;
ALTER TABLE ingrediente ADD CONSTRAINT fk_ingrediente_iva FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`);

UPDATE ingrediente set id_aliquota_iva=1;

ALTER TABLE ingrediente DROP COLUMN unita_di_misura;
ALTER TABLE ingrediente ADD COLUMN id_unita_misura int(10) unsigned AFTER prezzo;
ALTER TABLE ingrediente ADD CONSTRAINT fk_ingrediente_udm FOREIGN KEY (`id_unita_misura`) REFERENCES `unita_misura` (`id`);

ALTER TABLE ordine_cliente ADD COLUMN data date after anno_contabile;

ALTER TABLE contafood.giacenza_ingrediente MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.giacenza_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.produzione_ingrediente MODIFY COLUMN scadenza date NULL;

-- 07/07/2020
create or replace view contafood.v_giacenza_articolo as
select
	id_articolo,
	concat(articolo.codice,' ',coalesce(articolo.descrizione,'')) articolo,
	sum(quantita) quantita_tot,
	GROUP_CONCAT(giacenza_articolo.id) id_giacenze,
	GROUP_CONCAT(giacenza_articolo.lotto) lotto_giacenze,
	GROUP_CONCAT(giacenza_articolo.scadenza) scadenza_giacenze,
	articolo.attivo,
	articolo.id_fornitore,
	fornitore.ragione_sociale fornitore
from
	contafood.giacenza_articolo
join contafood.articolo on
	giacenza_articolo.id_articolo = articolo.id
left join contafood.fornitore on
	articolo.id_fornitore = fornitore.id
group by
	id_articolo
;

create or replace view contafood.v_giacenza_ingrediente as
select
	id_ingrediente,
	concat(ingrediente.codice,' ',coalesce(ingrediente.descrizione,'')) ingrediente,
	sum(quantita) quantita_tot,
	GROUP_CONCAT(giacenza_ingrediente.id) id_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.lotto) lotto_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.scadenza) scadenza_giacenze,
	ingrediente.attivo,
	ingrediente.id_fornitore,
	fornitore.ragione_sociale fornitore
from
	contafood.giacenza_ingrediente
join contafood.ingrediente on
	giacenza_ingrediente.id_ingrediente = ingrediente.id
left join contafood.fornitore on
	ingrediente.id_fornitore = fornitore.id
group by
	id_ingrediente
;

ALTER TABLE fattura_accom_articolo ADD COLUMN numero_pezzi_da_evadere int(10) AFTER numero_pezzi;

CREATE TABLE `fattura_accom_articolo_ordine_cliente` (
	id_fattura_accom int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	id_ordine_cliente int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id_fattura_accom, id_articolo, uuid, id_ordine_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- 13/07/2020
DROP TABLE IF EXISTS movimentazione_manuale_articolo;
DROP TABLE IF EXISTS movimentazione_manuale_ingrediente;

CREATE TABLE `movimentazione_manuale_articolo` (
	id int(10) unsigned auto_increment,
	id_articolo int(10) unsigned,
	lotto varchar(100),
	scadenza date,
	quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_mov_man_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `movimentazione_manuale_ingrediente` (
	id int(10) unsigned auto_increment,
	id_ingrediente int(10) unsigned,
	lotto varchar(100),
	scadenza date,
	quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_mov_man_ingrediente_ing` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;