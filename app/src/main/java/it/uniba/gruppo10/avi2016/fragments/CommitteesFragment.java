package it.uniba.gruppo10.avi2016.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.AdapterCommittees;

/**
 * Fragment contenente informazioni sulla commissione della conferenza.
 */
public class CommitteesFragment extends Fragment {
    ListView lista;
    String ID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commins, container, false);
        lista = (ListView) v.findViewById(R.id.listViewCommins);
        ID = getArguments().getString("ID");
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AdapterCommittees adapter = new AdapterCommittees(getActivity(), ID);
        lista.setAdapter(adapter);
        ((Activity) getActivity()).getActionBar().setSubtitle(ID);
    }
}
