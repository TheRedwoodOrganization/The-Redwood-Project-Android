package be.redwood.the_redwood_project.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.redwood.the_redwood_project.R;

public class DetailPageUserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.detail_page, container, false);

        return v;
    }

    public static DetailPageUserFragment newInstance(int page, String title) {
        DetailPageUserFragment fragment = new DetailPageUserFragment();
        return fragment;
    }


}
