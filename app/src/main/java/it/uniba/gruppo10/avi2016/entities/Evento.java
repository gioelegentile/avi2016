package it.uniba.gruppo10.avi2016.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Entità evento
 */
public class Evento implements Parcelable, Comparable<Evento> {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
    private String nome;
    private String orario;
    private String stanza;
    private String descrizione;
    private String partecipanti;
    private String tipologia;
    private String id;
    private ArrayList<String> votanti;

    /**
     * Costruttore dell'evento.
     * @param map       insieme dei dati
     * @param votanti   insieme dei votanti
     */
    public Evento(Map<String, String> map, ArrayList<String> votanti) {
        setNome(String.valueOf(map.get("nome")));
        setPartecipanti(String.valueOf(map.get("partecipanti")));
        setOrario(String.valueOf(map.get("orario")));
        setStanza(String.valueOf(map.get("stanza")));
        setTipologia(String.valueOf(map.get("tipologia")));
        setDescrizione(String.valueOf(map.get("descrizione")));
        setId(map.get("id"));
        this.votanti = votanti;
    }

    public Evento(Parcel in) {
        this.nome = in.readString();
        this.orario = in.readString();
        this.stanza = in.readString();
        this.descrizione = in.readString();
        this.partecipanti = in.readString();
        this.tipologia = in.readString();
        this.id = in.readString();
        this.votanti = in.createStringArrayList();
    }

    public ArrayList<String> getVotanti() {
        return votanti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public String getStanza() {
        return stanza;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(String partecipanti) {
        if (partecipanti.equals("null"))
            partecipanti = "25";
        this.partecipanti = partecipanti;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeString(orario);
        parcel.writeString(stanza);
        parcel.writeString(descrizione);
        parcel.writeString(partecipanti);
        parcel.writeString(tipologia);
        parcel.writeString(id);
        parcel.writeStringList(votanti);
    }

    @Override
    public int compareTo(Evento evento) {
        if (this.id.compareTo(evento.getId()) > 0)
            return 1;
        else
            return -1;
    }
}
