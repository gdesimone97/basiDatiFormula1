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
import java.sql.Savepoint;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1997g
 */
public class PopolaFormula1 {
    
    private static Scuderia leggiScuderia(Scanner sc) throws FineException {
        if (!sc.hasNext()) {
            throw new FineException();
        }
        return new Scuderia(sc.next(), sc.next(), sc.nextInt());
    }

    private static Personale leggiPersonale(Scanner sc) throws FineException {
        if (!sc.hasNext()) {
            throw new FineException();
        }
        return new Personale(sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), sc.next());
    }
    
    private static void inserisciScuderia(Scuderia sc, PreparedStatement pst) throws SQLException {
        pst.setString(1, sc.getNome_Scuderia());
        pst.setString(2, sc.getNazionalita_Scuderia());
        pst.setInt(3, sc.getNumero_Campionati_Vinti());
        pst.executeUpdate();
    }
    
    private static void inserisciPersonale(Personale p, PreparedStatement pst) throws SQLException {
        pst.setString(1, p.getCodice_personale());
        pst.setString(2, p.getNome_personale());
        pst.setString(3, p.getCognome_personale());
        pst.setString(4, p.getNazionalita());
        pst.setDate(5, Date.valueOf(p.getData()));
        pst.setString(6, p.getProfessione());
        pst.executeUpdate();
    }
    
    private static void inserisciDirigenza(Personale p, Scuderia sc, int numCampionato, PreparedStatement pst) throws SQLException {
        pst.setInt(1, numCampionato);
        pst.setString(2, p.getCodice_personale());
        pst.setString(3, sc.getNome_Scuderia());
        pst.executeUpdate();
    }
    
    private static void inserisciAfferenzaPersonale(Personale p, Scuderia sc, int numCampionato, PreparedStatement pst) throws SQLException {
        pst.setInt(1, numCampionato);
        pst.setString(2, p.getCodice_personale());
        pst.setString(3, sc.getNome_Scuderia());
        pst.executeUpdate();
    }

    public static void main(String[] args) throws SQLException {
        String nome = "postgres";
        String pass = "abc123";
        Connection conn=null; 
        
        
        Personale pers;

        int x = 0;
        int y = 0;

        //DATABASE
        String url = "jdbc:postgresql://localhost:5432/prova";
        try {
//******CONNESSIONE & STATEMENT
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, nome, pass);
            PreparedStatement pstInserisciScuderia = conn.prepareStatement("insert into scuderie "
                    + "(Nome_Scuderia, Nazionalita_Scuderia, Numero_Campionati_Vinti) "
                    + "values (?, ?, ?);");
            PreparedStatement pstInserisciPersonale = conn.prepareStatement("insert into personale"
                    + "(codice_personale, nome_personale, cognome_personale, nazionalita_personale, data_nascita, professione)"
                    + "values (?, ?, ?, ?, ?, ?);");
            PreparedStatement pstInserisciDirigenza = conn.prepareStatement("insert into dirigenza"
                    + "(numero_campionato, codice_personale, nome_scuderia)"
                    + "values (?, ?, ?);"); //MODIFICA QUI IL NUMERO_CAMPIONATO
            PreparedStatement pstInserisciAfferenzaPersonale = conn.prepareStatement("insert into afferenza_personale"
                    + "(numero_campionato, codice_personale, nome_scuderia)"
                    + "values (?, ?, ?);"); //MODIFICA QUI IL NUMERO_CAMPIONATO

//******FILE & SCANNER
            Scanner scScuderie = new Scanner(new BufferedReader(new FileReader("scuderie.txt")));
            scScuderie.useDelimiter(":");
            Scanner scDirigente = new Scanner(new BufferedReader(new FileReader("dirigente.txt")));
            scDirigente.useDelimiter(":");
            Scanner scIngegnere = new Scanner(new BufferedReader(new FileReader("ingegnere.txt")));
            scIngegnere.useDelimiter(":");
            Scanner scMeccanico = new Scanner(new BufferedReader(new FileReader("meccanico.txt")));
            scMeccanico.useDelimiter(":");
            Scanner scStaffMuretto = new Scanner(new BufferedReader(new FileReader("staffmuretto.txt")));
            scStaffMuretto.useDelimiter(":");
            
            
//******INSERIMENTO PISTE
        Piste.insert(conn, "piste.txt");
        System.out.println("Piste Inserite");
        
//******INSERIMENTO PILOTI
        Piloti.insert(conn, "piloti.txt");
        System.out.println("Piloti Inseriti");

//******INSERIMENTO CAMPIONATI
        Campionati.insert(conn, "campionati.txt");
        System.out.println("Campionati Inseriti");
        
//******INSERIMENTO CALENDARIO
        Calendario.insert(conn, "calendario.txt");
        System.out.println("Calendario Inserito");
        
//******INSERIMENTO RISULTATI
        Risultati.insert(conn, "risultati.txt");
        System.out.println("Risultati Inseriti");
        
//******INSERIMENTO AFFERENZA PILOTI
        AfferenzaPiloti.insert(conn, "afferenzapiloti.txt");
        System.out.println("Afferenze Piloti Inserite");

        
        System.out.println();
        System.out.println();
//******INSERIMENTO DI SCUDERIE E PERSONALE
            System.out.print("INSERISCI NUMERO DI CAMPIONATO (69 per il 2019): ");
            Scanner sc = new Scanner(System.in);
            int numCampionato = Integer.parseInt(sc.nextLine());
            
            //PER 20 SCUDERIE
            while (x < 20) {
                conn.setAutoCommit(false);

                //LEGGO 1 SCUDERIA
                Scuderia scuderia = leggiScuderia(scScuderie);
                inserisciScuderia(scuderia, pstInserisciScuderia);
                System.out.println(x + " Inserita scuderia con nome: " + scuderia.getNome_Scuderia());
                //Savepoint save1 = conn.setSavepoint();
                

                //LEGGO 5 DIRIGENTI DAL FILE
                while (y < 5) {
                    pers = leggiPersonale(scDirigente);
                    inserisciPersonale(pers, pstInserisciPersonale);
                    inserisciAfferenzaPersonale(pers, scuderia, numCampionato, pstInserisciAfferenzaPersonale);
                    
                    //SOLO IL PRIMO DIRIGENTE E' AMMINISTRATORE
                    if(y==0) 
                        inserisciDirigenza(pers, scuderia, numCampionato, pstInserisciDirigenza);
                    y++;
                }
                y = 0;
                System.out.println(x + " Ho inserito 5 dirigenti di cui (1 amministratore) per la scuderia in Aff.Pers. e Dirigenza");
                //Savepoint save2 = conn.setSavepoint();
                
                
                //LEGGO 15 INGEGNERI DAL FILE
                while (y < 15) {
                    pers = leggiPersonale(scIngegnere);
                    inserisciPersonale(pers, pstInserisciPersonale);
                    inserisciAfferenzaPersonale(pers, scuderia, numCampionato, pstInserisciAfferenzaPersonale);
                    y++;
                }
                y = 0;
                System.out.println(x + " Ho inserito 15 ingegneri per la scuderia in Aff.Pers.");
                //Savepoint save3 = conn.setSavepoint();
                
                
                //LEGGO 20 MECCANICI DAL FILE
                while (y < 20) {
                    pers = leggiPersonale(scMeccanico);
                    inserisciPersonale(pers, pstInserisciPersonale);
                    inserisciAfferenzaPersonale(pers, scuderia, numCampionato, pstInserisciAfferenzaPersonale);
                    y++;
                }
                y = 0;
                System.out.println(x + " Ho inserito 20 meccanici per la scuderia in Aff.Pers.");
                //Savepoint save4 = conn.setSavepoint();
                
                
                //LEGGO 10 STAFF MURETTO DAL FILE
                while (y < 10) {
                    pers = leggiPersonale(scStaffMuretto);
                    inserisciPersonale(pers, pstInserisciPersonale);
                    inserisciAfferenzaPersonale(pers, scuderia, numCampionato, pstInserisciAfferenzaPersonale);
                    y++;
                }
                y = 0;
                System.out.println(x + " Ho inserito 10 staff muretto per la scuderia in Aff.Pers.");
                //Savepoint save5 = conn.setSavepoint();
                
                
                x++;
                conn.commit();
                conn.setAutoCommit(true);
                System.out.println(x + " ok");
                System.out.println();
            }
            System.out.println(x + " FINE");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PopolaFormula1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PopolaFormula1.class.getName()).log(Level.SEVERE, null, ex);
            conn.rollback(); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PopolaFormula1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FineException ex) {
            System.out.println("IL FILE SCUDERIE NON CONTIENE ABBASTANZA SCUDERIE, MA QUELLE PRESENTI SONO STATE INSERITE TUTTE");
        }
    }

}
