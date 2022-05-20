
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
         join tipo_pagamento on
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
         join tipo_pagamento on
        pagamento.id_tipo_pagamento = tipo_pagamento.id
         join nota_reso on
        pagamento.id_nota_reso = nota_reso.id
         join fornitore on
        nota_reso.id_fornitore = fornitore.id
where
    pagamento.id_nota_reso is not null
;
