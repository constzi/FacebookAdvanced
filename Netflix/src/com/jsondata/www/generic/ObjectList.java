package com.jsondata.www.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.jsondata.www.generic.R;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ObjectList extends ListActivity implements Runnable{
	private ObjectAdapter adapter;
	private ArrayList<Object> objects;
	private ObjectApplication app;
	private TextView infoText;
	private String info;
	private String jsonUrl;
	private String[] jsonValues;
	private String type; 
    private Dialog dialog;
	private Button backButton;
	GoogleAnalyticsTracker tracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // analytics 1 of 2
        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.start("UA-21678350-1", this);
        tracker.trackPageView("/MovieTitle");
        
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            type = extras.getString("type").replace(" ", "%20").replace("&", "%26");
        else
        	type = "Classic%20Dramas";
        
        // call yelp
        //jsonUrl = "http://api.yelp.com/business_review_search?term=sushi&limit=10&tl_lat=34.040533&tl_long=-118.268223&br_lat=34.208395&br_long=-118.107147&ywsid=wVDYOGbcWk-2O2AlxeYkcg";
        //jsonValues = new String[] {"businesses", "name", "mobile_url"}; 
        
        // call net flicks
        //jsonUrl = "http://odata.netflix.com/Catalog/Titles?$filter=AverageRating%20lt%202%20and%20Instant/Available%20eq%20true&$top=20&$format=json";
        jsonUrl = "http://odata.netflix.com/Catalog/Genres('" + type + "')/Titles?$filter=Type%20eq%20'Movie'%20and%20Instant/Available%20eq%20true%20and%20AverageRating%20gt%203&$orderby=AverageRating%20desc&$top=500&$format=json";
        jsonValues = new String[] {"d", "Name", "Url"}; 
    
		/*
		app = (ObjectApplication)getApplication();
		app.loadObjects(jsonUrl, jsonValues);
		setContentView(R.layout.object_list);
		objects = app.getCurrentObjects();
		adapter = new ObjectAdapter(ObjectList.this, R.layout.row, objects);
		setListAdapter(adapter); 
		*/
		
        // or below to test Json direct: must extend Activity & comment out setListAdapter below & choose a runJsonParser
        //setContentView(R.layout.main);
        //runJsonParser(jsonUrl, jsonValues);
        //runJsonParser2(); // works
		
		setContentView(R.layout.object_list);
		
		// dialog: http://saigeethamn.blogspot.com/2010_04_01_archive.html
		dialog = ProgressDialog.show(this, "Please Wait..", "Loading 100's of movie titles sorted by highest rating...", true, false);
		Thread thread = new Thread(this);
		thread.start();
    }
    
	public void run() {
		loadData();
		handler.sendEmptyMessage(0);
		//Thread.sleep(800); pi_string = "123456";
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			showData();
			//infoText = (TextView)findViewById(R.id.info_text); infoText.setText(pi_string);
		}
	};
    
	private void loadData(){
		app = (ObjectApplication)getApplication();	
		objects = app.getCurrentObjects(jsonUrl, jsonValues);
	}
	
    private void showData() {
		adapter = new ObjectAdapter(ObjectList.this, R.layout.row, objects);
		setListAdapter(adapter); 
		
		backButton = (Button)findViewById(R.id.back_button);
		backButton.setVisibility(View.VISIBLE);
		
        app = (ObjectApplication)getApplication();	  
        app.setUpAds();
        //AdView adView = (AdView)findViewById(R.id.ad);
        //adView.requestFreshAd();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
    	Object object = adapter.clickAtPosition(position);	
		String url = object.getUrl();
	    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		startActivity(intent); 
		//finish();
	}
	
	// below is test code direct call to json 
    public void runJsonParser(String jsonUrl, String[] jsonValues){
    	infoText = (TextView)findViewById(R.id.info_text);
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
		   if (null != jsa) info += "..." + jsa.size() + "\n";
		   // http://kurssittaja.googlecode.com/svn-history/r84/trunk/softa/Kurssittaja/src/com/ahnehto/kurssittaja/kurssitiedosto/Kurssi.java
		   for (JsonElement element : jsa){
				String name = element.getAsJsonObject().get(jsonValues[1]).getAsString();
				String url = element.getAsJsonObject().get(jsonValues[2]).getAsString();	   
				info += name + "-" + url + "----\n";
				
				Object o = new Object();
				o.setName(name);
				o.setUrl(url);
		   }		   
		   // for (int i= 0; i<jsa.size(); i++ ) {info += jsa.get(i)+ "\n";}   
		   infoText.setText(info);
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
  		   infoText.setText("1111");
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
  		   infoText.setText("222");
    	} catch (IOException e) {
    		e.printStackTrace();
  		   infoText.setText("333");
    	}
		infoText.setText(info + "***");
    }   
    
    public void runJsonParser2(){
    	infoText = (TextView)findViewById(R.id.info_text);
        try{       	
	        Log.i("MY INFO", "Json Parser started..");
	        Gson gson = new Gson();
	        Reader r = new InputStreamReader(getJSONData("http://search.twitter.com/trends.json"));
	        Log.i("MY INFO", r.toString());
	        TwitterTrends objs = gson.fromJson(r, TwitterTrends.class);
	        Log.i("MY INFO", "" + objs.getTrends().size());
	        for(TwitterTrend tr : objs.getTrends()){
	            Log.i("TRENDS", tr.getName() + " - " + tr.getUrl());
	            info += tr.getName() + " - " + tr.getUrl() + "\n\n";
	        }
			infoText.setText(info);
        }catch(Exception ex){
            ex.printStackTrace();
        }
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
    
	public void BackButtonClicked(View view){
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// analytics 2 of 2
		// Send tack info 
        tracker.dispatch();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	}
}