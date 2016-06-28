package com.zonsim.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zonsim.qrcode.zxing.CaptureActivity;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void start(View view) {
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivity(intent);
	}
}
