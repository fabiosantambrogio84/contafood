ALTER TABLE `telefonata` ADD COLUMN id_autista int(10) unsigned AFTER id_punto_consegna;
ALTER TABLE telefonata ADD CONSTRAINT `fk_telefonata_autista` FOREIGN KEY (`id_autista`) REFERENCES `autista` (`id`);

