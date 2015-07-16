package com.gonz.upv.marksanalyzer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gonz.upv.marksanalyzer.ui.fragment.GeneralFragment;
import com.gonz.upv.marksanalyzer.ui.fragment.SearchFragment;

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
                GeneralFragment tab1 = new GeneralFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                SearchFragment tab2 = new SearchFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
