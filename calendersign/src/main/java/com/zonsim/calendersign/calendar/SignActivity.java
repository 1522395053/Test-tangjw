package com.zonsim.calendersign.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * CopyRight
 * Created by tang-jw on 2016/7/4.
 */
public class SignActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new CalendarSignView(this,mClickSignInCallBack));
	}
	
	private CalendarViewAdapter.onClickSignInCallBack mClickSignInCallBack = new CalendarViewAdapter.onClickSignInCallBack() {
		
		@Override
		public void onTimeOut(String msg) {
			
		}
		
		@Override
		public void onSucess() {
			Toast.makeText(SignActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onFail(String error) {
			
		}
	};
}
