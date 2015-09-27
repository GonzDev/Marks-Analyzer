package com.gonz.common;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class HTMLDataExtractor implements Serializable{

	private final String TAG = "HTMLDataExtractor";

	private LinkedList<Tuple> sortedList;
	private HashMap<String, Float> map;
	private String html;
	
	private String subject, context, date;
	private float mean, median, q1, q3, upper, lower;
	private int blanks, passed, total;

	private DecimalFormat formatter;

	public HTMLDataExtractor(String html) {
		
		this.subject = this.context = this.date = "";
		this.mean = this.median = this.q1 = this.q3 = this.upper = this.lower = 0.0f;
		this.blanks = this.passed = this.total = 0;
		
		this.sortedList = new LinkedList<>();
		this.map = new HashMap<>();
		this.html = html;

		this.formatter = new DecimalFormat("##.##");
		// System.out.println(html);
	}
	
	public String getSubject() {
		return subject;
	}
	public String getContext() {
		return context;
	}
	public String getDate() {
		return date;
	}
	public String getTotal() {
		return Integer.toString(total+blanks);
	}
	public String getBlanks() {
		return Integer.toString(blanks);
	}
	public String getPassed() {
		return Integer.toString(passed) +
				" (" + formatter.format(passed/(float)total * 100) + "%)";
	}
	public String getFailed() {
		return Integer.toString((total-passed)) +
				" (" + formatter.format(100 - (passed/(float)total * 100)) + "%)";
	}
	public String getMean() {
		return Double.toString(Math.rint(mean*100)/100);
	}
	public String getUpper() {
		return Float.toString(sortedList.getFirst().mark);
	}
	public String getLower() {
		return Float.toString(sortedList.getLast().mark);
	}

	public HashMap<String,Float> getMap() {
		return this.map;
	}
	public LinkedList<Tuple> getSortedList() {
		return this.sortedList;
	}

	public void init() {

		Document doc = Jsoup.parse(html);
		Element content = doc.getElementById("contenido");

		// 1. Obtain subject
		this.subject = content.getElementsByTag("h1").text();

		// 2. Obtain context and date
		String[] aux = content.getElementsByTag("h2").text().split("     ");
		this.context = aux[0];
		this.date = aux[2];

		// 3. Save all data in map and ordered list at same time
		Element body = content.getElementsByTag("tbody").get(0);
		Elements tuples = body.getElementsByTag("tr");
		for (Element tuple : tuples) {
			Elements datas = tuple.getElementsByTag("td");
			if (!datas.get(1).text().equals("")) {

				Tuple t = new Tuple(datas.get(0).text(),
						Float.parseFloat(datas.get(1).text().replace(',', '.')));
				map.put(t.name, t.mark);
				addToSortedList(t);

			} else
				blanks++;

		}
		
		// 4. Calculate statistics		
		this.total = sortedList.size();		
		for (Tuple t : sortedList) {
			this.mean += t.mark;
			if (t.mark >= 5.0f )
				this.passed++;
		}
		this.mean /= total;
		this.median = sortedList.get((int)(total*0.5)).mark;
		this.q1 =  sortedList.get((int)(total*0.75)).mark;
		this.q3 = sortedList.get((int)(total*0.25)).mark;
		
		showInfo();
		
	}
	
	private void addToSortedList(Tuple t) {
		
		if (sortedList.size() == 0) {
			sortedList.add(t);
			return;
		}
		for (int i = 0; i < sortedList.size(); i++) 
			if (sortedList.get(i).compareTo(t) < 0) {
				sortedList.add(i, t);
				return ;
			}
		sortedList.addLast(t);
		
	}
	
	private void showInfo() {

		Log.d(TAG, "ASIGNATURA: " + subject);
		Log.d(TAG, "CONTEXTO: " + context);
		Log.d(TAG, "FECHA DE PUBLICACIÓN: " + date + "\n");
		Log.d(TAG, "ALUMNOS EXAMINADOS: " + total);
		Log.d(TAG, "ALUMNOS CON NOTA EN BLANCO: " + blanks + "\n");
		Log.d(TAG, "MEDIA: " + mean);
		Log.d(TAG, "APROBADOS: " + passed + " (" + passed / (float) total * 100 + "%)");
		Log.d(TAG, "SUSPENDIDOS: " + (total - passed) + " (" + (100 - (passed / (float) total * 100)) + "%)\n");
		Log.d(TAG, "NOTA MÁS ALTA: " + sortedList.getFirst().mark + "(" + sortedList.getFirst().name + ")");
		Log.d(TAG, "PERCENTIL 75: " + q3 + "\n");
		Log.d(TAG, "MEDIANA: " + median);
		Log.d(TAG, "PERCENTIL 25: " + q1);
		Log.d(TAG, "NOTA MÁS BAJA: " + sortedList.getLast().mark + "\n");

		Log.d(TAG, "\n10 MEJORES NOTAS:\n");
		for (int i = 0; i < 10; i++)
			Log.d(TAG, sortedList.get(i).toString());
		
	}
	
}
