DROP TABLE IF EXISTS `tipo_fattura`;

CREATE TABLE `tipo_fattura` (
	id int(10) unsigned,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(0,'VENDITA','Vendita',1);
INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(1,'ACCOMPAGNATORIA','Accompagnatoria',2);

ALTER TABLE `fattura` ADD COLUMN id_tipo int(10) unsigned AFTER data;
ALTER TABLE `fattura` ADD CONSTRAINT fk_fattura_tipo FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`);

UPDATE `fattura` SET id_tipo = 0;