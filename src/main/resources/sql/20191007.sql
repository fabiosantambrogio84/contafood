ALTER TABLE `fornitore` DROP COLUMN ditta_individuale;
ALTER TABLE `fornitore` DROP COLUMN nome;
ALTER TABLE `fornitore` DROP COLUMN cognome;

CREATE UNIQUE INDEX idx_fornitore_codice ON fornitore(codice);

ALTER TABLE `ricetta` CHANGE COLUMN `numero_porzioni` `peso_totale` DECIMAL(10,3) NULL DEFAULT NULL ;
