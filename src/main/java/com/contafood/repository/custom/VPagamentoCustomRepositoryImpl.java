package com.contafood.repository.custom;

import com.contafood.model.views.VPagamento;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VPagamentoCustomRepositoryImpl implements VPagamentoCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<VPagamento> findByFilter(String tipologia, Date dataDa, Date dataA, String cliente, String fornitore, Float importo) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM contafood.v_pagamento WHERE 1=1 ");

        if(tipologia != null) {
            sb.append(" AND tipologia = :tipologia ");
        }
        if(dataDa != null) {
            sb.append(" AND data >= :dataDa ");
        }
        if(dataA != null) {
            sb.append(" AND data <= :dataA ");
        }
        if(cliente != null) {
            sb.append(" AND lower(cliente) LIKE concat('%',:cliente,'%') ");
        }
        if(fornitore != null) {
            sb.append(" AND lower(fornitore) LIKE concat('%',:fornitore,'%') ");
        }
        if(importo != null) {
            sb.append(" AND importo = :importo ");
        }

        sb.append(" ORDER BY v_pagamento.data DESC");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(tipologia != null) {
            query.setParameter("tipologia", tipologia);
        }
        if(dataDa != null) {
            query.setParameter("dataDa", dataDa);
        }
        if(dataA != null) {
            query.setParameter("dataA", dataA);
        }
        if(cliente != null) {
            query.setParameter("cliente", cliente.toLowerCase());
        }
        if(fornitore != null) {
            query.setParameter("fornitore", fornitore);
        }
        if(importo != null) {
            query.setParameter("importo", importo);
        }

        List<Object[]> queryResults = query.getResultList();
        List<VPagamento> result = new ArrayList<>();
        if(queryResults != null && !queryResults.isEmpty()){
            for(Object[] queryResult : queryResults){
                VPagamento vPagamento = new VPagamento();
                vPagamento.setId(((Integer)queryResult[0]).longValue());
                if(queryResult[1] != null){
                    vPagamento.setData((Date)queryResult[1]);
                }
                if(queryResult[2] != null){
                    vPagamento.setTipologia((String)queryResult[2]);
                }
                if(queryResult[3] != null){
                    vPagamento.setIdTipoPagamento(((Integer)queryResult[3]).longValue());
                }
                if(queryResult[4] != null){
                    vPagamento.setTipoPagamento((String)queryResult[4]);
                }
                if(queryResult[5] != null){
                    vPagamento.setDescrizione((String)queryResult[5]);
                }
                if(queryResult[6] != null){
                    vPagamento.setNote((String)queryResult[6]);
                }
                if(queryResult[7] != null){
                    vPagamento.setImporto((BigDecimal)queryResult[7]);
                }
                if(queryResult[8] != null){
                    vPagamento.setIdResource(((Integer)queryResult[8]).longValue());
                }
                if(queryResult[9] != null){
                    vPagamento.setIdCliente(((Integer)queryResult[9]).longValue());
                }
                if(queryResult[10] != null){
                    vPagamento.setCliente((String)queryResult[10]);
                }
                if(queryResult[11] != null){
                    vPagamento.setIdFornitore(((Integer)queryResult[11]).longValue());
                }
                if(queryResult[12] != null){
                    vPagamento.setFornitore((String)queryResult[12]);
                }
                result.add(vPagamento);
            }
        }

        return result;
    }
}
