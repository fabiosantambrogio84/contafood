DROP TABLE IF EXISTS nota_accredito_info;
DROP TABLE IF EXISTS nota_accredito_totale;
DROP TABLE IF EXISTS nota_accredito_articolo;
DROP TABLE IF EXISTS nota_accredito;

CREATE TABLE `nota_accredito` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_cliente int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
    totale decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_nota_accredito_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `nota_accredito_articolo` (
	id_nota_accredito int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	lotto varchar(100),
	quantita decimal(10,3),
	scadenza date,
	numero_pezzi int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
    totale decimal(10,3),
    totale_acconto decimal(10,3),
    totale_quantita decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_nota_accredito, id_articolo, uuid),
	CONSTRAINT `fk_nota_accredito_articolo_nota` FOREIGN KEY (`id_nota_accredito`) REFERENCES `nota_accredito` (`id`),
	CONSTRAINT `fk_nota_accredito_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
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

CREATE TABLE `nota_accredito_info` (
	id_nota_accredito int(10) unsigned,
	uuid varchar(255),
	descrizione text,
	lotto varchar(100),
	id_unita_misura int(10) unsigned,
	quantita decimal(10,3),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    id_aliquota_iva int(10) unsigned,
    imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_nota_accredito, uuid),
	CONSTRAINT `fk_nota_accredito_info_nota` FOREIGN KEY (`id_nota_accredito`) REFERENCES `nota_accredito` (`id`),
	CONSTRAINT `fk_nota_accredito_info_udm` FOREIGN KEY (`id_unita_misura`) REFERENCES `unita_misura` (`id`),
	CONSTRAINT `fk_nota_accredito_info_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;