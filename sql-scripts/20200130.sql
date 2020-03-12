DROP TABLE IF EXISTS `pagamento`;
DROP TABLE IF EXISTS `ddt_articolo`;
DROP TABLE IF EXISTS `ddt`;
DROP TABLE IF EXISTS `stato_ddt`;

CREATE TABLE `stato_ddt` (
	id int(10) unsigned NOT NULL,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE `ddt` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno_contabile int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	id_autista int(10) unsigned,
	id_stato int(10) unsigned,
    numero_colli int(10),
    tipo_trasporto varchar(100),
    data_trasporto date,
    ora_trasporto time,
    trasportatore varchar(255),
    fatturato bit(1) NOT NULL DEFAULT b'0',
    totale_imponibile decimal(10,3),
    totale_iva decimal(10,3),
    totale_costo decimal(10,3),
    totale decimal(10,3),
    totale_acconto decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ddt_vend_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_ddt_vend_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`),
	CONSTRAINT `fk_ddt_vend_autista` FOREIGN KEY (`id_autista`) REFERENCES `autista` (`id`),
	CONSTRAINT `fk_ddt_vend_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_ddt` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `ddt_articolo` (
	id_ddt int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	quantita decimal(10,3),
	numero_pezzi int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_ddt, id_articolo, uuid),
	CONSTRAINT `fk_ddt_articolo_ddt` FOREIGN KEY (`id_ddt`) REFERENCES `ddt` (`id`),
	CONSTRAINT `fk_ddt_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB;

INSERT INTO stato_ddt(id,codice,descrizione,ordine) VALUES(0,'DA_PAGARE','Da pagare',1);
INSERT INTO stato_ddt(id,codice,descrizione,ordine) VALUES(1,'PARZIALMENTE_PAGATO','Parzialmente pagato',2);
INSERT INTO stato_ddt(id,codice,descrizione,ordine) VALUES(2,'PAGATO','Pagato',3);

UPDATE stato_ordine SET codice='PARZIALMENTE_EVASO' WHERE id=1;

CREATE TABLE `pagamento` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	data DATE,
	id_tipo_pagamento int(10) unsigned,
	id_ddt int(10) unsigned,
	descrizione varchar(255),
	importo decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_pagamento_tipo_pag` FOREIGN KEY (`id_tipo_pagamento`) REFERENCES `tipo_pagamento` (`id`),
	CONSTRAINT `fk_pagamento_ddt` FOREIGN KEY (`id_ddt`) REFERENCES `ddt` (`id`)
) ENGINE=InnoDB;

ALTER TABLE `ddt_articolo` ADD COLUMN lotto varchar(255) AFTER uuid;