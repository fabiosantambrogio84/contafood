ALTER TABLE contafood.fornitore ADD COLUMN barcode_mask_lotto_scadenza varchar(255) AFTER pagamento;
ALTER TABLE contafood.fornitore ADD COLUMN barcode_regexp_lotto varchar(100) AFTER barcode_mask_lotto_scadenza;
ALTER TABLE contafood.fornitore ADD COLUMN barcode_regexp_data_scadenza varchar(100) AFTER barcode_regexp_lotto;

ALTER TABLE contafood.articolo ADD COLUMN barcode_mask_lotto_scadenza varchar(255) AFTER complete_barcode;
ALTER TABLE contafood.articolo ADD COLUMN barcode_regexp_lotto varchar(100) AFTER barcode_mask_lotto_scadenza;
ALTER TABLE contafood.articolo ADD COLUMN barcode_regexp_data_scadenza varchar(100) AFTER barcode_regexp_lotto;
