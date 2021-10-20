ALTER TABLE contafood.ordine_fornitore ADD COLUMN email_inviata varchar(10) default 'N' AFTER note;
ALTER TABLE contafood.ordine_fornitore ADD COLUMN data_invio_email timestamp null default null AFTER email_inviata;

update contafood.ordine_fornitore set email_inviata = 'N';