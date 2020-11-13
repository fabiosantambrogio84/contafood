package com.contafood.util.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Provincia {

    AGRIGENTO("Agrigento", "AG"),
    ALESSANDRIA("Alessandria", "AL"),
    ANCONA("Ancona", "AN"),
    AOSTA("Aosta", "AO"),
    AREZZO("Arezzo", "AR"),
    ASCOLI_PICENO("Ascoli Piceno", "AP"),
    ASTI("Asti", "AT"),
    AVELLINO("Avellino", "AV"),
    BARI("Bari", "BA"),
    BARLETTA_ANDRIA_TRANI("Barletta-Andria-Trani", "BT"),
    BELLUNO("Belluno","BL"),
    BENEVENTO("Benevento", "BN"),
    BERGAMO("Bergamo","BG"),
    BIELLA("Biella", "BI"),
    BOLOGNA("Bologna", "BO"),
    BOLZANO("Bolzano", "BZ"),
    BRESCIA("Brescia", "BS"),
    BRINDISI("Brindisi", "BR"),
    CAGLIARI("Cagliari", "CA"),
    CALTANISSETTA("Caltanissetta", "CL"),
    CAMPOBASSO("Campobasso","CB"),
    CARBONIA_IGLESIAS("Carbonia-Iglesias","CI"),
    CASERTA("Caserta","CE"),
    CATANIA("Catania","CT"),
    CATANZARO("Catanzaro","CZ"),
    CHIETI("Chieti","CH"),
    COMO("Como","CO"),
    COSENZA("Cosenza","CS"),
    CREMONA("Cremona","CR"),
    CROTONE("Crotone","KR"),
    CUNEO("Cuneo","CN"),
    ENNA("Enna","EN"),
    FERMO("Fermo","FM"),
    FERRARA("Ferrara","FE"),
    FIRENZE("Firenze","FI"),
    FOGGIA("Foggia","FG"),
    FORLI_CESENA("Forli-Cesena","FC"),
    FROSINONE("Frosinone","FR"),
    GENOVA("Genova","GE"),
    GORIZIA("Gorizia","GO"),
    GROSSETO("Grosseto","GR"),
    IMPERIA("Imperia","IM"),
    ISERNIA("Isernia","IS"),
    LA_SPEZIA("La Spezia","SP"),
    L_AQUILA("L'Aquila","AQ"),
    LATINA("Latina","LT"),
    LECCE("Lecce","LE"),
    LECCO("Lecco","LC"),
    LIVORNO("Livorno","LI"),
    LODI("Lodi","LO"),
    LUCCA("Lucca","LU"),
    MACERATA("Macerata","MC"),
    MANTOVA("Mantova","MN"),
    MASSA_CARRARA("Massa-Carrara","MS"),
    MATERA("Matera","MT"),
    MESSINA("Messina","ME"),
    MILANO("Milano","MI"),
    MODENA("Modena","MO"),
    MONZA_BRIANZA("Monza Brianza","MB"),
    NAPOLI("Napoli","NA"),
    NOVARA("Novara","NO"),
    NUORO("Nuoro","NU"),
    OLBIA_TEMPIO("Olbia-Tempio","OT"),
    ORISTANO("Oristano","OR"),
    PADOVA("Padova","PD"),
    PALERMO("Palermo","PA"),
    PARMA("Parma","PR"),
    PAVIA("Pavia","PV"),
    PERUGIA("Perugia","PG"),
    PESARO_URBINO("Pesaro Urbino","PU"),
    PESCARA("Pescara","PE"),
    PIACENZA("Piacenza","PC"),
    PISA("Pisa","PI"),
    PISTOIA("Pistoia","PT"),
    PORDENONE("Pordenone","PN"),
    POTENZA("Potenza","PZ"),
    PRATO("Prato","PO"),
    RAGUSA("Ragusa","RG"),
    RAVENNA("Ravenna","RA"),
    REGGIO_CALABRIA("Reggio Calabria","RC"),
    REGGIO_EMILIA("Reggio Emilia","RE"),
    RIETI("Rieti","RI"),
    RIMINI("Rimini","RN"),
    ROMA("Roma","RM"),
    ROVIGO("Rovigo","RO"),
    SALERNO("Salerno","SA"),
    MEDIO_CAMPIDANO("Medio Campidano","VS"),
    SASSARI("Sassari","SS"),
    SAVONA("Savona","SV"),
    SIENA("Siena","SI"),
    SIRACUSA("Siracusa","SR"),
    SONDRIO("Sondrio","SO"),
    TARANTO("Taranto","TA"),
    TERAMO("Teramo","TE"),
    TERNI("Terni","TR"),
    TORINO("Torino","TO"),
    OGLIASTRA("Ogliastra","OG"),
    TRAPANI("Trapani","TP"),
    TRENTO("Trento","TN"),
    TREVISO("Treviso","TV"),
    TRIESTE("Trieste","TS"),
    UDINE("Udine","UD"),
    VARESE("Varese","VA"),
    VENEZIA("Venezia","VE"),
    VERBANO_CUSIO_OSSOLA("Verbano-Cusio-Ossola", "VB"),
    VERCELLI("Vercelli","VC"),
    VERONA("Verona","VR"),
    VIBO_VALENTIA("Vibo Valentia","VV"),
    VICENZA("Vicenza","VI"),
    VITERBO("Viterbo","VT");

    private String label;

    private String sigla;

    Provincia(String label, String sigla) {
        this.label = label;
        this.sigla = sigla;
    }

    public String getLabel() {
        return label;
    }

    public String getSigla() {
        return sigla;
    }

    public static List<String> labels(){
        return Arrays.asList(Provincia.values()).stream().map(p -> p.getLabel()).collect(Collectors.toList());
    }

    public static Provincia getByLabel(String label){
        String name = label.toUpperCase().replaceAll("-","_").replaceAll(" ","_");
        return Provincia.valueOf(name);
    }

}
