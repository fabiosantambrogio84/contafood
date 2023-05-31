INSERT INTO contafood.tipo_fattura (id, codice, descrizione, ordine, data_inserimento, data_aggiornamento)
VALUES(2, 'ACCOMPAGNATORIA_ACQUISTO', 'Accompagnatoria acquisto', 3, '2020-05-25 10:11:25', '0000-00-00 00:00:00');

CREATE TABLE `fattura_accom_acquisto` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `numero` varchar(255) DEFAULT NULL,
    `anno` int DEFAULT NULL,
    `data` date DEFAULT NULL,
    `id_tipo` int unsigned DEFAULT NULL,
    `id_fornitore` int unsigned DEFAULT NULL,
    `id_stato` int unsigned DEFAULT NULL,
    `id_causale` int unsigned DEFAULT NULL,
    `spedito_ade` bit(1) DEFAULT b'0',
    `numero_colli` int DEFAULT NULL,
    `tipo_trasporto` varchar(100) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT NULL,
    `data_trasporto` date DEFAULT NULL,
    `ora_trasporto` time DEFAULT NULL,
    `trasportatore` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT NULL,
    `totale_imponibile` decimal(10,2) DEFAULT NULL,
    `totale_iva` decimal(10,2) DEFAULT NULL,
    `totale_acconto` decimal(10,2) DEFAULT NULL,
    `totale` decimal(10,2) DEFAULT NULL,
    `totale_quantita` decimal(10,3) DEFAULT NULL,
    `note` text CHARACTER SET latin1 COLLATE latin1_general_cs,
    `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_aggiornamento` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_fattura_accom_acq_tipo` (`id_tipo`),
    KEY `fk_fattura_accom_acq_fornitore` (`id_fornitore`),
    KEY `fk_fattura_accom_acq_stato` (`id_stato`),
    CONSTRAINT `fk_fattura_accom_acq_fornitore` FOREIGN KEY (`id_fornitore`) REFERENCES `fornitore` (`id`),
    CONSTRAINT `fk_fattura_accom_acq_stato` FOREIGN KEY (`id_stato`) REFERENCES `stato_fattura` (`id`),
    CONSTRAINT `fk_fattura_accom_acq_tipo` FOREIGN KEY (`id_tipo`) REFERENCES `tipo_fattura` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `fattura_accom_acquisto_articolo` (
    `id_fattura_accom_acquisto` int unsigned NOT NULL DEFAULT '0',
    `id_articolo` int unsigned NOT NULL DEFAULT '0',
    `uuid` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL DEFAULT '',
    `lotto` varchar(100) CHARACTER SET latin1 COLLATE latin1_general_cs DEFAULT NULL,
    `scadenza` date DEFAULT NULL,
    `quantita` decimal(10,3) DEFAULT NULL,
    `numero_pezzi` int DEFAULT NULL,
    `numero_pezzi_da_evadere` int DEFAULT NULL,
    `prezzo` decimal(10,2) DEFAULT NULL,
    `sconto` decimal(10,2) DEFAULT NULL,
    `imponibile` decimal(10,2) DEFAULT NULL,
    `costo` decimal(10,2) DEFAULT NULL,
    `totale` decimal(10,2) DEFAULT NULL,
    `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_aggiornamento` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id_fattura_accom_acquisto`,`id_articolo`,`uuid`),
    KEY `fk_fattura_accom_acq_articolo_art` (`id_articolo`),
    CONSTRAINT `fk_fattura_accom_acq_articolo_art` FOREIGN KEY (`id_articolo`) REFERENCES `articolo` (`id`),
    CONSTRAINT `fk_fattura_accom_acq_articolo_fatt` FOREIGN KEY (`id_fattura_accom_acquisto`) REFERENCES `fattura_accom_acquisto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `fattura_accom_acquisto_totale` (
    `id_fattura_accom_acquisto` int unsigned NOT NULL DEFAULT '0',
    `id_aliquota_iva` int unsigned NOT NULL DEFAULT '0',
    `uuid` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL DEFAULT '',
    `totale_iva` decimal(10,2) DEFAULT NULL,
    `totale_imponibile` decimal(10,2) DEFAULT NULL,
    `data_inserimento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_aggiornamento` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id_fattura_accom_acquisto`,`id_aliquota_iva`,`uuid`),
    KEY `fk_fattura_accom_acq_totale_iva` (`id_aliquota_iva`),
    CONSTRAINT `fk_fattura_accom_acq_totale_fatt` FOREIGN KEY (`id_fattura_accom_acquisto`) REFERENCES `fattura_accom_acquisto` (`id`),
    CONSTRAINT `fk_fattura_accom_acq_totale_iva` FOREIGN KEY (`id_aliquota_iva`) REFERENCES `aliquota_iva` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

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
    fattura_acquisto.numero as num_documento,
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
union all
select
    concat('faa_',fattura_accom_acquisto.id) as id,
    'Fattura accompagnatoria acquisto' as tipo_documento,
    fattura_accom_acquisto.id as id_documento,
    fattura_accom_acquisto.numero as num_documento,
    fattura_accom_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    fornitore.partita_iva as partita_iva_fornitore,
    fattura_accom_acquisto.id_stato,
    stato_fattura.codice as stato,
    fattura_accom_acquisto.totale_imponibile,
    fattura_accom_acquisto.totale_iva,
    fattura_accom_acquisto.totale,
    fattura_accom_acquisto.totale_acconto,
    1 as fatturato
from fattura_accom_acquisto
         join fornitore on
        fattura_accom_acquisto.id_fornitore = fornitore.id
         left join stato_fattura on
        fattura_accom_acquisto.id_stato = stato_fattura.id
;

alter table pagamento add column `id_fattura_accom_acquisto` int unsigned DEFAULT NULL after id_fattura_acquisto;


create or replace view `v_pagamento` as
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_ddt` as `id_resource`,
    `cliente`.`id` as `id_cliente`,
    (case
         when (`cliente`.`ditta_individuale` = 1) then concat(`cliente`.`nome`, ' ', `cliente`.`cognome`)
         else `cliente`.`ragione_sociale`
        end) as `cliente`,
    null as `id_fornitore`,
    null as `fornitore`
from
    (((`pagamento`
        join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `ddt` on
            ((`pagamento`.`id_ddt` = `ddt`.`id`)))
        join `cliente` on
        ((`ddt`.`id_cliente` = `cliente`.`id`)))
where
    (`pagamento`.`id_ddt` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_fattura` as `id_resource`,
    `cliente`.`id` as `id_cliente`,
    (case
         when (`cliente`.`ditta_individuale` = 1) then concat(`cliente`.`nome`, ' ', `cliente`.`cognome`)
         else `cliente`.`ragione_sociale`
        end) as `cliente`,
    null as `id_fornitore`,
    null as `fornitore`
from
    (((`pagamento`
        join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `fattura` on
            ((`pagamento`.`id_fattura` = `fattura`.`id`)))
        join `cliente` on
        ((`fattura`.`id_cliente` = `cliente`.`id`)))
where
    (`pagamento`.`id_fattura` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_fattura_accom` as `id_resource`,
    `cliente`.`id` as `id_cliente`,
    (case
         when (`cliente`.`ditta_individuale` = 1) then concat(`cliente`.`nome`, ' ', `cliente`.`cognome`)
         else `cliente`.`ragione_sociale`
        end) as `cliente`,
    null as `id_fornitore`,
    null as `fornitore`
from
    (((`pagamento`
        join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `fattura_accom` on
            ((`pagamento`.`id_fattura_accom` = `fattura_accom`.`id`)))
        join `cliente` on
        ((`fattura_accom`.`id_cliente` = `cliente`.`id`)))
where
    (`pagamento`.`id_fattura_accom` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_nota_accredito` as `id_resource`,
    `cliente`.`id` as `id_cliente`,
    (case
         when (`cliente`.`ditta_individuale` = 1) then concat(`cliente`.`nome`, ' ', `cliente`.`cognome`)
         else `cliente`.`ragione_sociale`
        end) as `cliente`,
    null as `id_fornitore`,
    null as `fornitore`
from
    (((`pagamento`
        left join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `nota_accredito` on
            ((`pagamento`.`id_nota_accredito` = `nota_accredito`.`id`)))
        join `cliente` on
        ((`nota_accredito`.`id_cliente` = `cliente`.`id`)))
where
    (`pagamento`.`id_nota_accredito` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_ricevuta_privato` as `id_resource`,
    `cliente`.`id` as `id_cliente`,
    concat(`cliente`.`nome`, ' ', `cliente`.`cognome`) as `cliente`,
    null as `id_fornitore`,
    null as `fornitore`
from
    (((`pagamento`
        join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `ricevuta_privato` on
            ((`pagamento`.`id_ricevuta_privato` = `ricevuta_privato`.`id`)))
        join `cliente` on
        ((`ricevuta_privato`.`id_cliente` = `cliente`.`id`)))
where
    (`pagamento`.`id_ricevuta_privato` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_nota_reso` as `id_resource`,
    null as `id_cliente`,
    null as `cliente`,
    `fornitore`.`id` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    (((`pagamento`
        left join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `nota_reso` on
            ((`pagamento`.`id_nota_reso` = `nota_reso`.`id`)))
        join `fornitore` on
        ((`nota_reso`.`id_fornitore` = `fornitore`.`id`)))
where
    (`pagamento`.`id_nota_reso` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_ddt_acquisto` as `id_resource`,
    null as `id_cliente`,
    null as `cliente`,
    `fornitore`.`id` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    (((`pagamento`
        left join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `ddt_acquisto` on
            ((`pagamento`.`id_ddt_acquisto` = `ddt_acquisto`.`id`)))
        join `fornitore` on
        ((`ddt_acquisto`.`id_fornitore` = `fornitore`.`id`)))
where
    (`pagamento`.`id_ddt_acquisto` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_fattura_acquisto` as `id_resource`,
    null as `id_cliente`,
    null as `cliente`,
    `fornitore`.`id` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    (((`pagamento`
        left join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `fattura_acquisto` on
            ((`pagamento`.`id_fattura_acquisto` = `fattura_acquisto`.`id`)))
        join `fornitore` on
        ((`fattura_acquisto`.`id_fornitore` = `fornitore`.`id`)))
where
    (`pagamento`.`id_fattura_acquisto` is not null)
union all
select
    `pagamento`.`id` as `id`,
    `pagamento`.`data` as `data`,
    `pagamento`.`tipologia` as `tipologia`,
    `pagamento`.`id_tipo_pagamento` as `id_tipo_pagamento`,
    `tipo_pagamento`.`descrizione` as `tipo_pagamento`,
    `pagamento`.`descrizione` as `descrizione`,
    `pagamento`.`note` as `note`,
    `pagamento`.`importo` as `importo`,
    `pagamento`.`id_fattura_accom_acquisto` as `id_resource`,
    null as `id_cliente`,
    null as `cliente`,
    `fornitore`.`id` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    (((`pagamento`
        left join `tipo_pagamento` on
            ((`pagamento`.`id_tipo_pagamento` = `tipo_pagamento`.`id`)))
        join `fattura_accom_acquisto` on
            ((`pagamento`.`id_fattura_accom_acquisto` = `fattura_accom_acquisto`.`id`)))
        join `fornitore` on
        ((`fattura_accom_acquisto`.`id_fornitore` = `fornitore`.`id`)))
where
    (`pagamento`.`id_fattura_accom_acquisto` is not null)
;