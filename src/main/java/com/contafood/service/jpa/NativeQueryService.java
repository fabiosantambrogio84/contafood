package com.contafood.service.jpa;

import com.contafood.model.OrdineClienteAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class NativeQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NativeQueryService.class);

    private static final String ORDINI_CLIENTI_AGGREGATE_QUERY = "select \n" +
            "ordine_cliente_articolo.id_articolo,\n" +
            "concat(articolo.codice,' ',articolo.descrizione) as articolo,\n" +
            "articolo.prezzo_listino_base,\n" +
            "sum(ordine_cliente_articolo.num_ordinati) as num_ordinati,\n" +
            "sum(ordine_cliente_articolo.num_da_evadere) as num_da_evadere,\n" +
            "group_concat(ordine_cliente_articolo.id_ordine_cliente separator ',') as ordini_clienti,\n" +
            "group_concat(concat(ordine_cliente.progressivo,'/', ordine_cliente.anno_contabile) separator ',') as codici_ordini_clienti,\n" +
            "group_concat(concat(ordine_cliente.progressivo,'/', ordine_cliente.anno_contabile,': ',ordine_cliente.note) separator ',') as note,\n" +
            "group_concat(ordine_cliente_articolo.id_ddts separator ',') as id_ddts\n" +
            "from contafood.ordine_cliente_articolo\n" +
            "join contafood.ordine_cliente on ordine_cliente_articolo.id_ordine_cliente = ordine_cliente.id\n" +
            "join contafood.articolo on ordine_cliente_articolo.id_articolo = articolo.id\n" +
            "where ordine_cliente.id_cliente = ?1 \n" +
            "and ordine_cliente.id_punto_consegna = ?2 \n" +
            "and ordine_cliente.id_stato_ordine <> ?3 \n" +
            "and ordine_cliente.data_consegna <= ?4 \n" +
            "and ordine_cliente_articolo.num_da_evadere > 0 \n" +
            "group by ordine_cliente_articolo.id_articolo";

    private static final String ADE_NEXT_ID_EXPORT_QUERY = "SELECT nextval('seq_e_fatturazione') as SEQ from dual;";

    private static final String ADE_PROGRESSIVO_XML_FILE_QUERY = "SELECT nextval('seq_e_fatturazione_file') as SEQ from dual;";

    private static final String ADE_PROGRESSIVO_ZIP_FILE_QUERY = "SELECT nextval('seq_e_fatturazione_file_zip') as SEQ from dual;";

    @PersistenceContext
    private EntityManager entityManager;

    public List<OrdineClienteAggregate> getOrdiniClientiAggregate(Integer idCliente, Integer idPuntoConsegna, Date dataConsegna, Integer idStato){
        LOGGER.info("Performing native query for retrieving 'ordini clienti aggregate' with idCliente '{}', idPuntoConsegna '{}', dataConsegna <= '{}', idStatoNot '{}'", idCliente, idPuntoConsegna, dataConsegna, idStato);
        List<OrdineClienteAggregate> ordiniClientiAggregate = new ArrayList<>();

        try {
            Query query = entityManager.createNativeQuery(ORDINI_CLIENTI_AGGREGATE_QUERY);
            query.setParameter(1, idCliente);
            query.setParameter(2, idPuntoConsegna);
            query.setParameter(3, idStato);
            query.setParameter(4, dataConsegna);

            List<Object[]> resultList = query.getResultList();

            if(resultList != null && !resultList.isEmpty()){
                resultList.forEach(rl -> {
                    OrdineClienteAggregate ordineClienteAggregate = new OrdineClienteAggregate();
                    ordineClienteAggregate.setIdArticolo(((Integer)rl[0]).longValue());
                    ordineClienteAggregate.setArticolo((String)rl[1]);
                    ordineClienteAggregate.setPrezzoListinoBase((BigDecimal)rl[2]);
                    BigDecimal numeroPezziOrdinati = ((BigDecimal)rl[3]);
                    BigDecimal numeroPezziDaEvadere = ((BigDecimal)rl[4]);
                    BigDecimal numeroPezziEvasi = numeroPezziOrdinati.subtract(numeroPezziDaEvadere);
                    ordineClienteAggregate.setNumeroPezziOrdinati(numeroPezziOrdinati.intValue());
                    ordineClienteAggregate.setNumeroPezziDaEvadere(numeroPezziDaEvadere.intValue());
                    ordineClienteAggregate.setNumeroPezziEvasi(numeroPezziEvasi.intValue());
                    ordineClienteAggregate.setIdsOrdiniClienti((String)rl[5]);
                    ordineClienteAggregate.setCodiciOrdiniClienti((String)rl[6]);
                    ordineClienteAggregate.setNote((String)rl[7]);
                    ordineClienteAggregate.setIdsDdts((String)rl[8]);

                    ordiniClientiAggregate.add(ordineClienteAggregate);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        LOGGER.info("Retrieved {} 'ordini clienti aggregate'", ordiniClientiAggregate.size());
        return ordiniClientiAggregate;
    }

    public Integer getAdeNextId(String operation){
        LOGGER.info("Performing native query for retrieving next progressivo for operation '{}'...", operation);
        Query query;
        switch(operation){
            case "export":
                query = entityManager.createNativeQuery(ADE_NEXT_ID_EXPORT_QUERY);
                break;
            case "xml_file":
                query = entityManager.createNativeQuery(ADE_PROGRESSIVO_XML_FILE_QUERY);
                break;
            case "zip_file":
                query = entityManager.createNativeQuery(ADE_PROGRESSIVO_ZIP_FILE_QUERY);
                break;
            default:
                String errorMessage = "Operation '"+operation+"' not expected";
                throw new RuntimeException(errorMessage);
        }

        BigInteger result;
        try{
            result = (BigInteger)query.getSingleResult();
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        LOGGER.info("Result is: {}", result);
        return result.intValue();
    }

}
