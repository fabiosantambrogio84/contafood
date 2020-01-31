DROP TABLE IF EXISTS `ddt_articolo`;
DROP TABLE IF EXISTS `ddt`;

CREATE TABLE `ddt` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno_contabile int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
    numero_colli int(10),
    tipo_trasporto varchar(100),
    data_trasporto date,
    ora_trasporto time,
    trasportatore varchar(255),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ddt_vend_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_ddt_vend_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `ddt_articolo` (
	id_ddt int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_articolo int(10) unsigned,
	quantita decimal(10,3),
	numero_pezzi int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id_ddt`, `id_articolo`),
	CONSTRAINT `fk_ddt_articolo_ddt` FOREIGN KEY (`id_ddt`) REFERENCES `ddt` (`id`),
	CONSTRAINT `fk_ddt_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;
