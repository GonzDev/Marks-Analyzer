package com.gonz.upv.marksanalyzer.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.ui.fragment.ChartsFragment;
import com.gonz.upv.marksanalyzer.ui.fragment.GeneralFragment;
import com.gonz.upv.marksanalyzer.ui.fragment.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int nTabs;
    private Bundle bundle;

    public PageAdapter(FragmentManager fm, int n, Bundle b) {
        super(fm);
        this.nTabs = n;
        this.bundle = b;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ArrayList<Tuple> marks = (ArrayList) bundle.getSerializable("sortedList");
                return ChartsFragment.newInstance(marks);
            case 1:
                GeneralFragment tab1 = new GeneralFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 2:
                HashMap<String,Float> map = (HashMap) bundle.getSerializable("map");
                ArrayList<Tuple> list = (ArrayList) bundle.getSerializable("sortedList");
                return ListFragment.newInstance(map, list);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
