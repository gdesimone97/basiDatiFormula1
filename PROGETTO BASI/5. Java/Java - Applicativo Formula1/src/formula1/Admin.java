/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Questa classe definisce metodi ed attributi per consentire all'utente admin
 * di effettuare operazioni di inserimento sulla base dati
 *
 * @author gruppo05
 */
public class Admin {

    private static final String USER_ADMIN = "amministratore";
    private static final String PASS_ADMIN = "abc123";
    private static final String URL = "jdbc:postgresql://localhost/formula1";
    private static Connection conn;

    private Admin() {
    }

    /**
     * Il metodo prende in input nome untente e password e controlla che
     * corrispondano al nome utente e password dell'utente admin presente nel
     * DBMS. In caso di errore lancia AdminLoginFailed In caso di successo
     * utilizza il costruttore privato e resituisce un oggetto Admin
     *
     * @param user
     * @param password
     * @return Admin
     * @throws SQLException Eventuali errori di connessione
     * @throws AdminLoginFailed Nome utente e password admin errati
     */
    public static Admin adminConnection(String user, String password) throws SQLException, AdminLoginFailed {
        if (user.compareTo(USER_ADMIN) == 0 && password.compareTo(PASS_ADMIN) == 0) {
            conn = DriverManager.getConnection(URL, user, password);
            Admin superUser = new Admin();
            return superUser;
        }
        throw new AdminLoginFailed();
    }

    private int aggiungiPilota(String codicePilota, String nomePilota, String cognomePilota, String nazionalita, Date date, int titoliVinti, boolean attivo, int dataRitiro) throws SQLException {
        String q = "insert into piloti values(?,?,?,?,?,?,?,?)ON CONFLICT DO NOTHING";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, codicePilota);
        pst.setString(2, nomePilota);
        pst.setString(3, cognomePilota);
        pst.setString(4, nazionalita);
        pst.setDate(5, date);
        pst.setInt(6, titoliVinti);
        pst.setBoolean(7, attivo);
        if (dataRitiro == 0) {
            pst.setNull(8, java.sql.Types.INTEGER);
        } else {
            pst.setInt(8, dataRitiro);
        }
        return pst.executeUpdate();
    }

    private int aggiungiScuderia(String nomeScuderia, String nazionalitaScuderia, int numeroCampionatiVinti) throws SQLException {
        String q = "insert into scuderie values(?,?,?)ON CONFLICT DO NOTHING";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, nomeScuderia);
        pst.setString(2, nazionalitaScuderia);
        pst.setInt(3, numeroCampionatiVinti);
        return pst.executeUpdate();
    }

    private int aggiungiPista(String sedePista, String nomePista, int lunghezza, int numeroCureve, int giro, int annoInaugurazione) throws SQLException {
        String q = "insert into piste values(?,?,?,?,?,?) ON CONFLICT DO NOTHING";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, sedePista);
        pst.setString(2, nomePista);
        pst.setInt(3, lunghezza);
        pst.setInt(4, numeroCureve);
        if (giro == 0) {
            pst.setNull(5, java.sql.Types.INTEGER);
        } else {
            pst.setInt(5, giro);
        }
        pst.setInt(6, annoInaugurazione);
        return pst.executeUpdate();
    }

    private int aggiungiPersonale(String codicePersonale, String nomePersonale, String cognomePersonale, String nazionalitaPersonale, Date date, String professione) throws SQLException {
        String q = "insert into personale values(?,?,?,?,?,?)ON CONFLICT DO NOTHING";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, codicePersonale);
        pst.setString(2, nomePersonale);
        pst.setString(3, cognomePersonale);
        pst.setString(4, nazionalitaPersonale);
        pst.setDate(5, date);
        pst.setString(6, professione);
        return pst.executeUpdate();
    }

    private int aggiungiCampionato(int numeroCampionato, Date dataInizio, Date dataFine, String motore, String gomme) throws SQLException {
        String q = "insert into campionati values(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numeroCampionato);
        pst.setDate(2, dataInizio);
        pst.setDate(3, dataFine);
        pst.setString(4, motore);
        pst.setString(5, gomme);
        return pst.executeUpdate();
    }

    /**
     * Metodo che permette di effuttuare il LogOut da parte dell'utente Admin La
     * connessione viene chiusa
     *
     * @param admin
     */
    public void logOut(Admin admin) throws SQLException {
        admin = null;
        this.closeAdminConnection();
    }

    /**
     * Questo metodo riceve una stringa formattata contenente il singolo
     * risulatato da cui estrarre le informazioni. L'acquisizione termina quando
     * sono stati raccolti i 20 risultati. Le informazioni vengono raccolte in
     * una tabella teporanea e successivamente trasferite al DBMS.
     *
     * @param str Stringa formattata in ingresso
     * @throws SQLException
     * @throws FileNotFoundException
     */
    public void inserisciRisultati(String[] str) throws SQLException, FileNotFoundException {
        try {
            int cont = 0;
            String qInsert_t = "insert into risultati_t values(?,?,?,?,?,?,?,?)";
            String qInsert = "insert into risultati_attuali select * from risultati_t";
            String qClear = "delete from risultati_t";

            PreparedStatement pst = conn.prepareStatement(qInsert_t);
            PreparedStatement pstTemporaryTable = conn.prepareStatement("create temporary table if not exists Risultati_t ("
                    + "   SEDE_PISTA           VARCHAR(50)          not null,"
                    + "   NOME_PISTA           VARCHAR(50)          not null,"
                    + "   CODICE_PILOTA        CHAR(8)              not null,"
                    + "   NUMERO_CAMPIONATO    INT                  not null,"
                    + "   PUNTEGGIO            INT                  not null,"
                    + "   MIGLIOR_TEMPO        TempoGiro            null,"
                    + "   TEMPO_QUALIFICA      TempoGiro            null,"
                    + "   RITIRO               BOOL                 not null);");

            PreparedStatement pstInsertAttuali = conn.prepareStatement(qInsert);
            PreparedStatement pstClear = conn.prepareStatement(qClear);

            pstTemporaryTable.executeUpdate();

            conn.setAutoCommit(false);
            while (cont < 20) {
                Scanner sc = new Scanner(str[cont]);
                if (!sc.hasNext()) {
                    break;
                }
                sc.useDelimiter(":");
                pst.setString(1, sc.next());
                pst.setString(2, sc.next());
                pst.setString(3, sc.next());
                pst.setInt(4, sc.nextInt());
                pst.setInt(5, sc.nextInt());
                int migliorTempo = TempoGiro.generaGiro(sc.nextInt(), sc.nextInt(), sc.nextInt());
                int tempoQualifica = TempoGiro.generaGiro(sc.nextInt(), sc.nextInt(), sc.nextInt());
                if (migliorTempo == 0) {
                    pst.setNull(6, java.sql.Types.INTEGER); //Miglior tempo
                } else {
                    pst.setInt(6, migliorTempo);
                }
                if (tempoQualifica == 0) {
                    pst.setNull(7, java.sql.Types.INTEGER); //Tempo Qualifica
                } else {
                    pst.setInt(7, tempoQualifica);
                }
                pst.setBoolean(8, (sc.nextInt() == 1 ? true : false));
                pst.executeUpdate();
                sc.nextLine();
                cont++;
            }
            pstInsertAttuali.executeUpdate();
            pstClear.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            conn.rollback();
            conn.setAutoCommit(true);
            ex.printStackTrace();
            throw new SQLException();
        }
    }

    private void aggiungiCalendario(String sedePista, String nomePista, int numeroCampionato, Date date, int numeroGiornata) throws SQLException {
        String q = "insert into calendario values(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, sedePista);
        pst.setString(2, nomePista);
        pst.setInt(3, numeroCampionato);
        pst.setDate(4, date);
        pst.setInt(5, numeroGiornata);
        pst.executeUpdate();
    }

    private int aggiungiAfferenzaPiloti(String codice, int numeroCampionato, String nomeScuderia) throws SQLException {
        String q = "insert into afferenza_piloti values(?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, codice);
        pst.setInt(2, numeroCampionato);
        pst.setString(3, nomeScuderia);
        return pst.executeUpdate();
    }

    private int aggiungiAfferenzaPersonale(String nomeScuderia, String codice, int numero) throws SQLException {
        String q = "insert into afferenza_personale values(?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, nomeScuderia);
        pst.setString(2, codice);
        pst.setInt(3, numero);
        return pst.executeUpdate();
    }

    private int aggiungiDirigente(int numeroCampionato, String codice, String nomeScuderia) throws SQLException {
        String q = "insert into dirigenza values(?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numeroCampionato);
        pst.setString(2, codice);
        pst.setString(3, nomeScuderia);
        return pst.executeUpdate();
    }

    /**
     * Questo metodo permette di inseriere un nuovo campionato specificando le
     * informazioni richieste dallo stesso.
     *
     * @param numeroCampionato
     * @param dataInizio
     * @param dataFine
     * @param motore
     * @param gomme
     * @param file
     * @throws FileNotFoundException
     * @throws SQLException
     */
    public void inserisci(int numeroCampionato, Date dataInizio, Date dataFine, String motore, String gomme, String... file) throws FileNotFoundException, SQLException {

        Scanner scPiste = new Scanner(new File(file[0]));
        Scanner scPiloti = new Scanner(new File(file[1]));
        Scanner scCalendario = new Scanner(new File(file[2]));
        Scanner scScuderie = new Scanner(new File(file[3]));
        Scanner scPersonale = new Scanner(new File(file[4]));
        Scanner scAfferenzaPiloti = new Scanner(new File(file[5]));
        Scanner scAfferenzaPersonale = new Scanner(new File(file[6]));
        Scanner scDirigenza = new Scanner(new File(file[7]));

        scPiste.useDelimiter(":");
        scPiloti.useDelimiter(":");
        scCalendario.useDelimiter(":");
        scScuderie.useDelimiter(":");
        scPersonale.useDelimiter(":");
        scAfferenzaPiloti.useDelimiter(":");
        scAfferenzaPersonale.useDelimiter(":");
        scDirigenza.useDelimiter(":");

        try {
            conn.setAutoCommit(false);

            aggiungiCampionato(numeroCampionato, dataInizio, dataFine, motore, gomme);

            while (scPiste.hasNext()) {
                aggiungiPista(scPiste.next(), scPiste.next(), scPiste.nextInt(), scPiste.nextInt(), scPiste.nextInt(), scPiste.nextInt());
                scPiste.nextLine();
            }

            while (scPiloti.hasNext()) {
                aggiungiPilota(scPiloti.next(), scPiloti.next(), scPiloti.next(), scPiloti.next(), Date.valueOf(scPiloti.next()), scPiloti.nextInt(), scPiloti.nextInt() == 1, scPiloti.nextInt());
                scPiloti.nextLine();
            }

            while (scCalendario.hasNext()) {
                aggiungiCalendario(scCalendario.next(), scCalendario.next(), scCalendario.nextInt(), Date.valueOf(scCalendario.next()), scCalendario.nextInt());
                scCalendario.nextLine();
            }

            while (scScuderie.hasNext()) {
                aggiungiScuderia(scScuderie.next(), scScuderie.next(), scScuderie.nextInt());
                scScuderie.nextLine();
            }

            while (scPersonale.hasNext()) {
                aggiungiPersonale(scPersonale.next(), scPersonale.next(), scPersonale.next(), scPersonale.next(), Date.valueOf(scPersonale.next()), scPersonale.next());
                scPersonale.nextLine();
            }

            while (scAfferenzaPiloti.hasNext()) {
                aggiungiAfferenzaPiloti(scAfferenzaPiloti.next(), scAfferenzaPiloti.nextInt(), scAfferenzaPiloti.next());
                scAfferenzaPiloti.nextLine();
            }

            while (scAfferenzaPersonale.hasNext()) {
                aggiungiAfferenzaPersonale(scAfferenzaPersonale.next(), scAfferenzaPersonale.next(), scAfferenzaPersonale.nextInt());
                scAfferenzaPersonale.nextLine();
            }

            while (scDirigenza.hasNext()) {
                aggiungiDirigente(scDirigenza.nextInt(), scDirigenza.next(), scDirigenza.next());
                scDirigenza.nextLine();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            conn.setAutoCommit(true);
            throw new SQLException();
        }

    }

    private void closeAdminConnection() throws SQLException {
        conn.close();
    }
}
