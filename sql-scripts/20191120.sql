DROP TABLE IF EXISTS `sconto`;
DROP TABLE IF EXISTS `articolo_immagine`;
DROP TABLE IF EXISTS `articolo`;
DROP TABLE IF EXISTS `categoria_articolo`;
DROP TABLE IF EXISTS `unita_misura`;


CREATE TABLE `unita_misura` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(255),
	etichetta varchar(255),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `categoria_articolo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(255),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `articolo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(50),
	descrizione varchar(255),
	id_categoria int(10) unsigned,
	id_fornitore int(10) unsigned,
	id_aliquota_iva int(10) unsigned,
	id_unita_misura int(10) unsigned,
	data date,
	quantita_predefinita decimal(10,3),
	prezzo_acquisto decimal(10,3),
	scadenza_giorni int(10),
	barcode varchar(255),
	complete_barcode bit(1) DEFAULT b'0',
	sito_web bit(1) DEFAULT b'0',
	attivo bit(1) DEFAULT b'1',
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_articolo_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria_articolo` (`id`),
	CONSTRAINT `fk_articolo_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`),
	CONSTRAINT `fk_articolo_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`),
	CONSTRAINT `fk_articolo_unita_misura` FOREIGN KEY (`id_unita_misura`) REFERENCES `unita_misura` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `articolo_immagine` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_articolo int(10) unsigned,
	file_name varchar(1000),
	file_path varchar(1000),
	file_complete_path varchar(1000),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_articolo_img_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `sconto` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_cliente int(10) unsigned,
	id_fornitore int(10) unsigned,
	id_articolo int(10) unsigned,
	valore decimal(10,3) NOT NULL DEFAULT '0.000',
	data_dal date,
	data_al date,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_sconto_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_sconto_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`),
	CONSTRAINT `fk_sconto_articolo` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;


-- https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
