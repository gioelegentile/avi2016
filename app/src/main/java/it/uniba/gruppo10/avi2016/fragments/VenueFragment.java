package it.uniba.gruppo10.avi2016.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.gruppo10.avi2016.R;

/**
 * Fragment contenente informazioni sulla sede della conferenza.
 */
public class VenueFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_venue, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Activity) getActivity()).getActionBar().setSubtitle("Sede");
    }

}
