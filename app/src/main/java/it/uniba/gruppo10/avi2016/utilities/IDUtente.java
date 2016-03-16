package it.uniba.gruppo10.avi2016.utilities;

import java.util.ArrayList;

/**
 * Classe dedicata alla memorizzazione di tutte le informazioni relative all'utente che ha effettuato
 * l'accesso all'applicazione.
 */
public class IDUtente {

    private static String id = "";
    private static String nome = "";
    private static String cognome = "";
    private static ArrayList<String> eventi = new ArrayList<String>();
    private static ArrayList<String> gruppi=new ArrayList<>();

    public static ArrayList<String> getGruppi() {
        return gruppi;
    }

    public static void setGruppi(ArrayList<String> gruppi) {
        IDUtente.gruppi = gruppi;
    }

    public static String getCognome() {
        return cognome;
    }

    public static void setCognome(String cognome) {
        IDUtente.cognome = cognome;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        IDUtente.nome = nome;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String email) {
        IDUtente.id = email;
    }

    public static void addEvento(String evento) {
        eventi.add(evento);
    }

    public static void deleteEvento(String evento) {
        eventi.remove(evento);
    }

    public static ArrayList<String> getArrayEventi() {
        return eventi;
    }

    public static void setArrayListEventi(ArrayList<String> dati) {
        if (dati != null) {
            for (String str : dati) {
                eventi.add(str);
            }
        }
    }

}
