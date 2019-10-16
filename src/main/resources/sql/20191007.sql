ALTER TABLE `fornitore` DROP COLUMN ditta_individuale;
ALTER TABLE `fornitore` DROP COLUMN nome;
ALTER TABLE `fornitore` DROP COLUMN cognome;
ALTER TABLE `fornitore` CHANGE COLUMN `codice` `codice` INT(10);

-- CREATE UNIQUE INDEX idx_fornitore_codice ON fornitore(codice);

ALTER TABLE `ricetta` CHANGE COLUMN `numero_porzioni` `peso_totale` DECIMAL(10,3) NULL DEFAULT NULL ;
ALTER TABLE `ricetta` ADD COLUMN scadenza_giorni int(10) AFTER peso_totale;

ALTER TABLE `produzione` CHANGE COLUMN `codice` `codice` INT(10);
ALTER TABLE `produzione` ADD COLUMN scadenza DATE;
ALTER TABLE `produzione` ADD COLUMN quantita_totale DECIMAL(10,3);
ALTER TABLE `produzione` ADD COLUMN scopo varchar(100) DEFAULT 'vendita';
ALTER TABLE `produzione` ADD COLUMN numero_confezioni int(10) DEFAULT NULL AFTER quantita_totale;
ALTER TABLE `produzione` DROP COLUMN id_confezione;
ALTER TABLE `produzione` DROP COLUMN num_confezioni;

DROP TABLE IF EXISTS `produzione_confezione`;

CREATE TABLE `produzione_confezione` (
	id_produzione int(10) unsigned NOT NULL,
	id_confezione int(10) unsigned NOT NULL,
	num_confezioni int(10),
	PRIMARY KEY (`id_produzione`, id_confezione)
) ENGINE=InnoDB;


ALTER TABLE `ricetta_ingrediente` ADD COLUMN percentuale DECIMAL(10,3);