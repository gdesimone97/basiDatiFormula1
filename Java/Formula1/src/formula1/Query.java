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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author desio
 */
public class Query {

    private String query;
    private static final String url = "jdbc:postgresql://localhost/prova";
    private static final String user = "utente_generico";
    private static final String pass = "password";
    private static Connection conn;

    private static PreparedStatement pstSelezionaPilota;

    public Query() {

    }

    static {
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet getClassificaPilotiAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_PILOTI_ATTUALE ";
        Statement stm = conn.createStatement();
        return stm.executeQuery(q);
    }

    public static ResultSet getClassifichePilotiPassati() throws SQLException {
        String q = "select * from CLASSIFICHE_PILOTI_PASSATI";
        return conn.createStatement().executeQuery(q);
    }

    public static ResultSet getClassificaScuderieAttuale() throws SQLException {
        String q = "select * from CLASSIFICA_COSTRUTTORI_ATTUALE";
        return conn.createStatement().executeQuery(q);
    }

    public static ResultSet getClassificheScuderiePassate() throws SQLException {
        String q = "select * from CLASSIFICHE_COSTRUTTORI_PASSATE";
        return conn.createStatement().executeQuery(q);
    }

    public static ResultSet selezionaPilota(int x) throws SQLException {
        String q = "select * from piloti where codice_pilota = "
                + "(select codice_pilota from classifica_piloti_attuale offset ? limit 1)";
        if (pstSelezionaPilota == null) {
            pstSelezionaPilota=conn.prepareStatement(q);
        }
        pstSelezionaPilota.setInt(1, x);
        return pstSelezionaPilota.executeQuery();
    }
        public static ResultSet selezionaScuderia(int x) throws SQLException {
        String q = "select * from scuderie where codice_scuderia = "
                + "(select codice_scuderia from classifica_costruttori_attuali offset ? limit 1)";
        if (pstSelezionaPilota == null) {
            pstSelezionaPilota=conn.prepareStatement(q);
        }
        pstSelezionaPilota.setInt(1, x);
        return pstSelezionaPilota.executeQuery();
    }
    
}
