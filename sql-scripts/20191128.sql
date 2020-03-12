ALTER TABLE `articolo` ADD COLUMN prezzo_listino_base decimal(10,3) AFTER prezzo_acquisto;

ALTER TABLE `sconto` ADD COLUMN tipologia varchar(255) default null AFTER id_cliente;

ALTER TABLE `listino` ADD COLUMN tipologia varchar(255) default 'STANDARD' AFTER id_listino;
ALTER TABLE `listino` ADD COLUMN tipologia_variazione_prezzo varchar(100) AFTER tipologia;
ALTER TABLE `listino` ADD COLUMN variazione_prezzo decimal(10,2) AFTER tipologia_variazione_prezzo;
ALTER TABLE `listino` ADD COLUMN id_categoria_articolo_variazione int(10) AFTER variazione_prezzo;
ALTER TABLE `listino` ADD COLUMN id_fornitore_variazione int(10) AFTER id_categoria_articolo_variazione;

ALTER TABLE `listino` DROP FOREIGN KEY fk_listino_ref;
ALTER TABLE `listino` DROP INDEX fk_listino_ref;
ALTER TABLE `listino` DROP COLUMN id_listino;

DROP TABLE IF EXISTS `listino_prezzo`;

CREATE TABLE `listino_prezzo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_listino int(10) unsigned,
	id_articolo int(10) unsigned,
	prezzo decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_listino_prezzo_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`),
	CONSTRAINT `fk_listino_prezzo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `telefonata`;

CREATE TABLE `telefonata` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	telefono varchar(100),
	telefono_2 varchar(100),
	telefono_3 varchar(100),
	giorno  varchar(100),
	giorno_ordinale int(10),
	ora time,
	note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_telefonata_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_telefonata_punto_consegna` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`)
) ENGINE=InnoDB;
