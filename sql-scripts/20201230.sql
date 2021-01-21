ALTER TABLE contafood.cliente ADD COLUMN modalita_invio_fatture VARCHAR(100) AFTER estrazione_conad;


DROP TABLE IF EXISTS contafood.proprieta;
DROP TABLE IF EXISTS contafood.cliente_articolo;

CREATE TABLE contafood.proprieta (
    id int(10) unsigned NOT NULL,
    nome varchar(100),
    descrizione text,
    valore varchar(150),
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_aggiornamento TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO contafood.proprieta(id,nome,descrizione) VALUES(0,'PEC_SMTP_HOST', 'PEC SMTP server host');
INSERT INTO contafood.proprieta(id,nome,descrizione) VALUES(1,'PEC_SMTP_PORT', 'PEC SMTP server port');
INSERT INTO contafood.proprieta(id,nome,descrizione) VALUES(2,'PEC_SMTP_USER', 'PEC STMP server username');
INSERT INTO contafood.proprieta(id,nome,descrizione) VALUES(3,'PEC_SMTP_PASSWORD', 'PEC SMTP server password');

CREATE TABLE `cliente_articolo` (
  `id_cliente` int(10) unsigned NOT NULL,
  `id_articolo` int(10) unsigned NOT NULL,
  `uuid` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id_cliente`,`id_articolo`,`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;