DROP TABLE IF EXISTS contafood.causale;

CREATE TABLE contafood.`causale` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `descrizione` text,
  `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_aggiornamento` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;