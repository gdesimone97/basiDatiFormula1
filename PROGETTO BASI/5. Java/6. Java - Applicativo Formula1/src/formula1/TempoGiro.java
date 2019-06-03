/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

/**
 *
 * @author gruppo05
 */
public class TempoGiro {

    private TempoGiro() {
    }

    /**
     * Prende come parametri d' ingresso minuti,secondi, millisecondi e
     * restiuisce il valore espresso in millisecondi.
     *
     * @param minuti
     * @param secondi
     * @param mSecondi
     * @return Restituisce il valore temporale espresso in millisecondi
     */
    public static int generaGiro(int minuti, int secondi, int mSecondi) {
        return (minuti * 60 + secondi) * 1000 + mSecondi;
    }

}
