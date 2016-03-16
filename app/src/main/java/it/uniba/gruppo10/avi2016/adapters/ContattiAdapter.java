package it.uniba.gruppo10.avi2016.adapters;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.telly.mrvector.MrVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Adapter personalizzato, adibito al recupero dal database di tutti gli utenti iscritti al sistema
 * con i quali è possibile iniziare una nuova chat. Ciascun contatto viene visualizzato con
 * la foto profilo (nel caso in cui non ne abbia inserita una, viene caricata quella di default)
 * il nome del contatto e un imageButton per visualizzare le restanti informazioni relative al contatto
 * stesso tramite un AlertDialog.
 */
public class ContattiAdapter extends BaseAdapter {

    private static final String ID = "Id";
    private Firebase firebase;
    private Context mContext;
    private List<Utente> utenti;
    private Map<String, Object> datiUtenti;

    public ContattiAdapter(Context c) {
        Firebase.setAndroidContext(c);
        firebase = new Firebase("https://scorching-heat-6469.firebaseio.com/Users");
        utenti = new ArrayList<Utente>();

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


    }

    public int getCount() {
        return utenti.size();
    }

    public Object getItem(int position) {
        return utenti.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }
    public void openTwitterPage(String username){
        Intent i = null;

        try {

// use the twitter app
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + username));

        }catch (Exception e) {

// try to use other intent
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + username));
        }

        checkIfAppExists(i, "Twitter");
    }
    // method to check whether an app exists or not
    public void checkIfAppExists(Intent appIntent, String appName){
        if (appIntent.resolveActivity(mContext.getPackageManager()) != null) {

// start the activity if the app exists in the system
            mContext.startActivity(appIntent);

        } else {

// tell the user the app does not exist
            Toast.makeText(mContext, appName + "app does not exist!", Toast.LENGTH_LONG).show();
            goToMarket(mContext,"Twitter");
        }
    }

 public boolean isSkypeClientInstalled(Context myContext) {
 PackageManager myPackageMgr = myContext.getPackageManager();
 try {
 myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
 }
 catch (PackageManager.NameNotFoundException e) {
 return (false);
 }
 return (true);
 }
    public void goToMarket(Context myContext,String app) {
        Intent myIntent=null;
        if(app.equals("Skype")){
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
         myIntent = new Intent(Intent.ACTION_VIEW, marketUri);}
        else
        {
            Uri marketUri = Uri.parse("market://details?id=com.twitter.android");
             myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        }
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }
    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext,"Skype");
            return;
        }

        // Create the Intent from our Skype URI.
        Intent skype = new Intent("android.intent.action.VIEW");
        skype.setData(Uri.parse("skype:" + mySkypeUri));

      skype.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(skype);

        return;
    }
    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone));
        mContext.startActivity(callIntent);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.imaPic = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.imaInfo = (ImageView) convertView.findViewById(R.id.imageViewInfo);
            viewHolder.tex = (TextView) convertView.findViewById(R.id.utente_adapter);
            viewHolder.informazioni=(TableLayout)convertView.findViewById(R.id.tabella);
            viewHolder.skype=(ImageView)convertView.findViewById(R.id.imageViewCallSkype);
            viewHolder.twitter=(ImageView)convertView.findViewById(R.id.imageViewFollow);
            viewHolder.number=(ImageView)convertView.findViewById(R.id.imageViewCall);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Utente utente = (Utente) getItem(position);
        if(utente.getPhone().equals(""))
            viewHolder.number.setVisibility(View.GONE);
        if(utente.getTwitter().equals(""))
            viewHolder.twitter.setVisibility(View.GONE);
        if(utente.getSkype().equals(""))
            viewHolder.skype.setVisibility(View.GONE);
        viewHolder.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             openTwitterPage(utente.getTwitter());
            }
        });
        viewHolder.skype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateSkypeUri(mContext,utente.getSkype());
            }
        });

        viewHolder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(utente.getPhone());
            }
        });
        if (utente.getFoto() == null) {
            viewHolder.imaPic.setImageResource(R.mipmap.user);
        } else {
            byte[] image = new byte[0];
            image = Base64.decode(utente.getFoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
            viewHolder.imaPic.setImageBitmap(decodedByte);
        }

        Drawable drawable = MrVector.inflate(convertView.getResources(), R.drawable.information);
        viewHolder.imaInfo.setImageDrawable(drawable);


        final ImageView image=viewHolder.imaInfo;

       final TableLayout finalViewHolder=(TableLayout)convertView.findViewById(R.id.tabella);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(finalViewHolder.getVisibility()==View.GONE){
                finalViewHolder.setVisibility(View.VISIBLE);}
                else
                    finalViewHolder.setVisibility(View.GONE);
            }
        });

        viewHolder.tex.setText(utente.getNome() + " " + utente.getCognome());

        return convertView;
    }


}
 class ViewHolder {
    ImageView imaPic;
    ImageView imaInfo;
    ImageView twitter;
    ImageView number;
    ImageView skype;
    TextView tex;
    static TableLayout  informazioni;
}