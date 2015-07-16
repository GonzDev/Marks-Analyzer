package com.gonz.upv.marksanalyzer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.activity.ReportActivity;

public class GeneralFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        setupUI(view);
        return view;
    }

    public void setupUI(View v) {

        Bundle b = getArguments();

        ((TextView) v.findViewById(R.id.tagSubject)).setText(b.getString("subject"));
        ((TextView) v.findViewById(R.id.tagContext)).setText(b.getString("context"));
        ((TextView) v.findViewById(R.id.tagDate)).setText(b.getString("date"));

        ((TextView) v.findViewById(R.id.total)).setText(b.getString("total"));
        ((TextView) v.findViewById(R.id.blanks)).setText(b.getString("blanks"));

        ((TextView) v.findViewById(R.id.passed)).setText(b.getString("passed"));
        ((TextView) v.findViewById(R.id.failed)).setText(b.getString("failed"));
        ((TextView) v.findViewById(R.id.mean)).setText(b.getString("mean"));

        ((TextView) v.findViewById(R.id.bestMark)).setText(b.getString("best"));
        ((TextView) v.findViewById(R.id.worstMark)).setText(b.getString("worst"));

    }

}
