package it.uniba.gruppo10.avi2016.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gc.materialdesign.widgets.SnackBar;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Activity per effettuare l'accesso e nel caso accedere alla registrazione.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "EMAIL", PASSWORD = "PASSWORD", CHECK = "CHECK";
    private Button loginButton;
    private EditText email, password;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.logintitle));

        if(getIntent().getBooleanExtra("NO_CONNECTION",false) == true) {
            SnackBar snackbar = new SnackBar(this, getString(R.string.no_connection), getString(R.string.ok), buttonSnackBar);
            snackbar.show();
        }

        Firebase.setAndroidContext(this);
        prendiRisorse();
        resetSharedPreferences();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllaDatiAccesso();
            }
        });
    }

    private void prendiRisorse() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingProgressDialog));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        loginButton = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.textfield_email_login);
        password = (EditText) findViewById(R.id.textfield_password);
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    private boolean isConnected() {
        boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void controllaDatiAccesso() {
        if(isConnected()) {
            Firebase db = new Firebase("https://scorching-heat-6469.firebaseio.com/");
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                progressDialog.show();
                db.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        final String id = authData.getUid();
                        Firebase user = new Firebase("https://scorching-heat-6469.firebaseio.com/Users/" + id);
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(EMAIL, email.getText().toString());
                                editor.putString(PASSWORD, password.getText().toString());
                                editor.putBoolean(CHECK, true);
                                editor.apply();

                                Toast.makeText(LoginActivity.this, getString(R.string.welcome) + " " + dataSnapshot.child("Nome").getValue(), Toast.LENGTH_SHORT).show();
                                IDUtente.setNome(dataSnapshot.child("Nome").getValue() + "");
                                IDUtente.setCognome(dataSnapshot.child("Cognome").getValue() + "");
                                IDUtente.setId(id);
                                IDUtente.setArrayListEventi((ArrayList<String>) dataSnapshot.child("eventi").getValue());
                                caricaGruppi(dataSnapshot.child("gruppi").getValue());
                                Intent intent = new Intent(LoginActivity.this, MenuPrincipaleActivity.class);
                                progressDialog.dismiss();
                                startActivityForResult(intent, 2);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        email.setError(getString(R.string.wrong_email));
                        password.setError(getString(R.string.wrong_password));
                    }
                });
            } else {
                if (email.getText().toString().isEmpty())
                    email.setError(getString(R.string.enter_email));
                else
                    email.clearError();
                if (password.getText().toString().isEmpty())
                    password.setError(getString(R.string.enter_password));
                else
                    password.clearError();
            }
        } else {
            SnackBar snackbar = new SnackBar(this, getString(R.string.no_connection), getString(R.string.ok), buttonSnackBar);
            snackbar.show();
        }
    }

    private void resetSharedPreferences() {
        email.setText(pref.getString(EMAIL, ""));
        password.setText("");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PASSWORD, "");
        editor.putBoolean(CHECK, false);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            } else if (resultCode == Activity.RESULT_OK) {
                resetSharedPreferences();
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signup) {
            if(isConnected()) {
                Intent iscriviti = new Intent(LoginActivity.this, RegistrazioneActivity.class);
                startActivity(iscriviti);
            } else {
                SnackBar snackbar = new SnackBar(this, getString(R.string.no_connection), getString(R.string.ok), buttonSnackBar);
                snackbar.show();
            }
        }
        return true;
    }

    private View.OnClickListener buttonSnackBar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
