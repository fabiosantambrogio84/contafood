alter table contafood.ricevuta_privato_articolo add column prezzo_iva decimal(10,2) after prezzo;
alter table contafood.ricevuta_privato add column totale_pezzi int after totale_quantita;

UPDATE contafood.ricevuta_privato_articolo
JOIN (
    select
        ricevuta_privato_articolo.id_ricevuta_privato,
        ricevuta_privato_articolo.id_articolo,
        ricevuta_privato_articolo.uuid,
        ricevuta_privato_articolo.prezzo,
        aliquota_iva.valore,
        round((ricevuta_privato_articolo.prezzo + (ricevuta_privato_articolo.prezzo*(aliquota_iva.valore/100))),2) as prezzo_iva
    from contafood.ricevuta_privato_articolo
    join contafood.articolo on
        ricevuta_privato_articolo.id_articolo = articolo.id
    join contafood.aliquota_iva on
        articolo.id_aliquota_iva = aliquota_iva.id
) t ON
    ricevuta_privato_articolo.id_ricevuta_privato = t.id_ricevuta_privato and
    ricevuta_privato_articolo.id_articolo = t.id_articolo and
    ricevuta_privato_articolo.uuid = t.uuid
SET ricevuta_privato_articolo.prezzo_iva = t.prezzo_iva;

UPDATE contafood.ricevuta_privato
JOIN (
    select
        ricevuta_privato_articolo.id_ricevuta_privato,
        sum(ricevuta_privato_articolo.numero_pezzi) as totale_pezzi
    from contafood.ricevuta_privato
    join contafood.ricevuta_privato_articolo on
        ricevuta_privato_articolo.id_ricevuta_privato = ricevuta_privato.id
    group by
        ricevuta_privato_articolo.id_ricevuta_privato
) t ON
    ricevuta_privato.id = t.id_ricevuta_privato
SET ricevuta_privato.totale_pezzi = t.totale_pezzi;


alter table contafood.ordine_cliente_articolo add column id_ddts text after id_ordine_fornitore;