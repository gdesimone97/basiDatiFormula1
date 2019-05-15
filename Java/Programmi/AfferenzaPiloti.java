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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Mariano Esposito
 */
public class AfferenzaPiloti {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into Afferenza Piloti "
                    + "(Codice_Pilota, Numero_Campionato, Nome_Scuderia) "
                    + "values (?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");

        while (sc.hasNext()) {
            pst.setString(1, sc.next());
            pst.setInt(3, sc.nextInt());
            pst.setString(2, sc.next());

            sc.nextLine();
            pst.executeUpdate();
        }
    }
}
