ALTER TABLE `produzione` ADD COLUMN lotto varchar(100) NOT NULL;
ALTER TABLE `produzione` ADD COLUMN lotto_anno int NOT NULL;
ALTER TABLE `produzione` ADD COLUMN lotto_giorno int NOT NULL;
ALTER TABLE `produzione` ADD COLUMN lotto_numero_progressivo int NOT NULL;

CREATE INDEX idx_lotto_anno_giorno ON produzione(lotto_anno, lotto_giorno);