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
public class Risultati_temp {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException, FineException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into risultati "
                    + "(numero_campionato_t, numero_giornata_t, Codice_Pilota_t, Punteggio_t, Miglior_Tempo_t, Tempo_Qualifica_t, Ritiro_t) "
                    + "values (?, ?, ?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");
        int minuti;
        int secondi;
        int millisecondi;
        int tempoms;
        int cont = 0;

        for (int i = 0; i < 20; i++) {
            while (sc.hasNext()) {
                pst.setInt(1, sc.nextInt());
                pst.setInt(2, sc.nextInt());
                pst.setString(3, sc.next());
                pst.setInt(4, sc.nextInt());
                minuti = sc.nextInt();
                secondi = sc.nextInt();
                millisecondi = sc.nextInt();
                tempoms = (((minuti * 60) + secondi) * 1000) + millisecondi;
                if (tempoms == 0) {
                    pst.setNull(5, java.sql.Types.INTEGER);
                } else {
                    pst.setInt(5, tempoms);
                }
                minuti = sc.nextInt();
                secondi = sc.nextInt();
                millisecondi = sc.nextInt();
                tempoms = (((minuti * 60) + secondi) * 1000) + millisecondi;
                if (tempoms == 0) {
                    pst.setNull(6, java.sql.Types.INTEGER);
                } else {
                    pst.setInt(6, tempoms);
                }
                pst.setBoolean(7, (sc.nextInt() == 1 ? true : false));

                sc.nextLine();
                pst.executeUpdate();
                cont ++;
            }
            if (cont == 0)
                throw new FineException();
        }
    }

}
