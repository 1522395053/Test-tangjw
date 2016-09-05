package com.zonsim.picasso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
	
	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGridView = (GridView) findViewById(R.id.gv_images);
		
		initData();
	}
	
	private void initData() {
		mGridView.setAdapter(new GridViewAdapter(MainActivity.this,Data.URLS));
	}
}
