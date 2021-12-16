ALTER TABLE contafood.ddt_acquisto_articolo ADD COLUMN numero_pezzi int AFTER quantita;
ALTER TABLE contafood.ddt_acquisto_ingrediente ADD COLUMN numero_pezzi int AFTER quantita;

ALTER TABLE contafood.ddt_acquisto ADD COLUMN totale_iva decimal(10,3) after totale_imponibile;