create or replace view contafood.`v_documento_acquisto_ingrediente` as
select
    uuid() as id,
    'DDT acquisto' as tipo_documento,
    ddt_acquisto.id as id_documento,
    ddt_acquisto.numero as num_documento,
    ddt_acquisto.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    ddt_acquisto_ingrediente.lotto as lotto_ingrediente
from ddt_acquisto
         join ddt_acquisto_ingrediente on
        ddt_acquisto.id = ddt_acquisto_ingrediente.id_ddt_acquisto
         join fornitore on
        ddt_acquisto.id_fornitore = fornitore.id
union all
select
    uuid() as id,
    'Note reso fornitore' as tipo_documento,
    nota_reso.id as id_documento,
    concat(nota_reso.progressivo,'/',nota_reso.anno) as num_documento,
    nota_reso.data as data_documento,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as ragione_sociale_fornitore,
    nota_reso_riga.lotto as lotto_ingrediente
from nota_reso
         join nota_reso_riga on
        nota_reso.id = nota_reso_riga.id_nota_reso
         join fornitore on
        nota_reso.id_fornitore = fornitore.id
;

create or replace view contafood.`v_documento_vendita_ingrediente` as
select
    uuid() as id,
    'DDT' as tipo_documento,
    ddt.id as id_documento,
    concat(ddt.progressivo,'/',ddt.anno_contabile) as num_documento,
    ddt.data as data_documento,
    produzione_ingrediente.lotto as lotto_ingrediente
from ddt
         join ddt_articolo on
        ddt_articolo.id_ddt = ddt.id
         join produzione_confezione on
        produzione_confezione.id_articolo = ddt_articolo.id_articolo
         join produzione on
        produzione.id = produzione_confezione.id_produzione
         join produzione_ingrediente on
        produzione_ingrediente.id_produzione = produzione.id
union all
select
    uuid() as id,
    'Fattura accompagnatoria' as tipo_documento,
    fattura_accom.id as id_documento,
    concat(fattura_accom.progressivo,'/',fattura_accom.anno) as num_documento,
    fattura_accom.data as data_documento,
    produzione_ingrediente.lotto as lotto_ingrediente
from fattura_accom
         join fattura_accom_articolo on
        fattura_accom_articolo.id_fattura_accom = fattura_accom.id
         join produzione_confezione on
        produzione_confezione.id_articolo = fattura_accom_articolo.id_articolo
         join produzione on
        produzione.id = produzione_confezione.id_produzione
         join produzione_ingrediente on
        produzione_ingrediente.id_produzione = produzione.id
union all
select
    uuid() as id,
    'Nota accredito' as tipo_documento,
    nota_accredito.id as id_documento,
    concat(nota_accredito.progressivo,'/',nota_accredito.anno) as num_documento,
    nota_accredito.data as data_documento,
    produzione_ingrediente.lotto as lotto_ingrediente
from nota_accredito
         join nota_accredito_riga on
        nota_accredito_riga.id_nota_accredito = nota_accredito.id
         join produzione_confezione on
        produzione_confezione.id_articolo = nota_accredito_riga.id_articolo
         join produzione on
        produzione.id = produzione_confezione.id_produzione
         join produzione_ingrediente on
        produzione_ingrediente.id_produzione = produzione.id
;

create or replace view contafood.`v_ordine_cliente_articolo_da_evadere` as
select
    uuid() as id,
    ordine_cliente.id as id_ordine_cliente,
    concat('Ordine ',ordine_cliente.progressivo,' del ',DATE_FORMAT(ordine_cliente.`data`, '%d/%m/%Y'),' - ',articolo.codice,' ',articolo.descrizione,' nr. ',ordine_cliente_articolo.num_da_evadere) as descrizione,
    ordine_cliente.progressivo,
    ordine_cliente.anno_contabile,
    ordine_cliente.`data`,
    ordine_cliente_articolo.id_articolo,
    articolo.codice as codice_articolo,
    articolo.descrizione as descrizione_articolo,
    ordine_cliente_articolo.num_ordinati,
    ordine_cliente_articolo.num_da_evadere,
    ordine_cliente.id_cliente
from ordine_cliente
         join ordine_cliente_articolo on
        ordine_cliente_articolo.id_ordine_cliente = ordine_cliente.id
         join articolo on
        articolo.id = ordine_cliente_articolo.id_articolo
where
        ordine_cliente.id_stato_ordine <> 2 and
        ordine_cliente_articolo.num_da_evadere > 0
;

create table contafood.listino_prezzo_20230105 as
select * from contafood.listino_prezzo;

update contafood.listino_prezzo
set prezzo = round(prezzo, 2);

ALTER TABLE contafood.listino_prezzo MODIFY COLUMN prezzo decimal(10,2) NULL;

ALTER TABLE contafood.articolo MODIFY COLUMN prezzo_acquisto decimal(10,2) NULL;
ALTER TABLE contafood.articolo MODIFY COLUMN prezzo_listino_base decimal(10,2) NULL;

ALTER TABLE contafood.ddt MODIFY COLUMN totale_acconto decimal(10,2) NULL;
ALTER TABLE contafood.ddt MODIFY COLUMN totale decimal(10,2) NULL;
ALTER TABLE contafood.ddt MODIFY COLUMN totale_costo decimal(10,2) NULL;
ALTER TABLE contafood.ddt MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.ddt MODIFY COLUMN totale_imponibile decimal(10,2) NULL;

ALTER TABLE contafood.fattura MODIFY COLUMN totale_imponibile decimal(10,2) NULL;
ALTER TABLE contafood.fattura MODIFY COLUMN totale_acconto decimal(10,2) NULL;
ALTER TABLE contafood.fattura MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.fattura MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.confezione MODIFY COLUMN prezzo decimal(10,2) NULL;

ALTER TABLE contafood.ddt_acquisto MODIFY COLUMN totale_imponibile decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.ddt_acquisto_articolo MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto_articolo MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto_articolo MODIFY COLUMN imponibile decimal(10,2) NULL;

ALTER TABLE contafood.ddt_acquisto_ingrediente MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto_ingrediente MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.ddt_acquisto_ingrediente MODIFY COLUMN imponibile decimal(10,2) NULL;

ALTER TABLE contafood.ddt_articolo MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.ddt_articolo MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.ddt_articolo MODIFY COLUMN imponibile decimal(10,2) NULL;
ALTER TABLE contafood.ddt_articolo MODIFY COLUMN costo decimal(10,2) NULL;
ALTER TABLE contafood.ddt_articolo MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.fattura_accom MODIFY COLUMN totale_acconto decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN imponibile decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN costo decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom_articolo MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.fattura_accom_totale MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.fattura_accom_totale MODIFY COLUMN totale_imponibile decimal(10,2) NULL;

ALTER TABLE contafood.ingrediente MODIFY COLUMN prezzo decimal(10,2) NULL;

ALTER TABLE contafood.nota_accredito MODIFY COLUMN totale decimal(10,2) NULL;
ALTER TABLE contafood.nota_accredito MODIFY COLUMN totale_acconto decimal(10,2) NULL;

ALTER TABLE contafood.nota_accredito_riga MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.nota_accredito_riga MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.nota_accredito_riga MODIFY COLUMN imponibile decimal(10,2) NULL;
ALTER TABLE contafood.nota_accredito_riga MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.nota_accredito_totale MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.nota_accredito_totale MODIFY COLUMN totale_imponibile decimal(10,2) NULL;

ALTER TABLE contafood.nota_reso MODIFY COLUMN totale decimal(10,2) NULL;
ALTER TABLE contafood.nota_reso MODIFY COLUMN totale_acconto decimal(10,2) NULL;

ALTER TABLE contafood.nota_reso_riga MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.nota_reso_riga MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.nota_reso_riga MODIFY COLUMN imponibile decimal(10,2) NULL;
ALTER TABLE contafood.nota_reso_riga MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.nota_reso_totale MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.nota_reso_totale MODIFY COLUMN totale_imponibile decimal(10,2) NULL;

ALTER TABLE contafood.pagamento MODIFY COLUMN importo decimal(10,2) NULL;

ALTER TABLE contafood.ricevuta_privato MODIFY COLUMN totale_imponibile decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato MODIFY COLUMN totale_costo decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato MODIFY COLUMN totale_acconto decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.ricevuta_privato_articolo MODIFY COLUMN prezzo decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato_articolo MODIFY COLUMN sconto decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato_articolo MODIFY COLUMN imponibile decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato_articolo MODIFY COLUMN costo decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato_articolo MODIFY COLUMN totale decimal(10,2) NULL;

ALTER TABLE contafood.ricevuta_privato_totale MODIFY COLUMN totale_iva decimal(10,2) NULL;
ALTER TABLE contafood.ricevuta_privato_totale MODIFY COLUMN totale_imponibile decimal(10,2) NULL;

ALTER TABLE contafood.sconto MODIFY COLUMN valore decimal(10,2) DEFAULT 0.000 NOT NULL;
