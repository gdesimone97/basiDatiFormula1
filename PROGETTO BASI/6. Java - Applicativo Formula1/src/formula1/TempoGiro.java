/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

/**
 *
 * @author desio
 */
public class TempoGiro {

    private TempoGiro() {
    }

    public static int generaGiro(int minuti, int secondi, int mSecondi) {
        return (minuti * 60 + secondi) * 1000 + mSecondi;
    }

}
