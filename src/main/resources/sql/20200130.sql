DROP TABLE IF EXISTS `ddt`;


CREATE TABLE `ddt` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno_contabile int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
    tipo_trasporto varchar(100),

    numero_colli int(10),
    data_trasporto date,
    ora_trasporto time,

    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ddt_vend_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_ddt_vend_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`)
) ENGINE=InnoDB;

