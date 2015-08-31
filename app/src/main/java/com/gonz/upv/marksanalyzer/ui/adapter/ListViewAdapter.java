package com.gonz.upv.marksanalyzer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Tuple> data;
    private static LayoutInflater inflater = null;

    public ListViewAdapter(Context context, ArrayList<Tuple> list) {
        this.context = context;
        this.data = list;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = inflater.inflate(R.layout.result_row, null);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(data.get(position).getName());
        TextView mark = (TextView) v.findViewById(R.id.mark);
        mark.setText(Float.toString(data.get(position).getMark()));
        return v;
    }
}
