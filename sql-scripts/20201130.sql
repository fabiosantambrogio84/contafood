DROP TABLE IF EXISTS contafood.sequence_data;

CREATE TABLE contafood.`sequence_data` (
  `sequence_name` varchar(100) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `sequence_increment` int(11) unsigned NOT NULL DEFAULT '1',
  `sequence_min_value` int(11) unsigned NOT NULL DEFAULT '1',
  `sequence_max_value` bigint(20) unsigned NOT NULL DEFAULT '18446744073709551615',
  `sequence_cur_value` bigint(20) unsigned DEFAULT '1',
  `sequence_cycle` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO contafood.sequence_data(sequence_name) VALUES('seq_e_fatturazione');
INSERT INTO contafood.sequence_data(sequence_name) VALUES('seq_e_fatturazione_file');
INSERT INTO contafood.sequence_data(sequence_name) VALUES('seq_e_fatturazione_file_zip');


SET GLOBAL log_bin_trust_function_creators = 1;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION contafood.`nextval`(`seq_name` varchar(100)) RETURNS bigint(20)
BEGIN
    DECLARE cur_val bigint(20);

    SELECT
        sequence_cur_value INTO cur_val
    FROM
        contafood.sequence_data
    WHERE
        sequence_name = seq_name;

    IF cur_val IS NOT NULL THEN
        UPDATE
            contafood.sequence_data
        SET
            sequence_cur_value = IF (
                (sequence_cur_value + sequence_increment) > sequence_max_value,
                IF (
                    sequence_cycle = TRUE,
                    sequence_min_value,
                    NULL
                ),
                sequence_cur_value + sequence_increment
            )
        WHERE
            sequence_name = seq_name;
    END IF;

    RETURN cur_val;
END$$
DELIMITER ;
SET GLOBAL log_bin_trust_function_creators = 0;


DROP TABLE IF EXISTS contafood.causale;

CREATE TABLE `causale` (
    id int(10) unsigned NOT NULL,
    descrizione text,
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_aggiornamento TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO causale(id,descrizione) VALUES(0,'Consulenza');
INSERT INTO causale(id,descrizione) VALUES(1,'Conto lavorazione');
INSERT INTO causale(id,descrizione) VALUES(2,'Reso merce');
INSERT INTO causale(id,descrizione) VALUES(3,'Sconto merce');
INSERT INTO causale(id,descrizione) VALUES(4,'Spese trasporto');
INSERT INTO causale(id,descrizione) VALUES(5,'Vendita');

ALTER TABLE `ddt` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;
ALTER TABLE `fattura` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;
ALTER TABLE `fattura_accom` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;
ALTER TABLE `nota_accredito` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;
ALTER TABLE `nota_reso` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;
ALTER TABLE `ricevuta_privato` ADD COLUMN id_causale int(10) unsigned AFTER id_stato;

UPDATE ddt SET id_causale=5;
UPDATE fattura SET id_causale=5;
UPDATE fattura_accom SET id_causale=5;
UPDATE ricevuta_privato SET id_causale=5;
UPDATE nota_accredito SET id_causale=3;
UPDATE nota_reso SET id_causale=2;
