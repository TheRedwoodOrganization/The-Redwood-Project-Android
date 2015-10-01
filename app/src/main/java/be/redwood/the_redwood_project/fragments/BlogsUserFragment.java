package be.redwood.the_redwood_project.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.redwood.the_redwood_project.R;

public class BlogsUserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.create_blog, container, false);

        return v;

    }


}
