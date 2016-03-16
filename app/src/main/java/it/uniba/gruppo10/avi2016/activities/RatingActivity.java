package it.uniba.gruppo10.avi2016.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.gc.materialdesign.views.ButtonFloat;
import com.rey.material.widget.EditText;
import com.telly.mrvector.MrVector;

import java.util.HashMap;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Evento;
import it.uniba.gruppo10.avi2016.entities.Voto;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * E' l'activity dedicata alla votazione di un evento.
 */
public class RatingActivity extends AppCompatActivity {

    private static final String DB_ADDRESS = "https://scorching-heat-6469.firebaseio.com/Events/";
    private Evento evento;
    private Firebase firebase;
    private ButtonFloat rate;
    private EditText comment;
    private TextView nomeEvento;
    private LinearLayout like;
    private LinearLayout neutral;
    private LinearLayout dislike;
    private TextView likeTextView;
    private TextView neutralTextView;
    private TextView dislikeTextView;
    private ImageView thumb_up;
    private ImageView thumb_up_down;
    private ImageView thumb_down;
    private Drawable drawable;
    private STATUS stato;
    private String giorno;
    private View.OnClickListener ratingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stato != STATUS.NOT_DEFINED) {
                salvaVotante();
                salvaVoto();
                finish();
            } else {
                Toast.makeText(RatingActivity.this, R.string.express_preference, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener dislikeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stato == STATUS.NOT_DEFINED) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down_azure);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.DISLIKE;
                return;
            } else if (stato == STATUS.LIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down_azure);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.DISLIKE;
            } else if (stato == STATUS.NEUTRAL) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down_azure);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.DISLIKE;
            } else if (stato == STATUS.DISLIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                rate.setEnabled(false);
                stato = STATUS.NOT_DEFINED;
            }
        }
    };
    private View.OnClickListener neutralListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stato == STATUS.NOT_DEFINED) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down_azure);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.NEUTRAL;
                return;
            } else if (stato == STATUS.LIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down_azure);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.NEUTRAL;
                return;
            } else if (stato == STATUS.NEUTRAL) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                rate.setEnabled(false);
                stato = STATUS.NOT_DEFINED;
                return;
            } else if (stato == STATUS.DISLIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down_azure);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.NEUTRAL;
            }
        }
    };
    private View.OnClickListener likeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stato == STATUS.NOT_DEFINED) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up_azure);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.LIKE;
                return;
            } else if (stato == STATUS.LIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                rate.setEnabled(false);
                stato = STATUS.NOT_DEFINED;
                return;
            } else if (stato == STATUS.NEUTRAL) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down);
                thumb_up_down.setImageDrawable(drawable);
                neutralTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up_azure);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.LIKE;
                return;
            } else if (stato == STATUS.DISLIKE) {
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_down);
                thumb_down.setImageDrawable(drawable);
                dislikeTextView.setTextColor(getResources().getColor(R.color.darker_gray));
                drawable = MrVector.inflate(getResources(), R.drawable.thumb_up_azure);
                thumb_up.setImageDrawable(drawable);
                likeTextView.setTextColor(getResources().getColor(R.color.azure));
                rate.setEnabled(true);
                stato = STATUS.LIKE;
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebase = new Firebase(DB_ADDRESS);
        Bundle data = getIntent().getExtras();
        giorno = data.getString("ID");
        evento = (Evento) data.getParcelable("evento");
        setTitle(getString(R.string.title_activity_rating) + " " + evento.getTipologia());

        prendiRisorse();

        nomeEvento.setText(evento.getNome());
        rate.setOnClickListener(ratingListener);

        like.setOnClickListener(likeListener);
        neutral.setOnClickListener(neutralListener);
        dislike.setOnClickListener(dislikeListener);


    }

    private void salvaVotante() {
        Firebase fb = firebase.child(giorno).child(evento.getId()).child("/votanti");
        Map<String, String> votanti = new HashMap<>();
        votanti.put("userid", IDUtente.getId());
        fb.push().setValue(votanti);
    }

    private void salvaVoto() {
        Firebase fb = firebase.child(giorno).child(evento.getId()).child("/voti");
        Voto v = new Voto(comment.getText().toString(), getPreference());
        fb.push().setValue(v);
    }

    private String getPreference() {
        switch (stato) {
            case LIKE:
                return "mi piace";
            case DISLIKE:
                return "non mi piace";
            case NEUTRAL:
                return "neutrale";
            default:
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rating, menu);
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

    private void prendiRisorse() {
        Drawable drawable;
        stato = STATUS.NOT_DEFINED;
        comment = (EditText) findViewById(R.id.commentEditText);
        rate = (ButtonFloat) findViewById(R.id.rateButton);
        drawable = MrVector.inflate(getResources(), R.drawable.check);
        rate.setDrawableIcon(drawable);

        rate.setEnabled(false);
        nomeEvento = (TextView) findViewById(R.id.nomeEventoTextView);
        like = (LinearLayout) findViewById(R.id.likeLayout);
        neutral = (LinearLayout) findViewById(R.id.neutralLayout);
        dislike = (LinearLayout) findViewById(R.id.dislikeLayout);
        likeTextView = (TextView) findViewById(R.id.likeTextView);
        neutralTextView = (TextView) findViewById(R.id.neutralTextView);
        dislikeTextView = (TextView) findViewById(R.id.dislikeTextView);
        thumb_up = (ImageView) findViewById(R.id.thumb_up);
        drawable = MrVector.inflate(getResources(), R.drawable.thumb_up);
        thumb_up.setImageDrawable(drawable);
        thumb_up_down = (ImageView) findViewById(R.id.thumbs_up_down);
        drawable = MrVector.inflate(getResources(), R.drawable.thumbs_up_down);
        thumb_up_down.setImageDrawable(drawable);
        thumb_down = (ImageView) findViewById(R.id.thumb_down);
        drawable = MrVector.inflate(getResources(), R.drawable.thumb_down);
        thumb_down.setImageDrawable(drawable);

    }

    private enum STATUS {
        NOT_DEFINED,
        LIKE,
        NEUTRAL,
        DISLIKE
    }
}
