package com.contafood.service;

import com.contafood.exception.GenericException;
import com.contafood.model.*;
import com.contafood.util.RibaConstants;
import com.contafood.util.RibaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Service
public class RibaService {

    private static Logger LOGGER = LoggerFactory.getLogger(RibaService.class);

    private final PagamentoService pagamentoService;

    private final PagamentoAggregatoService pagamentoAggregatoService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");

    private DecimalFormat df = new DecimalFormat();

    @Autowired
    public RibaService(final PagamentoService pagamentoService,
                       final PagamentoAggregatoService pagamentoAggregatoService){
        this.pagamentoService = pagamentoService;
        this.pagamentoAggregatoService = pagamentoAggregatoService;
    }

    public String create(Set<Fattura> fatture){
        LOGGER.info("Start creation of RiBa file...");

        List<PagamentoFattura> pagamentiFatture = new ArrayList<>();
        List<PagamentoAggregato> pagamentiAggregati = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        try{
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
            df.applyPattern("#.00");

            Date data_invio = new Date();
            int totale = 0;
            int idx = 1;

            // create list of Pagamento for fatture
            LOGGER.info("Creating list of 'Pagamento' for fatture...");
            pagamentiFatture = createPagamentiFatture(fatture);
            LOGGER.info("Successfully created list of 'Pagamento' for fatture");

            LOGGER.info("Creating map of 'Cliente' and list of 'Pagamento' for fatture");
            Map<ClienteSimple, Map<Date, List<PagamentoFattura>>> pagamentiFattureByPartitaIva = createMap(pagamentiFatture);
            LOGGER.info("Successfully created map of 'Cliente' and list of 'Pagamento' for fatture");

            LOGGER.info("Creating record testa IB...");
            sb.append(" ");
            sb.append("IB");
            sb.append(RibaConstants.CODICE_SIA);
            sb.append(RibaConstants.CODICE_ABI);
            sb.append(sdf.format(data_invio));
            sb.append(String.format("%1$-20s", "URBANI ALIMENTARI"));
            sb.append(String.format("%1$6s", "")); // Campo Libero
            sb.append(String.format("%1$59s", "")); // Campo Vuoto - Filler
            sb.append(String.format("%1$7s", "")); // Qualificatore flusso
            sb.append(String.format("%1$2s", "")); // Campo Vuoto - Filler
            sb.append("E"); // Divisa
            sb.append(" "); // Campo Vuoto - Filler
            sb.append(String.format("%1$5s", "")); // Campo non disponibile
            sb.append("\n");
            LOGGER.info("Successfully created record testa IB");

            // create the list of rows to be inserted
            List<RiBaRow> rows = new ArrayList<>();

            // loop over map
            for (Map.Entry<ClienteSimple, Map<Date, List<PagamentoFattura>>> entry : pagamentiFattureByPartitaIva.entrySet()) {
                ClienteSimple clienteSimple = entry.getKey();
                for (Map.Entry<Date, List<PagamentoFattura>> innerEntry : entry.getValue().entrySet()) {
                    List<PagamentoFattura> pagamentoFatturaByPartitaIva = innerEntry.getValue();

                    RiBaRow row = null;
                    // check if data must ne grouped
                    if (!clienteSimple.getRaggruppaRiba()) {
                        // create row for each Pagamento
                        for(int i=0; i<pagamentoFatturaByPartitaIva.size(); i++){
                            row = new RiBaRow();

                            PagamentoFattura pagamentoFatturaFatt = pagamentoFatturaByPartitaIva.get(i);
                            Fattura fattura = pagamentoFatturaFatt.getFattura();
                            Pagamento pagamento = pagamentoFatturaFatt.getPagamento();
                            Cliente cliente = pagamentoFatturaFatt.getCliente();

                            Banca banca = cliente.getBanca();
                            String abi = "";
                            String cab = "";
                            if(banca != null) {
                                abi = !StringUtils.isEmpty(banca.getAbi()) ? banca.getAbi() : "";
                                cab = !StringUtils.isEmpty(banca.getCab()) ? banca.getCab() : "";
                            }

                            row.setNumProgressivoFattura(fattura.getProgressivo());
                            row.setAbiCliente(abi);
                            row.setCabCliente(cab);
                            row.setCapCliente(cliente.getCap());
                            row.setDataScadenza(pagamentoFatturaFatt.getDataScadenza());
                            row.setDescrizionePagamento(pagamento.getDescrizione());
                            row.setIdCliente(cliente.getId().intValue());
                            row.setIdPagamento(pagamento.getId().intValue());

                            row.setImporto((int) (pagamento.getImporto().doubleValue() * 100));
                            row.setIndirizzoCliente(cliente.getIndirizzo());
                            row.setLocalitaCliente(cliente.getCitta());
                            row.setPartitaIvaCliente(cliente.getPartitaIva());
                            row.setRagioneSocialeCliente(cliente.getRagioneSociale());
                            row.setRagioneSociale2Cliente(cliente.getRagioneSociale2());

                            row.setLogNumProgressivo(String.valueOf(fattura.getProgressivo()));

                            rows.add(row);
                        }
                    } else {
                        if(pagamentoFatturaByPartitaIva != null && !pagamentoFatturaByPartitaIva.isEmpty()){
                            row = new RiBaRow();

                            // retrieve 'Cliente'
                            Cliente cliente = RibaUtils.getGroupedCliente(pagamentoFatturaByPartitaIva);

                            // compute 'importo'
                            Integer importoFattura = 0;
                            for (PagamentoFattura pagFatt : pagamentoFatturaByPartitaIva) {
                                importoFattura = importoFattura + (int) (pagFatt.getPagamento().getImporto().doubleValue() * 100);
                            }

                            // create a string with list of 'Fattura.numeroProgressivo'
                            String logNumProgressivo = "";
                            for(PagamentoFattura pagamentoFattura : pagamentoFatturaByPartitaIva){
                                Fattura fattura = pagamentoFattura.getFattura();
                                if(fattura != null){
                                    Integer progressivo = fattura.getProgressivo();
                                    if(progressivo != null){
                                        logNumProgressivo = logNumProgressivo + progressivo + ",";
                                        row.setNumProgressivoFattura(progressivo);
                                    }
                                }
                            }

                            // create 'PagamentoAggregato'
                            PagamentoAggregato pagamentoAggregato = new PagamentoAggregato();
                            pagamentoAggregato.setDescrizione("Fatture: " + logNumProgressivo);
                            pagamentoAggregato.setNote("Da RiBa");
                            PagamentoAggregato createdPagamentoAggregato = pagamentoAggregatoService.createPagamentoAggregato(pagamentoAggregato);

                            pagamentiAggregati.add(createdPagamentoAggregato);

                            // update list elements 'Pagamento' setting 'PagamentoAggregato.id'
                            for(PagamentoFattura pagamentoFattura : pagamentoFatturaByPartitaIva){
                                Pagamento pagamento = pagamentoFattura.getPagamento();
                                pagamento.setPagamentoAggregato(pagamentoAggregato);
                                Pagamento updatedPagamento = pagamentoService.updateSimple(pagamento);
                                pagamentoFattura.setPagamento(updatedPagamento);
                            }

                            Banca banca = cliente.getBanca();
                            String abi = "";
                            String cab = "";
                            if(banca != null) {
                                abi = !StringUtils.isEmpty(banca.getAbi()) ? banca.getAbi() : "";
                                cab = !StringUtils.isEmpty(banca.getCab()) ? banca.getCab() : "";
                            }

                            row.setAbiCliente(abi);
                            row.setCabCliente(cab);
                            row.setCapCliente(cliente.getCap());
                            row.setDataScadenza(innerEntry.getKey());
                            row.setDescrizionePagamento("Pagamento fatture '" + cliente.getRagioneSociale() + "'");
                            row.setIdCliente(cliente.getId().intValue());
                            row.setIdPagamento(pagamentoAggregato.getId().intValue());
                            row.setImporto(importoFattura);
                            row.setIndirizzoCliente(cliente.getIndirizzo());
                            row.setLocalitaCliente(cliente.getCitta());
                            row.setPartitaIvaCliente(cliente.getPartitaIva());
                            row.setRagioneSociale2Cliente("");
                            row.setRagioneSocialeCliente(cliente.getNomeGruppoRiba());
                            row.setLogNumProgressivo(logNumProgressivo);

                            rows.add(row);
                        }
                    }
                }
            }

            // order rows based on 'Fattura.progressivo'
            Collections.sort(rows, (o1, o2) -> o1.getNumProgressivoFattura().compareTo(o2.getNumProgressivoFattura()));

            for (RiBaRow row : rows) {
                LOGGER.info("Create record...");

                // update total
                totale += row.getImporto();

                // set index row
                row.setIndex(idx);

                // get 'Fattura.progressivo'
                String fatturaNumProgressivo = row.getLogNumProgressivo();

                // RECORD 14
                sb.append(" ");
                sb.append("14");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
                sb.append(sdf.format(row.getDataScadenza()));
                sb.append("30000");
                sb.append(String.format("%013d", row.getImporto()));
                sb.append("-");
                sb.append(RibaConstants.CODICE_ABI);
                sb.append(RibaConstants.CODICE_CAB);
                sb.append(String.format("%1$12s", RibaConstants.CC));
                sb.append(String.format("%1$5s", row.getAbiCliente().trim()));
                sb.append(String.format("%1$5s", row.getCabCliente().trim()));
                sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
                sb.append(RibaConstants.CODICE_SIA);
                sb.append("4");
                sb.append(String.format("%1$-16s", row.getIdCliente())); // Campo Vuoto - Filler
                sb.append(" ");
                sb.append(String.format("%1$5s", "")); // Campo Vuoto - Filler
                sb.append("E");
                sb.append("\n");

                LOGGER.info("Successfully create record 14 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                // RECORD 20
                sb.append(" ");
                sb.append("20");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$-24s", RibaConstants.URBANI_ALIMENTARI_NAME));
                sb.append(String.format("%1$-24s", RibaConstants.URBANI_ALIMENTARI_VIA_CIVICO));
                sb.append(String.format("%1$-24s", RibaConstants.URBANI_ALIMENTARI_CAP_CITTA));
                sb.append(String.format("%1$-24s", RibaConstants.URBANI_ALIMENTARI_CITTA_PROVINCIA));
                sb.append(String.format("%1$14s", "")); // Campo Vuoto - Filler
                sb.append("\n");

                LOGGER.info("Successfully create record 20 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                // RECORD 30
                sb.append(" ");
                sb.append("30");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$-30s", StringUtils.left(row.getRagioneSocialeCliente(), 30)));
                sb.append(String.format("%1$-30s", StringUtils.left(row.getRagioneSociale2Cliente(), 30)));
                if(row.getPartitaIvaCliente() != null && !row.getPartitaIvaCliente().equals("")){
                    sb.append(String.format("%1$-16s", row.getPartitaIvaCliente()));
                } else{
                    sb.append(String.format("%1$-16s", "")); // Campo Vuoto - Filler
                }

                LOGGER.info("Successfully created record 30 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                sb.append(String.format("%1$34s", "")); // Campo Vuoto - Filler
                sb.append("\n");

                // RECORD 40
                sb.append(" ");
                sb.append("40");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$-30s", StringUtils.left(row.getIndirizzoCliente(), 30)));
                sb.append(String.format("%1$5s", StringUtils.left(row.getCapCliente(), 5)));
                sb.append(String.format("%1$-25s", StringUtils.left(row.getLocalitaCliente(), 25)));
                sb.append(String.format("%1$50s", ""));
                sb.append("\n");

                LOGGER.info("Successfully created record 40 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                // RECORD 50
                sb.append(" ");
                sb.append("50");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$-40s", StringUtils.left(row.getDescrizionePagamento(), 40)));
                sb.append(String.format("%1$40s", ""));
                sb.append(String.format("%1$10s", ""));
                sb.append(String.format("%1$-16s", RibaConstants.CODICE_FISCALE));
                sb.append(String.format("%1$4s", ""));
                sb.append("\n");

                LOGGER.info("Successfully created record 50 for 'fattura' with progressivoa '" + fatturaNumProgressivo + "'");

                // RECORD 51
                sb.append(" ");
                sb.append("51");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%010d", row.getIdPagamento()));
                sb.append(String.format("%1$20s", RibaConstants.URBANI_ALIMENTARI_NAME));
                sb.append(String.format("%1$15s", ""));
                sb.append(String.format("%1$10s", ""));
                sb.append(String.format("%1$6s", ""));
                sb.append(String.format("%1$49s", ""));
                sb.append("\n");

                LOGGER.info("Successfully created record 51 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                // RECORD 70
                sb.append(" ");
                sb.append("70");
                sb.append(String.format("%07d", idx));
                sb.append(String.format("%1$78s", ""));
                sb.append(String.format("%1$12s", ""));
                sb.append(" ");
                sb.append(" ");
                sb.append(" ");
                sb.append(String.format("%1$17s", ""));
                sb.append("\n");

                LOGGER.info("Successfully created record 70 for 'fattura' with progressivo '" + fatturaNumProgressivo + "'");

                idx++;
            }
            idx--;

            LOGGER.info("Successfully created record rows");

            // RECORD DI CODA EF
            sb.append(" ");
            sb.append("EF");
            sb.append(RibaConstants.CODICE_SIA);
            sb.append(RibaConstants.CODICE_ABI);
            sb.append(sdf.format(data_invio));
            sb.append(String.format("%1$-20s", RibaConstants.URBANI_ALIMENTARI_NAME));
            sb.append(String.format("%1$6s", "")); // Campo Libero
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%015d", totale));
            sb.append(String.format("%015d", 0));
            sb.append(String.format("%07d", (idx * 7 + 2)));
            sb.append(String.format("%1$24s", "")); // Campo Vuoto - Filler
            sb.append("E"); // Divisa
            sb.append(" "); // Campo Vuoto - Filler
            sb.append(String.format("%1$5s", "")); // Campo non disponibile
            sb.append("\n");

            LOGGER.info("Successfully created RiBa file");

        } catch(Exception e){
            // delete created pagamenti
            try{
                for(PagamentoFattura pagamentoFattura : pagamentiFatture){
                    Pagamento pagamento = pagamentoFattura.getPagamento();
                    if(pagamento.getId() != null){
                        pagamentoService.deletePagamento(pagamento.getId());
                    }
                }

                for(PagamentoAggregato pagamentoAggregato : pagamentiAggregati){
                    if(pagamentoAggregato.getId() != null){
                        pagamentoAggregatoService.deletePagamentoAggregato(pagamentoAggregato.getId());
                    }
                }

            } catch(Exception e1){
                // do nothing
            }

            e.printStackTrace();
            throw new GenericException("Error creating RiBa file. "+e.getMessage());
        }
        return StringUtils.stripAccents(sb.toString());
    }

    private List<PagamentoFattura> createPagamentiFatture(Set<Fattura> fatture){
        List<PagamentoFattura> pagamentiFatture = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(Fattura fattura : fatture){
            Cliente cliente = fattura.getCliente();
            TipoPagamento tipoPagamento = cliente.getTipoPagamento();

            BigDecimal fatturaTotale = fattura.getTotale() != null ? fattura.getTotale() : BigDecimal.ZERO;
            BigDecimal fatturaTotaleAcconto = fattura.getTotaleAcconto() != null ? fattura.getTotaleAcconto() : BigDecimal.ZERO;
            BigDecimal importoPagamento = fatturaTotale.subtract(fatturaTotaleAcconto).setScale(2, RoundingMode.HALF_DOWN);

            Pagamento pagamento = new Pagamento();
            pagamento.setFattura(fattura);
            pagamento.setTipologia("FATTURA");
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setData(java.sql.Date.valueOf(LocalDate.now()));
            pagamento.setDescrizione("Pagamento FATTURA n. "+fattura.getProgressivo()+" del "+sdf.format(fattura.getData()));
            pagamento.setImporto(importoPagamento);
            Pagamento createdPagamento = pagamentoService.createPagamento(pagamento);

            PagamentoFattura pagamentoFattura = new PagamentoFattura();
            pagamentoFattura.setFattura(fattura);
            pagamentoFattura.setCliente(cliente);
            pagamentoFattura.setPagamento(createdPagamento);
            pagamentoFattura.setDataScadenza(RibaUtils.getDataScadenza(fattura.getData(), tipoPagamento));

            pagamentiFatture.add(pagamentoFattura);
        }

        return pagamentiFatture;
    }

    private ClienteSimple createClienteSimple(Cliente cliente){
        return new ClienteSimple(cliente.getPartitaIva(), cliente.getRaggruppaRiba());
    }

    private Map<ClienteSimple, Map<Date, List<PagamentoFattura>>> createMap(List<PagamentoFattura> pagamentiFatture){
        /*
         * Create map with key=Cliente and value is another map.
         * The inner map has key=dataScadenza and value List<PagamentoFattura>
         */
        Map<ClienteSimple, Map<Date, List<PagamentoFattura>>> pagamentiFattureByPartitaIva = new HashMap<>();
        for(PagamentoFattura pagamentoFattura: pagamentiFatture){
            ClienteSimple cliente = createClienteSimple(pagamentoFattura.getCliente());
            Date dataScadenza = pagamentoFattura.getDataScadenza();
            List<PagamentoFattura> pagFatt;
            Map<Date, List<PagamentoFattura>> pagamentiByDate = pagamentiFattureByPartitaIva.get(cliente);
            if(pagamentiByDate != null && !pagamentiByDate.isEmpty()){
                pagFatt = pagamentiByDate.get(dataScadenza);
                if(pagFatt != null && !pagFatt.isEmpty()){
                    pagFatt.add(pagamentoFattura);
                } else {
                    pagFatt = new ArrayList<>();
                    pagFatt.add(pagamentoFattura);
                }
            } else{
                pagamentiByDate = new HashMap<>();
                pagFatt = new ArrayList<>();
                pagFatt.add(pagamentoFattura);
                pagamentiByDate.put(dataScadenza, pagFatt);
                pagamentiFattureByPartitaIva.put(cliente, pagamentiByDate);
            }
        }
        return pagamentiFattureByPartitaIva;
    }

    private class RiBaRow {
        private Integer index;

        private Integer numProgressivoFattura;

        private Date dataScadenza;

        private Integer importo;

        private String abiCliente;

        private String cabCliente;

        private Integer idCliente;

        private String ragioneSocialeCliente;

        private String ragioneSociale2Cliente;

        private String partitaIvaCliente;

        private String indirizzoCliente;

        private String capCliente;

        private String localitaCliente;

        private String descrizionePagamento;

        private Integer idPagamento;

        private String logNumProgressivo;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getNumProgressivoFattura() {
            return numProgressivoFattura;
        }

        public void setNumProgressivoFattura(Integer numProgressivoFattura) {
            this.numProgressivoFattura = numProgressivoFattura;
        }

        public Date getDataScadenza() {
            return dataScadenza;
        }

        public void setDataScadenza(Date dataScadenza) {
            this.dataScadenza = dataScadenza;
        }

        public Integer getImporto() {
            return importo;
        }

        public void setImporto(Integer importo) {
            this.importo = importo;
        }

        public String getAbiCliente() {
            return abiCliente;
        }

        public void setAbiCliente(String abiCliente) {
            this.abiCliente = abiCliente;
        }

        public String getCabCliente() {
            return cabCliente;
        }

        public void setCabCliente(String cabCliente) {
            this.cabCliente = cabCliente;
        }

        public Integer getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(Integer idCliente) {
            this.idCliente = idCliente;
        }

        public String getRagioneSocialeCliente() {
            return ragioneSocialeCliente;
        }

        public void setRagioneSocialeCliente(String ragioneSocialeCliente) {
            this.ragioneSocialeCliente = ragioneSocialeCliente;
        }

        public String getRagioneSociale2Cliente() {
            return ragioneSociale2Cliente;
        }

        public void setRagioneSociale2Cliente(String ragioneSociale2Cliente) {
            this.ragioneSociale2Cliente = ragioneSociale2Cliente;
        }

        public String getPartitaIvaCliente() {
            return partitaIvaCliente;
        }

        public void setPartitaIvaCliente(String partitaIvaCliente) {
            this.partitaIvaCliente = partitaIvaCliente;
        }

        public String getIndirizzoCliente() {
            return indirizzoCliente;
        }

        public void setIndirizzoCliente(String indirizzoCliente) {
            this.indirizzoCliente = indirizzoCliente;
        }

        public String getCapCliente() {
            return capCliente;
        }

        public void setCapCliente(String capCliente) {
            this.capCliente = capCliente;
        }

        public String getLocalitaCliente() {
            return localitaCliente;
        }

        public void setLocalitaCliente(String localitaCliente) {
            this.localitaCliente = localitaCliente;
        }

        public String getDescrizionePagamento() {
            return descrizionePagamento;
        }

        public void setDescrizionePagamento(String descrizionePagamento) {
            this.descrizionePagamento = descrizionePagamento;
        }

        public Integer getIdPagamento() {
            return idPagamento;
        }

        public void setIdPagamento(Integer idPagamento) {
            this.idPagamento = idPagamento;
        }

        public String getLogNumProgressivo() {
            return logNumProgressivo;
        }

        public void setLogNumProgressivo(String logNumProgressivo) {
            this.logNumProgressivo = logNumProgressivo;
        }
    }

    final private class ClienteSimple {

        final private String partitaIva;

        final private Boolean isRaggruppaRiba;

        public ClienteSimple(String partitaIva, Boolean isRaggruppaRiba){
            this.partitaIva = partitaIva;
            this.isRaggruppaRiba = isRaggruppaRiba;
        }

        public String getPartitaIva() {
            return partitaIva;
        }

        public Boolean getRaggruppaRiba() {
            return isRaggruppaRiba;
        }

        @Override
        public int hashCode() {
            return partitaIva.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ClienteSimple other = (ClienteSimple) obj;
            if (partitaIva != other.partitaIva)
                return false;
            return true;
        }
    }

    public class PagamentoFattura {

        private Fattura fattura;

        private Cliente cliente;

        private Pagamento pagamento;

        private Date dataScadenza;

        public Fattura getFattura() {
            return fattura;
        }

        public void setFattura(Fattura fattura) {
            this.fattura = fattura;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public Pagamento getPagamento() {
            return pagamento;
        }

        public void setPagamento(Pagamento pagamento) {
            this.pagamento = pagamento;
        }

        public Date getDataScadenza() {
            return dataScadenza;
        }

        public void setDataScadenza(Date dataScadenza) {
            this.dataScadenza = dataScadenza;
        }
    }
}
