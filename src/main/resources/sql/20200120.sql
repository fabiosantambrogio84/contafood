ALTER TABLE `confezione` ADD COLUMN codice varchar(100) AFTER id;
ALTER TABLE `confezione` ADD COLUMN prezzo decimal(10,3) AFTER peso;
ALTER TABLE `confezione` ADD COLUMN id_fornitore int(10) unsigned AFTER prezzo;
ALTER TABLE confezione ADD CONSTRAINT `fk_confezione_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`);
ALTER TABLE `confezione` ADD COLUMN note text AFTER id_fornitore;
ALTER TABLE `confezione` ADD COLUMN data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER note;
ALTER TABLE `confezione` ADD COLUMN data_aggiornamento TIMESTAMP AFTER data_inserimento;

ALTER TABLE `produzione_confezione` ADD COLUMN lotto varchar(100);

ALTER TABLE `produzione` ADD COLUMN film_chiusura varchar(100) AFTER scopo;
ALTER TABLE `produzione` ADD COLUMN lotto_film_chiusura varchar(100) AFTER film_chiusura;
ALTER TABLE `produzione` ADD COLUMN data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER lotto_film_chiusura;
ALTER TABLE `produzione` ADD COLUMN data_aggiornamento TIMESTAMP AFTER data_inserimento;
