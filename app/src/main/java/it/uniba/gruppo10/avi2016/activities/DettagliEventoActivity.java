package it.uniba.gruppo10.avi2016.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.gc.materialdesign.views.ButtonFlat;
import com.telly.mrvector.MrVector;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Evento;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Activity dedicata a mostrare i dettagli dell'evento selezionato. Da quì è possibile aggiungere
 * l'evento tra i promemoria (calendario), o, se l'evento è terminato, votarlo aprendo l'activity
 * {@link it.uniba.gruppo10.avi2016.activities.RatingActivity}.
 */
public class DettagliEventoActivity extends AppCompatActivity {

    private static final String DB_ADDRESS = "https://scorching-heat-6469.firebaseio.com/Users/";
    private ButtonFlat button;
    private TextView orario, stanza, partecipanti, descrizione, textMinuti;
    private EditText minuti;
    private String giorno;
    private Evento evento;
    private Firebase firebase;
    private Drawable drawable;
    private boolean check;
    private ArrayList<String> eventi = new ArrayList<>();
    private View.OnClickListener pushToCalendar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isEventAlreayExisting()) {
                if (!minuti.getText().toString().equals("")) {
                    String data = getDate();
                    long start, end;
                    String[] orari = evento.getOrario().split("-");
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

                    if (pushAppointmentsToCalender(DettagliEventoActivity.this, evento.getNome(),
                            evento.getStanza(), 1, start, end, true)) {
                        button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.calendarButton_width);
                        button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.calendarButton_heigth);
                        button.requestLayout();
                        drawable = MrVector.inflate(getResources(), R.drawable.calendar_check);
                        button.setBackground(drawable);
                        minuti.setVisibility(View.INVISIBLE);
                        textMinuti.setVisibility(View.INVISIBLE);
                        minuti.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(minuti.getWindowToken(), 0);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Inserisci quanti minuti prima desideri avere il promemoria", Toast.LENGTH_SHORT).show();
                    minuti.setVisibility(View.VISIBLE);
                    textMinuti.setVisibility(View.VISIBLE);
                    minuti.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(minuti, InputMethodManager.SHOW_IMPLICIT);
                }
            } else {
                boolean done = false;
                for (int i = 0; i < eventi.size() && !done; i++) {
                    if (eventi.get(i).equals(evento.getId())) {
                        eventi.remove(i);
                        deleteAppointmentToCalendar("1", evento.getNome());
                        salvaDB(eventi);
                        done = true;
                    }
                }
                if (done) {
                    button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.calendarButton_width);
                    button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.calendarButton_heigth);
                    button.requestLayout();
                    drawable = MrVector.inflate(getResources(), R.drawable.calendar_plus);
                    button.setBackground(drawable);
                } else {
                    Toast.makeText(DettagliEventoActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_evento);

        prendiRisorse();

        Bundle data = getIntent().getExtras();
        Firebase.setAndroidContext(this);
        firebase = new Firebase(DB_ADDRESS);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        giorno = data.getString("ID");
        evento = (Evento) data.getParcelable("evento");
        setTitle(evento.getNome());
        eventi = IDUtente.getArrayEventi();
        orario.setText(evento.getOrario());
        stanza.setText(evento.getStanza());
        partecipanti.setText(evento.getPartecipanti().equals("-1") ? getString(R.string.unlimited_participation) : evento.getPartecipanti());
        descrizione.setText(evento.getDescrizione());


        if (!isEventFinished()) {
            if (isEventAlreayExisting()) {
                button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.calendarButton_width);
                button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.calendarButton_heigth);
                button.requestLayout();
                drawable = MrVector.inflate(getResources(), R.drawable.calendar_check);
                button.setBackground(drawable);
            }
            button.setOnClickListener(pushToCalendar);
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(DettagliEventoActivity.this, getString(R.string.add_to_calendar), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        } else {
            if (isRatable()) {
                check = isAlreadyRated();
                if (!check) {
                    button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.rateButton_width);
                    button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.rateButton_heigth);
                    button.requestLayout();
                    button.setBackgroundResource(android.R.color.transparent);
                    button.setBackgroundColor(getResources().getColor(R.color.azure));
                    button.setText(getString(R.string.rate_button));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DettagliEventoActivity.this, RatingActivity.class);
                            intent.putExtra("evento", evento);
                            intent.putExtra("ID", giorno);
                            startActivity(intent);
                            finish();
                        }
                    });
                    button.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(DettagliEventoActivity.this, getString(R.string.rate_event), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } else {
                    button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.rateButton_width);
                    button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.rateButton_heigth);
                    button.requestLayout();
                    button.setBackgroundResource(android.R.color.transparent);
                    button.setBackgroundColor(getResources().getColor(R.color.azure));
                    button.setText(getString(R.string.rated_button));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DettagliEventoActivity.this, getString(R.string.event_already_rated), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                button.setVisibility(View.INVISIBLE);
            }

        }

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

    private boolean isAlreadyRated() {

        for (String id : evento.getVotanti()) {
            if (id.equals(IDUtente.getId()))
                return true;
        }
        return false;
    }

    private boolean isRatable() {
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

    private boolean isEventFinished() {
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

    private boolean isEventAlreayExisting() {
        ArrayList<String> e = IDUtente.getArrayEventi();
        for (int i = 0; i < e.size(); i++)
            if (e.get(i).equals(evento.getId()))
                return true;
        return false;
    }

    private void salvaDB(ArrayList<String> a) {
        Firebase fb = firebase.child(IDUtente.getId()).child("/eventi");
        fb.setValue(a);
    }

    private void prendiRisorse() {
        check = false;
        button = (ButtonFlat) findViewById(R.id.imageViewCalendario);
        button.getLayoutParams().width = (int) getResources().getDimension(R.dimen.calendarButton_width);
        button.getLayoutParams().height = (int) getResources().getDimension(R.dimen.calendarButton_heigth);
        button.requestLayout();
        drawable = MrVector.inflate(getResources(), R.drawable.calendar_plus);
        button.setBackground(drawable);
        orario = (TextView) findViewById(R.id.textViewOrario);
        stanza = (TextView) findViewById(R.id.textViewStanza);
        partecipanti = (TextView) findViewById(R.id.textViewPartecipanti);
        descrizione = (TextView) findViewById(R.id.textViewDescrizione);
        minuti = (EditText) findViewById(R.id.editTextMinutiDettagli);
        textMinuti = (TextView) findViewById(R.id.textViewMinuti);
        minuti.setVisibility(View.INVISIBLE);
        textMinuti.setVisibility(View.INVISIBLE);
    }

    private void deleteAppointmentToCalendar(String id, String nome) {
        String eventUriString = "content://com.android.calendar/events";
        getApplicationContext().getContentResolver().delete(Uri.parse(eventUriString),
                "calendar_id=? and title=?", new String[]{id, nome});
    }

    private boolean pushAppointmentsToCalender(Activity curActivity, String title,
                                               String place, int status,
                                               long startDate, long endDate, boolean needReminder) {

        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();

            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            eventValues.put(CalendarContract.Events.TITLE, title);
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, place);
            eventValues.put(CalendarContract.Events.DTSTART, startDate);
            eventValues.put(CalendarContract.Events.DTEND, endDate);
            eventValues.put(CalendarContract.Events.ALL_DAY, 0);
            eventValues.put(CalendarContract.Events.EVENT_COLOR, getResources().getColor(R.color.azure));
            eventValues.put(CalendarContract.Events.STATUS, status);
            TimeZone tz = TimeZone.getDefault();
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
            eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

            Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
            eventi.add(evento.getId());
            salvaDB(eventi);
            long eventID = Long.parseLong(eventUri.getLastPathSegment());

            if (needReminder) {
                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("minutes", 15);
                reminderValues.put("event_id", eventID);
                reminderValues.put("method", 1);
                curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(DettagliEventoActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dettagli_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
