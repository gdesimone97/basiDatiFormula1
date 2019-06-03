/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popoladatabase;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author 1997g
 */
public class Risultati_temp {

    private static PreparedStatement pst = null;
    private static PreparedStatement pst2 = null;
    private static PreparedStatement pst3 = null;
    private static PreparedStatement pst4 = null;
    

    public static void insert(Connection conn, Scanner sc) throws SQLException, FileNotFoundException, FineException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into Risultati_t "
                    + "(Sede_Pista, Nome_Pista, Codice_Pilota, Numero_Campionato, Punteggio, Miglior_Tempo, Tempo_Qualifica, Ritiro) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?);");
        }
        if (pst2 == null) {
            pst2 = conn.prepareStatement("insert into risultati_attuali select * from risultati_t");
        }
        if (pst3 == null) {
            pst3 = conn.prepareStatement("create temporary table if not exists Risultati_t ("
                    + "   SEDE_PISTA           VARCHAR(50)          not null,"
                    + "   NOME_PISTA           VARCHAR(50)          not null,"
                    + "   CODICE_PILOTA        CHAR(8)              not null,"
                    + "   NUMERO_CAMPIONATO    INT                  not null,"
                    + "   PUNTEGGIO            INT                  not null,"
                    + "   MIGLIOR_TEMPO        TempoGiro            null,"
                    + "   TEMPO_QUALIFICA      TempoGiro            null,"
                    + "   RITIRO               BOOL                 not null);");
        }
        if (pst4 == null) {
            pst4 = conn.prepareStatement("delete from risultati_t");

        }

        int minuti;
        int secondi;
        int millisecondi;
        int tempoms;
        int cont = 0;

        while (sc.hasNext() & cont < 20) {
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
            pst.setBoolean(8, (sc.nextInt() == 1 ? true : false));

            sc.nextLine();
            pst3.executeUpdate();
            pst.executeUpdate();

            cont++;
        }
        
        //trasferisco i risultati da Risultati_t a Risultati_attuali
        pst2.executeUpdate();
        
        //cancello tutte le righe di Risultati_t
        pst4.executeUpdate();
        conn.commit();
        //se il file ha raggiunto la Fine, restituisce eccezione
        if (cont == 0) {
            throw new FineException();
        }
    }


}
