package com.zonsim.mywebview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
	
	private MyWebView mWebViewFragment;
	private EditText mEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		mEditText = (EditText) findViewById(R.id.editText);
		mEditText.setText("http://118.145.26.214:8186/lianyi/MtsMeeting/toMaterialDetail.do?mid=153&uid=646&like=0");
		init();
	}
	
	private void init() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		mWebViewFragment = MyWebView.newInstance("file:///android_asset/index.html");
		mWebViewFragment = MyWebView.newInstance("http://118.145.26.214:8186/lianyi/MtsMeeting/toMaterialDetail.do?mid=153&uid=646&like=0");
		
		
		fragmentManager.beginTransaction()
				.replace(R.id.mw_FrameLayout, mWebViewFragment)
				.commit();
		
	}
	
	public void load(View view) {
		hideInput();
		
		String s = mEditText.getText().toString().trim();
		if (s.startsWith("http://") || s.startsWith("https://")) {
			mWebViewFragment.loadUrl(s);
		} else {
			mWebViewFragment.loadUrl("http://" + s);
		}
//		mEditText.setText("");
	}
	
	/**
	 * 隐藏输入法
	 */
	private void hideInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 点击非EditText控件，隐藏软键盘
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
						hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(), 
								InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		if (mWebViewFragment.getWebView().canGoBack()) {
			mWebViewFragment.getWebView().goBack();
		} else {
			super.onBackPressed();
		}
	}
}
