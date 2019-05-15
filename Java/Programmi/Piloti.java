/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popoladatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author 1997g
 */
public class Piloti {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into piloti "
                    + "(Codice Pilota, Nome Pilota, Cognome Pilota, Nazionalità, Data Nascita, Titoli Vinti, Attivo, Data Ritiro) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");

        while (sc.hasNext()) {
            pst.setString(1, sc.next());
            pst.setString(2, sc.next());
            pst.setString(3, sc.next());
            pst.setString(4, sc.next());
            pst.setDate(5, Date.valueOf(sc.next()));
            pst.setInt(6, sc.nextInt());
            pst.setBoolean(7, (sc.nextInt() == 1 ? true : false));
            if (sc.nextInt() == 0) {
                pst.setNull(8, java.sql.Types.INTEGER);
            } else {
                pst.setInt(8, sc.nextInt());
            }

            sc.nextLine();
            pst.executeUpdate();
        }
    }
}
