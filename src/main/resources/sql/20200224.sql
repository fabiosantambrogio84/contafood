ALTER TABLE `cliente` ADD COLUMN id_listino int(10) unsigned AFTER id_agente;
ALTER TABLE cliente ADD CONSTRAINT `fk_cliente_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`);

ALTER TABLE `produzione_confezione` ADD COLUMN peso float;

ALTER TABLE `produzione_ingrediente` ADD COLUMN uuid varchar(255) NOT NULL after id_ingrediente;
ALTER TABLE `produzione_ingrediente` DROP PRIMARY KEY;
ALTER TABLE `produzione_ingrediente` ADD CONSTRAINT pk_produzione_ingrediente PRIMARY KEY (id_produzione, id_ingrediente, uuid);

DROP TABLE IF EXISTS fattura_ddt;
DROP TABLE IF EXISTS fattura;
DROP TABLE IF EXISTS stato_fattura;

CREATE TABLE `stato_fattura` (
	id int(10) unsigned NOT NULL,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO stato_fattura(id,codice,descrizione,ordine) VALUES(0,'DA_PAGARE','Da pagare',1);
INSERT INTO stato_fattura(id,codice,descrizione,ordine) VALUES(1,'PARZIALMENTE_PAGATA','Parzialmente pagata',2);
INSERT INTO stato_fattura(id,codice,descrizione,ordine) VALUES(2,'PAGATA','Pagata',3);

CREATE TABLE `fattura` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_agente int(10) unsigned,
	id_stato int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
    totale_imponibile decimal(10,3),
    totale_acconto decimal(10,3),
    totale_iva decimal(10,3),
    totale decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_fattura_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_fattura_agente` FOREIGN KEY (`id_agente`) REFERENCES `agente` (`id`),
	CONSTRAINT `fk_fattura_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_fattura` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `fattura_ddt` (
	id_fattura int(10) unsigned,
	id_ddt int(10) unsigned,
	uuid varchar(255),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_fattura, id_ddt, uuid),
	CONSTRAINT `fk_fattura_ddt_ddt` FOREIGN KEY (`id_ddt`) REFERENCES `ddt` (`id`),
	CONSTRAINT `fk_fattura_ddt_fattura` FOREIGN KEY (`id_fattura`) REFERENCES `fattura` (`id`)
) ENGINE=InnoDB;