DROP TABLE IF EXISTS `parametro`;

CREATE TABLE `parametro` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(100),
	descrizione varchar(100),
	unita_di_misura varchar(100),
	valore varchar(100),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;


INSERT INTO `parametro`(nome, descrizione, unita_di_misura, valore)
VALUES ('COSTO_ORARIO_PREPARAZIONE_RICETTA', 'Costo orario di preparazione per ogni ricetta', '&euro;', '15');


-- https://www.mkyong.com/spring-boot/spring-rest-spring-security-example/