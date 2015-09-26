package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.task.AnalyzeTask;

public class MainActivity extends Activity {

    public static final String KEY_CREDENTIALS = "pref_remember_credentials";
    public static final String KEY_DNI = "pref_dni";
    public static final String KEY_PASS = "pref_pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPreferences();
    }

    private void checkPreferences() {
        Boolean b = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains(MainActivity.KEY_CREDENTIALS)) {
            b = preferences.getBoolean(MainActivity.KEY_CREDENTIALS, false);
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

        dniEditText.setText("");
        passEditText.setText("");

    }

    public void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Error al acceder a la intranet.", Toast.LENGTH_SHORT).show();
    }

}
