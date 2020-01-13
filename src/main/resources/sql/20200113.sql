ALTER TABLE `telefonata` ADD COLUMN giorno_consegna varchar(100) AFTER giorno_ordinale;
ALTER TABLE `telefonata` ADD COLUMN giorno_consegna_ordinale int(10) AFTER giorno_consegna;
ALTER TABLE `telefonata` ADD COLUMN ora_consegna time AFTER ora;
