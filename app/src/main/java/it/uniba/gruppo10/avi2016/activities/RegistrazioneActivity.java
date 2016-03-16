package it.uniba.gruppo10.avi2016.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pkmmte.view.CircularImageView;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.telly.mrvector.MrVector;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.utilities.Mail;

/**
 * Activity dedicata alla registrazione al sistema.
 */
public class RegistrazioneActivity extends AppCompatActivity {

    private Button confirmButton;
    private EditText nome, cognome, email;
    private android.widget.EditText twitter, skype, phone;
    private ProgressDialog progressDialog;
    private ImageView twi, sky, pho, at;
    private CircularImageView imageView;
    private static final int SELECT_PICTURE = 1;
    private Bitmap bitmap;
    Random rdm = new Random();
    int cod;
    Mail send;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        prendiRisorse();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        cod = rdm.nextInt(99999);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvaDB();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                bitmap = data.getExtras().getParcelable("data");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void prendiRisorse() {
        progressDialog = new ProgressDialog(RegistrazioneActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        nome = (EditText) findViewById(R.id.textfield_name);
        cognome = (EditText) findViewById(R.id.textfield_surname);
        email = (EditText) findViewById(R.id.textfield_email);
        twitter = (android.widget.EditText) findViewById(R.id.editTextTwitter);
        skype = (android.widget.EditText) findViewById(R.id.editTextSkype);
        phone = (android.widget.EditText) findViewById(R.id.editTextPhone);
        twi = (ImageView) findViewById(R.id.imageViewTwitter);
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.twitter);
        twi.setImageDrawable(drawable);
        sky = (ImageView) findViewById(R.id.imageViewSkype);
        sky.setImageDrawable(drawable);
        pho = (ImageView) findViewById(R.id.imageViewPhone);
        drawable = MrVector.inflate(getResources(), R.drawable.phone);
        pho.setImageDrawable(drawable);
        at = (ImageView) findViewById(R.id.imageViewat);
        drawable = MrVector.inflate(getResources(), R.drawable.at);
        at.setImageDrawable(drawable);
        imageView = (CircularImageView) findViewById(R.id.imageViewRegistrazione);
        imageView.setImageResource(R.mipmap.user);
    }

    private void salvaDB() {
        Firebase db = new Firebase("https://scorching-heat-6469.firebaseio.com/");
        if (!nome.getText().toString().isEmpty()
                && !cognome.getText().toString().isEmpty()
                && !email.getText().toString().isEmpty()
                ) {

            progressDialog.show();

            mail = email.getText().toString();

            db.createUser(email.getText().toString(), cod + "", new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    creaUser((String) result.get("uid"));
                    new Task().execute();

                    progressDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrazioneActivity.this);
                    builder.setTitle(getString(R.string.registration));
                    builder.setMessage(getString(R.string.registration_message));
                    builder.setCancelable(false);

                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.show();

                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    email.setError(getString(R.string.wrong_email));
                    progressDialog.dismiss();
                }

            });
        } else {
            if (nome.getText().toString().isEmpty())
                nome.setError(getString(R.string.enter_name));
            else
                nome.clearError();
            if (cognome.getText().toString().isEmpty())
                cognome.setError(getString(R.string.enter_surname));
            else
                cognome.clearError();
            if (email.getText().toString().isEmpty())
                email.setError(getString(R.string.enter_email));
            else
                email.clearError();

        }
    }

    private void creaUser(String uid) {
        Firebase db = new Firebase("https://scorching-heat-6469.firebaseio.com/");
        Firebase refUser = db.child("Users");
        Map<String, String> users = new HashMap<>();
        users.put("Nome", nome.getText().toString());
        users.put("Cognome", cognome.getText().toString());
        users.put("Email", email.getText().toString());
        users.put("Twitter", twitter.getText().toString());
        users.put("Skype", skype.getText().toString());
        users.put("Phone", phone.getText().toString());

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            users.put("Foto", imageEncoded);
        } else {
            users.put("Foto", "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");
        }

        refUser.child(uid).setValue(users);
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                send = new Mail(cod + "");
                String[] emailA = {mail};
                send.setTo(emailA);

                send.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
