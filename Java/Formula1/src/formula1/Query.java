/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author desio
 */
public class Query {

    private static final String URL = "jdbc:postgresql://localhost/prova";
    private static final String USER = "utente_generico";
    private static final String PASS = "password";
    private static Connection conn;

    private static PreparedStatement pstSelezionaPilotaAttuale = null;
    private static PreparedStatement pstSelezionaScuderiaAttuale = null;
    private static PreparedStatement pstSelezionaScuderiaPassata = null;
    private static PreparedStatement pstSelezionaPilota2 = null;
    private static PreparedStatement pstSelezionaGiornata = null;
    private static PreparedStatement pstSelezionaPista = null;
    private static PreparedStatement pstSelezionaPilotaPassato;

    public Query() {

    }

    public static void InitConnection() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASS);

    }

    public static ResultSet getClassificaPilotiAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_PILOTI_ATTUALE ";
        Statement stm = conn.createStatement();
        ResultSet rst = stm.executeQuery(q);
        return rst;
    }

    public static ResultSet getClassifichePilotiPassati(int annoCampionato) throws SQLException {
        String q = "select * from CLASSIFICHE_PILOTI_PASSATI where numero_campionato = ?";
        PreparedStatement pstPilotiPassati = conn.prepareStatement(q);
        pstPilotiPassati.setInt(1, annoCampionato);
        return pstPilotiPassati.executeQuery();
    }

    public static ResultSet getClassificaScuderieAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_COSTRUTTORI_ATTUALE";
        return conn.createStatement().executeQuery(q);
    }

    public static ResultSet getClassificheScuderiePassate(int annoCampionato) throws SQLException {
        String q = "select * from CLASSIFICHE_COSTRUTTORI_PASSATE where numero_campionato=?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, annoCampionato);
        return pst.executeQuery();
    }

    public static ResultSet getCalendario(int numCampionato) throws SQLException {
        String q = "select numero_giornata, sede_pista, nome_pista, data from CALENDARIO where numero_campionato = ? order by numero_giornata";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numCampionato);
        return pst.executeQuery();
    }

    public static ResultSet selezionaPilota(int x, int numeroCampionato) throws SQLException {
        if (isCurrent(numeroCampionato)) {
            String q = "select * from piloti where codice_pilota = "
                    + "(select codice_pilota from classifica_piloti_attuale  offset ? limit 1)";
            if (pstSelezionaPilotaAttuale == null) {
                pstSelezionaPilotaAttuale = conn.prepareStatement(q);
            }
            pstSelezionaPilotaAttuale.setInt(1, x);
            return pstSelezionaPilotaPassato.executeQuery();
        }
        
        String q = "select * from piloti where codice_pilota= (select codice_pilota from CLASSIFICHE_PILOTI_PASSATI where numero_campionato = ? offset ? limit 1)";
        if (pstSelezionaPilotaPassato == null) {
            pstSelezionaPilotaPassato = conn.prepareStatement(q);
        }

        pstSelezionaPilotaPassato.setInt(1, numeroCampionato);
        pstSelezionaPilotaPassato.setInt(2, x);
        return pstSelezionaPilotaPassato.executeQuery();
    }

    public static ResultSet selezionaPilota(String x, int annoCampionato) throws SQLException {
        String q = "select * from piloti where codice_pilota = ?";
        if (pstSelezionaPilota2 == null) {
            pstSelezionaPilota2 = conn.prepareStatement(q);
        }
        pstSelezionaPilota2.setString(1, x);
        return pstSelezionaPilota2.executeQuery();
    }

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

    public static ResultSet selezionaAfferenza(int x, int annoCampionato) throws SQLException {
        String q = "select * from afferenza_piloti ";
        if (x >= 0 && x <= 19) {
            q += "where codice_pilota = (select codice_pilota from CLASSIFICA_PILOTI_ATTUALE offset ? limit 1) and numero_campionato = ?";
        } else if (x >= 20 && x <= 29) {
            x = x - 20;
            q += "where nome_scuderia = (select nome_scuderia from CLASSIFICA_COSTRUTTORI_ATTUALE offset ? limit 1) and numero_campionato = ?";
        } else {
            System.out.println("Parametro errato nel metodo: \"selezionaAfferenza\" ");
        }
        PreparedStatement pstSelezionaAfferenza = conn.prepareStatement(q);
        pstSelezionaAfferenza.setInt(1, x);
        pstSelezionaAfferenza.setInt(2, annoCampionato);
        return pstSelezionaAfferenza.executeQuery();
    }

    public static ResultSet selezionaGiornata(int numeroCampionato, int x) throws SQLException {
        String q = "select * from CALENDARIO where numero_campionato = ? and numero_giornata = ?";
        if (pstSelezionaGiornata == null) {
            pstSelezionaGiornata = conn.prepareStatement(q);
        }
        pstSelezionaGiornata.setInt(1, numeroCampionato);
        pstSelezionaGiornata.setInt(2, x);
        return pstSelezionaGiornata.executeQuery();
    }

    public static ResultSet selezionaPista(String sedePista, String nomePista) throws SQLException {
        String q = "select * from PISTE where sede_pista = ? and nome_pista = ?";
        if (pstSelezionaPista == null) {
            pstSelezionaPista = conn.prepareStatement(q);
        }
        pstSelezionaPista.setString(1, sedePista);
        pstSelezionaPista.setString(2, nomePista);
        return pstSelezionaPista.executeQuery();
    }

    public static boolean isCurrent(int numeroCampionato) {
        int anno = numeroCampionato + 1950;
        int dataAnnoCorrente = LocalDate.now().getYear();
        return dataAnnoCorrente == anno;
    }

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

    public static ResultSet selezionaPista(int numeroCampionato, int numeroGiornata) throws SQLException {
        String q = "select sede_pista,nome_pista from calendario where numero_campionato= ? and numero_giornata = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numeroCampionato);
        pst.setInt(2, numeroGiornata);
        return pst.executeQuery();
    }
}
