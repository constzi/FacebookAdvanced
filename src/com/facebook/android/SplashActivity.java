package com.facebook.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	//SOS READ: connect with facebook sdk notes @:
	//http://blog.doityourselfandroid.com/2011/02/28/30-minute-guide-integrating-facebook-android-application/
	// facebook sdk: https://github.com/facebook/facebook-android-sdk	
	private long splashDelay = 1500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent hackbookIntent = new Intent().setClass(SplashActivity.this, Hackbook.class);
                startActivity(hackbookIntent);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, splashDelay);
    }
}
