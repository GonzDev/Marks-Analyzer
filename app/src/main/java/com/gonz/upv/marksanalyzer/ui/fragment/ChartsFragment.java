package com.gonz.upv.marksanalyzer.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.Plot;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ChartsFragment extends Fragment {

    private class CustomBarFormatter extends BarFormatter {

        public CustomBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return CustomBarRenderer.class;
        }

        @Override
        public SeriesRenderer getRendererInstance(XYPlot plot) {
            return new CustomBarRenderer(plot);
        }

    }

    private class CustomBarRenderer extends BarRenderer<CustomBarFormatter> {

        public CustomBarRenderer(XYPlot plot) {
            super(plot);
        }

        public CustomBarFormatter getFormatter(int index, XYSeries series) {
            return getFormatter(series);
        }

    }

    // the fragment initialization parameters
    private static final String SORTEDLIST = "sortedList";

    private ArrayList<Tuple> sortedList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sortedList Parameter 1.
     * @return A new instance of fragment ChartsFragment.
     */
    public static ChartsFragment newInstance(ArrayList<Tuple> sortedList) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putSerializable(SORTEDLIST, sortedList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sortedList = (ArrayList) getArguments().getStringArrayList(SORTEDLIST);
        }
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        XYPlot plot = (XYPlot) view.findViewById(R.id.plot);
        setupXYPlot(plot);
        return view;
    }

    private void setupXYPlot(XYPlot plot) {

        // Create points array to plot
        int[] counter = new int[11];
        Number[] array = new Number[counter.length];
        for (int i=0; i<sortedList.size(); i++) {
            int mark = (int) sortedList.get(i).getMark();
            if(mark == 10)  counter[11] += 1;
            else counter[mark] += 1;

        }
        for (int i=0; i<counter.length; i++)
            array[i] = counter[i];

        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);

        plot.getGraphWidget().setSize(new SizeMetrics(
                0, SizeLayoutType.FILL,
                0, SizeLayoutType.FILL));

        plot.setBorderStyle(Plot.BorderStyle.NONE, null, null);
        plot.setPlotMargins(0, 0, 0, 0);
        plot.setPlotPadding(0, 0, 0, 0);
        plot.setGridPadding(0, 10, 5, 0);

        plot.setBackgroundColor(Color.TRANSPARENT);

        // Remove Legends
        plot.getLayoutManager().remove(plot.getLegendWidget());
        plot.getLayoutManager().remove(plot.getDomainLabelWidget());
        plot.getLayoutManager().remove(plot.getRangeLabelWidget());
        plot.getLayoutManager().remove(plot.getTitleWidget());

        // Domain
        plot.setDomainBoundaries(0, 10, BoundaryMode.FIXED);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
        plot.setDomainValueFormat(new DecimalFormat("0"));

        //Range
        int max = 0;
        for (int i=0; i<counter.length; i++)
            if (counter[i] > max)
                max = counter[i];

        plot.setRangeBoundaries(0, max + (max % 10), BoundaryMode.FIXED);
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);
        plot.setRangeValueFormat(new DecimalFormat("0"));

        XYSeries serie = new SimpleXYSeries(Arrays.asList(array),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "");

        BarFormatter formatter = new BarFormatter(Color.parseColor("#8B0000"), Color.BLACK);
        formatter.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK));

        plot.addSeries(serie, formatter);

        BarRenderer renderer = (BarRenderer) plot.getRenderer(BarRenderer.class);
        renderer.setBarWidth(30);

        plot.redraw();

    }
}
