create or replace view `v_produzione_ingrediente` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    `produzione`.`tipologia` as `tipologia`,
    `produzione`.`lotto` as `lotto_produzione`,
    `produzione`.`scadenza` as `scadenza`,
    produzione_ingrediente.lotto as lotto_ingrediente,
    `ingrediente`.id as id_ingrediente,
    `ingrediente`.`codice` as `codice_ingrediente`,
    `ingrediente`.`descrizione` as `descrizione_ingrediente`,
    `produzione`.`quantita_totale` as `quantita`,
    concat(`ricetta`.`codice`, ' ', `ricetta`.`nome`) as `ricetta`
from produzione
         join produzione_ingrediente on
        produzione.id = produzione_ingrediente.id_produzione
         join ingrediente on
        produzione_ingrediente.id_ingrediente = ingrediente.id
         join ricetta on
        produzione.id_ricetta = ricetta.id
;

create or replace view `v_produzione_confezione` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    `produzione`.`tipologia` as `tipologia`,
    `produzione_confezione`.`id_confezione` as `id_confezione`,
    produzione_confezione.num_confezioni,
    `produzione_confezione`.`num_confezioni_prodotte` as `num_confezioni_prodotte`,
    produzione_confezione.lotto as lotto_confezione,
    confezione.tipo as tipo_confezione,
    `produzione`.`lotto` as `lotto_produzione`,
    `produzione`.`scadenza` as `scadenza`,
    `produzione_confezione`.`id_articolo` as `id_articolo`,
    `articolo`.`codice` as `codice_articolo`,
    `articolo`.`descrizione` as `descrizione_articolo`,
    `produzione_confezione`.`id_ingrediente` as `id_ingrediente`,
    `ingrediente`.`codice` as `codice_ingrediente`,
    `ingrediente`.`descrizione` as `descrizione_ingrediente`,
    `produzione`.`quantita_totale` as `quantita`,
    concat(`ricetta`.`codice`, ' ', `ricetta`.`nome`) as `ricetta`
from produzione
join produzione_confezione on
    produzione.id = produzione_confezione.id_produzione
join confezione on
    produzione_confezione.id_confezione = confezione.id
join ricetta on
    produzione.id_ricetta = ricetta.id
left join articolo on
    produzione_confezione.id_articolo = articolo.id
left join ingrediente on
    produzione_confezione.id_ingrediente = ingrediente.id
;

create or replace view `v_documento_acquisto_ingrediente` as
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
    nota_reso.progressivo as num_documento,
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