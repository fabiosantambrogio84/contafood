DROP TABLE IF EXISTS `confezione`;

CREATE TABLE `confezione` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	tipo varchar(100),
	peso float,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `produzione`;

CREATE TABLE `produzione` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	id_ricetta int(10),
	id_categoria int(10),
	id_confezione int(10),
	num_confezioni int(10),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `produzione_ingrediente`;

CREATE TABLE `produzione_ingrediente` (
	id_produzione int(10) unsigned NOT NULL,
	id_ingrediente int(10) unsigned NOT NULL,
	lotto varchar(100),
	scadenza timestamp,
	quantita decimal(10,3),
	PRIMARY KEY (`id_produzione`, id_ingrediente)
) ENGINE=InnoDB;

