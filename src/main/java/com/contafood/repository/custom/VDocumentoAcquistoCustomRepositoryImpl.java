package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VDocumentoAcquisto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VDocumentoAcquistoCustomRepositoryImpl implements VDocumentoAcquistoCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VDocumentoAcquisto> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato) {
        StringBuilder sb = createQueryAsString("*", fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        if(draw != null && draw != -1){
            int limit = length != null ? length : 20;
            int offset = start != null ? start : 0;
            String order = " ORDER BY data_documento DESC, ragione_sociale_fornitore ASC, tipo_documento ASC, num_documento DESC";
            if(!sortOrders.isEmpty()){
                int i = 0;
                order = " ORDER BY ";
                for(SortOrder sortOrder : sortOrders){
                    order += sortOrder.getColumnName() + " " + sortOrder.getDirection();
                    if(i < (sortOrders.size() - 1)){
                        order += ",";
                    }
                    i++;
                }
            }
            sb.append(order).append(" LIMIT ").append(limit).append(" OFFSET ").append(offset);
        } else {
            sb.append(" ORDER BY data_documento DESC, ragione_sociale_fornitore ASC, tipo_documento ASC, num_documento DESC");
        }

        Query query = createQuery(sb.toString(), fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        List<Object[]> queryResults = query.getResultList();
        List<VDocumentoAcquisto> result = new ArrayList<>();
        if(queryResults != null && !queryResults.isEmpty()){
            for(Object[] queryResult : queryResults){
                VDocumentoAcquisto vDocumentoAcquisto = new VDocumentoAcquisto();
                vDocumentoAcquisto.setId(((String)queryResult[0]));
                if(queryResult[1] != null){
                    vDocumentoAcquisto.setTipoDocumento((String)queryResult[1]);
                }
                if(queryResult[2] != null){
                    vDocumentoAcquisto.setIdDocumento(((Integer)queryResult[2]).longValue());
                }
                if(queryResult[3] != null){
                    vDocumentoAcquisto.setNumDocumento((String)queryResult[3]);
                }
                if(queryResult[4] != null){
                    vDocumentoAcquisto.setDataDocumento((Date)queryResult[4]);
                }
                if(queryResult[5] != null){
                    vDocumentoAcquisto.setIdFornitore(((Integer)queryResult[5]).longValue());
                }
                if(queryResult[6] != null){
                    vDocumentoAcquisto.setRagioneSocialeFornitore((String)queryResult[6]);
                }
                if(queryResult[7] != null){
                    vDocumentoAcquisto.setPartitaIvaFornitore((String)queryResult[7]);
                }
                if(queryResult[8] != null){
                    vDocumentoAcquisto.setIdStato(((Integer)queryResult[8]).longValue());
                }
                if(queryResult[9] != null){
                    vDocumentoAcquisto.setStato((String)queryResult[9]);
                }
                if(queryResult[10] != null){
                    vDocumentoAcquisto.setTotaleImponibile((BigDecimal)queryResult[10]);
                }
                if(queryResult[11] != null){
                    vDocumentoAcquisto.setTotaleIva((BigDecimal) queryResult[11]);
                }
                if(queryResult[12] != null){
                    vDocumentoAcquisto.setTotale((BigDecimal)queryResult[12]);
                }
                if(queryResult[13] != null){
                    vDocumentoAcquisto.setTotaleAcconto((BigDecimal)queryResult[13]);
                }
                if(queryResult[14] != null){
                    BigDecimal fatturatoResult = (BigDecimal)queryResult[14];
                    vDocumentoAcquisto.setFatturato(fatturatoResult.compareTo(BigDecimal.ONE)==0 ? Boolean.TRUE : Boolean.FALSE);
                }
                result.add(vDocumentoAcquisto);
            }
        }
        return result;
    }

    @Override
    public Integer countByFilters(String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato) {
        Integer count = 0;

        StringBuilder sb = createQueryAsString("count(1)", fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        Query query = createQuery(sb.toString(), fornitore, numDocumento, tipoDocumento, dataDa, dataA, idFornitore, fatturato);

        Object result = query.getSingleResult();
        if(result != null){
            count = ((BigInteger)result).intValue();
        }
        return count;
    }

    private StringBuilder createQueryAsString(String select, String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(select);
        sb.append(" FROM contafood.v_documento_acquisto WHERE 1=1 ");

        if(fornitore != null) {
            sb.append(" AND lower(ragione_sociale_fornitore) LIKE concat('%',:fornitore,'%') ");
        }
        if(numDocumento != null) {
            sb.append(" AND lower(num_documento) LIKE concat('%',:numDocumento,'%') ");
        }
        if(tipoDocumento != null) {
            sb.append(" AND tipo_documento = :tipoDocumento ");
        }
        if(dataDa != null) {
            sb.append(" AND data_documento >= :dataDa ");
        }
        if(dataA != null) {
            sb.append(" AND data_documento <= :dataA ");
        }
        if(idFornitore != null){
            sb.append(" AND id_fornitore = :idFornitore ");
        }
        if(fatturato != null){
            sb.append(" AND fatturato = :fatturato ");
        }
        return sb;
    }

    private Query createQuery(String queryAsString, String fornitore, String numDocumento, String tipoDocumento, Date dataDa, Date dataA, Long idFornitore, Boolean fatturato) {
        Query query = entityManager.createNativeQuery(queryAsString);

        if(fornitore != null) {
            query.setParameter("fornitore", fornitore);
        }
        if(numDocumento != null) {
            query.setParameter("numDocumento", numDocumento);
        }
        if(tipoDocumento != null) {
            query.setParameter("tipoDocumento", tipoDocumento);
        }
        if(dataDa != null){
            query.setParameter("dataDa", dataDa);
        }
        if(dataA != null){
            query.setParameter("dataA", dataA);
        }
        if(idFornitore != null){
            query.setParameter("idFornitore", idFornitore);
        }
        if(fatturato != null){
            query.setParameter("fatturato", fatturato == Boolean.TRUE ? 1 : 0);
        }
        return query;
    }
}
