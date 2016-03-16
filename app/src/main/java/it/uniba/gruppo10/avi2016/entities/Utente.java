package it.uniba.gruppo10.avi2016.entities;

import java.util.ArrayList;
import java.util.Map;

/**
 * Entità utente
 */
public class Utente {

    /**
     * Nome
     */
    public static final String NOME = "Nome";
    /**
     * Cognome
     */
    public static final String COGNOME = "Cognome";
    /**
     * Corso
     */
    public static final String TWITTER = "Twitter";
    /**
     * Email
     */
    public static final String EMAIL = "Email";
    /**
     * Anno
     */
    public static final String SKYPE = "Skype";
    public static final String PHONE="Phone";
    /**
     * Id
     */
    public static final String ID = "Id";
    /**
     * Foto
     */
    public static final String FOTO = "Foto";
    /**
     * Eventi
     */
    public static final String EVENTI = "eventi";

    private String nome, cognome, email, id, foto;
    private String twitter="";
    private String skype="";
    private String phone="";

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    private Boolean check=false;
    private ArrayList<String> eventi;

    /**
     * Costruttore di un utente
     * @param dati  insieme dati utente
     */
    public Utente(Map<String, Object> dati) {
        setNome((String) dati.get(NOME));
        setCognome((String) dati.get(COGNOME));
        twitter=(String) dati.get(TWITTER);
        setEmail((String) dati.get(EMAIL));
        skype=(String) dati.get(SKYPE);
        phone=(String)dati.get(PHONE);
        setId((String) dati.get(ID));
        setFoto((String) dati.get(FOTO));
        setEventi((ArrayList<String>) dati.get(EVENTI));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTwitter() {
        if(twitter==null||twitter.equals("null"))
            return "";
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getSkype() {
        if(skype==null||skype.equals("null"))
            return "";
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getPhone() {
        if(phone==null||phone.equals("null"))
            return "";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Ritorna l'attributo specificato nel parametro formale
     * @param data  tipo attributo desiderato
     * @return      attributo
     */
    public String get(String data) {
        switch (data) {
            case NOME:
                return this.getNome();
            case COGNOME:
                return this.getCognome();
            case TWITTER:
                return this.twitter;
            case EMAIL:
                return this.getEmail();
            case SKYPE:
                return this.skype;
            case PHONE:
                return this.phone;
            case FOTO:
                return this.getFoto();
            case ID:
                return this.getId();
            default:
                return null;
        }
    }

    public ArrayList<String> getEventi() {
        return eventi;
    }

    public void setEventi(ArrayList<String> eventi) {
        this.eventi = eventi;
    }

}
