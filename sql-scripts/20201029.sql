ALTER TABLE nota_accredito_riga ADD COLUMN num_riga int AFTER id_articolo;

ALTER TABLE contafood.nota_reso MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;
ALTER TABLE contafood.nota_reso_riga MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;
ALTER TABLE contafood.nota_reso_totale MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;

DROP TABLE IF EXISTS `ricevuta_privato_ordine_cliente`;
DROP TABLE IF EXISTS `ricevuta_privato_articolo`;
DROP TABLE IF EXISTS `ricevuta_privato_totale`;
DROP TABLE IF EXISTS `ricevuta_privato`;
DROP TABLE IF EXISTS stato_ricevuta_privato;

CREATE TABLE `stato_ricevuta_privato` (
	id int(10) unsigned NOT NULL,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO stato_ricevuta_privato(id,codice,descrizione,ordine) VALUES(0,'DA_PAGARE','Da pagare',1);
INSERT INTO stato_ricevuta_privato(id,codice,descrizione,ordine) VALUES(1,'PARZIALMENTE_PAGATA','Parzialmente pagata',2);
INSERT INTO stato_ricevuta_privato(id,codice,descrizione,ordine) VALUES(2,'PAGATA','Pagata',3);

CREATE TABLE `ricevuta_privato` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	id_autista int(10) unsigned,
	id_stato int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
	numero_colli int(10),
    tipo_trasporto varchar(100),
    data_trasporto date,
    ora_trasporto time,
    trasportatore varchar(255),
    totale_imponibile decimal(10,3),
    totale_iva decimal(10,3),
    totale_costo decimal(10,3),
    totale_acconto decimal(10,3),
    totale decimal(10,3),
    totale_quantita decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_ricevuta_pvt_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_ricevuta_pvt_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`),
	CONSTRAINT `fk_ricevuta_pvt_autista` FOREIGN KEY (`id_autista`) REFERENCES `autista` (`id`),
	CONSTRAINT `fk_ricevuta_pvt_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_ricevuta_privato` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `ricevuta_privato_articolo` (
	id_ricevuta_privato int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	lotto varchar(100),
	scadenza DATE,
	quantita decimal(10,3),
	numero_pezzi int(10),
	numero_pezzi_da_evadere int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
    totale decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP NULL,
	PRIMARY KEY (id_ricevuta_privato, id_articolo, uuid),
	CONSTRAINT `fk_ricevuta_pvt_articolo_fatt` FOREIGN KEY (`id_ricevuta_privato`) REFERENCES `ricevuta_privato` (`id`),
	CONSTRAINT `fk_ricevuta_pvt_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `ricevuta_privato_totale` (
	id_ricevuta_privato int(10) unsigned,
	id_aliquota_iva int(10) unsigned,
	uuid varchar(255),
	totale_iva decimal(10,3),
	totale_imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP NULL,
	PRIMARY KEY (id_ricevuta_privato, id_aliquota_iva, uuid),
	CONSTRAINT `fk_ricevuta_pvt_totale_fatt` FOREIGN KEY (`id_ricevuta_privato`) REFERENCES `ricevuta_privato` (`id`),
	CONSTRAINT `fk_ricevuta_pvt_totale_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `ricevuta_privato_ordine_cliente` (
	id_ricevuta_privato int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	id_ordine_cliente int(10) unsigned,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id_ricevuta_privato, id_articolo, uuid, id_ordine_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;


ALTER TABLE cliente ADD COLUMN privato bit(1) NOT NULL DEFAULT b'0' AFTER codice;

---------------------------------------------

ALTER TABLE pagamento ADD COLUMN id_ricevuta_privato int(10) unsigned AFTER id_nota_reso;

-- 13/11/2020
ALTER TABLE pagamento ADD COLUMN id_fattura int(10) unsigned AFTER id_ricevuta_privato;
ALTER TABLE pagamento ADD COLUMN id_fattura_accom int(10) unsigned AFTER id_fattura;