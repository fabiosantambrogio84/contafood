DROP TABLE IF EXISTS `autista`;

CREATE TABLE `autista` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(100),
	cognome varchar(100),
	telefono varchar(100),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `agente`;

CREATE TABLE `agente` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(100),
	cognome varchar(100),
	telefono varchar(100),
	email varchar(100),
	indirizzo varchar(100),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- ALTER TABLE `fornitore` ADD COLUMN id_autista int(10) AFTER pagamento;
-- ALTER TABLE `fornitore` ADD COLUMN id_agente int(10) AFTER id_autista;
-- ALTER TABLE `fornitore` ADD COLUMN blocca_ddt bit(1) NOT NULL DEFAULT b'0' AFTER id_agente;
-- ALTER TABLE `fornitore` ADD COLUMN nascondi_prezzi bit(1) NOT NULL DEFAULT b'0' AFTER blocca_ddt;
-- ALTER TABLE `fornitore` ADD COLUMN raggruppa_riba bit(1) NOT NULL DEFAULT b'0' AFTER nascondi_prezzi;
-- ALTER TABLE `fornitore` ADD COLUMN nome_gruppo_riba varchar(100) NOT NULL DEFAULT b'0' AFTER raggruppa_riba;

