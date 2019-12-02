ALTER TABLE `articolo` ADD COLUMN prezzo_listino_base decimal(10,3) AFTER prezzo_acquisto;

ALTER TABLE `listino` ADD COLUMN tipologia varchar(255) default 'STANDARD' AFTER id_listino;

ALTER TABLE `sconto` ADD COLUMN tipologia varchar(255) default null AFTER id_cliente;
