package com.gonz.common;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class UPVConnection {

	private final String LOG_TAG = "UPVConnection";

	private String dni, clau;
	private String name;
	
	private List<String> cookies;
	private HttpsURLConnection conn;
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final String HOST = "intranet.upv.es";
	private final String ACCEPT = 
			"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
	private final String ACCEPT_LANGUAJE = "ca-ES,ca;q=0.8";
	private final String REFERER = 
			"https://intranet.upv.es/pls/soalu/est_intranet.Ni_portal_n?P_IDIOMA=c";
	private final String CONNECTION = "keep-alive";
	private final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	
	private final String REQUEST_URL = 
			"https://intranet.upv.es/exp/aute_intranet";
	private final String LOGON_URL = 
			"https://intranet.upv.es/pls/soalu/est_intranet.Ni_portal_n?P_IDIOMA=c";

	public UPVConnection(String dni, String clau) {
		this.dni = dni;
		this.clau = clau;
		this.name = "";
		 
		// Make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());	 
	}

	public String getName() {
		return this.name;
	}

	public int logon() throws Exception {
		
		// Make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
 
		// 1. Send a "GET" request, so that you can extract the form's data.
		String pageCode = this.GET(LOGON_URL);
		String postParams = this.getFormParams(pageCode, this.dni, this.clau);

		// 2. Send a POST request for authentication
		pageCode = this.POST(REQUEST_URL, postParams);
		
		return isValid(pageCode);
		
	}
	
	public String GET(String url) throws Exception {
		 
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
 
		// Default is GET
		conn.setRequestMethod("GET");
 
		conn.setUseCaches(false);
 
		// Let's act like a browser
		conn.setRequestProperty("Accept", ACCEPT);
		conn.setRequestProperty("Accept-Language", ACCEPT_LANGUAJE);
		conn.setRequestProperty("User-Agent", USER_AGENT);

		if (cookies != null)
			for (String cookie : this.cookies)
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		
		int responseCode = conn.getResponseCode();
		
		Log.d(LOG_TAG, "\nSending 'GET' request to URL : " + url);
		Log.d(LOG_TAG, "Response Code : " + responseCode);

		BufferedReader br =
				new BufferedReader(new InputStreamReader(conn.getInputStream(), "Windows-1252"));
		String inputLine = "";
		StringBuilder response = new StringBuilder();

		while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
		}
		
		br.close();
 
		// Get the response cookies
		this.cookies = conn.getHeaderFields().get("Set-Cookie");
 
		// return new String(response.toString().getBytes(),"Windows-1252");
		return response.toString();
 
	}
	 
	private String POST(String url, String postParams) throws Exception {
 
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
 
		// Let's act like a browser
		conn.setRequestMethod("POST");
		
		conn.setUseCaches(false);

		conn.setRequestProperty("Accept", ACCEPT);
		conn.setRequestProperty("Connection", CONNECTION);
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);
		conn.setRequestProperty("Host", HOST);
		conn.setRequestProperty("Referer", REFERER);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		
		for (String cookie : this.cookies)
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
 
		// Send post request
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.writeBytes(postParams);
		dos.flush();
		dos.close();
 
		int responseCode = conn.getResponseCode();
		Log.d(LOG_TAG, "\nSending 'POST' request to URL : " + url);
		Log.d(LOG_TAG, "Post parameters : " + postParams);
		Log.d(LOG_TAG, "Response Code : " + responseCode);
 
		
		BufferedReader br = 
				new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = br.readLine()) != null)
			response.append(inputLine);
		
		br.close();
		
		return response.toString();
		
		
	}

	private String getFormParams(String html, String username, String password) 
			throws UnsupportedEncodingException {
	 
		Log.d(LOG_TAG, "Extracting form's data...");
 
		Document doc = Jsoup.parse(html);
 
		// UPV form id
		Element loginform = doc.getElementById("alumno");
		Elements inputElements = loginform.getElementsByTag("input");
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");
 
			if (key.equals("dni"))
				value = username;
			else if (key.equals("clau"))
				value = password;
			
			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
		}
 
		// Build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList)
			if (result.length() == 0)
				result.append(param);
			else
				result.append("&" + param);
		
		return result.toString();

	}
	
	private int isValid(String html) throws Exception{
		
		Document doc = Jsoup.parse(html);
		String h1 = doc.getElementsByTag("h1").text();

		if (h1.equals("Mi UPV")) {
			String aux = doc.getElementsByClass("menuAD").text();
			this.name = aux.substring(aux.indexOf('[')+3, aux.lastIndexOf(']')-2);
			Log.d(LOG_TAG, "\nSuccesfully logged in: " + name);
			return 0;
		}
		
		Log.d(LOG_TAG, "\nError al acceder a la intranet.");
		return 1;
		
	}
	
}