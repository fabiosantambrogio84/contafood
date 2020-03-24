DROP VIEW IF EXISTS `v_fattura`;
DROP TABLE IF EXISTS `fattura_accom_articolo`;
DROP TABLE IF EXISTS `fattura_accom_totale`;
DROP TABLE IF EXISTS `fattura_accom`;
DROP TABLE IF EXISTS `tipo_fattura`;

CREATE TABLE `tipo_fattura` (
	id int(10) unsigned,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(0,'VENDITA','Vendita',1);
INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(1,'ACCOMPAGNATORIA','Accompagnatoria',2);

ALTER TABLE `fattura` ADD COLUMN id_tipo int(10) unsigned AFTER data;
ALTER TABLE `fattura` ADD CONSTRAINT fk_fattura_tipo FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`);

UPDATE `fattura` SET id_tipo = 0;

CREATE TABLE `fattura_accom` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_tipo int(10) unsigned,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	id_stato int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
	numero_colli int(10),
    tipo_trasporto varchar(100),
    data_trasporto date,
    ora_trasporto time,
    trasportatore varchar(255),
    totale_acconto decimal(10,3),
    totale decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_fattura_accom_tipo` FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`),
	CONSTRAINT `fk_fattura_accom_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_fattura_accom_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`),
	CONSTRAINT `fk_fattura_accom_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_fattura` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `fattura_accom_articolo` (
	id_fattura_accom int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	lotto varchar(100),
	quantita decimal(10,3),
	numero_pezzi int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_fattura_accom, id_articolo, uuid),
	CONSTRAINT `fk_fattura_accom_articolo_fatt` FOREIGN KEY (`id_fattura_accom`) REFERENCES `fattura_accom` (`id`),
	CONSTRAINT `fk_fattura_accom_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `fattura_accom_totale` (
	id_fattura_accom int(10) unsigned,
	id_aliquota_iva int(10) unsigned,
	uuid varchar(255),
	totale_iva decimal(10,3),
	totale_imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_fattura_accom, id_aliquota_iva, uuid),
	CONSTRAINT `fk_fattura_accom_totale_fatt` FOREIGN KEY (`id_fattura_accom`) REFERENCES `fattura_accom` (`id`),
	CONSTRAINT `fk_fattura_accom_totale_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB;

CREATE VIEW `v_fattura` AS
SELECT
    id, progressivo, anno, data, id_tipo, id_cliente, id_stato, spedito_ade, totale_acconto, totale, note, data_inserimento, data_aggiornamento
FROM
    fattura
UNION ALL
select
	id, progressivo, anno, data, id_tipo, id_cliente, id_stato, spedito_ade, totale_acconto, totale, note, data_inserimento, data_aggiornamento
FROM
    fattura_accom;
