package it.uniba.gruppo10.avi2016.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.ContattiSearchAdapter;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

public class SceltaContattiActivity extends AppCompatActivity {
    private final String FIREBASEURL = "https://scorching-heat-6469.firebaseio.com";
    EditText ricerca;
    ListView contatti;
    TextWatcher filter;
    ContattiSearchAdapter adapter;
    String name, foto;
    Firebase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);
        name = getIntent().getExtras().getString("nome");
        foto = getIntent().getExtras().getString("foto");
        creaFiltro();

        setContentView(R.layout.activity_scelta_contatti);
        ricerca = (EditText) findViewById(R.id.editTextRicerca);
        ricerca.addTextChangedListener(filter);
        contatti = (ListView) findViewById(R.id.listViewContatti);
        adapter = new ContattiSearchAdapter(this);
        contatti.setAdapter(adapter);

    }

    private void creaFiltro() {
        filter = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scelta_contatti, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fine) {
            setResult(Activity.RESULT_OK);
            ricerca.setText("");
            salvadb();
            finish();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvadb() {
        db=new Firebase(FIREBASEURL).child("chat").child(name).child("foto");
        db.setValue(foto);
        for (Utente u : ((ContattiSearchAdapter) contatti.getAdapter()).getAll()) {
            if (u.getCheck()) {
                db = new Firebase(FIREBASEURL).child("Users").child(u.getId()).child("gruppi");
                db.push().setValue(name);
            }
        }
        db = new Firebase(FIREBASEURL).child("Users").child(IDUtente.getId()).child("gruppi");
        db.push().setValue(name);
        ArrayList<String> temp = IDUtente.getGruppi();
        temp.add(name);
        IDUtente.setGruppi(temp);
    }
}
