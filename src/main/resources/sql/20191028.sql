DROP TABLE IF EXISTS `listino_associato`;
DROP TABLE IF EXISTS `punto_consegna`;
DROP TABLE IF EXISTS `cliente`;
DROP TABLE IF EXISTS `listino`;
DROP TABLE IF EXISTS `aliquota_iva`;
DROP TABLE IF EXISTS `tipo_pagamento`;
DROP TABLE IF EXISTS `banca`;


CREATE TABLE `aliquota_iva` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	valore decimal(10,2),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `tipo_pagamento` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	descrizione varchar(255),
	scadenza_giorni int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `listino` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(255),
	id_listino int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT fk_listino_ref FOREIGN KEY (id_listino) REFERENCES `listino` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `banca` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(255),
	abi varchar(100),
	cab varchar(100),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `cliente` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice int(10),
	ragione_sociale varchar(255),
	ragione_sociale_2 varchar(255),
	ditta_individuale bit(1) NOT NULL DEFAULT b'0',
	nome varchar(50) DEFAULT NULL,
	cognome varchar(100) DEFAULT NULL,
	indirizzo varchar(100),
	citta varchar(100),
	provincia varchar(100),
	cap varchar(50),
	partita_iva varchar(100),
	codice_fiscale varchar(100),
	telefono varchar(100),
	email varchar(100),
	email_pec varchar(100),
	id_banca int(10) unsigned,
	conto_corrente varchar(100),
	id_tipo_pagamento int(10) unsigned,
	id_agente int(10) unsigned,
	estrazione_conad varchar(100) DEFAULT NULL,
	blocca_ddt bit(1) NOT NULL DEFAULT b'0',
	nascondi_prezzi bit(1) NOT NULL DEFAULT b'0',
	raggruppa_riba bit(1) NOT NULL DEFAULT b'0',
	nome_gruppo_riba varchar(100) default null,
	codice_univoco_sdi varchar(100) DEFAULT '0000000',
	note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_cliente_banca` FOREIGN KEY (`id_banca`) REFERENCES `banca` (`id`),
	CONSTRAINT `fk_cliente_tipo_pag` FOREIGN KEY (`id_tipo_pagamento`) REFERENCES `tipo_pagamento` (`id`),
	CONSTRAINT `fk_cliente_agente` FOREIGN KEY (`id_agente`) REFERENCES `agente` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `listino_associato` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	`id_cliente` int(10) unsigned NOT NULL,
	`id_fornitore` int(10) unsigned NOT NULL,
	`id_listino` int(10) unsigned NOT NULL,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	CONSTRAINT `fk_listino_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_listino_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`),
	CONSTRAINT `fk_listino_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `punto_consegna` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_cliente int(10) unsigned,
	nome varchar(255),
	indirizzo varchar(100),
	localita varchar(100),
	cap varchar(50),
	provincia varchar(100),
	codice_conad varchar(100),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_punto_consegna_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB;
