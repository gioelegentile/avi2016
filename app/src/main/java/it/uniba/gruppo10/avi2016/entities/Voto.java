package it.uniba.gruppo10.avi2016.entities;

/**
 * Entità voto
 */
public class Voto {

    private String commento;
    private String preferenza;

    public Voto(String commento, String preferenza) {
        this.commento = commento;
        this.preferenza = preferenza;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public String getPreferenza() {
        return preferenza;
    }

    public void setPreferenza(String preferenza) {
        this.preferenza = preferenza;
    }
}
