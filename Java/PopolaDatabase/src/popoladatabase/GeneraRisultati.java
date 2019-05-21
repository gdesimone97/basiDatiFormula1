/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popoladatabase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1997g
 */
public class GeneraRisultati {

    public static void main(String[] args) {
        String nome = "postgres";
        String pass = "abc123";
        Connection conn = null;

        //DATABASE
        String url = "jdbc:postgresql://localhost:5432/formula1";
        //******CONNESSIONE & STATEMENT
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, nome, pass);
            PreparedStatement pst = conn.prepareStatement("select sede_pista, nome_pista from calendario where numero_campionato = ? and numero_giornata = ?");

            String campionato;
            int num;
            int cont = 0;
            int punteggio;
            String tempoms;
            int inizio;
            String nomeFile = "risultati.txt";

            Scanner sc = new Scanner(System.in);
            sc.useDelimiter(":");

            System.out.print("Numero Campionato: ");
            campionato = sc.nextLine();
            pst.setInt(1, Integer.parseInt(campionato));
            System.out.print("Numero giornate da inserire per questo campionato: ");
            num = Integer.parseInt(sc.nextLine());
            System.out.print("Da quale giornata vuoi inserire? ");
            inizio = Integer.parseInt(sc.nextLine());

            BufferedWriter out = new BufferedWriter(new FileWriter(nomeFile, true));

            //PER OGNI PILOTA:
            while (cont < num) {
                pst.setInt(2, inizio + cont);
                for (int i = 0; i < 20; i++) {
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        out.write(rs.getString("sede_pista") + ":");
                        out.write(rs.getString("nome_pista") + ":");
                    }

                    System.out.print("Codice Pilota: ");
                    out.write(sc.nextLine() + ":");

                    out.write((campionato) + ":");

                    System.out.print("Punteggio: ");
                    punteggio = Integer.parseInt(sc.nextLine());
                    out.write(punteggio + ":");

                    System.out.print("Miglior tempo (mm:ss:lll): ");
                    tempoms = sc.nextLine();
                    out.write(tempoms + ":");

                    System.out.print("Tempo Qualifica (mm:ss:lll): ");
                    out.write(sc.nextLine() + ":");

                    if (punteggio == 0) {
                        System.out.print("Pilota ritirato [0/1]: ");
                        if (Integer.parseInt(sc.nextLine()) == 1) {
                            out.write("0:");
                        } else {
                            out.write("1:");
                        }
                    }
                    else out.write("0:");
                    
                    System.out.println("PILOTA INSERITO\n");
                    out.newLine();
                }
                System.out.println("GIORNATA INSERITA\n");
				System.out.println("Siam Lello Deghello le tue scarpine oh yeah\n");
                out.close();
                cont++;
            }

        } catch (IOException ex) {
            Logger.getLogger(GeneraRisultati.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GeneraRisultati.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GeneraRisultati.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
