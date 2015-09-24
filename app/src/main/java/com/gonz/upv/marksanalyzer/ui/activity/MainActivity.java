package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.task.MarksAnalyzer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {

        EditText dniEditText = (EditText) findViewById(R.id.editTextDNI);
        EditText passEditText = (EditText) findViewById(R.id.editTextPASS);
        EditText urlEditText = (EditText) findViewById(R.id.editTextURL);

        String dni = dniEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String url = urlEditText.getText().toString();

        MarksAnalyzer analyzer = (MarksAnalyzer) new MarksAnalyzer(dni, pass, url, this).execute("");
        analyzer.getData();

    }

    public void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Error al acceder a la intranet.", Toast.LENGTH_SHORT).show();
    }

}
