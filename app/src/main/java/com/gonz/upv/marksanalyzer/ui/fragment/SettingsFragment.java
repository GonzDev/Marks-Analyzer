package com.gonz.upv.marksanalyzer.ui.fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.activity.MainActivity;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
