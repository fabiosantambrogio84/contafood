CREATE TABLE `fattura_accom_acquisto_ingrediente` (
   `id_fattura_accom_acquisto` int unsigned NOT NULL DEFAULT '0',
   `id_ingrediente` int unsigned NOT NULL DEFAULT '0',
   `uuid` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL DEFAULT '',
   `lotto` varchar(100) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT NULL,
   `scadenza` date DEFAULT NULL,
   `quantita` decimal(10,3) DEFAULT NULL,
   `numero_pezzi` int DEFAULT NULL,
   `numero_pezzi_da_evadere` int DEFAULT NULL,
   `prezzo` decimal(10,2) DEFAULT NULL,
   `sconto` decimal(10,2) DEFAULT NULL,
   `imponibile` decimal(10,2) DEFAULT NULL,
   `costo` decimal(10,2) DEFAULT NULL,
   `totale` decimal(10,2) DEFAULT NULL,
   `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `data_aggiornamento` timestamp NULL DEFAULT NULL,
   PRIMARY KEY (`id_fattura_accom_acquisto`,`id_ingrediente`,`uuid`),
   KEY `fk_fattura_accom_acq_ingrediente_art` (`id_ingrediente`),
   CONSTRAINT `fk_fattura_accom_acq_ingrediente_art` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`),
   CONSTRAINT `fk_fattura_accom_acq_ingrediente_fatt` FOREIGN KEY (`id_fattura_accom_acquisto`) REFERENCES `fattura_accom_acquisto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

