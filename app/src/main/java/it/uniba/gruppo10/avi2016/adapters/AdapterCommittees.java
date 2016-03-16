package it.uniba.gruppo10.avi2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.uniba.gruppo10.avi2016.R;

/**
 * Adapter personalizzato per la valorizzazione della listview presente nel fragment
 * {@link it.uniba.gruppo10.avi2016.fragments.CommitteesFragment}: la list view verrà inizializzata
 * con tutti i membri dei comitati avi2016 o con le informazioni sui contatti di tutti i membri.
 */
public class AdapterCommittees extends BaseAdapter {
    private Map<String, ArrayList<String>> committees;
    private ArrayList<String> key;
    private LinearLayout ll;
    private Context context;

    /**
     * Costruttore dell'adapter che in base alla String id valorizza il paramentro committees recuperando da
     * strings.xml l'array corrispondente.
     * @param context
     * @param ID
     */
    public AdapterCommittees(Context context, String ID) {
        this.context = context;
        if (ID.equals("Comitati"))
            key = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.keys_commi)));
        else
            key = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.keys_conta)));
        committees = new HashMap<>();

        for (String s : key) {
            String resorce = s.replaceAll(" ", "_");
            committees.put(s, new ArrayList<String>(Arrays.asList(getStringResourceByName(resorce))));
        }

    }

    @Override
    public int getCount() {
        return key.size();
    }

    @Override
    public Object getItem(int i) {
        return key.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView titolo;
        View v = null;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_commins, null);
            titolo = (TextView) v.findViewById(R.id.textViewTitolo);
            ll = (LinearLayout) v.findViewById(R.id.commi);
        } else {
            titolo = (TextView) v;
            ll = (LinearLayout) v;
        }
        titolo.setText((String) getItem(i));
        for (String s : committees.get(getItem(i))) {
            TextView r = new TextView(context);
            r.setText(s);
            ll.addView(r);
        }
        return v;
    }

    private String[] getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "array", packageName);
        return context.getResources().getStringArray(resId);
    }
}
