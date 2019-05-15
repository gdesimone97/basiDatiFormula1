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
public class Risultati {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into risultati "
                    + "(Sede_Pista, Nome_Pista, Codice_Pilota, Numero_Campionato, Punteggio, MIglior_Tempo, Tempo_Qualifica, Ritiro) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");
        int minuti;
        int secondi;
        int millisecondi;
        int tempoms;

        while (sc.hasNext()) {
            pst.setString(1, sc.next());
            pst.setString(2, sc.next());
            pst.setString(3, sc.next());
            pst.setInt(4, sc.nextInt());
            pst.setInt(5, sc.nextInt());
            minuti = sc.nextInt();
            secondi = sc.nextInt();
            millisecondi = sc.nextInt();
            tempoms = (((minuti * 60) + secondi) * 1000) + millisecondi;
            if (tempoms == 0) {
                pst.setNull(6, java.sql.Types.INTEGER);
            } else {
                pst.setInt(6, tempoms);
            }
            minuti = sc.nextInt();
            secondi = sc.nextInt();
            millisecondi = sc.nextInt();
            tempoms = (((minuti * 60) + secondi) * 1000) + millisecondi;
            if (tempoms == 0) {
                pst.setNull(7, java.sql.Types.INTEGER);
            } else {
                pst.setInt(7, tempoms);
            }
            pst.setInt(8, sc.nextInt());

            sc.nextLine();
            pst.executeUpdate();

        }
    }

}
