/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popoladatabase;

/**
 *
 * @author 1997g
 */
public class Scuderia {
    private String Nome_Scuderia;
    private String Nazionalita_Scuderia;
    private int Numero_Campionati_Vinti;

    public Scuderia(String Nome_Scuderia, String Nazionalita_Scuderia, int Numero_Campionati_Vinti) {
        this.Nome_Scuderia = Nome_Scuderia;
        this.Nazionalita_Scuderia = Nazionalita_Scuderia;
        this.Numero_Campionati_Vinti = Numero_Campionati_Vinti;
    }

    public String getNome_Scuderia() {
        return Nome_Scuderia;
    }

    public String getNazionalita_Scuderia() {
        return Nazionalita_Scuderia;
    }

    public int getNumero_Campionati_Vinti() {
        return Numero_Campionati_Vinti;
    }
    
    
}
