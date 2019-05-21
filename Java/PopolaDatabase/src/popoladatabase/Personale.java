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
class Personale {
    private String codice_personale;
    private String nome_personale;
    private String cognome_personale;
    private String nazionalita;
    private String data;
    private String professione;

    public Personale(String codice_personale, String nome_personale, String cognome_personale, String nazionalita, String data, String professione) {
        this.codice_personale = codice_personale;
        this.nome_personale = nome_personale;
        this.cognome_personale = cognome_personale;
        this.nazionalita = nazionalita;
        this.data = data;
        this.professione = professione;
    }

    public String getCodice_personale() {
        return codice_personale;
    }

    public String getNome_personale() {
        return nome_personale;
    }

    public String getCognome_personale() {
        return cognome_personale;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public String getData() {
        return data;
    }

    public String getProfessione() {
        return professione;
    }
    
    
}
