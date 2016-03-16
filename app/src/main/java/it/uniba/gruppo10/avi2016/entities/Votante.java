package it.uniba.gruppo10.avi2016.entities;

import java.util.Map;

/**
 * Entità votante
 */
public class Votante {

    private String id;

    public Votante(Map<String, String> map) {
       setId(map.get("userid"));
    }

    public Votante(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
