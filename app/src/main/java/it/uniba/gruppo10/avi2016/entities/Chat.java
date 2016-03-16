package it.uniba.gruppo10.avi2016.entities;

/**
 * Entità di chat
 */
public class Chat {
    private String message;
    private String author;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    /**
     * Crea un oggetto di tipo Chat contenente il messaggio e l'autore di esso
     * @param message   messaggio
     * @param author    autore messaggio
     */
    public Chat(String message, String author) {
        this.message = message;
        this.author = author;
    }

    /**
     * Restituisce il messaggio di testo
     * @return messaggio di testo
     */
    public String getMessage() {
        return message;
    }

    /**
     * Restituisce l'autore del messaggio
     * @return autore messaggio
     */
    public String getAuthor() {
        return author;
    }
}

