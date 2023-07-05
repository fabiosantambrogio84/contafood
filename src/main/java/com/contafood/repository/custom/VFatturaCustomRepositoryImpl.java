package com.contafood.repository.custom;

import com.contafood.model.beans.SortOrder;
import com.contafood.model.views.VFattura;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VFatturaCustomRepositoryImpl implements VFatturaCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VFattura> findByFilters(Integer draw, Integer start, Integer length, List<SortOrder> sortOrders, Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo) {
        StringBuilder sb = createQueryAsString("*", dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        if(draw != null && draw != -1){
            int limit = length != null ? length : 20;
            int offset = start != null ? start : 0;
            String order = " ORDER BY anno DESC, progressivo DESC";
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
            sb.append(" ORDER BY anno DESC, progressivo DESC");
        }

        Query query = createQuery(sb.toString(), dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        List<Object[]> queryResults = query.getResultList();
        List<VFattura> result = new ArrayList<>();
        if(queryResults != null && !queryResults.isEmpty()){
            for(Object[] queryResult : queryResults){
                VFattura vFattura = new VFattura();
                vFattura.setId(((Integer)queryResult[0]).longValue());
                if(queryResult[1] != null){
                    vFattura.setProgressivo((Integer)queryResult[1]);
                }
                if(queryResult[2] != null){
                    vFattura.setAnno((Integer)queryResult[2]);
                }
                if(queryResult[3] != null){
                    vFattura.setData((Date)queryResult[3]);
                }
                if(queryResult[4] != null){
                    vFattura.setIdTipo(((Integer)queryResult[4]).longValue());
                }
                if(queryResult[5] != null){
                    vFattura.setTipoCodice((String)queryResult[5]);
                }
                if(queryResult[6] != null){
                    vFattura.setIdCliente(((Integer)queryResult[6]).longValue());
                }
                if(queryResult[7] != null){
                    vFattura.setCliente((String)queryResult[7]);
                }
                if(queryResult[8] != null){
                    vFattura.setClienteEmail((String)queryResult[8]);
                }
                if(queryResult[9] != null){
                    vFattura.setIdTipoPagamento(((Integer)queryResult[9]).longValue());
                }
                if(queryResult[10] != null){
                    vFattura.setIdAgente(((Integer)queryResult[10]).longValue());
                }
                if(queryResult[11] != null){
                    vFattura.setAgente((String)queryResult[11]);
                }
                if(queryResult[12] != null){
                    vFattura.setIdStato(((Integer)queryResult[12]).longValue());
                }
                if(queryResult[13] != null){
                    vFattura.setStatoCodice((String)queryResult[13]);
                }
                if(queryResult[14] != null){
                    vFattura.setSpeditoAde((Boolean)queryResult[14]);
                }
                if(queryResult[15] != null){
                    vFattura.setTotaleImponibile((BigDecimal)queryResult[15]);
                }
                if(queryResult[16] != null){
                    vFattura.setTotaleIva((BigDecimal)queryResult[16]);
                }
                if(queryResult[17] != null){
                    vFattura.setTotaleAcconto((BigDecimal)queryResult[17]);
                }
                if(queryResult[18] != null){
                    vFattura.setTotale((BigDecimal)queryResult[18]);
                }
                if(queryResult[19] != null){
                    vFattura.setNote((String)queryResult[19]);
                }
                if(queryResult[20] != null){
                    vFattura.setDataInserimento((Timestamp)queryResult[20]);
                }
                if(queryResult[21] != null){
                    vFattura.setDataAggiornamento((Timestamp)queryResult[21]);
                }
                if(queryResult[22] != null){
                    vFattura.setIdArticoli((String)queryResult[22]);
                }
                result.add(vFattura);
            }
        }

        return result;
    }

    @Override
    public Integer countByFilters(Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo) {
        Integer count = 0;

        StringBuilder sb = createQueryAsString("count(1)", dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        Query query = createQuery(sb.toString(), dataDa, dataA, progressivo, importo, idTipoPagamento, cliente, idAgente, idArticolo, idStato, idTipo);

        Object result = query.getSingleResult();
        if(result != null){
            count = ((BigInteger)result).intValue();
        }
        return count;
    }

    private StringBuilder createQueryAsString(String select, Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(select);
        sb.append(" FROM contafood.v_fattura WHERE 1=1 ");

        if(dataDa != null) {
            sb.append(" AND data >= :dataDa ");
        }
        if(dataA != null) {
            sb.append(" AND data <= :dataA ");
        }
        if(progressivo != null) {
            sb.append(" AND progressivo = :progressivo ");
        }
        if(importo != null) {
            sb.append(" AND totale = :importo ");
        }
        if(idTipoPagamento != null) {
            sb.append(" AND id_tipo_pagamento in (:idTipoPagamento) ");
        }
        if(cliente != null) {
            sb.append(" AND lower(cliente) LIKE concat('%',:cliente,'%') ");
        }
        if(idAgente != null) {
            sb.append(" AND id_agente = :idAgente ");
        }
        if(idStato != null) {
            sb.append(" AND id_stato = :idStato ");
        }
        if(idTipo != null) {
            sb.append(" AND id_tipo = :idTipo ");
        }
        if(idArticolo != null) {
            sb.append(" AND concat(id_articoli,',') like :idArticolo ");
        }
        return sb;
    }

    private Query createQuery(String queryAsString, Date dataDa, Date dataA, Integer progressivo, Float importo, String idTipoPagamento, String cliente, Integer idAgente, Integer idArticolo, Integer idStato, Integer idTipo) {
        Query query = entityManager.createNativeQuery(queryAsString);

        if(dataDa != null) {
            query.setParameter("dataDa", dataDa);
        }
        if(dataA != null) {
            query.setParameter("dataA", dataA);
        }
        if(progressivo != null) {
            query.setParameter("progressivo", progressivo);
        }
        if(importo != null) {
            query.setParameter("importo", importo);
        }
        if(idTipoPagamento != null) {
            List<String> idTipiPagamento = Arrays.asList(idTipoPagamento.split(","));
            query.setParameter("idTipoPagamento", idTipiPagamento);
        }
        if(cliente != null) {
            query.setParameter("cliente", cliente.toLowerCase());
        }
        if(idAgente != null) {
            query.setParameter("idAgente", idAgente);
        }
        if(idStato != null) {
            query.setParameter("idStato", idStato);
        }
        if(idTipo != null) {
            query.setParameter("idTipo", idTipo);
        }
        if(idArticolo != null) {
            query.setParameter("idArticolo", "%"+idArticolo+"%");
        }
        return query;
    }
}