package com.zonsim.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zonsim.jpush.jpush.MainActivity;

public class JPushActivity extends AppCompatActivity {
	
	private Button mButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jpush);
		
		mButton = (Button) findViewById(R.id.button);
		
		mButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(JPushActivity.this, MainActivity.class);
						startActivity(intent);
					}
				}
		);
	}
}
