package com.gonz.upv.marksanalyzer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.gonz.upv.marksanalyzer.R;

import java.util.HashMap;

public class SearchFragment extends Fragment {

    private HashMap<String, Float> map;
    private AutoCompleteTextView autocomplete;
    private ArrayAdapter<String> adapter;

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Bundle b = getArguments();
        map = (HashMap<String, Float>) b.getSerializable("map");
        String[] list = map.keySet().toArray(new String[map.keySet().size()]);

        autocomplete = (AutoCompleteTextView) view.findViewById(R.id.query);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list);

        autocomplete.setAdapter(adapter);

        return view;
    }

}
