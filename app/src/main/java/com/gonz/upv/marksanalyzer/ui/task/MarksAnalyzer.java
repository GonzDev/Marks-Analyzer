package com.gonz.upv.marksanalyzer.ui.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gonz.common.HTMLDataExtractor;
import com.gonz.common.UPVConnection;
import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.activity.ReportActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MarksAnalyzer extends AsyncTask<String, String, Integer> {
	
	private UPVConnection conn;
	private HTMLDataExtractor extractor;
	private String dni, pass, url, name;
	
	private Activity activity;
	private boolean succes;

	private ProgressBar pb;

	public MarksAnalyzer(String dni, String pass, String url, Activity activity) {
		
		this.dni = dni;
		this.pass = pass;
		this.url = url;
		this.name = "unknown";

		this.activity = activity;

		this.succes = false;
		this.pb = (ProgressBar) this.activity.findViewById(R.id.progressBar);

	}
	
	public HTMLDataExtractor getData() {
		return this.extractor;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		this.conn = new UPVConnection(dni, pass);

		Button b = (Button) activity.findViewById(R.id.buttonStart);
		b.setEnabled(false);
		pb.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		
		// 0 = NO ERROR
		// 1 = ERROR
		int success = 1;

		try {
			
			success = conn.logon();
			if (success == 0) {

				String html = "";

				/** This part of code is for offline demo and will be removed shortly */
				if (url.equals("demo")) {
					BufferedReader br =
							new BufferedReader(new InputStreamReader(
									activity.getAssets().open("sample.htm"), "Windows-1252"));
					String inputLine = "";
					StringBuilder response = new StringBuilder();

					while ((inputLine = br.readLine()) != null) {
						response.append(inputLine);
					}

					br.close();
					html = response.toString();
				/*********************************************************************/

				} else {
					html = conn.GET(url);
				}
				name = conn.getName();
				extractor = new HTMLDataExtractor(html);
				extractor.init();
				this.succes = true;

				return 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	
	@Override
	protected void onPostExecute(Integer success) {

		super.onPostExecute(success);
		pb.setVisibility(View.GONE);
		Button b = (Button) activity.findViewById(R.id.buttonStart);
		if (!b.isEnabled())
		b.setEnabled(true);

		if(!this.succes) {
			Toast.makeText(activity.getApplicationContext(), "  Error al acceder a la intranet.  ",
					Toast.LENGTH_LONG).show();
			return ;
		}
		
		Intent i = new Intent(activity, ReportActivity.class);
		Bundle extras = new Bundle();

		extras.putString("name", name);

		extras.putString("subject", extractor.getSubject());
		extras.putString("context", extractor.getContext());
		extras.putString("date", extractor.getDate());
		extras.putString("total", extractor.getTotal());
		extras.putString("blanks", extractor.getBlanks());
		extras.putString("passed", extractor.getPassed());
		extras.putString("failed", extractor.getFailed());
		extras.putString("mean", extractor.getMean());
		extras.putString("best", extractor.getUpper());
		extras.putString("worst", extractor.getLower());

		extras.putSerializable("map", extractor.getMap());
		extras.putSerializable("sortedList", extractor.getSortedList());

		i.putExtras(extras);

		activity.startActivity(i);		
		
	}


}
