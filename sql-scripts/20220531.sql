create or replace view `v_produzione` as
select
    uuid() as `id`,
    `produzione`.`id` as `id_produzione`,
    `produzione`.`codice` as `codice_produzione`,
    `produzione`.`data_produzione` as `data_produzione`,
    `produzione_confezione`.`id_confezione` as `id_confezione`,
    `produzione`.`lotto` as `lotto`,
    `produzione`.`scadenza` as `scadenza`,
    `produzione_confezione`.`id_articolo` as `id_articolo`,
    `articolo`.`codice` as `codice_articolo`,
    `articolo`.`descrizione` as `descrizione_articolo`,
    `produzione_confezione`.`num_confezioni_prodotte` as `num_confezioni_prodotte`,
    produzione.quantita_totale as quantita,
    concat(ricetta.codice, ' ', ricetta.nome) as ricetta
from
    `produzione_confezione`
join `produzione` on
    `produzione_confezione`.`id_produzione` = `produzione`.`id`
join ricetta on
    produzione.id_ricetta = ricetta.id
left join `articolo` on
    `produzione_confezione`.`id_articolo` = `articolo`.`id`;


create or replace view `v_giacenza_articolo` as
select
    `giacenza_articolo`.`id_articolo` as `id_articolo`,
    concat(`articolo`.`codice`, ' ', coalesce(`articolo`.`descrizione`, '')) as `articolo`,
    articolo.prezzo_acquisto,
    articolo.prezzo_listino_base,
    sum(`giacenza_articolo`.`quantita`) quantita_tot,
    null as quantita_kg,
    group_concat(`giacenza_articolo`.`id` separator ',') as `id_giacenze`,
    group_concat(`giacenza_articolo`.`lotto` separator ',') as `lotto_giacenze`,
    group_concat(`giacenza_articolo`.`scadenza` separator ',') as `scadenza_giacenze`,
    `articolo`.`attivo` as `attivo`,
    `articolo`.`id_fornitore` as `id_fornitore`,
    `fornitore`.`ragione_sociale` as `fornitore`
from
    `giacenza_articolo`
join `articolo` on
    `giacenza_articolo`.`id_articolo` = `articolo`.`id`
left join `fornitore` on
    `articolo`.`id_fornitore` = `fornitore`.`id`
group by
    `giacenza_articolo`.`id_articolo`;

alter table contafood.nota_reso_riga add column id_ingrediente int unsigned after id_articolo;
alter table contafood.nota_reso_riga add CONSTRAINT `fk_nota_reso_riga_ingr` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id`);

create or replace view contafood.v_pagamento as
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_ddt as id_resource,
    cliente.id as id_cliente,
    case
        when cliente.ditta_individuale = 1 then
            concat(cliente.nome, ' ', cliente.cognome)
        else
            cliente.ragione_sociale
        end as cliente,
    null as id_fornitore,
    null as fornitore
from contafood.pagamento
         join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join ddt on
        pagamento.id_ddt = ddt.id
         join cliente on
        ddt.id_cliente = cliente.id
where
    pagamento.id_ddt is not null
union
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_fattura as id_resource,
    cliente.id as id_cliente,
    case
        when cliente.ditta_individuale = 1 then
            concat(cliente.nome, ' ', cliente.cognome)
        else
            cliente.ragione_sociale
        end as cliente,
    null as id_fornitore,
    null as fornitore
from contafood.pagamento
         join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join fattura on
        pagamento.id_fattura = fattura.id
         join cliente on
        fattura.id_cliente = cliente.id
where
    pagamento.id_fattura is not null
union
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_fattura_accom as id_resource,
    cliente.id as id_cliente,
    case
        when cliente.ditta_individuale = 1 then
            concat(cliente.nome, ' ', cliente.cognome)
        else
            cliente.ragione_sociale
        end as cliente,
    null as id_fornitore,
    null as fornitore
from contafood.pagamento
         join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join fattura_accom on
        pagamento.id_fattura_accom = fattura_accom.id
         join cliente on
        fattura_accom.id_cliente = cliente.id
where
    pagamento.id_fattura_accom is not null
union
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_nota_accredito as id_resource,
    cliente.id as id_cliente,
    case
        when cliente.ditta_individuale = 1 then
            concat(cliente.nome, ' ', cliente.cognome)
        else
            cliente.ragione_sociale
        end as cliente,
    null as id_fornitore,
    null as fornitore
from contafood.pagamento
         left join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join nota_accredito on
        pagamento.id_nota_accredito = nota_accredito.id
         join cliente on
        nota_accredito.id_cliente = cliente.id
where
    pagamento.id_nota_accredito is not null
union
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_ricevuta_privato as id_resource,
    cliente.id as id_cliente,
    concat(cliente.nome, ' ', cliente.cognome) as cliente,
    null as id_fornitore,
    null as fornitore
from contafood.pagamento
         join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join ricevuta_privato on
        pagamento.id_ricevuta_privato = ricevuta_privato.id
         join cliente on
        ricevuta_privato.id_cliente = cliente.id
where
    pagamento.id_ricevuta_privato is not null
union
select
    pagamento.id,
    pagamento.data,
    pagamento.tipologia,
    pagamento.id_tipo_pagamento,
    tipo_pagamento.descrizione tipo_pagamento,
    pagamento.descrizione,
    pagamento.note,
    pagamento.importo,
    pagamento.id_nota_reso as id_resource,
    null as id_cliente,
    null as cliente,
    fornitore.id as id_fornitore,
    fornitore.ragione_sociale as fornitore
from contafood.pagamento
         left join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join nota_reso on
        pagamento.id_nota_reso = nota_reso.id
         join fornitore on
        nota_reso.id_fornitore = fornitore.id
where
    pagamento.id_nota_reso is not null
;