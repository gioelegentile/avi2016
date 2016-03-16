package it.uniba.gruppo10.avi2016.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gc.materialdesign.views.ButtonFlat;
import com.rey.material.widget.Button;
import com.telly.mrvector.MrVector;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.activities.DettagliEventoActivity;
import it.uniba.gruppo10.avi2016.activities.RatingActivity;
import it.uniba.gruppo10.avi2016.entities.Evento;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Adapter per la visualizzazione di tutti gli eventi disponibili: a seconda del giorno prescelto
 * nel NavigationDrawer({@link it.uniba.gruppo10.avi2016.activities.MenuPrincipaleActivity}) vengono
 * recuperati dal database tutti gli eventi in programma per quel giorno e visualizzati sotto forma
 * di "Card".
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CardViewHolder> {

    private List<Evento> eventi;
    private ArrayList<String> idEventi;
    private Map<String, Object> datiEventi;
    private String giorno;
    private Context context;
    private Firebase firebase;
    private int lastPosition = -1;

    public RecyclerViewAdapter(Context context, final String giorno) {
        this.giorno = giorno;
        this.context = context;
        Firebase.setAndroidContext(this.context);
        firebase = new Firebase("https://scorching-heat-6469.firebaseio.com/Events/" + this.giorno);

        eventi = new ArrayList<Evento>();
        idEventi = IDUtente.getArrayEventi();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                eventi.clear();
                datiEventi = (Map<String, Object>) snapshot.getValue();
                for (Map.Entry<String, Object> evento : datiEventi.entrySet()) {
                    final Map<String, String> dati = (Map<String, String>) evento.getValue();
                    dati.put("id", evento.getKey());
                    Firebase votanti = new Firebase("https://scorching-heat-6469.firebaseio.com/Events/").child(giorno).child(evento.getKey()).child("votanti");
                    votanti.addValueEventListener(getVotanti(dati));


                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

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
                Collections.sort(eventi);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        return vot;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_adapter, viewGroup, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder cardViewHolder, int i) {
        cardViewHolder.evento = eventi.get(i);
        lastPosition = i;
        if (lastPosition == getItemCount() - 1) {
            float d = context.getResources().getDisplayMetrics().density;
            CardView.LayoutParams p = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) (16 * d), (int) (16 * d), (int) (16 * d), (int) (16 * d));
            cardViewHolder.cv.setLayoutParams(p);
        } else {
            float d = context.getResources().getDisplayMetrics().density;
            CardView.LayoutParams p = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) (16 * d), (int) (16 * d), (int) (16 * d), 0);
            cardViewHolder.cv.setLayoutParams(p);
        }

        cardViewHolder.minuti.setVisibility(View.INVISIBLE);
        cardViewHolder.textMinuti.setVisibility(View.INVISIBLE);

        if (!isEventFinished(cardViewHolder.evento)) {
            if (isEventAlreayExisting(cardViewHolder.evento)) {
                cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.calendarButton_width);
                cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.calendarButton_heigth);
                cardViewHolder.button.requestLayout();
                cardViewHolder.drawable = MrVector.inflate(context.getResources(), R.drawable.calendar_check);
                cardViewHolder.button.setBackground(cardViewHolder.drawable);

            } else {
                cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.calendarButton_width);
                cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.calendarButton_heigth);
                cardViewHolder.button.requestLayout();
                cardViewHolder.drawable = MrVector.inflate(context.getResources(), R.drawable.calendar_plus);
                cardViewHolder.button.setBackground(cardViewHolder.drawable);
            }

            cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEventAlreayExisting(cardViewHolder.evento)) {
                        if (!cardViewHolder.minuti.getText().toString().equals("")) {
                            String data = getDate();
                            long start, end;
                            String[] orari = cardViewHolder.evento.getOrario().split("-");
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date dateStart = null, dateEnd = null;
                            try {
                                dateStart = dateFormat.parse(data + " " + orari[0]);
                                dateEnd = dateFormat.parse(data + " " + orari[1]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            start = dateStart.getTime();
                            end = dateEnd.getTime();

                            if (pushAppointmentsToCalender(cardViewHolder.evento, cardViewHolder.evento.getNome(),
                                    cardViewHolder.evento.getStanza(), 1, start, end, true,
                                    Integer.parseInt(cardViewHolder.minuti.getText().toString()))) {

                                cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.calendarButton_width);
                                cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.calendarButton_heigth);
                                cardViewHolder.button.requestLayout();
                                cardViewHolder.drawable = MrVector.inflate(context.getResources(), R.drawable.calendar_check);
                                cardViewHolder.button.setBackground(cardViewHolder.drawable);
                                cardViewHolder.minuti.setVisibility(View.INVISIBLE);
                                cardViewHolder.textMinuti.setVisibility(View.INVISIBLE);
                                cardViewHolder.minuti.setText("");
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(cardViewHolder.minuti.getWindowToken(), 0);

                            }
                        } else {
                            Toast.makeText(context, "Inserisci quanti minuti prima desideri avere il promemoria", Toast.LENGTH_SHORT).show();
                            cardViewHolder.minuti.setVisibility(View.VISIBLE);
                            cardViewHolder.textMinuti.setVisibility(View.VISIBLE);
                            cardViewHolder.minuti.requestFocus();
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(cardViewHolder.minuti, InputMethodManager.SHOW_IMPLICIT);
                        }

                    } else {
                        boolean done = false;
                        for (int i = 0; i < eventi.size() && !done; i++) {
                            if (idEventi.get(i).equals(cardViewHolder.evento.getId())) {
                                idEventi.remove(i);
                                deleteAppointmentToCalendar("1", cardViewHolder.evento.getNome());
                                salvaDB(idEventi);
                                done = true;
                            }
                        }
                        if (done) {
                            cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.calendarButton_width);
                            cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.calendarButton_heigth);
                            cardViewHolder.button.requestLayout();
                            cardViewHolder.drawable = MrVector.inflate(context.getResources(), R.drawable.calendar_plus);
                            cardViewHolder.button.setBackground(cardViewHolder.drawable);
                            cardViewHolder.minuti.setVisibility(View.VISIBLE);
                            cardViewHolder.textMinuti.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            cardViewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, context.getString(R.string.add_to_calendar), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        } else {
            if (isRatable(cardViewHolder.evento)) {
                cardViewHolder.check = isAlreadyRated(cardViewHolder.evento);
                if (!cardViewHolder.check) {
                    cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.rateButton_width);
                    cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.rateButton_heigth);
                    cardViewHolder.button.requestLayout();
                    cardViewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.azure));
                    cardViewHolder.button.setText(context.getString(R.string.rate_button));

                    cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RatingActivity.class);
                            intent.putExtra("evento", cardViewHolder.evento);
                            intent.putExtra("ID", giorno);
                            context.startActivity(intent);
                        }
                    });
                    cardViewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(context, context.getString(R.string.rate_event), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } else {
                    cardViewHolder.button.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.rateButton_width);
                    cardViewHolder.button.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.rateButton_heigth);
                    cardViewHolder.button.requestLayout();
                    cardViewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.azure));
                    cardViewHolder.button.setText(context.getString(R.string.rated_button));
                    cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, context.getString(R.string.event_already_rated), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                cardViewHolder.button.setVisibility(View.INVISIBLE);
            }
            cardViewHolder.minuti.setVisibility(View.INVISIBLE);
            cardViewHolder.textMinuti.setVisibility(View.INVISIBLE);

        }

        cardViewHolder.nomeEvento.setText(eventi.get(i).getNome());
        cardViewHolder.orarioEvento.setText(eventi.get(i).getOrario());
        cardViewHolder.tipologiaEvento.setText(eventi.get(i).getTipologia());
        cardViewHolder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DettagliEventoActivity.class);
                intent.putExtra("evento", cardViewHolder.evento);
                intent.putExtra("ID", giorno);
                context.startActivity(intent);
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

    private boolean isAlreadyRated(Evento evento) {

        for (String id : evento.getVotanti()) {
            if (id.equals(IDUtente.getId()))
                return true;
        }

        return false;
    }

    private boolean isRatable(Evento evento) {
        switch (evento.getTipologia()) {
            case "Break":
                return false;
            case "Lunch":
                return false;
            case "Trip":
                return false;
            case "Dinner":
                return false;
            default:
        }
        return true;
    }

    private boolean isEventFinished(Evento evento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String[] orari = evento.getOrario().split("-");
        String data = getDate();
        String dataCompleta = data + " " + orari[0] + ":00";

        try {
            Date oggi = sdf.parse(currentDateandTime);
            Date dataEvento = sdf.parse(dataCompleta);
            if ((dataEvento.getTime() < oggi.getTime())) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isEventAlreayExisting(Evento evento) {
        ArrayList<String> e = IDUtente.getArrayEventi();
        for (int i = 0; i < e.size(); i++)
            if (e.get(i).equals(evento.getId()))
                return true;
        return false;
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

    private boolean pushAppointmentsToCalender(Evento evento, String title,
                                               String place, int status,
                                               long startDate, long endDate, boolean needReminder,
                                               int minuti) {

        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();

            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            eventValues.put(CalendarContract.Events.TITLE, title);
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, place);
            eventValues.put(CalendarContract.Events.DTSTART, startDate);
            eventValues.put(CalendarContract.Events.DTEND, endDate);
            eventValues.put(CalendarContract.Events.ALL_DAY, 0);
            eventValues.put(CalendarContract.Events.EVENT_COLOR, context.getResources().getColor(R.color.azure));
            eventValues.put(CalendarContract.Events.STATUS, status);
            TimeZone tz = TimeZone.getDefault();
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
            eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

            Uri eventUri = context.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
            idEventi.add(evento.getId());
            salvaDB(idEventi);
            long eventID = Long.parseLong(eventUri.getLastPathSegment());

            if (needReminder && appInstalledOrNot("com.google.android.calendar")) {
                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("minutes", minuti);
                reminderValues.put("event_id", eventID);
                reminderValues.put("method", 1);
                context.getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public int getItemCount() {
        return eventi.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nomeEvento;
        TextView orarioEvento;
        TextView tipologiaEvento;
        ButtonFlat button;
        Button detailButton;
        Drawable drawable;
        boolean check;
        Evento evento;
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
        }
    }

}