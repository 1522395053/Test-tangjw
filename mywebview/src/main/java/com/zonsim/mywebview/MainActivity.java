package com.zonsim.mywebview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
	
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}
	
	private void init() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		MyWebView fragment = MyWebView.newInstance("http://m.uczzd.cn/webview/article/news.html?app=uc-iflow&aid=10898359130797346774&cid=100&zzd_from=uc-iflow&uc_param_str=dndsfrvesvntnwpfgi&ut=KvkEWYUWagFjNX0VvzVwd1xwHXBuFcVhSmf5H97KmCAhPA%3D%3D&gp=KvkQ1q0D42%2Bdsp%2B%2FP5pVy6OpYTlxwJPuUvPTTuvW6XmNGA%3D%3D&recoid=12209850584064359653&rd_type=share&tt_from=uc_btn&btifl=100&pagetype=share&refrd_id=");
		
		
		fragmentManager.beginTransaction()
				.replace(R.id.mw_FrameLayout, fragment)
				.commit();
		
	}
}
