DROP TABLE IF EXISTS contafood.pagamento_aggregato;

CREATE TABLE contafood.pagamento_aggregato (
    id int(10) unsigned NOT NULL AUTO_INCREMENT,
    descrizione text,
    note text,
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

ALTER TABLE contafood.pagamento ADD COLUMN id_pagamento_aggregato int(10) unsigned AFTER importo;

