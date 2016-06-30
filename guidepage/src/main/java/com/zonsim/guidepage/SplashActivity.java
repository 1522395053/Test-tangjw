package com.zonsim.guidepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * CopyRight
 * Created by tang-jw on 2016/6/30.
 */
public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		
		new Thread(){
			@Override
			public void run() {
				SystemClock.sleep(1500L);
				startActivity(new Intent(SplashActivity.this, GuideActivity.class));
				finish();
			}
		}.start();
	}
}
