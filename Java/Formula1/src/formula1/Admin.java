/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author desio
 */
public class Admin {

    private static final String USER_ADMIN = "postgres";
    private static final String PASS_ADMIN = "abc123";
    private static final String URL = "jdbc:postgresql://localhost/prova";
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
     * @return
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

    //Per inserire la data usare il metodo statico della classe Date: Date.valueOf
    public int aggiungiPilota(String codicePilota, String nomePilota, String cognomePilota, String nazionalita, Date date, int titoliVinti, boolean attivo, int dataRitiro) throws SQLException {
        String q = "inser into piloti values(?,?;?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, codicePilota);
        pst.setString(2, nomePilota);
        pst.setString(3, cognomePilota);
        pst.setString(4, nazionalita);
        pst.setDate(5, date);
        pst.setInt(6, titoliVinti);
        pst.setBoolean(7, attivo);
        pst.setInt(8, dataRitiro);
        return pst.executeUpdate();
    }

    public int aggiungiScuderia(String nomeScuderia, String nazionalitaScuderia, int numeroCampionatiVinti) throws SQLException {
        String q = "insert into scuderie values(?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, nomeScuderia);
        pst.setString(2, nazionalitaScuderia);
        pst.setInt(3, numeroCampionatiVinti);
        return pst.executeUpdate();
    }

    // Per inserire tempoGiro usrare il metodo statico della classe Tempogiro: TempoGiro.generaGiro
    public int aggiungiPista(String sedePista, String nomePista, int lunghezza, int numeroCureve, TempoGiro giro, int annoInaugurazione) throws SQLException {
        String q = "insert into piste values(?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, sedePista);
        pst.setString(2, nomePista);
        pst.setInt(3, lunghezza);
        pst.setInt(4, numeroCureve);
        pst.setObject(5, giro); //controllare qui
        pst.setInt(6, annoInaugurazione);
        return pst.executeUpdate();
    }

    //Per inserire la data usare il metodo statico della classe Date: Date.valueOf
    public int aggiungiPersonale(String codicePersonale, String nomePersonale, String cognomePersonale, String nazionalitaPersonale, Date date, String professione) throws SQLException {
        String q = "insert into personale values(?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, codicePersonale);
        pst.setString(2, nomePersonale);
        pst.setString(3, cognomePersonale);
        pst.setString(4, nazionalitaPersonale);
        pst.setDate(5, date);
        pst.setString(6, professione);
        return pst.executeUpdate();
    }

    //Per inserire la data usare il metodo statico della classe Date: Date.valueOf
    public int aggiungiCampionato(int numeroCampionato, Date dataInizio, Date dataFine, String motore, String gomme) throws SQLException {
        String q = "insert into campionati values(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, numeroCampionato);
        pst.setDate(2, dataInizio);
        pst.setDate(3, dataFine);
        pst.setString(4, motore);
        pst.setString(5, gomme);
        return pst.executeUpdate();
    }

    public void logOut(Admin admin) {
        admin = null;
    }

    public void inserisciRisultati(String file) throws SQLException {
        try {
            int cont = 0;
            Scanner sc = new Scanner(new File(file));
            sc.useDelimiter(":");
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
            while (sc.hasNext() && cont < 20) {
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
                boolean attivo = sc.nextInt() == 0;
                pst.setBoolean(8, attivo);
                pst.executeUpdate();
                sc.nextLine();
                cont++;
            }
            System.out.println(cont);
            pstInsertAttuali.executeUpdate();
            pstClear.executeQuery();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception ex) {
            conn.rollback();
            conn.setAutoCommit(true);
            ex.printStackTrace();
            throw new SQLException();
        }
    }

    public void inserisciCalendario(String sedePista, String nomePista, int numeroCampionato, Date date, int numeroGiornata) throws SQLException {
        String q = "insert into calendario values(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, sedePista);
        pst.setString(2, nomePista);
        pst.setInt(3, numeroCampionato);
        pst.setDate(4, date);
        pst.setInt(5, numeroGiornata);
    }

}
