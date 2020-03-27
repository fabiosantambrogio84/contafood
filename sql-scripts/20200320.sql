DROP VIEW IF EXISTS `v_fattura`;
DROP VIEW IF EXISTS `v_ddt_fattura_accom`;
DROP VIEW IF EXISTS `v_ddt_fattura_accom_articolo`;
ALTER TABLE `fattura` DROP FOREIGN KEY fk_fattura_tipo;
ALTER TABLE `fattura` DROP COLUMN id_tipo;
DROP TABLE IF EXISTS `fattura_accom_articolo`;
DROP TABLE IF EXISTS `fattura_accom_totale`;
DROP TABLE IF EXISTS `fattura_accom`;
DROP TABLE IF EXISTS `tipo_fattura`;

ALTER TABLE contafood.fattura MODIFY COLUMN note text CHARACTER SET latin1 COLLATE latin1_general_cs NULL;

CREATE TABLE `tipo_fattura` (
	id int(10) unsigned,
	codice varchar(255),
	descrizione text,
	ordine int(10),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(0,'VENDITA','Vendita',1);
INSERT INTO tipo_fattura(id,codice,descrizione,ordine) VALUES(1,'ACCOMPAGNATORIA','Accompagnatoria',2);

ALTER TABLE `fattura` ADD COLUMN id_tipo int(10) unsigned AFTER data;
ALTER TABLE `fattura` ADD CONSTRAINT fk_fattura_tipo FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`);

UPDATE `fattura` SET id_tipo = 0;

CREATE TABLE `fattura_accom` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	progressivo int(11),
	anno int(11),
	data DATE,
	id_tipo int(10) unsigned,
	id_cliente int(10) unsigned,
	id_punto_consegna int(10) unsigned,
	id_stato int(10) unsigned,
	spedito_ade bit(1) DEFAULT b'0',
	numero_colli int(10),
    tipo_trasporto varchar(100),
    data_trasporto date,
    ora_trasporto time,
    trasportatore varchar(255),
    totale_acconto decimal(10,3),
    totale decimal(10,3),
    note text,
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_fattura_accom_tipo` FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`),
	CONSTRAINT `fk_fattura_accom_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
	CONSTRAINT `fk_fattura_accom_punto_con` FOREIGN KEY (`id_punto_consegna`) REFERENCES `punto_consegna` (`id`),
	CONSTRAINT `fk_fattura_accom_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_fattura` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `fattura_accom_articolo` (
	id_fattura_accom int(10) unsigned,
	id_articolo int(10) unsigned,
	uuid varchar(255),
	lotto varchar(100),
	quantita decimal(10,3),
	numero_pezzi int(10),
	prezzo decimal(10,3),
    sconto decimal(10,3),
    imponibile decimal(10,3),
    costo decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_fattura_accom, id_articolo, uuid),
	CONSTRAINT `fk_fattura_accom_articolo_fatt` FOREIGN KEY (`id_fattura_accom`) REFERENCES `fattura_accom` (`id`),
	CONSTRAINT `fk_fattura_accom_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `fattura_accom_totale` (
	id_fattura_accom int(10) unsigned,
	id_aliquota_iva int(10) unsigned,
	uuid varchar(255),
	totale_iva decimal(10,3),
	totale_imponibile decimal(10,3),
	data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_aggiornamento TIMESTAMP,
	PRIMARY KEY (id_fattura_accom, id_aliquota_iva, uuid),
	CONSTRAINT `fk_fattura_accom_totale_fatt` FOREIGN KEY (`id_fattura_accom`) REFERENCES `fattura_accom` (`id`),
	CONSTRAINT `fk_fattura_accom_totale_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE VIEW `v_fattura` AS
SELECT
    id, progressivo, anno, data, id_tipo, id_cliente, id_stato, spedito_ade, totale_acconto, totale, note, data_inserimento, data_aggiornamento
FROM
    fattura
UNION ALL
select
	id, progressivo, anno, data, id_tipo, id_cliente, id_stato, spedito_ade, totale_acconto, totale, note, data_inserimento, data_aggiornamento
FROM
    fattura_accom;


ALTER TABLE `ddt_articolo` ADD COLUMN totale decimal(10,3) after costo;
ALTER TABLE `ddt` ADD COLUMN totale_quantita decimal(10,3) after totale_acconto;

---------------------------------------------------------------------------------------------------------------------------------
DROP procedure IF EXISTS setTotaleDdtArticoli;
DROP procedure IF EXISTS setTotaliDdt;


DELIMITER //

CREATE PROCEDURE setTotaleDdtArticoli()
begin
	DECLARE done INT DEFAULT FALSE;
	DECLARE v_id_ddt, v_id_articolo INT(10);
	DECLARE v_uuid VARCHAR(255) CHARACTER SET latin1;
	declare v_totale DECIMAL(10,3);

	DECLARE cur1 CURSOR FOR select ddt_articolo.id_ddt, ddt_articolo.id_articolo, ddt_articolo.uuid,
							round(imponibile + ((aliquota_iva.valore/100)*imponibile),2) as totale
							FROM ddt_articolo
							join articolo on ddt_articolo.id_articolo = articolo.id
							join aliquota_iva on articolo.id_aliquota_iva = aliquota_iva.id;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	OPEN cur1;

	read_loop: LOOP
	    FETCH cur1 INTO v_id_ddt,v_id_articolo,v_uuid,v_totale;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;

		update ddt_articolo set totale = v_totale where ddt_articolo.id_ddt=v_id_ddt and ddt_articolo.id_articolo=v_id_articolo and ddt_articolo.uuid=v_uuid;

	END LOOP;

  CLOSE cur1;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE setTotaliDdt()
begin
	DECLARE done INT DEFAULT FALSE;
	DECLARE v_id_ddt INT(10);
	declare v_quantita, v_totale DECIMAL(10,3);

	DECLARE cur1 CURSOR FOR SELECT id_ddt, round(sum(quantita),2) as quantita, round(sum(totale),2) as totale
							FROM ddt_articolo
							group by id_ddt;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	OPEN cur1;

	read_loop: LOOP
	    FETCH cur1 INTO v_id_ddt,v_quantita,v_totale;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;

		update ddt set totale_quantita = v_quantita, totale = v_totale where ddt.id=v_id_ddt;

	END LOOP;

    CLOSE cur1;

    update ddt set totale_iva = round(totale-totale_imponibile,2);
END //

DELIMITER ;

-- CALL setTotaleDdtArticoli();
-- CALL setTotaliDdt();

------------------------------------------------------------------------------------------------------------------------
ALTER TABLE `fattura_accom_articolo` ADD COLUMN totale decimal(10,3) after costo;
ALTER TABLE `fattura_accom` ADD COLUMN totale_quantita decimal(10,3) after totale;

DROP procedure IF EXISTS setTotaleFatturaAccompagnatoriaArticoli;
DROP procedure IF EXISTS setTotaliFatturaAccompagnatoria;


DELIMITER //

CREATE PROCEDURE setTotaleFatturaAccompagnatoriaArticoli()
begin
	DECLARE done INT DEFAULT FALSE;
	DECLARE v_id_fattura_accom, v_id_articolo INT(10);
	DECLARE v_uuid VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs;
	declare v_totale DECIMAL(10,3);

	DECLARE cur1 CURSOR FOR select fattura_accom_articolo.id_fattura_accom, fattura_accom_articolo.id_articolo, fattura_accom_articolo.uuid,
							round(imponibile + ((aliquota_iva.valore/100)*imponibile),2) as totale
							FROM fattura_accom_articolo
							join articolo on fattura_accom_articolo.id_articolo = articolo.id
							join aliquota_iva on articolo.id_aliquota_iva = aliquota_iva.id;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	OPEN cur1;

	read_loop: LOOP
	    FETCH cur1 INTO v_id_fattura_accom,v_id_articolo,v_uuid,v_totale;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;

		update fattura_accom_articolo set totale = v_totale where fattura_accom_articolo.id_fattura_accom=v_id_fattura_accom and fattura_accom_articolo.id_articolo=v_id_articolo and fattura_accom_articolo.uuid=v_uuid;

	END LOOP;

  CLOSE cur1;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE setTotaliFatturaAccompagnatoria()
begin
	DECLARE done INT DEFAULT FALSE;
	DECLARE v_id_fattura_accom INT(10);
	declare v_quantita, v_totale DECIMAL(10,3);

	DECLARE cur1 CURSOR FOR SELECT id_fattura_accom, round(sum(quantita),2) as quantita, round(sum(totale),2) as totale
							FROM fattura_accom_articolo
							group by id_fattura_accom;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	OPEN cur1;

	read_loop: LOOP
	    FETCH cur1 INTO v_id_fattura_accom,v_quantita,v_totale;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;

		update fattura_accom set totale_quantita = v_quantita, totale = v_totale where id=v_id_fattura_accom;

	END LOOP;

    CLOSE cur1;

END //

DELIMITER ;

-- CALL setTotaleFatturaAccompagnatoriaArticoli();
-- CALL setTotaliFatturaAccompagnatoria();

------------------------------------------------------------------------------------------------------------------------
/*
CREATE VIEW `v_ddt_fattura_accom` AS
SELECT
    'DDT' as tipo,id,progressivo,anno_contabile as anno,data,id_cliente,totale,totale_quantita
FROM
    ddt
UNION ALL
select
	'FATTURA_ACCOMPAGNATORIA' as tipo,id,progressivo,anno as anno,data,id_cliente,totale,totale_quantita
FROM
    fattura_accom;

CREATE VIEW `v_ddt_fattura_accom_articolo` AS
SELECT
    id_articolo,lotto,quantita,prezzo,sconto,imponibile,totale
FROM
    ddt_articolo
UNION ALL
select
    id_articolo,lotto,quantita,prezzo,sconto,imponibile,totale
FROM
    fattura_accom_articolo;

 */