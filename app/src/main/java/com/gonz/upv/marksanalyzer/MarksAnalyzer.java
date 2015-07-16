package com.gonz.upv.marksanalyzer;

import com.gonz.upv.marksanalyzer.ui.activity.ReportActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;

public class MarksAnalyzer extends AsyncTask<String, String, Integer> {
	
	private UPVConnection conn;
	private HTMLDataExtractor extractor;
	private String dni, pass, url;
	
	private ProgressDialog progressDialog;
	private Activity activity;

	public MarksAnalyzer(String dni, String pass, String url, Activity activity) {
		
		this.dni = dni;
		this.pass = pass;
		this.url = url;
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity);
		this.progressDialog.setMessage(activity.getResources().getText(R.string.progressText));
		this.progressDialog.setIndeterminate(true);
		
	}	
	
	public HTMLDataExtractor getData() {
		return this.extractor;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		this.conn = new UPVConnection(dni, pass);
		
		this.progressDialog.show();
		Button b = (Button) activity.findViewById(R.id.buttonStart);
		b.setEnabled(false);	
		
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		
		// 0 = NO ERROR
		// 1 = ERROR
		int success = 1;			
		try {
			
			success = conn.logon();
			if (success == 0) {
				
				String html = conn.GET(url);
				extractor = new HTMLDataExtractor(html);
				extractor.init();
				
				return 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	protected void onPostExecute(Integer success) {
		
		if (progressDialog.isShowing())
			progressDialog.dismiss();
		Button b = (Button) activity.findViewById(R.id.buttonStart);
		if (!b.isEnabled())
			b.setEnabled(true);
		
		Intent i = new Intent(activity, ReportActivity.class);
		i.putExtra("subject", extractor.getSubject());
		i.putExtra("context", extractor.getContext());
		i.putExtra("date", extractor.getDate());
		i.putExtra("total", extractor.getTotal());
		i.putExtra("blanks", extractor.getBlanks());
		i.putExtra("passed", extractor.getPassed());
		i.putExtra("failed", extractor.getFailed());
		i.putExtra("mean", extractor.getMean());
		i.putExtra("best", extractor.getUpper());
		i.putExtra("worst", extractor.getLower());
		activity.startActivity(i);		
		
	}


}
