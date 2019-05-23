/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
     * @throws SQLException
     * @throws AdminLoginFailed 
     */
    public static Admin adminConnection(String user, String password) throws SQLException, AdminLoginFailed {
        if (user.compareTo(USER_ADMIN)==0 && pass.compareTo(PASS_ADMIN)==0) {
            conn=DriverManager.getConnection(URL, user, password);
            Admin superUser=new Admin(user, password);
            return superUser;
        }
        throw new AdminLoginFailed();
    }
}
