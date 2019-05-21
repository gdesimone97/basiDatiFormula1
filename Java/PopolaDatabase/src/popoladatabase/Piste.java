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
 * @author 1997g
 */
public class Piste {

    private static PreparedStatement pst = null;

    public static void insert(Connection conn, String nomeFile) throws SQLException, FileNotFoundException {
        if (pst == null) {
            pst = conn.prepareStatement("insert into piste "
                    + "(sede_pista, nome_pista, lunghezza, num_curve, giro_veloce, anno_inaugurazione) "
                    + "values (?, ?, ?, ?, ?, ?);");
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(nomeFile)));
        sc.useDelimiter(":");
        int x;
        
        while (sc.hasNext()) {
                pst.setString(1, sc.next());
                pst.setString(2, sc.next());
                pst.setInt(3, sc.nextInt());
                pst.setInt(4, sc.nextInt());
                x = sc.nextInt();
                if (x == 0) {
                    pst.setNull(5, java.sql.Types.INTEGER);
                } else {
                    pst.setInt(5, x);
                }
                pst.setInt(6, sc.nextInt());
                sc.nextLine();

                pst.executeUpdate();
            }

    }
}
