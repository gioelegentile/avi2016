package it.uniba.gruppo10.avi2016.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Adapter personalizzato per la valorizzazione della listview in {@link it.uniba.gruppo10.avi2016.fragments.MainFragment}:
 * Vengono recuperate dal database tutte le chat dell'utente, a meno che esse non siano state precedentemente
 * cancellate
 */
public class ChatAdapter extends BaseAdapter {
    private static final String ID = "Id";
    private final String FIREBASEURL = "https://scorching-heat-6469.firebaseio.com";

    private Firebase firebase, chatref;
    private Context mContext;
    private List<Utente> utenti;
    private List<Utente> chat;
    private Map<String, Object> datiUtenti;

    public ChatAdapter(Context c) {
        Firebase.setAndroidContext(c);
        firebase = new Firebase(FIREBASEURL).child("Users");
        chatref = new Firebase(FIREBASEURL).child("chat");
        utenti = new ArrayList<Utente>();
        chat = new ArrayList<Utente>();

        mContext = c;
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                utenti.clear();
                datiUtenti = (Map<String, Object>) snapshot.getValue();

                for (Map.Entry<String, Object> dati : datiUtenti.entrySet()) {
                    Map<String, Object> map = (Map<String, Object>) dati.getValue();
                    map.put(ID, dati.getKey());
                    Utente utente = new Utente(map);
                    if (!IDUtente.getId().equals(utente.getId())) {
                        utenti.add(utente);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chat.clear();
                datiUtenti = (Map<String, Object>) dataSnapshot.getValue();
                if (datiUtenti != null) {
                    for (Map.Entry<String, Object> dati : datiUtenti.entrySet()) {
                        for (Utente u : utenti) {
                            if (dati.getKey().equals(IDUtente.getId() + u.getId()))
                                chat.add(u);
                        }
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                notifyDataSetChanged();

            }
        });

    }

    public int getCount() {
        return chat.size();
    }

    public Object getItem(int position) {
        return chat.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView immagine, cancel;
        TextView utente;
        View v = null;
        final Utente e = (Utente) getItem(position);

        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.chat_adapter, null);
            immagine = (ImageView) v.findViewById(R.id.imageView);
            utente = (TextView) v.findViewById(R.id.utente_adapter);
            cancel = (ImageView) v.findViewById(R.id.imageViewInfo);
        } else {
            immagine = (ImageView) v;
            utente = (TextView) v;
            cancel = (ImageView) v;
        }

        cancel.setImageResource(R.drawable.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminaChat(e.getId());
            }
        });

        if (e.getFoto() == null) {
            immagine.setImageResource(R.mipmap.user);
        } else {
            byte[] image = new byte[0];
            image = Base64.decode(e.getFoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
            immagine.setImageBitmap(decodedByte);
        }

        utente.setText(e.getNome() + " " + e.getCognome());

        return v;
    }

    private void eliminaChat(String idUtente) {
        final Firebase ref = new Firebase(FIREBASEURL).child("chat").child(IDUtente.getId() + idUtente);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("ATTENZIONE");
        builder.setMessage("Vuoi davvero cancellare questa conversazione?");
        builder.setCancelable(false);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.removeValue();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

}
