package it.uniba.gruppo10.avi2016.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gc.materialdesign.views.ButtonFlat;
import com.rey.material.widget.Button;
import com.telly.mrvector.MrVector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.activities.DettagliEventoActivity;
import it.uniba.gruppo10.avi2016.entities.Evento;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Adapter usato per la visualizzazione di tutti gli eventi cui un utente ha
 */
public class EventiUserAdapter extends RecyclerView.Adapter<EventiUserAdapter.CardViewHolder> {

    private List<Evento> eventi;
    private Map<String, Object> datiEventi;
    private ArrayList<String> idEventi;
    private Context context;
    private Firebase firebase;
    private ArrayList<String> eve;
    private ArrayList<String> primo = new ArrayList<>();
    private ArrayList<String> secondo = new ArrayList<>();
    private ArrayList<String> terzo = new ArrayList<>();
    private int lastPosition = -1;
    private String giorno = "Primo giorno";

    public EventiUserAdapter(final Context context) {

        this.context = context;
        Firebase.setAndroidContext(this.context);
        firebase = new Firebase("https://scorching-heat-6469.firebaseio.com/").child("Users").child(IDUtente.getId()).child("eventi");
        eve = new ArrayList<>();
        eventi = new ArrayList<Evento>();
        idEventi = IDUtente.getArrayEventi();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                eventi.clear();
                if (snapshot.getValue() != null)
                    eve = new ArrayList<String>((ArrayList) snapshot.getValue());
                else {
                    eve = new ArrayList<String>();


                }
                Firebase db1 = new Firebase("https://scorching-heat-6469.firebaseio.com/").child("Events").child("Primo giorno");
                Firebase db2 = new Firebase("https://scorching-heat-6469.firebaseio.com/").child("Events").child("Secondo giorno");
                Firebase db3 = new Firebase("https://scorching-heat-6469.firebaseio.com/").child("Events").child("Terzo giorno");


                db1.addValueEventListener(cercaEventi());
                db2.addValueEventListener(cercaEventi());
                db3.addValueEventListener(cercaEventi());

                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    private ValueEventListener cercaEventi() {
        ValueEventListener eventio = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datiEventi = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> evento : datiEventi.entrySet()) {
                    Map<String, String> dati = (Map<String, String>) evento.getValue();
                    if (eve.indexOf(evento.getKey()) != -1) {
                        dati.put("id", evento.getKey());
                        if (evento.getKey().charAt(0) == 'a')
                            primo.add(evento.getKey());
                        else if (evento.getKey().charAt(0) == 'b') {
                            secondo.add(evento.getKey());
                            giorno = "Secondo giorno";
                        } else {
                            terzo.add(evento.getKey());
                            giorno = "Terzo giorno";
                        }
                        Firebase votanti = new Firebase("https://scorching-heat-6469.firebaseio.com/Events/").child(giorno).child(evento.getKey()).child("votanti");
                        votanti.addValueEventListener(getVotanti(dati));

                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        return eventio;
    }

    private ValueEventListener getVotanti(final Map<String, String> dati2) {
        ValueEventListener vot = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> v = new ArrayList<>();
                Map<String, Object> dati = (Map<String, Object>) dataSnapshot.getValue();
                if (dati != null)
                    for (Map.Entry<String, Object> entry : dati.entrySet()) {
                        Map<String, String> prova = (Map<String, String>) entry.getValue();
                        v.add(prova.get("userid"));
                    }
                Evento evento0 = new Evento(dati2, v);
                eventi.add(evento0);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        return vot;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder cardViewHolder, int position) {
        cardViewHolder.evento = eventi.get(position);
        lastPosition = position;
        float d = context.getResources().getDisplayMetrics().density;

        if (lastPosition == getItemCount() - 1) {
            CardView.LayoutParams p = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) (16 * d), (int) (16 * d), (int) (16 * d), (int) (16 * d));
            cardViewHolder.cv.setLayoutParams(p);
        } else {
            CardView.LayoutParams p = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) (16 * d), (int) (16 * d), (int) (16 * d), 0);
            cardViewHolder.cv.setLayoutParams(p);
        }

        if (primo.contains(cardViewHolder.evento.getId()))
            cardViewHolder.orarioEvento.setText("Primo Giorno" + " - " + cardViewHolder.evento.getOrario());
        else if (secondo.contains(cardViewHolder.evento.getId()))
            cardViewHolder.orarioEvento.setText("Secondo Giorno" + " - " + cardViewHolder.evento.getOrario());
        else
            cardViewHolder.orarioEvento.setText("Terzo Giorno" + " - " + cardViewHolder.evento.getOrario());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (50 * d),(int) (50 * d));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.setMargins(0, (int) (12 * d), (int) (12 * d), 0);
        cardViewHolder.button.setLayoutParams(params);

        cardViewHolder.nomeEvento.setText(cardViewHolder.evento.getNome());
        cardViewHolder.tipologiaEvento.setText(cardViewHolder.evento.getTipologia());
        cardViewHolder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DettagliEventoActivity.class);
                intent.putExtra("evento", cardViewHolder.evento);
                intent.putExtra("ID", giorno);

                context.startActivity(intent);
            }
        });

        cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.calendarButton_width);
        cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.calendarButton_heigth);
        cardViewHolder.button.requestLayout();
        cardViewHolder.drawable = MrVector.inflate(context.getResources(), R.drawable.close);
        cardViewHolder.button.setBackground(cardViewHolder.drawable);

        cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = false;
                for (int i = 0; i < eventi.size() && !done; i++) {
                    if (idEventi.get(i).equals(cardViewHolder.evento.getId())) {
                        idEventi.remove(i);
                        deleteAppointmentToCalendar("1", cardViewHolder.evento.getNome());
                        salvaDB(idEventi);
                        done = true;
                    }
                }
            }
        });

    }

    private String getDate() {
        switch (giorno) {
            case "Primo giorno":
                return "02/06/2015";
            case "Secondo giorno":
                return "21/06/2015";
            case "Terzo giorno":
                return "22/06/2015";
            default:
        }
        return null;
    }

    private void salvaDB(ArrayList<String> a) {
        Firebase fb = new Firebase("https://scorching-heat-6469.firebaseio.com/Users/").child(IDUtente.getId()).child("/eventi");
        fb.setValue(a);
    }

    private void deleteAppointmentToCalendar(String id, String nome) {
        String eventUriString = "content://com.android.calendar/events";
        context.getContentResolver().delete(Uri.parse(eventUriString),
                "calendar_id=? and title=?", new String[]{id, nome});
    }

    @Override
    public int getItemCount() {
        return eventi.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_adapter, parent, false);
        CardViewHolder card = new CardViewHolder(v);
        return card;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nomeEvento;
        TextView orarioEvento;
        TextView tipologiaEvento;
        Button detailButton;
        Evento evento;
        ButtonFlat button;
        Drawable drawable;
        TextView textMinuti;
        EditText minuti;

        CardViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            nomeEvento = (TextView) itemView.findViewById(R.id.nome_evento);
            orarioEvento = (TextView) itemView.findViewById(R.id.orario_evento);
            tipologiaEvento = (TextView) itemView.findViewById(R.id.tipologia_evento);
            detailButton = (Button) itemView.findViewById(R.id.detailButton);
            button = (ButtonFlat) itemView.findViewById(R.id.imageViewCard);
            minuti = (EditText) itemView.findViewById(R.id.editTextMinuti);
            textMinuti = (TextView) itemView.findViewById(R.id.textViewMinuti);
            minuti.setVisibility(View.INVISIBLE);
            textMinuti.setVisibility(View.INVISIBLE);
        }
    }

}



