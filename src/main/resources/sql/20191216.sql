ALTER TABLE `telefonata` ADD COLUMN id_autista int(10) unsigned AFTER id_punto_consegna;
ALTER TABLE telefonata ADD CONSTRAINT `fk_telefonata_autista` FOREIGN KEY (`id_autista`) REFERENCES `autista` (`id`);

ALTER TABLE `listino` DROP COLUMN tipologia_variazione_prezzo;
ALTER TABLE `listino` DROP COLUMN variazione_prezzo;
ALTER TABLE `listino` DROP COLUMN id_categoria_articolo_variazione;
ALTER TABLE `listino` DROP COLUMN id_fornitore_variazione;

DROP TABLE IF EXISTS `listino_prezzo_variazione`;

CREATE TABLE `listino_prezzo_variazione` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	tipologia_variazione_prezzo varchar(100),
	variazione_prezzo decimal(10,3),
	id_listino int(10) unsigned,
	id_articolo int(10) unsigned,
	id_fornitore int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_listino_prezzo_var_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`),
	CONSTRAINT `fk_listino_prezzo_var_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`),
	CONSTRAINT `fk_listino_prezzo_var_forn` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`)
) ENGINE=InnoDB;


ALTER TABLE listino ADD COLUMN data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE listino_prezzo ADD COLUMN data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `sconto` DROP FOREIGN KEY fk_sconto_fornitore;
ALTER TABLE `sconto` DROP INDEX fk_sconto_fornitore;
ALTER TABLE sconto DROP COLUMN id_fornitore;
ALTER TABLE `sconto` DROP FOREIGN KEY fk_sconto_articolo;
ALTER TABLE `sconto` DROP INDEX fk_sconto_articolo;
ALTER TABLE sconto DROP COLUMN id_articolo;

DROP TABLE IF EXISTS `sconto_articolo`;

CREATE TABLE `sconto_articolo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_sconto int(10) unsigned,
	id_articolo int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_sconto_articolo_sconto` FOREIGN KEY (`id_sconto`) REFERENCES `sconto` (`id`),
	CONSTRAINT `fk_sconto_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `sconto_fornitore`;

CREATE TABLE `sconto_fornitore` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_sconto int(10) unsigned,
	id_fornitore int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_sconto_articolo_sconto` FOREIGN KEY (`id_sconto`) REFERENCES `sconto` (`id`),
	CONSTRAINT `fk_sconto_articolo_forn` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`)
) ENGINE=InnoDB;