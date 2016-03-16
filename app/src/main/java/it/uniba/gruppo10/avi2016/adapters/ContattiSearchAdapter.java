package it.uniba.gruppo10.avi2016.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Created by Vincenzo on 11/06/2015.
 */
public class ContattiSearchAdapter extends BaseAdapter implements Filterable {
    private static final String ID = "Id";
    private Firebase firebase;
    private Context mContext;
    private List<Utente> utenti;
    private List<Utente> utentiF;
    private Map<String, Object> datiUtenti;

    public ContattiSearchAdapter(Context c) {
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
                utentiF = utenti;
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        getFilter();

    }

    public List<Utente> getAll() {
        return utentiF;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_contact, null);
            viewHolder = new ViewHolder();
            viewHolder.imaPic = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.checkBoxSelect);
            viewHolder.tex = (TextView) convertView.findViewById(R.id.utente_adapter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Utente utente = (Utente) getItem(position);
        if (utente.getCheck()) {
            viewHolder.check.setChecked(true);
        } else
            viewHolder.check.setChecked(false);


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalViewHolder.check.isChecked())
                    utente.setCheck(true);
                else {
                    utente.setCheck(false);
                }
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


        viewHolder.tex.setText(utente.getNome() + " " + utente.getCognome());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new ContattiFilter();
    }

    private class ViewHolder {
        ImageView imaPic;
        CheckBox check;
        TextView tex;
    }

    private class ContattiFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Utente> filtro = new ArrayList<>();
                for (Utente e : utenti) {
                    if (e.getCognome().contains(constraint) || e.getNome().contains(constraint)) {
                        filtro.add(e);
                    }
                }
                results.count = filtro.size();
                results.values = filtro;
            } else {
                results.count = utentiF.size();
                results.values = utentiF;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            utenti = (ArrayList<Utente>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
