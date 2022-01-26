ALTER TABLE contafood.fornitore ADD COLUMN attivo bit(1) NOT NULL DEFAULT b'1';
ALTER TABLE contafood.autista ADD COLUMN attivo bit(1) NOT NULL DEFAULT b'1';

DROP TABLE IF EXISTS contafood.configurazione_client;

CREATE TABLE contafood.configurazione_client (
    id int(10) unsigned,
    codice varchar(100),
    descrizione text,
    abilitato bit(1) NOT NULL DEFAULT b'1',
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_aggiornamento TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO contafood.configurazione_client(id, codice, descrizione, abilitato, data_inserimento)
values(1, 'MENU_CONFIGURAZIONE', 'Visualizza menu ''Configurazione''', 0, CURRENT_TIMESTAMP );

INSERT INTO contafood.configurazione_client(id, codice, descrizione, abilitato, data_inserimento)
values(2, 'RICEVUTA_PRIVATO', 'Abilita ''Ricevuta a privato''', 0, CURRENT_TIMESTAMP );

INSERT INTO contafood.configurazione_client(id, codice, descrizione, abilitato, data_inserimento)
values(3, 'DDT_COSTO', 'Visualizza ''costo'' in elenchi DDT', 0, CURRENT_TIMESTAMP );

INSERT INTO contafood.configurazione_client(id, codice, descrizione, abilitato, data_inserimento)
values(4, 'DDT_GUADAGNO', 'Visualizza ''guadagno'' in elenchi DDT', 0, CURRENT_TIMESTAMP );