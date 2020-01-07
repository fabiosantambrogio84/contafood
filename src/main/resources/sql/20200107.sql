DROP TABLE IF EXISTS `ordine_cliente_articolo`;
DROP TABLE IF EXISTS `ordine_cliente`;

CREATE TABLE `ordine_cliente` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	data_consegna DATE,
	id_autista int(10) unsigned,
	id_agente int(10) unsigned,
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ordine_cliente_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_ordine_cliente_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`),
	CONSTRAINT `fk_ordine_cliente_autista` FOREIGN KEY (`id_autista`) REFERENCES `autista` (`id`),
	CONSTRAINT `fk_ordine_cliente_agente` FOREIGN KEY (`id_agente`) REFERENCES `agente` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `ordine_cliente_articolo` (
	id_ordine_cliente int(10) unsigned NOT NULL AUTO_INCREMENT,
	id_articolo int(10) unsigned,
	num_ordinati int(10),
    num_da_evadere int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id_ordine_cliente`, `id_articolo`),
	CONSTRAINT `fk_ordine_cliente_art_cliente` FOREIGN KEY (`id_ordine_cliente`) REFERENCES `ordine_cliente` (`id`),
	CONSTRAINT `fk_ordine_cliente_art_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

