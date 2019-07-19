--
-- "docker run --name mysql01 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.6.44 "
--
-- mysql -u root -p
-- -> chiede password. Inserire root

-- mysql> create user 'contafood'@'%' identified by 'contafood';
-- mysql> grant all on contafood.* to 'contafood'@'%';

CREATE DATABASE  IF NOT EXISTS `contafood`;
USE `contafood`;

DROP TABLE IF EXISTS `fornitore`;

CREATE TABLE `fornitore` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	ragione_sociale varchar(100),
	ragione_sociale_2 varchar(100),
	fl_ditta_individuale bit(1) NOT NULL DEFAULT b'0',
	nome varchar(100),
	cognome varchar(100),
	indirizzo varchar(100),
	citta varchar(100),
	provincia varchar(100),
	cap varchar(50),
	nazione varchar(100),
	partita_iva varchar(100),
	codice_fiscale varchar(100),
	telefono varchar(100),
	telefono_2 varchar(100),
	telefono_3 varchar(100),
	email varchar(100),
	email_pec varchar(100),
	codice_univoco_sdi varchar(100),
	iban varchar(100),
	pagamento varchar(100),
	note text,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `categoria_ricetta`;

CREATE TABLE `categoria_ricetta` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	nome varchar(100),
	ordine int(11),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ingrediente`;

CREATE TABLE `ingrediente` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	descrizione text,
	prezzo decimal(10,3),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `ricetta`;

CREATE TABLE `ricetta` (
	id int(10) unsigned NOT NULL AUTO_INCREMENT,
	codice varchar(100),
	nome varchar(100),
	categoria_id int(10),
	tempo_preparazione varchar(100),
	numero_porzioni numeric,
	costo_ingredienti decimal(10,3),
	costo_preparazione decimal(10,3),
	costo_totale decimal(10,3),
	preparazione text,
	allergeni text,
	valori_nutrizionali text,
	note text,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ricetta_ingrediente`;

CREATE TABLE `ricetta_ingrediente` (
	id_ricetta int(10) unsigned NOT NULL,
	id_ingrediente int(10) unsigned NOT NULL,
	quantita decimal(10,3),
	PRIMARY KEY (`id_ricetta`, id_ingrediente)
) ENGINE=InnoDB;

/*

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class BookPublisher implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn
    private Publisher publisher;

    private Date publishedDate;

    public BookPublisher(Publisher publisher, Date publishedDate) {
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookPublisher)) return false;
        BookPublisher that = (BookPublisher) o;
        return Objects.equals(book.getName(), that.book.getName()) &&
                Objects.equals(publisher.getName(), that.publisher.getName()) &&
                Objects.equals(publishedDate, that.publishedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book.getName(), publisher.getName(), publishedDate);
    }
}

 */
