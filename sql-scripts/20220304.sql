ALTER TABLE contafood.telefonata ADD COLUMN eseguito bit(1) NOT NULL DEFAULT b'0' after ora_consegna;