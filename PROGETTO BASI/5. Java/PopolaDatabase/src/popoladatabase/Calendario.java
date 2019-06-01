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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Mariano Esposito
 */
public class Calendario {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into calendario "
                    + "(Sede_Pista, Nome_Pista, Numero_Campionato, Data, Numero_Giornata) "
                    + "values (?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");
        int cont = 0;

        while (sc.hasNext()) {
            pst.setString(1, sc.next());
            pst.setString(2, sc.next());
            pst.setInt(3, sc.nextInt());
            pst.setDate(4, Date.valueOf(sc.next()));
            pst.setInt(5, sc.nextInt());

            sc.nextLine();
            pst.executeUpdate();

        }
    }
}
