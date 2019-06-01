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
import java.sql.Statement;
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
        Scuderia s = new Scuderia(sc.next(), sc.next(), sc.nextInt());
        sc.nextLine();
        return s;
    }

    private static Personale leggiPersonale(Scanner sc) throws FineException {
        if (!sc.hasNext()) {
            throw new FineException();
        }
        Personale p = new Personale(sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), sc.next());
        sc.nextLine();
        return p;
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
        Connection conn = null;

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
                    + "(Nome_Scuderia, Nazionalita_Scuderia, Num_Campionati_Vinti) "
                    + "values (?, ?, ?)"
                    + "ON CONFLICT DO NOTHING;"); //ON CONFLICT EVITA CHE SI BLOCCHI IL PROGRAMMA QUANDO C'è UN CONFLITTO DI CHIAVI SU SCUDERIA
            PreparedStatement pstInserisciPersonale = conn.prepareStatement("insert into personale"
                    + "(codice_personale, nome_personale, cognome_personale, nazionalita_personale, data_nascita, professione)"
                    + "values (?, ?, ?, ?, ?, ?);");
            PreparedStatement pstInserisciDirigenza = conn.prepareStatement("insert into dirigenza"
                    + "(numero_campionato, codice_personale, nome_scuderia)"
                    + "values (?, ?, ?);");
            PreparedStatement pstInserisciAfferenzaPersonale = conn.prepareStatement("insert into afferenza_personale"
                    + "(numero_campionato, codice_personale, nome_scuderia)"
                    + "values (?, ?, ?);");

//******FILE & SCANNER
            Scanner scScuderie68 = new Scanner(new BufferedReader(new FileReader("scuderie2018.txt")));
            scScuderie68.useDelimiter(":");
            Scanner scScuderie69 = new Scanner(new BufferedReader(new FileReader("scuderie2019.txt")));
            scScuderie69.useDelimiter(":");
            Scanner scDirigente = new Scanner(new BufferedReader(new FileReader("dirigente.txt")));
            scDirigente.useDelimiter(":");
            Scanner scIngegnere = new Scanner(new BufferedReader(new FileReader("ingegnere.txt")));
            scIngegnere.useDelimiter(":");
            Scanner scMeccanico = new Scanner(new BufferedReader(new FileReader("meccanico.txt")));
            scMeccanico.useDelimiter(":");
            Scanner scStaffMuretto = new Scanner(new BufferedReader(new FileReader("staffmuretto.txt")));
            scStaffMuretto.useDelimiter(":");
            Scanner scRisultati2018 = new Scanner(new BufferedReader(new FileReader("risultati2018.txt")));
            scRisultati2018.useDelimiter(":");
            Scanner scRisultati2019 = new Scanner(new BufferedReader(new FileReader("risultati2019.txt")));
            scRisultati2019.useDelimiter(":");

//******SVUOTAMENTO DATABASE
            Statement deleteStm = conn.createStatement();
            deleteStm.executeUpdate("TRUNCATE piloti, scuderie, campionati, personale, piste, calendario, risultati_attuali, risultati_passati, "
                    + "afferenza_piloti, afferenza_personale, dirigenza CASCADE;");

            conn.setAutoCommit(false);

//******INSERIMENTO PISTE
            Piste.insert(conn, "piste 2019.txt");
            System.out.println("Piste Inserite");

//******INSERIMENTO PILOTI
            Piloti.insert(conn, "piloti 2019.txt");
            Piloti.insert(conn, "piloti 2018.txt");
            System.out.println("Piloti Inseriti");

//******INSERIMENTO CAMPIONATO 2018
            Campionati.insert(conn, "campionati2018.txt");
            System.out.println("Campionato 2018 Inserito");

//******INSERIMENTO CALENDARIO 2018
            Calendario.insert(conn, "calendario2018.txt");
            System.out.println("Calendario 2018 Inserito");

//******INSERIMENTO RISULTATI 2018
            int count = 0;
            while (count < 21) {
                Risultati_temp.insert(conn, scRisultati2018);
                count ++;
            }
            System.out.println("Risultati inseriti per campionato 2018");

            System.out.println();

//******INSERIMENTO CAMPIONATO 2019
            Campionati.insert(conn, "campionati2019.txt");
            System.out.println("Campionato 2019 Inserito");

//******INSERIMENTO CALENDARIO 2019
            Calendario.insert(conn, "calendario2019.txt");
            System.out.println("Calendario 2019 Inserito");


//******INSERIMENTO RISULTATI 2019
            count = 0;
            while (count < 6) {
                Risultati_temp.insert(conn, scRisultati2019);
                count ++;
            }
            System.out.println("Risultati inseriti per campionato 2019");
            
            System.out.println();

//******INSERIMENTO DI SCUDERIE E PERSONALE
            System.out.println("INSERIMENTO SCUDERIE E PERSONALE");
            int numCampionato = 68;

            while (numCampionato < 70) {
                //PER 10 SCUDERIE
                while (x < 10) {
                    Scuderia scuderia = null;
                    //LEGGO 1 SCUDERIA

                    if (numCampionato == 68) {
                        scuderia = leggiScuderia(scScuderie68);
                    } else if (numCampionato == 69) {
                        scuderia = leggiScuderia(scScuderie69);
                    }
                    inserisciScuderia(scuderia, pstInserisciScuderia);
                    System.out.println(x + " Inserita scuderia con nome: " + scuderia.getNome_Scuderia());
                    //Savepoint save1 = conn.setSavepoint();

                    if (conn.getAutoCommit()) {
                        conn.setAutoCommit(false);
                    }

                    //LEGGO 5 DIRIGENTI DAL FILE
                    while (y < 5) {
                        pers = leggiPersonale(scDirigente);
                        inserisciPersonale(pers, pstInserisciPersonale);
                        inserisciAfferenzaPersonale(pers, scuderia, numCampionato, pstInserisciAfferenzaPersonale);

                        //SOLO IL PRIMO DIRIGENTE E' AMMINISTRATORE
                        if (y == 0) {
                            inserisciDirigenza(pers, scuderia, numCampionato, pstInserisciDirigenza);
                        }
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

                    conn.commit();
                    System.out.println(x + " ok");
                    System.out.println();
                    x++;
                }

                //INSERISCO L'AFFERENZA PILOTI PER QUESTO CAMPIONATO
                AfferenzaPiloti.insert(conn, "afferenzapiloti" + numCampionato + ".txt");
                System.out.println("Ho inserito le afferenze piloti per le scuderie del campionato " + numCampionato);
                numCampionato++;
                x = 0;
            }

            conn.commit();

            conn.setAutoCommit(true);
            System.out.println("***>>>FINE<<<***");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PopolaFormula1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PopolaFormula1.class.getName()).log(Level.SEVERE, null, ex);
            conn.rollback();
        } catch (FileNotFoundException ex) {
            System.out.println("Manca un file!");
            ex.getMessage();
        } catch (FineException ex) {
            conn.rollback();
            System.out.println("ERRORE DI FILE!");
            System.out.println("Può dipendere da: \n1. file scuderie non contiene abbastanza scuderie\n2. file risultati non contiene tutti i risultati per la giornata");
            ex.getMessage();
        }
    }

}
