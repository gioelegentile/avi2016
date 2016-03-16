package it.uniba.gruppo10.avi2016.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.telly.mrvector.MrVector;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.activities.ChatUserActivity;
import it.uniba.gruppo10.avi2016.activities.GroupChatActivity;
import it.uniba.gruppo10.avi2016.activities.NomeGruppoActivity;
import it.uniba.gruppo10.avi2016.adapters.ChatAdapter;
import it.uniba.gruppo10.avi2016.adapters.ContattiAdapter;
import it.uniba.gruppo10.avi2016.adapters.GroupAdapter;
import it.uniba.gruppo10.avi2016.entities.Utente;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Fragment istanziato in maniera dinamica che setta l'adapter alla ListView in base all'id passato
 * in input. E' dedicato alla chat, quindi alle tre schede Gruppi, Chat e Contatti.
 */
public class MainFragment extends Fragment {
    private static final String ARG_TEXT = "ID";
    private static final int CHAT = 1, CONTATTI = 2, GROUP = 0;
    private ListView listView;
    private TextView textView;
private ButtonFloat add;

    public MainFragment() {
    }

    /**
     * Istanza statica della classe
     * @param id    id per popolare diversamente la listview
     * @return      oggetto di tipo MainFragment
     */
    public static MainFragment newInstance(int id) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TEXT, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = (ListView) view.findViewById(R.id.listViewChat);
        textView = (TextView) view.findViewById(R.id.textViewChat);
        add=(ButtonFloat)view.findViewById(R.id.plusButton);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_TEXT) && getArguments().getInt(ARG_TEXT) == CHAT) {
            listView.setAdapter(new ChatAdapter(getActivity()));
            Spanned message = Html.fromHtml("<b><big><sup>" + getActivity().getString(R.string.no_chat) + "</sup></big></b>" + "<br />"
                    + getActivity().getString(R.string.no_chat_message));
            textView.setText(message);
            listView.setEmptyView(textView);
        } else if (getArguments() != null && getArguments().containsKey(ARG_TEXT) && getArguments().getInt(ARG_TEXT) == CONTATTI) {
            listView.setAdapter(new ContattiAdapter(getActivity()));
        } else{
            listView.setAdapter(new GroupAdapter(getActivity(), IDUtente.getGruppi()));
            Spanned message = Html.fromHtml("<b><big><sup>" + getActivity().getString(R.string.no_group) + "</sup></big></b>" + "<br />"
                    + getActivity().getString(R.string.no_group_message));
            textView.setText(message);
            listView.setEmptyView(textView);
            Drawable drawable= MrVector.inflate(getResources(),R.drawable.account_multiple_plus);
            add.setDrawableIcon(drawable);
                    add.setVisibility(View.VISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent creaGruppo=new Intent(getActivity(), NomeGruppoActivity.class);
                    startActivity(creaGruppo);
                }
            });

        }

        if (getArguments() != null && getArguments().containsKey(ARG_TEXT) && getArguments().getInt(ARG_TEXT) != GROUP) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent chat = new Intent(getActivity(), ChatUserActivity.class);
                    chat.putExtra("ID", ((Utente) adapterView.getItemAtPosition(i)).getId());
                    chat.putExtra("Name", ((Utente) adapterView.getItemAtPosition(i)).getNome() + " " + ((Utente) adapterView.getItemAtPosition(i)).getCognome());
                    startActivity(chat);
                }
            });
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent chat = new Intent(getActivity(), GroupChatActivity.class);
                    chat.putExtra("Name",(String)adapterView.getItemAtPosition(i));
                    startActivity(chat);
                }
            });
        }

    }

}