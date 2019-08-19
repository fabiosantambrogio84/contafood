ALTER TABLE `ingrediente` ADD COLUMN unita_di_misura varchar(100);
ALTER TABLE `ingrediente` ADD COLUMN id_fornitore int(10);
ALTER TABLE `ingrediente` ADD COLUMN data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE `ingrediente` ADD COLUMN attivo bit(1) NOT NULL DEFAULT b'1';
ALTER TABLE `ingrediente` ADD COLUMN note TEXT;


ALTER TABLE `ricetta` MODIFY tempo_preparazione int(10);