package com.jsondata.www.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.Application;

public class ObjectApplication extends Application {
	private ArrayList<Object> currentObjects;
	private String jsonUrl;
	private String[] jsonValues;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (null == currentObjects && null != jsonUrl && null != jsonValues) {
			loadObjects();
		}
	}
	
	public void loadObjects() {
   	 	ArrayList<Object> objects = new ArrayList<Object>();
    	URL URLsource = null;
    	JsonElement jse = null;
    	BufferedReader in;
    	try {
    	   // http://nicholaskey.blogspot.com/2009/12/using-gson-to-parse-yelp-json-result.html
		   URLsource = new URL(jsonUrl);
		   in = new BufferedReader(new InputStreamReader(URLsource.openStream(), "UTF-8"));
		   jse = new JsonParser().parse(in);
		   in.close();
		   JsonArray jsa = jse.getAsJsonObject().getAsJsonArray(jsonValues[0]);
		   
		   // http://kurssittaja.googlecode.com/svn-history/r84/trunk/softa/Kurssittaja/src/com/ahnehto/kurssittaja/kurssitiedosto/Kurssi.java
		   for (JsonElement element : jsa){
				String name = element.getAsJsonObject().get(jsonValues[1]).getAsString();
				String url = element.getAsJsonObject().get(jsonValues[2]).getAsString();	   
				
				Object o = new Object();
				o.setName(name);
				o.setUrl(url);
				objects.add(o);
		   }
		   // for (int i= 0; i<jsa.size(); i++ ) {info += jsa.get(i)+ "\n";} 
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	currentObjects = objects;
	}
	
    public InputStream getJSONData(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri;
        InputStream data = null;
        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            HttpResponse response = httpClient.execute(method);
            data = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }     
        return data;
    }
    
	public void setCurrentObjects(ArrayList<Object> currentObjects) {
		this.currentObjects = currentObjects;
	}
	
	public ArrayList<Object> getCurrentObjects(String jsonUrl, String[] jsonValues) {
		this.jsonUrl = jsonUrl;
		this.jsonValues = jsonValues;
		loadObjects();
		return currentObjects;
	}

	public ArrayList<Object> getCurrentObjects() {
		return currentObjects;
	}

	public void setJsonUrl(String jsonUrl) {
		this.jsonUrl = jsonUrl;
	}

	public String getJsonUrl() {
		return jsonUrl;
	}

	public void setJsonValues(String[] jsonValues) {
		this.jsonValues = jsonValues;
	}

	public String[] getJsonValues() {
		return jsonValues;
	}
	
	public void setUpAds(){
		// setup test devices
		/*
        AdManager.setTestDevices(new String[]{
            	AdManager.TEST_EMULATOR, // emulator
            	"A6A2F455B3C89E2B05A4CF8164D7FE41" // device
            	});
        */
	}
}
