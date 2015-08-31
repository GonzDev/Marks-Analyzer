package com.gonz.upv.marksanalyzer.ui.listener;

import android.view.View;
import android.widget.AdapterView;

import com.gonz.upv.marksanalyzer.ui.activity.ReportActivity;

public class SearchListener implements AdapterView.OnItemClickListener {

    private ReportActivity activity;

    public SearchListener(ReportActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s = (String) parent.getItemAtPosition(position);
        activity.highlight(s);
    }
}
