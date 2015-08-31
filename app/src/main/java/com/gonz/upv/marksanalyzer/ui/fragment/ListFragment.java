package com.gonz.upv.marksanalyzer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ListFragment extends Fragment {

    // the fragment initialization parameters
    private static final String SORTEDLIST = "sortedList";
    private static final String MAP = "map";

    private ArrayList<Tuple> sortedList;
    private HashMap<String, Float> map;
    private ListView list;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map Parameter 1.
     * @param sortedList Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance(HashMap<String, Float> map, ArrayList<Tuple> sortedList) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAP, map);
        args.putSerializable(SORTEDLIST, sortedList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle b = getArguments();
        this.map = (HashMap) b.getSerializable(MAP);
        this.sortedList = (ArrayList) b.getSerializable(SORTEDLIST);

        this.list = (ListView) view.findViewById(R.id.queryresult_list);
        this.list.setAdapter(new ListViewAdapter(getActivity(), sortedList));
        return view;
    }

    public void highlight(String name) {

        int pos = 0;
        for (int i = 0; i < sortedList.size(); i++)
            if (sortedList.get(i).getName().equals(name)) {
                pos = i;
                break;
            }

        list.smoothScrollToPosition(pos);
        list.setItemChecked(pos, true);

    }

    public void clearHighlight() {

        list.clearChoices();
        list.requestLayout();

    }

}
