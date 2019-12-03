ALTER TABLE `produzione` ADD COLUMN data_produzione DATE AFTER codice;
ALTER TABLE produzione MODIFY COLUMN lotto varchar(100) NULL;
ALTER TABLE produzione MODIFY COLUMN lotto_anno int(11) NULL;
ALTER TABLE produzione MODIFY COLUMN lotto_giorno int(11) NULL;
ALTER TABLE produzione MODIFY COLUMN lotto_numero_progressivo int(11) NULL;

