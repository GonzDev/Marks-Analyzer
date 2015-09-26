package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.fragment.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {

    private String dni, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        dni = b.getString("dni");
        pass = b.getString("pass");

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_action_bar);
        toolbar.setTitle(R.string.pref_action_bar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void finish() {
        Boolean b = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains(MainActivity.KEY_CREDENTIALS)) {
            b = preferences.getBoolean(MainActivity.KEY_CREDENTIALS, false);
            if(b == true) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(MainActivity.KEY_DNI, dni);
                editor.putString(MainActivity.KEY_PASS, pass);
                editor.commit();
                // System.out.println("DNI=" + preferences.getString(MainActivity.KEY_DNI, ""));
                // System.out.println("PASS=" + preferences.getString(MainActivity.KEY_PASS, ""));
                // System.out.println("Se guardarán los credenciales");
            }
        }
        // System.out.println("Preferencias actualizadas");
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
