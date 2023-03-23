alter table ddt_acquisto add column `totale_acconto` decimal(10,2) DEFAULT NULL after totale;
alter table ddt_acquisto add column `fatturato` bit(1) DEFAULT b'0' after totale_acconto;
alter table ddt_acquisto add column `id_stato` int unsigned DEFAULT NULL after id_fornitore;
alter table ddt_acquisto add CONSTRAINT `fk_ddt_acquisto_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_ddt` (`id`);

CREATE TABLE `fattura_acquisto` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `progressivo` int DEFAULT NULL,
    `anno` int DEFAULT NULL,
    `data` date DEFAULT NULL,
    `id_fornitore` int unsigned DEFAULT NULL,
    `id_stato` int unsigned DEFAULT NULL,
    `id_causale` int unsigned DEFAULT NULL,
    `totale_imponibile` decimal(10,2) DEFAULT NULL,
    `totale_acconto` decimal(10,2) DEFAULT NULL,
    `totale_iva` decimal(10,2) DEFAULT NULL,
    `totale` decimal(10,2) DEFAULT NULL,
    `note` text CHARACTER SET latin1 COLLATE latin1_general_cs,
    `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_aggiornamento` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_fattura_acquisto_fornitore` (`id_fornitore`),
    CONSTRAINT `fk_fattura_acquisto_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`),
    CONSTRAINT `fk_fattura_acquisto_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_fattura` (`id`),
    CONSTRAINT `fk_fattura_acquisto_causale` FOREIGN KEY (`id_causale`) REFERENCES `causale` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `fattura_acquisto_ddt_acquisto` (
   `id_fattura_acquisto` int unsigned NOT NULL DEFAULT '0',
   `id_ddt_acquisto` int unsigned NOT NULL DEFAULT '0',
   `uuid` varchar(255) NOT NULL DEFAULT '',
   `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `data_aggiornamento` timestamp NULL DEFAULT NULL,
   PRIMARY KEY (`id_fattura_acquisto`,`id_ddt_acquisto`,`uuid`),
   CONSTRAINT `fk_fattura_acquisto_ddt_acquisto` FOREIGN KEY (`id_ddt_acquisto`) REFERENCES `ddt_acquisto` (`id`),
   CONSTRAINT `fk_fattura_acquisto_fattura_acquisto` FOREIGN KEY (`id_fattura_acquisto`) REFERENCES `fattura_acquisto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

alter table pagamento add column `id_ddt_acquisto` int unsigned DEFAULT NULL after id_ddt;
alter table pagamento add CONSTRAINT `fk_pagamento_ddt_acquisto` FOREIGN KEY (`id_ddt_acquisto`) REFERENCES `ddt_acquisto` (`id`);
alter table pagamento add column `id_fattura_acquisto` int unsigned DEFAULT NULL after id_fattura_accom;
alter table pagamento add CONSTRAINT `fk_pagamento_fattura_acquisto` FOREIGN KEY (`id_fattura_acquisto`) REFERENCES `fattura_acquisto` (`id`);


create or replace view `v_documento_acquisto` as
select
    concat('da_',ddt_acquisto.id) as id,
    'DDT acquisto' as tipo_documento,
    ddt_acquisto.id as id_documento,
    ddt_acquisto.numero as num_documento,
    ddt_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    ddt_acquisto.id_stato,
    stato_ddt.codice as stato,
    ddt_acquisto.totale_imponibile,
    ddt_acquisto.totale_iva,
    ddt_acquisto.totale,
    ddt_acquisto.totale_acconto,
    case
        when ddt_acquisto.fatturato = 1 then 1
        else 0
    end as fatturato
from ddt_acquisto
         join fornitore on
        ddt_acquisto.id_fornitore = fornitore.id
         left join stato_ddt on
        ddt_acquisto.id_stato = stato_ddt.id
union all
select
    concat('fa_',fattura_acquisto.id) as id,
    'Fattura acquisto' as tipo_documento,
    fattura_acquisto.id as id_documento,
    fattura_acquisto.progressivo as num_documento,
    fattura_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    fattura_acquisto.id_stato,
    stato_fattura.codice as stato,
    fattura_acquisto.totale_imponibile,
    fattura_acquisto.totale_iva,
    fattura_acquisto.totale,
    fattura_acquisto.totale_acconto,
    0 as fatturato
from fattura_acquisto
         join fornitore on
        fattura_acquisto.id_fornitore = fornitore.id
         left join stato_fattura on
        fattura_acquisto.id_stato = stato_fattura.id
;