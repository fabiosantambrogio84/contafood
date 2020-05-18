DROP TABLE IF EXISTS nota_accredito_totale;
DROP TABLE IF EXISTS nota_accredito_riga;
DROP TABLE IF EXISTS nota_accredito;
DROP TABLE IF EXISTS stato_nota_accredito;

CREATE TABLE `stato_nota_accredito` (
	id int(10) unsigned NOT NULL,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `nota_accredito` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_stato int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
    totale decimal(10,3),
    totale_acconto decimal(10,3),
    totale_quantita decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_nota_accredito_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_nota_accredito_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_nota_accredito` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `nota_accredito_riga` (
	id_nota_accredito int(10) unsigned,
	uuid varchar(255),
	descrizione text,
	lotto varchar(100),
	scadenza date,
	id_unita_misura int(10) unsigned,
	quantita decimal(10,3),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    id_aliquota_iva int(10) unsigned,
    imponibile decimal(10,3),
    totale decimal(10,3),
    id_articolo int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_nota_accredito, uuid),
	CONSTRAINT `fk_nota_accredito_riga_nota` FOREIGN KEY (`id_nota_accredito`) REFERENCES `nota_accredito` (`id`),
	CONSTRAINT `fk_nota_accredito_riga_udm` FOREIGN KEY (`id_unita_misura`) REFERENCES `unita_misura` (`id`),
	CONSTRAINT `fk_nota_accredito_riga_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`),
	CONSTRAINT `fk_nota_accredito_riga_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `nota_accredito_totale` (
	id_nota_accredito int(10) unsigned,
	id_aliquota_iva int(10) unsigned,
	uuid varchar(255),
	totale_iva decimal(10,3),
	totale_imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_nota_accredito, id_aliquota_iva, uuid),
	CONSTRAINT `fk_nota_accredito_totale_fatt` FOREIGN KEY (`id_nota_accredito`) REFERENCES `nota_accredito` (`id`),
	CONSTRAINT `fk_nota_accredito_totale_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO stato_nota_accredito(id,codice,descrizione,ordine) VALUES(0,'DA_PAGARE','Da pagare',1);
INSERT INTO stato_nota_accredito(id,codice,descrizione,ordine) VALUES(1,'PARZIALMENTE_PAGATA','Parzialmente pagata',2);
INSERT INTO stato_nota_accredito(id,codice,descrizione,ordine) VALUES(2,'PAGATA','Pagata',3);


ALTER TABLE aliquota_iva ADD COLUMN zero bit(1) DEFAULT b'0' after valore;

INSERT INTO aliquota_iva VALUES(4, 0, true, now());