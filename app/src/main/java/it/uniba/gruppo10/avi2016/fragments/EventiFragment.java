package it.uniba.gruppo10.avi2016.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.EventiUserAdapter;
import it.uniba.gruppo10.avi2016.adapters.RecyclerViewAdapter;
import it.uniba.gruppo10.avi2016.utilities.MyRecyclerView;

/**
 * Fragment contenente gli eventi salvati dall'utente.
 */
public class EventiFragment extends Fragment {
    private MyRecyclerView rv;
    private String ID;
    private TextView t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_eventi, container, false);


        t = (TextView) rootView.findViewById(R.id.textViewEventi);
        rv = (MyRecyclerView) rootView.findViewById(R.id.rv);

        ID = getArguments().getString("ID");
        configuraRecycleView();
        return rootView;

    }

    private void configuraRecycleView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        Spanned message = Html.fromHtml("<b><big><sup>" + getActivity().getString(R.string.no_event) + "</sup></big></b>" + "<br />"
                + getActivity().getString(R.string.no_event_message));
        t.setText(message);
        rv.setEmptyView(t);
        if (ID.equals("Eventi salvati"))
            rv.setAdapter(new EventiUserAdapter(getActivity()));
        else
            rv.setAdapter(new RecyclerViewAdapter(getActivity(), ID));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Activity) getActivity()).getActionBar().setSubtitle(ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        configuraRecycleView();
    }
}
