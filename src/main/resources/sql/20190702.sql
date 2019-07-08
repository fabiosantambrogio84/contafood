--
-- "docker run --name mysql01 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.6.44 "
--
-- mysql -u root -p
-- -> chiede password. Inserire root

-- mysql> create user 'contafood'@'%' identified by 'contafood';
-- mysql> grant all on contafood.* to 'contafood'@'%';

CREATE DATABASE  IF NOT EXISTS `contafood`;
USE `contafood`;

DROP TABLE IF EXISTS `fornitore`;

CREATE TABLE `fornitore` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	ragione_sociale varchar(100),
	ragione_sociale_2 varchar(100),
	fl_ditta_individuale bit(1) NOT NULL DEFAULT b'0',
	nome varchar(100),
	cognome varchar(100),
	indirizzo varchar(100),
	citta varchar(100),
	provincia varchar(100),
	cap varchar(50),
	nazione varchar(100),
	partita_iva varchar(100),
	codice_fiscale varchar(100),
	telefono varchar(100),
	telefono_2 varchar(100),
	telefono_3 varchar(100),
	email varchar(100),
	email_pec varchar(100),
	codice_univoco_sdi varchar(100),
	iban varchar(100),
	pagamento varchar(100),
	note text,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `categoria_articolo`;

CREATE TABLE `categoria_articolo` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(100),
	ordine int(11),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

