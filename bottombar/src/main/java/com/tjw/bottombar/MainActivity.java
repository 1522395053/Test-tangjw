package com.tjw.bottombar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	
	private BottomBar mBottomBar1;
	private BottomBar mBottomBar2;
	private BottomBar mBottomBar3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBottomBar1 = (BottomBar) findViewById(R.id.bb_button1);
		mBottomBar2 = (BottomBar) findViewById(R.id.bb_button2);
		mBottomBar3 = (BottomBar) findViewById(R.id.bb_button3);
		
		mBottomBar1.setOnClickListener(this);
		mBottomBar2.setOnClickListener(this);
		mBottomBar3.setOnClickListener(this);
		
	}
	
	public void point(View view) {
		mBottomBar2.showPoint(true);
	}
	
	@Override
	public void onClick(View view) {
		mBottomBar1.setChecked(view.getId() == R.id.bb_button1);
		mBottomBar2.setChecked(view.getId() == R.id.bb_button2);
		mBottomBar3.setChecked(view.getId() == R.id.bb_button3);
	}
	
}
