package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VProduzione;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VProduzioneCustomRepositoryImpl implements VProduzioneCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VProduzione> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, Integer codice, String ricetta, String barcodeEan13, String barcodeEan128) {
        StringBuilder sb = createQueryAsString("*", codice, ricetta, barcodeEan13, barcodeEan128);

        if(draw != null && draw != -1){
            int limit = length != null ? length : 20;
            int offset = start != null ? start : 0;
            String order = " ORDER BY data_produzione DESC, codice_produzione DESC";
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
            sb.append(" ORDER BY data_produzione DESC, codice_produzione DESC");
        }

        Query query = createQuery(sb.toString(), codice, ricetta, barcodeEan13, barcodeEan128);

        List<Object[]> queryResults = query.getResultList();
        List<VProduzione> result = new ArrayList<>();
        if(queryResults != null && !queryResults.isEmpty()){
            for(Object[] queryResult : queryResults){
                VProduzione vProduzione = new VProduzione();
                vProduzione.setId(((String)queryResult[0]));
                if(queryResult[1] != null){
                    vProduzione.setIdProduzione(((Integer)queryResult[1]).longValue());
                }
                if(queryResult[2] != null){
                    vProduzione.setCodiceProduzione((Integer)queryResult[2]);
                }
                if(queryResult[3] != null){
                    vProduzione.setDataProduzione((Date)queryResult[3]);
                }
                if(queryResult[4] != null){
                    vProduzione.setTipologia(((String)queryResult[4]));
                }
                if(queryResult[5] != null){
                    vProduzione.setIdConfezione(((Integer)queryResult[5]).longValue());
                }
                if(queryResult[6] != null){
                    vProduzione.setLotto((String)queryResult[6]);
                }
                if(queryResult[7] != null){
                    vProduzione.setScadenza(((Date)queryResult[7]));
                }
                if(queryResult[8] != null){
                    vProduzione.setIdArticolo(((Integer)queryResult[8]).longValue());
                }
                if(queryResult[9] != null){
                    vProduzione.setCodiceArticolo(((String)queryResult[9]));
                }
                if(queryResult[10] != null){
                    vProduzione.setDescrizioneArticolo(((String)queryResult[10]));
                }
                if(queryResult[11] != null){
                    vProduzione.setIdIngrediente(((Integer)queryResult[11]).longValue());
                }
                if(queryResult[12] != null){
                    vProduzione.setCodiceIngrediente((String)queryResult[12]);
                }
                if(queryResult[13] != null){
                    vProduzione.setDescrizioneIngrediente((String)queryResult[13]);
                }
                if(queryResult[14] != null){
                    vProduzione.setNumConfezioniProdotte((Integer)queryResult[14]);
                }
                if(queryResult[15] != null){
                    vProduzione.setQuantita((BigDecimal)queryResult[15]);
                }
                if(queryResult[16] != null){
                    vProduzione.setRicetta((String)queryResult[16]);
                }
                if(queryResult[17] != null){
                    vProduzione.setBarcodeEan13((String)queryResult[17]);
                }
                if(queryResult[18] != null){
                    vProduzione.setBarcodeEan128((String)queryResult[18]);
                }
                result.add(vProduzione);
            }
        }

        return result;
    }

    @Override
    public Integer countByFilters(Integer codice, String ricetta, String barcodeEan13, String barcodeEan128) {
        Integer count = 0;

        StringBuilder sb = createQueryAsString("count(1)", codice, ricetta, barcodeEan13, barcodeEan128);

        Query query = createQuery(sb.toString(), codice, ricetta, barcodeEan13, barcodeEan128);

        Object result = query.getSingleResult();
        if(result != null){
            count = ((BigInteger)result).intValue();
        }
        return count;
    }

    private StringBuilder createQueryAsString(String select, Integer codice, String ricetta, String barcodeEan13, String barcodeEan128){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(select);
        sb.append(" FROM contafood.v_produzione WHERE 1=1 ");

        if(codice != null) {
            sb.append(" AND codice_produzione = :codice ");
        }
        if(ricetta != null) {
            sb.append(" AND lower(ricetta) LIKE concat('%',:ricetta,'%') ");
        }
        if(barcodeEan13 != null) {
            sb.append(" AND lower(barcodeEan13) LIKE concat('%',:barcodeEan13,'%') ");
        }
        if(barcodeEan128 != null) {
            sb.append(" AND lower(barcodeEan128) LIKE concat('%',:barcodeEan128,'%') ");
        }
        return sb;
    }

    private Query createQuery(String queryAsString, Integer codice, String ricetta, String barcodeEan13, String barcodeEan128) {
        Query query = entityManager.createNativeQuery(queryAsString);
        if(codice != null) {
            query.setParameter("codice", codice);
        }
        if(ricetta != null) {
            query.setParameter("ricetta", ricetta);
        }
        if(barcodeEan13 != null) {
            query.setParameter("barcodeEan13", barcodeEan13);
        }
        if(barcodeEan128 != null){
            query.setParameter("barcodeEan128", barcodeEan128);
        }
        return query;
    }
}
