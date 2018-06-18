package com.krintos.krintos.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import info.androidhive.loginandregistration.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Philology extends Fragment {


    public Philology() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Philology");
        View rootView = inflater.inflate(R.layout.fragment_philology, container, false);
        TabHost host = (TabHost) rootView.findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("LIBRARY");
        spec.setContent(R.id.LIBRARY);
        spec.setIndicator("LIBRARY");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("SOLUTIONS");
        spec.setContent(R.id.SOLUTIONS);
        spec.setIndicator("SOLUTIONS");
        host.addTab(spec);
        //Tab 3
        spec = host.newTabSpec("INDEX");
        spec.setContent(R.id.INDEX);
        spec.setIndicator("INDEX");
        host.addTab(spec);
        return rootView;
    }

}
