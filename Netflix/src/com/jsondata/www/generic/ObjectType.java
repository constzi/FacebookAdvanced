package com.jsondata.www.generic;

//import com.admob.android.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ObjectType extends Activity implements OnClickListener{
	private Button comedyButton;
	private Button dramaButton;
	private Button romanceButton;
	private Button documentaryButton;
	private Button classicsButton;
	private Button thrillersButton;
	private Button actionButton;
	//private Button foreignButton;
	private Button scifiButton;	
	//private Button televisionButton;
	private Button horrorButton;
	private Button childrenButton;
	private ObjectApplication app;
	GoogleAnalyticsTracker tracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        // analytics 1 of 2
        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.start("UA-21678350-1", this);
        tracker.trackPageView("/MovieGenre");
        
        setContentView(R.layout.object_type); 
        
        app = (ObjectApplication)getApplication();	  
        app.setUpAds();
        //AdView adView = (AdView)findViewById(R.id.ad);
        //adView.requestFreshAd();
        
        comedyButton = (Button)findViewById(R.id.comedy_button);
        dramaButton = (Button)findViewById(R.id.drama_button);
        romanceButton = (Button)findViewById(R.id.romance_button);
        documentaryButton = (Button)findViewById(R.id.documentary_button);
        classicsButton = (Button)findViewById(R.id.classics_button);
        thrillersButton = (Button)findViewById(R.id.thrillers_button);
        actionButton = (Button)findViewById(R.id.action_button);
        //foreignButton = (Button)findViewById(R.id.foreign_button);
        scifiButton = (Button)findViewById(R.id.scifi_button);
        //televisionButton = (Button)findViewById(R.id.television_button);
        horrorButton = (Button)findViewById(R.id.horror_button);
        childrenButton = (Button)findViewById(R.id.children_button);
        
        comedyButton.setOnClickListener(this);
        dramaButton.setOnClickListener(this);
        romanceButton.setOnClickListener(this);
        documentaryButton.setOnClickListener(this);
        classicsButton.setOnClickListener(this);
        thrillersButton.setOnClickListener(this);
        actionButton.setOnClickListener(this);
        //foreignButton.setOnClickListener(this);
        scifiButton.setOnClickListener(this);
        //televisionButton.setOnClickListener(this);
        horrorButton.setOnClickListener(this);
        childrenButton.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		String type = null;
		switch (v.getId()) {
		case R.id.comedy_button:
			type = "Stand-up Comedy";
			break;
		case R.id.drama_button:
			type = "Classic Dramas";
			break;
		case R.id.romance_button:
			type = "Classic Romantic Movies";
			break;
		case R.id.documentary_button:
			type = "Documentaries";
			break;
		case R.id.classics_button:
			type = "Classic Action & Adventure";
			break;					
		case R.id.thrillers_button:
			type = "Action Thrillers";
			break;	
		case R.id.action_button:
			type = "Action & Adventure";
			break;				
//		case R.id.foreign_button:
//			type = "Classic Foreign Movies";
//			break;	
		case R.id.scifi_button:
			type = "Action Sci-Fi & Fantasy";
			break;				
//		case R.id.television_button:
//			type = "Classic TV Shows";
//			break;				
		case R.id.horror_button:
			type = "Horror Movies";
			break;	
		case R.id.children_button:
			type = "Children & Family Movies";
			break;
		default:
			break;
		}
		if (null != type){
			Intent intent = new Intent(ObjectType.this, ObjectList.class);
			intent.putExtra("type", type);
			startActivity(intent);
		}		
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
