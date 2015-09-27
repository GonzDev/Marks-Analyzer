package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.task.AnalyzeTask;

import java.util.Locale;

public class MainActivity extends Activity {

    public static final String KEY_CREDENTIALS = "pref_remember_credentials";
    public static final String KEY_DNI = "pref_dni";
    public static final String KEY_PASS = "pref_pass";

    public static final String KEY_LANGUAJE = "pref_languaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLanguaje();
        checkPreferences();
    }

    private void checkPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check login data
        Boolean b = false;
        if(preferences.contains(KEY_CREDENTIALS)) {
            b = preferences.getBoolean(KEY_CREDENTIALS, false);
            // System.out.println(b);
            if(b == true) {
                String dni = "", pass = "";
                if (preferences.contains(KEY_DNI) && preferences.contains(KEY_PASS)) {
                    dni = preferences.getString(KEY_DNI, "");
                    // System.out.println(dni);
                    pass = preferences.getString(KEY_PASS, "");
                    // System.out.println(pass);
                    ((EditText) findViewById(R.id.editTextDNI)).setText(dni);
                    ((EditText) findViewById(R.id.editTextPASS)).setText(pass);
                }
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(MainActivity.KEY_DNI, "");
                editor.putString(MainActivity.KEY_PASS, "");
                editor.commit();
            }
        }
    }

    public void start(View view) {

        EditText dniEditText = (EditText) findViewById(R.id.editTextDNI);
        EditText passEditText = (EditText) findViewById(R.id.editTextPASS);
        EditText urlEditText = (EditText) findViewById(R.id.editTextURL);

        String dni = dniEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String url = urlEditText.getText().toString();

        AnalyzeTask analyzer = (AnalyzeTask) new AnalyzeTask(dni, pass, url, this).execute("");
        analyzer.getData();
    }

    public void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Error al acceder a la intranet.", Toast.LENGTH_SHORT).show();
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
        }
    }
}
