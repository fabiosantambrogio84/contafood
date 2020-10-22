ALTER TABLE nota_accredito ADD COLUMN tipo_riferimento varchar(100) AFTER note;
ALTER TABLE nota_accredito ADD COLUMN documento_riferimento varchar(100) AFTER tipo_riferimento;
ALTER TABLE nota_accredito ADD COLUMN data_documento_riferimento date AFTER documento_riferimento;

ALTER TABLE contafood.nota_accredito MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;
ALTER TABLE contafood.nota_accredito_riga MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;
ALTER TABLE contafood.nota_accredito_totale MODIFY COLUMN data_aggiornamento timestamp DEFAULT '0000-00-00 00:00:00' NULL;
