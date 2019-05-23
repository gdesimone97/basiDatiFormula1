/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author desio
 */
public class Admin {

    private static final String USER_ADMIN = "postgres";
    private static final String PASS_ADMIN = "abc123";
    private static String  user;
    private static String  pass;
    private static final String URL = "jdbc:postgresql://localhost/prova";
    private static Connection conn;
    
    private Admin(String user, String pass){
        this.user = user;
        this.pass = pass;
    }

    /**
     * Il metodo prende in input nome untente e password e controlla
     * che corrispondano al nome utente e password dell'utente admin presente nel DBMS.
     * In caso di errore lancia AdminLoginFailed
     * In caso di successo utilizza il costruttore privato e resituisce un oggetto Admin
     * @param user
     * @param password
     * @return
     * @throws SQLException Eventuali errori di connessione
     * @throws AdminLoginFailed Nome utente e password admin errati
     */
    public static Admin adminConnection(String user, String password) throws SQLException, AdminLoginFailed {
        if (user.compareTo(USER_ADMIN)==0 && pass.compareTo(PASS_ADMIN)==0) {
            conn=DriverManager.getConnection(URL, user, password);
            Admin superUser=new Admin(user, password);
            return superUser;
        }
        throw new AdminLoginFailed();
    }
    
    public int aggiungiPilota(String codicePilota,String nomePilota,String cognomePilota,String nazionalita,Date date,int titoliVinti,boolean  attivo,int dataRitiro) throws SQLException{
        String q="inser into piloti values(?,?;?,?,?,?,?,?)";
        PreparedStatement pst=conn.prepareStatement(q);
        pst.setString(1, codicePilota);
        pst.setString(2, nomePilota);
        pst.setString(3, cognomePilota);
        pst.setString(4, nazionalita);
        pst.setDate(5,date );
        pst.setInt(6, titoliVinti);
        pst.setBoolean(7, attivo);
        pst.setInt(8, dataRitiro);
        return pst.executeUpdate();
    }
    
    public int aggiungiScuderia(String nomeScuderia,String nazionalitaScuderia,int numeroCampionatiVinti) throws SQLException{
        String q="insert into scuderie values(?,?,?)";
        PreparedStatement pst=conn.prepareStatement(q);
        pst.setString(1, nomeScuderia);
        pst.setString(2, nazionalitaScuderia);
        pst.setInt(3, numeroCampionatiVinti);
        return pst.executeUpdate();
    }
    
    public int aggiungiPista(String sedePista,String nomePista,int lunghezza,int numeroCureve,TempoGiro giro,int annoInaugurazione) throws SQLException{
        String q="insert into piste values(?,?,?,?,?,?)";
        PreparedStatement pst=conn.prepareStatement(q);
        pst.setString(1, sedePista);
        pst.setString(2, nomePista);
        pst.setInt(3, lunghezza);
        pst.setInt(4, numeroCureve);
        pst.setObject(5,giro); //controllare qui
        pst.setInt(6, annoInaugurazione);
        return pst.executeUpdate();
    }
    
    public int aggingiPersonale(String codicePersonale,String nomePersonale,String cognomePersonale,String nazionalitaPersonale,Date date,String professione) throws SQLException{
        String q="insert into personale values(?,?,?,?,?,?)";
        PreparedStatement pst=conn.prepareStatement(q);
        pst.setString(1, codicePersonale);
        pst.setString(2, nomePersonale);
        pst.setString(3, cognomePersonale);
        pst.setString(4, nazionalitaPersonale);
        pst.setDate(5, date);
        pst.setString(6, professione);
        return pst.executeUpdate();
    }
    
}
