package formula1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Classe statica che definisce metodi ed attributi per consentire l'esecuzione
 * delle Query.
 *
 * @author gruppo05
 */
public class Query {

    private static final String URL = "jdbc:postgresql://localhost/prova";
    private static final String USER = "utente_generico";
    private static final String PASS = "password";
    private static Connection conn;

    private static PreparedStatement pstSelezionaPilotaAttuale = null;
    private static PreparedStatement pstSelezionaScuderiaAttuale = null;
    private static PreparedStatement pstSelezionaScuderiaPassata = null;
    private static PreparedStatement pstSelezionaPilotaString = null;
    private static PreparedStatement pstSelezionaGiornata = null;
    private static PreparedStatement pstSelezionaPista = null;
    private static PreparedStatement pstSelezionaPilotaPassato = null;
    private static PreparedStatement pstRisultatiAttuali = null;
    private static PreparedStatement pstRisultatiPassati = null;

    public Query() {

    }

    /**
     * Stabilisce una connessione con il DBMS. Si presuppone che a effettuare il
     * login sia l' "utente_generico", ovvero un utente che ha solo i permessi
     * di lettura sulla Base Dati. Se il login fallisce viene lanciata
     * SQLException.
     *
     * @throws SQLException
     */
    public static void InitConnection() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASS);

    }

    /**
     * Restituisce il ResultSet contenente la classifica piloti del campionato
     * attuale
     *
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet getClassificaPilotiAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_PILOTI_ATTUALE ";
        Statement stm = conn.createStatement();
        ResultSet rst = stm.executeQuery(q);
        return rst;
    }

    /**
     * Restituisce il ResultSet contenente le classifiche piloti dei campionati
     * passati
     *
     * @param annoCampionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet getClassifichePilotiPassati(int annoCampionato) throws SQLException {
        String q = "select * from CLASSIFICHE_PILOTI_PASSATI where numero_campionato = ?";
        PreparedStatement pstPilotiPassati = conn.prepareStatement(q);
        pstPilotiPassati.setInt(1, annoCampionato);
        return pstPilotiPassati.executeQuery();
    }

    /**
     * Restituisce il ResultSet contente la classifica costruttori del
     * campionato attuale
     *
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet getClassificaScuderieAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_COSTRUTTORI_ATTUALE";
        return conn.createStatement().executeQuery(q);
    }

    /**
     * Restituisce il ResultSet contenente le classifiche costruttori dei
     * campionati passati
     *
     * @param annoCampionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet getClassificheScuderiePassate(int annoCampionato) throws SQLException {
        String q = "select * from CLASSIFICHE_COSTRUTTORI_PASSATE where numero_campionato=?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, annoCampionato);
        return pst.executeQuery();
    }

    /**
     * Restituisce il ResultSet contenente il calendario delle gare rispetto
     * l'anno campionato passato come parametro
     *
     * @param numCampionato Anno campionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet getCalendario(int numCampionato) throws SQLException {
        String q = "select numero_giornata, sede_pista, nome_pista, data from CALENDARIO where numero_campionato = ? order by numero_giornata";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numCampionato);
        return pst.executeQuery();
    }

    /**
     * Seleziona le informazioni del singolo pilota rispetto l' anno campionato
     *
     * @param x Parametro intero usato come offset all'interno della query
     * @param numeroCampionato Anno campionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaPilota(int x, int numeroCampionato) throws SQLException {
        if (isCurrent(numeroCampionato)) {
            String q = "select * from piloti where codice_pilota = "
                    + "(select codice_pilota from classifica_piloti_attuale  offset ? limit 1)";
            if (pstSelezionaPilotaAttuale == null) {
                pstSelezionaPilotaAttuale = conn.prepareStatement(q);
            }
            pstSelezionaPilotaAttuale.setInt(1, x);
            return pstSelezionaPilotaAttuale.executeQuery();
        }

        String q = "select * from piloti where codice_pilota= (select codice_pilota from CLASSIFICHE_PILOTI_PASSATI where numero_campionato = ? offset ? limit 1)";
        if (pstSelezionaPilotaPassato == null) {
            pstSelezionaPilotaPassato = conn.prepareStatement(q);
        }

        pstSelezionaPilotaPassato.setInt(1, numeroCampionato);
        pstSelezionaPilotaPassato.setInt(2, x);
        return pstSelezionaPilotaPassato.executeQuery();
    }

    /**
     * Seleziona il singolo pilota in base al codice identificativo dello stesso
     * (codice pilota)
     *
     * @param x Codice Pilota
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaPilota(String x) throws SQLException {
        String q = "select * from piloti where codice_pilota = ?";
        if (pstSelezionaPilotaString == null) {
            pstSelezionaPilotaString = conn.prepareStatement(q);
        }
        pstSelezionaPilotaString.setString(1, x);
        return pstSelezionaPilotaString.executeQuery();
    }

    /**
     * Seleziona le informazioni della singola scuderia rispetto l' anno
     * campionato
     *
     * @param x Parametro di tipo intero usato come offset all'interno della query
     * @param numeroCampionato Anno campionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaScuderia(int x, int numeroCampionato) throws SQLException {
        if (isCurrent(numeroCampionato)) {
            String q = "select * from scuderie where nome_scuderia = "
                    + "(select nome_scuderia from classifica_costruttori_attuale offset ? limit 1)";
            if (pstSelezionaScuderiaAttuale == null) {
                pstSelezionaScuderiaAttuale = conn.prepareStatement(q);
            }
            pstSelezionaScuderiaAttuale.setInt(1, x);
            return pstSelezionaScuderiaAttuale.executeQuery();
        }

        String q = "select * from scuderie where nome_scuderia= (select nome_scuderia from classifiche_costruttori_passate where numero_campionato= ? offset ? limit 1 )";
        if (pstSelezionaScuderiaPassata == null) {
            pstSelezionaScuderiaPassata = conn.prepareStatement(q);
        }
        pstSelezionaScuderiaPassata.setInt(1, numeroCampionato);
        pstSelezionaScuderiaPassata.setInt(2, x);
        return pstSelezionaScuderiaPassata.executeQuery();
    }

    /**
     * Seleziona le informazioni riguardo la singola scuderia e i piloti
     * associati alla stessa rispetto l' anno campionato passato come parametro
     *
     * @param nomeScuderia
     * @param numeroCampionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaAfferenzaScuderia(String nomeScuderia, int numeroCampionato) throws SQLException {
        String q = "select * from afferenza_piloti where nome_scuderia = ? and numero_campionato = ? ";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, nomeScuderia);
        pst.setInt(2, numeroCampionato);
        return pst.executeQuery();
    }

    /**
     * Seleziona le informazioni riguardo il singolo pilota e la scuderia
     * associata rispetto l' anno campionato passato come parametro
     *
     * @param codicePilota
     * @param numeroCampionato
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaAfferenzaPiloti(String codicePilota, int numeroCampionato) throws SQLException {
        String q = "select * from afferenza_piloti where codice_pilota = ? and numero_campionato = ?";
        PreparedStatement pstSelezionaAfferenzaPiloti = conn.prepareStatement(q);
        pstSelezionaAfferenzaPiloti.setString(1, codicePilota);
        pstSelezionaAfferenzaPiloti.setInt(2, numeroCampionato);
        return pstSelezionaAfferenzaPiloti.executeQuery();

    }

    /**
     * Seleziona le informazioni della singola giornata del calendario rispetto
     * l' anno campionato e la giornata passati come parametri
     *
     * @param numeroCampionato
     * @param x Numero della giornata
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaGiornata(int numeroCampionato, int x) throws SQLException {
        String q = "select * from CALENDARIO where numero_campionato = ? and numero_giornata = ?";
        if (pstSelezionaGiornata == null) {
            pstSelezionaGiornata = conn.prepareStatement(q);
        }
        pstSelezionaGiornata.setInt(1, numeroCampionato);
        pstSelezionaGiornata.setInt(2, x);
        return pstSelezionaGiornata.executeQuery();
    }

    /**
     * Seleziona le informazioni della singola pista in base alla sede e al nome
     * della stessa
     *
     * @param sedePista
     * @param nomePista
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaPista(String sedePista, String nomePista) throws SQLException {
        String q = "select * from PISTE where sede_pista = ? and nome_pista = ?";
        if (pstSelezionaPista == null) {
            pstSelezionaPista = conn.prepareStatement(q);
        }
        pstSelezionaPista.setString(1, sedePista);
        pstSelezionaPista.setString(2, nomePista);
        return pstSelezionaPista.executeQuery();
    }

    /**
     * Controlla se l'anno campionato passato come parametro corrisponde a
     * quello del campionato attualmente in corso
     *
     * @param numeroCampionato
     * @return boolean
     */
    public static boolean isCurrent(int numeroCampionato) {
        int anno = numeroCampionato + 1950;
        int dataAnnoCorrente = LocalDate.now().getYear();
        return dataAnnoCorrente == anno;
    }

    /**
     * Seleziona le informazioni relative al personale della scuderia passata
     * come parametro, rispetto l'anno campionato
     *
     * @param numeroCampionato
     * @param nomeScuderia
     * @return ResultSet[] Restituisce un vettore di ResultSet dove in
     * ResultSet[0] sono contenute le informazioni relative al dirigente della
     * scuderia richiesta. In ResultSet[1] sono contenute le informazioni
     * relative ai membri del personale della scuderia richiesta.
     * @throws SQLException
     */
    public static ResultSet[] selezionaPersonale(int numeroCampionato, String nomeScuderia) throws SQLException {
        String qPersonale = "select * from personale where codice_personale in (select codice_personale from afferenza_personale where numero_campionato = ? and nome_scuderia = ?)";
        String qDirigente = "select * from personale where codice_personale in (select codice_personale from dirigenza where numero_campionato = ? and nome_scuderia = ?)";
        ResultSet rst[] = new ResultSet[2];
        PreparedStatement pstPersonale = conn.prepareStatement(qPersonale);
        PreparedStatement pstDirigente = conn.prepareStatement(qDirigente);
        pstPersonale.setInt(1, numeroCampionato);
        pstPersonale.setString(2, nomeScuderia);
        pstDirigente.setInt(1, numeroCampionato);
        pstDirigente.setString(2, nomeScuderia);
        rst[0] = pstDirigente.executeQuery();
        rst[1] = pstPersonale.executeQuery();
        return rst;
    }

    /**
     * Seleziona le informazioni riguardo la singola pista rispetto l' anno
     * campionato e il numero giornata passati come parametri
     *
     * @param numeroCampionato
     * @param numeroGiornata
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaPista(int numeroCampionato, int numeroGiornata) throws SQLException {
        String q = "select sede_pista,nome_pista from calendario where numero_campionato= ? and numero_giornata = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numeroCampionato);
        pst.setInt(2, numeroGiornata);
        return pst.executeQuery();
    }

    /**
     * Seleziona le informazioni della singola gara rispetto l'anno campionato e
     * il numero giornata passati come parametri
     *
     * @param numeroCampionato
     * @param numeroGiornata
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet selezionaRisultati(int numeroCampionato, int numeroGiornata) throws SQLException {
        String condizione = "where sede_pista=(select sede_pista from calendario where numero_campionato= ? and  numero_giornata = ?) and nome_pista= (select nome_pista from calendario where numero_campionato= ? and numero_giornata=?) order by punteggio desc, ritiro";
        if (isCurrent(numeroCampionato)) {
            String q = "select * from risultati_attuali " + condizione;
            if (pstRisultatiAttuali == null) {
                pstRisultatiAttuali = conn.prepareStatement(q);
            }
            pstRisultatiAttuali.setInt(1, numeroCampionato);
            pstRisultatiAttuali.setInt(2, numeroGiornata);
            pstRisultatiAttuali.setInt(3, numeroCampionato);
            pstRisultatiAttuali.setInt(4, numeroGiornata);
            return pstRisultatiAttuali.executeQuery();
        }
        String q = "select * from risultati_passati " + condizione;
        if (pstRisultatiPassati == null) {
            pstRisultatiPassati = conn.prepareStatement(q);
        }
        pstRisultatiPassati.setInt(1, numeroCampionato);
        pstRisultatiPassati.setInt(2, numeroGiornata);
        pstRisultatiPassati.setInt(3, numeroCampionato);
        pstRisultatiPassati.setInt(4, numeroGiornata);
        return pstRisultatiPassati.executeQuery();
    }

}
