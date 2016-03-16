package it.uniba.gruppo10.avi2016.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.activities.LoginActivity;
import it.uniba.gruppo10.avi2016.activities.MenuPrincipaleActivity;

/**
 * Activity usata per processare il login dell'utente in automatico, qualora abbia già effettuato
 * l'accesso per la prima volta, per evitare di far inserire nuovamente le credenziali di accesso.
 */
public class SplashScreen extends AppCompatActivity {

    private static final String EMAIL = "EMAIL", PASSWORD = "PASSWORD", CHECK = "CHECK";

    private SharedPreferences pref;
    private ProgressBarCircularIndeterminate progressBarCircularIndeterminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        if(isConnected()) {
            if (pref.getBoolean(CHECK, false)) {
                Firebase.setAndroidContext(this);
                progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
                checkAlreadyLoggedIn();
            } else {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            intent.putExtra("NO_CONNECTION",true);
            startActivity(intent);
            finish();
        }
    }

    private boolean isConnected() {
        boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void checkAlreadyLoggedIn() {
        Firebase db = new Firebase("https://scorching-heat-6469.firebaseio.com/");
        db.authWithPassword(pref.getString(EMAIL, ""), pref.getString(PASSWORD, ""), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                final String id = authData.getUid();
                Firebase user = new Firebase("https://scorching-heat-6469.firebaseio.com/Users/" + id);
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        IDUtente.setNome(dataSnapshot.child("Nome").getValue() + "");
                        IDUtente.setCognome(dataSnapshot.child("Cognome").getValue() + "");
                        IDUtente.setId(id);
                        IDUtente.setArrayListEventi((ArrayList<String>) dataSnapshot.child("eventi").getValue());
                        Intent intent = new Intent(SplashScreen.this, MenuPrincipaleActivity.class);
                        caricaGruppi(dataSnapshot.child("gruppi").getValue());

                        startActivityForResult(intent, 1);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void caricaGruppi(Object gruppi) {
        ArrayList<String>gruppiLis=new ArrayList<>();
        Map<String, String> dati = (Map<String, String>) gruppi;
        if (dati != null)

                for(String key :dati.keySet())
                gruppiLis.add(dati.get(key));


        IDUtente.setGruppi(gruppiLis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            } else if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
