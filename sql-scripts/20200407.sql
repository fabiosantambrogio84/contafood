ALTER TABLE contafood.articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.confezione MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ddt MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ddt_acquisto MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ddt_acquisto_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ddt_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.fattura MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.fattura_accom MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.fattura_accom_totale MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.fattura_ddt MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.listino MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.listino_prezzo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.listino_prezzo_variazione MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ordine_cliente MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ordine_cliente_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ordine_fornitore MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.ordine_fornitore_articolo MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.pagamento MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.produzione MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.stato_ddt MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.stato_fattura MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.stato_ordine MODIFY COLUMN data_aggiornamento timestamp NULL;
ALTER TABLE contafood.tipo_fattura MODIFY COLUMN data_aggiornamento timestamp NULL;


ALTER TABLE contafood.produzione ADD COLUMN tempo_impiegato decimal(10,2) after scadenza;

ALTER TABLE contafood.ddt_articolo ADD COLUMN scadenza DATE after lotto;
ALTER TABLE contafood.fattura_accom_articolo ADD COLUMN scadenza DATE after lotto;
