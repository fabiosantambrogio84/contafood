ALTER TABLE `articolo` ADD COLUMN prezzo_listino_base decimal(10,3) AFTER prezzo_acquisto;

ALTER TABLE `sconto` ADD COLUMN tipologia varchar(255) default null AFTER id_cliente;

ALTER TABLE `listino` ADD COLUMN tipologia varchar(255) default 'STANDARD' AFTER id_listino;
ALTER TABLE `listino` ADD COLUMN tipologia_variazione_prezzo varchar(100) AFTER tipologia;
ALTER TABLE `listino` ADD COLUMN variazione_prezzo decimal(10,2) AFTER tipologia_variazione_prezzo;

DROP TABLE IF EXISTS `listino_prezzo`;

CREATE TABLE `listino_prezzo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_listino int(10) unsigned,
	id_articolo int(10) unsigned,
	prezzo decimal(10,2),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_listino_prezzo_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`),
	CONSTRAINT `fk_listino_prezzo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;
