package it.uniba.gruppo10.avi2016.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFloat;
import com.rey.material.widget.EditText;
import com.telly.mrvector.MrVector;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;


/**
 * Fragment contenente le informazioni personali. Esse possono essere modificate
 */
public class InfoPersonaliFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;
    private Firebase firebase;
    private EditText nome, cognome;
           private android.widget.EditText twitter,skype,phone;
    private ImageView imageView,twi,sky,pho,at;

    private Bitmap bitmap;
    private Utente utente;
    private ButtonFloat button;
    private String imageEncoded;
    private ProgressDialog progressDialog;
    private String ID ;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_info_personali, container, false);

        ID = getArguments().getString("ID");
        firebase = new Firebase("https://scorching-heat-6469.firebaseio.com/Users");
        prendiRisorse(v);

        progressDialog.show();

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                Map<String, Object> dati = (Map<String, Object>) map.get(IDUtente.getId());
                utente = new Utente(dati);

                nome.setText(utente.getNome());
                cognome.setText(utente.getCognome());
                twitter.setText(utente.getTwitter());
                skype.setText(utente.getSkype());
                phone.setText(utente.getPhone());
                if (utente.getFoto() == null)
                    imageView.setImageResource(R.mipmap.user);
                else {
                    byte[] image = new byte[0];
                    image = Base64.decode((String) dati.get(Utente.FOTO), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imageView.setImageBitmap(decodedByte);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conferma();
            }
        });

        nome.addTextChangedListener(new TW(Utente.NOME));
        cognome.addTextChangedListener(new TW(Utente.COGNOME));
        twitter.addTextChangedListener(new TW(Utente.TWITTER));
        skype.addTextChangedListener(new TW(Utente.SKYPE));
        phone.addTextChangedListener(new TW(Utente.PHONE));


        return v;
    }

    private void conferma() {
        Firebase db = firebase.child(IDUtente.getId());
        Map<String, Object> nuoviDati = new HashMap<String, Object>();

        if (bitmap != null) {
            nuoviDati.put(Utente.FOTO, imageEncoded);
        } else {
            nuoviDati.put(Utente.FOTO, utente.getFoto());
        }

        nuoviDati.put(Utente.NOME, nome.getText().toString());
        nuoviDati.put(Utente.COGNOME, cognome.getText().toString());
        nuoviDati.put(Utente.EMAIL, utente.getEmail());
        nuoviDati.put(Utente.EVENTI, utente.getEventi());
        nuoviDati.put(Utente.TWITTER,twitter.getText().toString());
        nuoviDati.put(Utente.SKYPE,skype.getText().toString());
        nuoviDati.put(Utente.PHONE, phone.getText().toString());

        db.setValue(nuoviDati);
        Toast.makeText(getActivity(), v.getResources().getString(R.string.data_modified), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Activity) getActivity()).getActionBar().setSubtitle(ID);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                bitmap = data.getExtras().getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                imageView.setImageBitmap(bitmap);
                if (!imageEncoded.equals(utente.getFoto())) {
                    button.setEnabled(true);
                }
            }
        }
    }

    private void prendiRisorse(View v) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        imageView = (ImageView) v.findViewById(R.id.imageViewInfo);
        nome = (EditText) v.findViewById(R.id.textfield_name_edit);
        cognome = (EditText) v.findViewById(R.id.textfield_surname_edit);
        button = (ButtonFloat) v.findViewById(R.id.buttonFloatInfoPersonali);
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.check);
        button.setDrawableIcon(drawable);
        button.setEnabled(false);

        twitter = (android.widget.EditText)v. findViewById(R.id.editTextTwitter);
        skype = (android.widget.EditText)v. findViewById(R.id.editTextSkype);
        phone= (android.widget.EditText) v.findViewById(R.id.editTextPhone);
        twi=(ImageView)v.findViewById(R.id.imageViewTwitter);
        Drawable drawable1= MrVector.inflate(getResources(), R.drawable.twitter);
        twi.setImageDrawable(drawable1);
        sky=(ImageView)v.findViewById(R.id.imageViewSkype);
        sky.setImageDrawable(drawable1);
        pho=(ImageView)v.findViewById(R.id.imageViewPhone);
        drawable1= MrVector.inflate(getResources(), R.drawable.phone);
        pho.setImageDrawable(drawable1);
        at=(ImageView)v.findViewById(R.id.imageViewat);
        drawable1= MrVector.inflate(getResources(), R.drawable.at);
        at.setImageDrawable(drawable1);
    }

    private class TW implements TextWatcher {

        private String str;

        public TW(String str) {
            this.str = str;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().equals(utente.get(str))) {
                button.setEnabled(true);
            }
        }

    }

}
