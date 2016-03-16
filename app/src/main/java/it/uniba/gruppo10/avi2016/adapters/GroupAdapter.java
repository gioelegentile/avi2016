package it.uniba.gruppo10.avi2016.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.uniba.gruppo10.avi2016.R;

/**
 * Adapter per la visualizzazione delle chat di gruppo nel tab "Gruppi" .
 */
public class GroupAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> gruppi;
    private Firebase fb;
    Map<String, Object> groups;
    public GroupAdapter(Context c,ArrayList<String> gruppi){
        mContext=c;
        this.gruppi=gruppi;
        groups = new HashMap<>();
        fb = new Firebase("https://scorching-heat-6469.firebaseio.com/").child("chat");
        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> chats = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> chat : chats.entrySet()) {
                    if (chat.getKey().contains("-")) {
                        groups.put(chat.getKey(), chat.getValue());
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    @Override
    public int getCount() {
        return gruppi.size();
    }

    @Override
    public Object getItem(int i) {
        return gruppi.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView immagine;
        TextView gruppo;
        View v = null;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.chat_adapter, null);
            immagine = (ImageView) v.findViewById(R.id.imageView);

            gruppo = (TextView) v.findViewById(R.id.utente_adapter);
        } else {
            immagine = (ImageView) v;
            gruppo = (TextView) v;
        }

        HashMap<String, Object> group = (HashMap<String, Object>) groups.get((String) getItem(i));
        if (group != null) {
            String foto = (String) group.get("foto");
            byte[] image = new byte[0];
            image = Base64.decode(foto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
            immagine.setImageBitmap(decodedByte);
        } else {
            immagine.setImageResource(R.mipmap.group);
        }
        String[] titolo=((String)getItem(i)).split("-");
        gruppo.setText(titolo[0]);
        return v;
    }
}
