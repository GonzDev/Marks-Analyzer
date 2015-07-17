package com.gonz.upv.marksanalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class HTMLDataExtractor implements Serializable{
	
	private class Tuple implements Comparable<Tuple> {
	
		String name;
		float mark;
		
		public Tuple(String n, float m) {
			this.name = decode(n);
			this.mark = m;
		}

		private String decode(String s) {
			String res = null;
			try {
				res = URLDecoder.decode(s, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return res;
		}

		@Override
		public int compareTo(Tuple o) {
			Tuple other = (Tuple) o;
			if(this.mark > other.mark)
				return 1;
			else if (this.mark < other.mark)
				return -1;
			return 0;
		}
		
		@Override
		public String toString() {
			return this.name + " :  " + this.mark;
		}
		
	}

	private LinkedList<Tuple> sortedList;
	private HashMap<String, Float> map;
	private String html;
	
	String subject, context, date;
	float mean, median, q1, q3, upper, lower;
	int blanks, passed, total;
	
	public HTMLDataExtractor(String html) {
		
		this.subject = this.context = this.date = "";
		this.mean = this.median = this.q1 = this.q3 = this.upper = this.lower = 0.0f;
		this.blanks = this.passed = this.total = 0;
		
		this.sortedList = new LinkedList<Tuple>();
		this.map = new HashMap<String, Float>();
		this.html = html;

		System.out.println(html);
		
	}
	
	public String getSubject() {	return subject;	}
	public String getContext() {	return context;	}
	public String getDate() {	return date;	}
	public String getTotal() {	return Integer.toString(total+blanks);	}
	public String getBlanks() {	return Integer.toString(blanks);	}
	public String getPassed() {	return Integer.toString(passed) +  
			"  (" + Float.toString(passed/(float)total * 100) + "%)";	}
	public String getFailed() {	return Integer.toString((int)(total-passed)) +  
			"  (" + Float.toString(100 - (passed/(float)total * 100)) + "%)";	}
	public String getMean() {	return Float.toString(mean);	}
	public String getUpper() {	return Float.toString(sortedList.getFirst().mark);	}
	public String getLower() {	return Float.toString(sortedList.getLast().mark); 	}

	public HashMap<String,Float> getMap() {	return this.map;	}
	
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

		System.out.println("ASIGNATURA: " + subject);
		System.out.println("CONTEXTO: " + context);
		System.out.println("FECHA DE PUBLICACIÓN: " + date + "\n");
		System.out.println("ALUMNOS EXAMINADOS: " + total);
		System.out.println("ALUMNOS CON NOTA EN BLANCO: " + blanks + "\n");
		System.out.println("MEDIA: " + mean);
		System.out.println("APROBADOS: " + passed + " ("+ passed/(float)total * 100 + "%)");
		System.out.println("SUSPENDIDOS: " + (int)(total - passed) + " ("+ (100 - (passed/(float)total * 100)) + "%)\n");
		System.out.println("NOTA MÁS ALTA: " + sortedList.getFirst().mark + "(" +sortedList.getFirst().name +")");
		System.out.println("PERCENTIL 75: " + q3 + "\n");
		System.out.println("MEDIANA: " + median);
		System.out.println("PERCENTIL 25: " + q1);
		System.out.println("NOTA MÁS BAJA: " + sortedList.getLast().mark+"\n");

		System.out.println("\n10 MEJORES NOTAS:\n");
		for (int i = 0; i < 10; i++)
			System.out.println(sortedList.get(i).toString());
		
	}
	
}
