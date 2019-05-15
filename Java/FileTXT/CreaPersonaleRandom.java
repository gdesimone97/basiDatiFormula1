package popoladatabase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * QUESTA CLASSE CREA 4 FILE DI TESTO CONTENENTI OCCORRENZE DELL'ENTITà PERSONALE
 * IN MANIERA CASUALE, IN PARTICOLARE: dirigenti, ingegneri, meccanici, staff muretto
 */
/**
 *
 * @author 1997g
 */
public class CreaPersonaleRandom {

    private static final ArrayList<String> maleNames = new ArrayList();
    private static final ArrayList<String> femaleNames = new ArrayList();
    private static final ArrayList<String> surNames = new ArrayList();
    public static final ArrayList<String> nationalities = new ArrayList();

    static {
        nationalities.add("IT");
        nationalities.add("EN");
        nationalities.add("FR");
        nationalities.add("DE");
        nationalities.add("ES");
        nationalities.add("CH");
        nationalities.add("DA");
        nationalities.add("US");
        nationalities.add("CN");
        nationalities.add("JN");
        nationalities.add("KO");
        nationalities.add("CA");
        nationalities.add("EG");
        nationalities.add("DZ");
        nationalities.add("MA");
        nationalities.add("TN");
        nationalities.add("IR");
        nationalities.add("ID");
        nationalities.add("IN");
        nationalities.add("ZA");
        nationalities.add("PL");
        nationalities.add("GR");
        nationalities.add("PT");

        maleNames.add("Antonio");
        maleNames.add("Angelo");
        maleNames.add("Carmine");
        maleNames.add("Ettore");
        maleNames.add("Federico");
        maleNames.add("Francesco");
        maleNames.add("Giovanni");
        maleNames.add("Luigi");
        maleNames.add("Massimo");
        maleNames.add("Mario");
        maleNames.add("Nicola");
        maleNames.add("Paolo");
        maleNames.add("Pietro");
        maleNames.add("Roberto");
        maleNames.add("Sergio");
        maleNames.add("Simone");
        maleNames.add("Stefano");
        maleNames.add("Tiziano");
        maleNames.add("Vincenzo");
        maleNames.add("Vittorio");
        maleNames.add("Gennaro");
        maleNames.add("Mariano");
        maleNames.add("Giuseppe");
        maleNames.add("Alfonso");
        maleNames.add("Michele");
        maleNames.add("Valerio");
        maleNames.add("Enzo");
        maleNames.add("Marco");
        maleNames.add("Vito");
        maleNames.add("Salvatore");
        maleNames.add("Augusto");
        maleNames.add("Ciccio");

        femaleNames.add("Anna");
        femaleNames.add("Beatrice");
        femaleNames.add("Clelia");
        femaleNames.add("Filomena");
        femaleNames.add("Fiorella");
        femaleNames.add("Gianna");
        femaleNames.add("Loredana");
        femaleNames.add("Paola");
        femaleNames.add("Rossella");
        femaleNames.add("Rosa");
        femaleNames.add("Tiziana");
        femaleNames.add("Valentina");

        surNames.add("Barrasso");
        surNames.add("Battiato");
        surNames.add("Bennato");
        surNames.add("Bianchi");
        surNames.add("Caio");
        surNames.add("Canonico");
        surNames.add("Chianese");
        surNames.add("Cipriano");
        surNames.add("Coppola");
        surNames.add("d'Acierno");
        surNames.add("De Santo");
        surNames.add("Esposito");
        surNames.add("Faruolo");
        surNames.add("Galdi");
        surNames.add("Guccini");
        surNames.add("Marcelli");
        surNames.add("Moscato");
        surNames.add("Picariello");
        surNames.add("Paoli");
        surNames.add("Paolino");
        surNames.add("Sempronio");
        surNames.add("Smith");
        surNames.add("Wesson");
        surNames.add("Rossi");
        surNames.add("Russo");
        surNames.add("Giaquinto");
        surNames.add("Esposito");
        surNames.add("De Simone");
        surNames.add("De Masi");
        surNames.add("Faggiano");
        surNames.add("Di Marzio");
        surNames.add("Vietri");
        surNames.add("Cappella");
        surNames.add("Lionetti");
        surNames.add("Iandolo");
    }

    public static String getRandomMaleName(Random R) {
        return maleNames.get(R.nextInt(maleNames.size()));
    }

    public static String getRandomFemaleName(Random R) {
        return femaleNames.get(R.nextInt(femaleNames.size()));
    }

    public static String getRandomSurName(Random R) {
        return surNames.get(R.nextInt(surNames.size()));
    }

    public static String getRandomNationality(Random R) {
        return nationalities.get(R.nextInt(nationalities.size()));
    }

    public static GregorianCalendar getRandomDate(Random R, int fromYear, int toYear) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = fromYear + R.nextInt(toYear - fromYear + 1);
        gc.set(gc.YEAR, year);
        int dayOfYear = 1 + R.nextInt(gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        return gc;
    }

    public static String getCodice(String nome, String cognome, int day, int month, int year) {
        StringBuilder result = new StringBuilder();
        result.append(nome.substring(0, 1).toUpperCase());
        result.append(cognome.substring(0, 1).toUpperCase());
        
        if (day >= 10)
            result.append(day);
        else 
            result.append("0" + day);  
        
        if (month >= 10)
            result.append(month);
        else
            result.append("0" + month);
        
        result.append(year%1900);
        
        
        return result.toString();
    }

    public static void main(String args[]) {
        int x = 0;
        String name;
        String surName;
        String codice;
        GregorianCalendar birthDate;
        int day;
        int month;
        String nationality;
        
        Scanner sc = new Scanner(System.in);        
        
        //LEGGI LA PROFESSIONE
        System.out.print("Professione {dirigente | ingegnere | meccanico | staffmuretto}: ");
        String profession = sc.nextLine();
        
        System.out.print("Numero di occorrenze da generare: ");
        int num = Integer.parseInt(sc.nextLine());
        
        //MODIFICA IL NOME DEL FILE DA CREARE
        try (BufferedWriter w = new BufferedWriter(new FileWriter(profession + ".txt"))) {

            while (x < num) {
                Random R = new Random(Double.doubleToLongBits(Math.random()));

                //PERSONALE E' DONNA CON PROBABILITA' 1-0.8
                if ((R.nextFloat()*100)%1 > 0.8) {
                    name = getRandomFemaleName(R);
                } else {
                    name = getRandomMaleName(R);
                }

                surName = getRandomSurName(R);
                birthDate = getRandomDate(R, 1920, 1999);
                day = birthDate.get(Calendar.DAY_OF_MONTH);
                month = birthDate.get(Calendar.MONTH)+1;
                codice = getCodice(name, surName, day, month, birthDate.get(Calendar.YEAR));
                nationality = getRandomNationality(R);

                
                //SCRIVI SU FILE
                w.write(codice + ":" + name + ":" + surName + ":" + nationality + ":" + (day < 10 ? "0" + day : day) + "-" + (month < 10 ? "0" + month : month) + "-" + birthDate.get(Calendar.YEAR)+ ":" + profession + ":");
                w.newLine();

                x++;
                
            }
            System.out.println("FINE");
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
