
create or replace view contafood.v_giacenza_ingrediente as
select
	id_ingrediente,
	concat(ingrediente.codice,' ',coalesce(ingrediente.descrizione,'')) ingrediente,
	sum(quantita) quantita_tot,
	GROUP_CONCAT(giacenza_ingrediente.id) id_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.lotto) lotto_giacenze,
	GROUP_CONCAT(giacenza_ingrediente.scadenza) scadenza_giacenze,
	ingrediente.attivo,
	unita_misura.etichetta udm,
	ingrediente.id_fornitore,
	fornitore.ragione_sociale fornitore,
	ingrediente.codice codice_ingrediente,
	ingrediente.descrizione descrizione_ingrediente
from
	contafood.giacenza_ingrediente
join contafood.ingrediente on
	giacenza_ingrediente.id_ingrediente = ingrediente.id
left join contafood.unita_misura on
    ingrediente.id_unita_misura = unita_misura.id
left join contafood.fornitore on
	ingrediente.id_fornitore = fornitore.id
group by
	id_ingrediente
;