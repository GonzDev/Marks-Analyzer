package com.gonz.upv.marksanalyzer.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.activity.MainActivity;

public class SettingsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
