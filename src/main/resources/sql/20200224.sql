ALTER TABLE `cliente` ADD COLUMN id_listino int(10) unsigned AFTER id_agente;
ALTER TABLE cliente ADD CONSTRAINT `fk_cliente_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`);

ALTER TABLE `produzione_confezione` ADD COLUMN peso float;

ALTER TABLE `produzione_ingrediente` ADD COLUMN uuid varchar(255) NOT NULL after id_ingrediente;
ALTER TABLE `produzione_ingrediente` DROP PRIMARY KEY;
ALTER TABLE `produzione_ingrediente` ADD CONSTRAINT pk_produzione_ingrediente PRIMARY KEY (id_produzione, id_ingrediente, uuid);