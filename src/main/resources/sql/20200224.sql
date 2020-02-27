ALTER TABLE `cliente` ADD COLUMN id_listino int(10) unsigned AFTER id_agente;
ALTER TABLE cliente ADD CONSTRAINT `fk_cliente_listino` FOREIGN KEY (`id_listino`) REFERENCES `listino` (`id`);