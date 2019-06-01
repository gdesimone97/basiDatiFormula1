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
public class Campionati {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into campionati "
                    + "(Numero_Campionato, Data_Inizio, Data_Fine, Motore, Gomme) "
                    + "values (?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");

        while (sc.hasNext()) {
            pst.setInt(1, sc.nextInt());
            pst.setDate(2, Date.valueOf(sc.next()));
            pst.setDate(3, Date.valueOf(sc.next()));
            pst.setString(4, sc.next());
            pst.setString(5, sc.next());

            sc.nextLine();
            pst.executeUpdate();
        }
    }
}
