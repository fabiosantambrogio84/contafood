package com.contafood.repository.custom;

import com.contafood.model.views.VDdt;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VDdtCustomRepositoryImpl implements VDdtCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<VDdt> findByFilter(Date dataDa, Date dataA, Integer progressivo, Integer idCliente, String cliente, Integer idAgente, Integer idAutista, Integer idStato, Boolean fatturato, Float importo, Integer idTipoPagamento, Integer idArticolo) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM contafood.v_ddt WHERE 1=1 ");

        if(dataDa != null) {
            sb.append(" AND data >= :dataDa ");
        }
        if(dataA != null) {
            sb.append(" AND data <= :dataA ");
        }
        if(progressivo != null) {
            sb.append(" AND progressivo = :progressivo ");
        }
        if(idCliente != null) {
            sb.append(" AND id_cliente = :idCliente ");
        }
        if(cliente != null) {
            sb.append(" AND lower(cliente) LIKE concat('%',:cliente,'%') ");
        }
        if(idAgente != null) {
            sb.append(" AND id_agente = :idAgente ");
        }
        if(idAutista != null) {
            sb.append(" AND id_autista = :idAutista ");
        }
        if(idStato != null) {
            sb.append(" AND id_stato = :idStato ");
        }
        if(fatturato != null) {
            sb.append(" AND fatturato = :fatturato ");
        }
        if(importo != null) {
            sb.append(" AND totale = :importo ");
        }
        if(idTipoPagamento != null) {
            sb.append(" AND id in (select distinct(id_ddt) from pagamento where id_tipo_pagamento = :idTipoPagamento and tipologia = 'DDT') ");
        }
        if(idArticolo != null) {
            sb.append(" AND id in (select distinct(id_ddt) from ddt_articolo where id_articolo = :idArticolo) ");
        }

        sb.append(" ORDER BY v_ddt.progressivo DESC");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(dataDa != null) {
            query.setParameter("dataDa", dataDa);
        }
        if(dataA != null) {
            query.setParameter("dataA", dataA);
        }
        if(progressivo != null) {
            query.setParameter("progressivo", progressivo);
        }
        if(idCliente != null) {
            query.setParameter("idCliente", idCliente);
        }
        if(cliente != null) {
            query.setParameter("cliente", cliente.toLowerCase());
        }
        if(idAgente != null) {
            query.setParameter("idAgente", idAgente);
        }
        if(idAutista != null) {
            query.setParameter("idAutista", idAutista);
        }
        if(idStato != null) {
            query.setParameter("idStato", idStato);
        }
        if(fatturato != null) {
            query.setParameter("fatturato", fatturato ? 1 : 0);
        }
        if(importo != null) {
            query.setParameter("importo", importo);
        }
        if(idTipoPagamento != null) {
            query.setParameter("idTipoPagamento", idTipoPagamento);
        }
        if(idArticolo != null) {
            query.setParameter("idArticolo", idArticolo);
        }

        List<Object[]> queryResults = query.getResultList();
        List<VDdt> result = new ArrayList<>();
        if(queryResults != null && !queryResults.isEmpty()){
            for(Object[] queryResult : queryResults){
                VDdt vDdt = new VDdt();
                vDdt.setId(((Integer)queryResult[0]).longValue());
                if(queryResult[1] != null){
                    vDdt.setAnnoContabile((Integer)queryResult[1]);
                }
                if(queryResult[2] != null){
                    vDdt.setProgressivo((Integer)queryResult[2]);
                }
                if(queryResult[3] != null){
                    vDdt.setData((Date)queryResult[3]);
                }
                if(queryResult[4] != null){
                    vDdt.setIdCliente(((Integer)queryResult[4]).longValue());
                }
                if(queryResult[5] != null){
                    vDdt.setCliente((String)queryResult[5]);
                }
                if(queryResult[6] != null){
                    vDdt.setClienteEmail((String)queryResult[6]);
                }
                if(queryResult[7] != null){
                    vDdt.setIdAgente(((Integer)queryResult[7]).longValue());
                }
                if(queryResult[8] != null){
                    vDdt.setAgente((String)queryResult[8]);
                }
                if(queryResult[9] != null){
                    vDdt.setIdAutista(((Integer)queryResult[9]).longValue());
                }
                if(queryResult[10] != null){
                    vDdt.setIdStato(((Integer)queryResult[10]).longValue());
                }
                if(queryResult[11] != null){
                    vDdt.setStato((String)queryResult[11]);
                }
                if(queryResult[12] != null){
                    vDdt.setFatturato((Boolean)queryResult[12]);
                }
                if(queryResult[13] != null){
                    vDdt.setTotaleAcconto((BigDecimal)queryResult[13]);
                }
                if(queryResult[14] != null){
                    vDdt.setTotale((BigDecimal)queryResult[14]);
                }
                if(queryResult[15] != null){
                    vDdt.setTotaleImponibile((BigDecimal)queryResult[15]);
                }
                if(queryResult[16] != null){
                    vDdt.setTotaleCosto((BigDecimal)queryResult[16]);
                }
                if(queryResult[17] != null){
                    vDdt.setTotaleIva((BigDecimal)queryResult[17]);
                }
                result.add(vDdt);
            }
        }

        return result;
    }
}
