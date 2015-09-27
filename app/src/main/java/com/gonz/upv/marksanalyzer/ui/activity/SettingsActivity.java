package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.fragment.SettingsFragment;

import java.util.Locale;


public class SettingsActivity extends AppCompatActivity {

    private String dni, pass;
    private String currLangCode;

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
        setLanguaje();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sp,
                                                  String key) {

                switch (key) {
                    case MainActivity.KEY_CREDENTIALS:
                        updatePrivacy(sp.getBoolean(MainActivity.KEY_CREDENTIALS, false), sp);
                        break;
                    case MainActivity.KEY_LANGUAJE:
                        askForLanguajeUpdate(sp.getString(MainActivity.KEY_LANGUAJE, "es"));
                        break;
                }
            }
        });
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

    public void updatePrivacy(boolean b, SharedPreferences sp) {
        SharedPreferences.Editor editor = sp.edit();
        if(b) {
            editor.putString(MainActivity.KEY_DNI, dni);
            editor.putString(MainActivity.KEY_PASS, pass);

        } else {
            editor.remove(MainActivity.KEY_DNI);
            editor.remove(MainActivity.KEY_PASS);
        }
        editor.commit();
    }

    private void setLanguaje() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(MainActivity.KEY_LANGUAJE)) {
            String localeCode = preferences.getString(MainActivity.KEY_LANGUAJE, "es");
            Locale locale = new Locale(localeCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            currLangCode = localeCode;
        } else {
            currLangCode = "none";
        }
    }

    private void askForLanguajeUpdate(String newLangCode) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.dialog_restart_title);
        alertDialogBuilder.setMessage(R.string.dialog_restart_message);
        alertDialogBuilder.setPositiveButton(R.string.dialog_restart_button_yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(i);
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.dialog_restart_button_no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = PreferenceManager.
                                getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        if (currLangCode.equals("none"))
                            editor.remove(MainActivity.KEY_LANGUAJE);
                        else {
                            editor.putString(MainActivity.KEY_LANGUAJE, currLangCode);
                        }
                        dialog.dismiss();
                    }
                }
        );
        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b1 = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button b2 = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                b1.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_primary));
                b2.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_primary));
            }
        });
        dialog.show();
    }
}
