DROP TABLE IF EXISTS `ddt_acquisto_articolo`;
DROP TABLE IF EXISTS `ddt_acquisto`;
DROP TABLE IF EXISTS `stato_ddt_acquisto`;

/*
CREATE TABLE `stato_ddt_acquisto` (
	id int(10) unsigned NOT NULL,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB;
*/

CREATE TABLE `ddt_acquisto` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	numero varchar(255),
	data DATE,
	id_fornitore int(10) unsigned,
	-- id_stato int(10) unsigned,
    numero_colli int(10),
    totale_imponibile decimal(10,3),
    totale decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ddt_acq_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`)
	-- CONSTRAINT `fk_ddt_acq_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_ddt_acquisto` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `ddt_acquisto_articolo` (
	id_ddt_acquisto int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	lotto varchar(255),
	data_scadenza date,
	quantita decimal(10,3),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_ddt_acquisto, id_articolo, uuid),
	CONSTRAINT `fk_ddt_acq_articolo_ddt_acq` FOREIGN KEY (`id_ddt_acquisto`) REFERENCES `ddt_acquisto` (`id`),
	CONSTRAINT `fk_ddt_acq_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

-- INSERT INTO stato_ddt_acquisto(id,codice,descrizione,ordine) VALUES(0,'DA_PAGARE','Da pagare',1);
-- INSERT INTO stato_ddt_acquisto(id,codice,descrizione,ordine) VALUES(1,'PARZIALMENTE_PAGATO','Parzialmente pagato',2);
-- INSERT INTO stato_ddt_acquisto(id,codice,descrizione,ordine) VALUES(2,'PAGATO','Pagato',3);
