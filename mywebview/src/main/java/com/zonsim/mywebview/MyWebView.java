package com.zonsim.mywebview;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * CopyRight
 * Created by tang-jw on 2016/9/1.
 */
public class MyWebView extends Fragment {
	
	private WebView mWebView;
	
	public WebView getWebView() {
		return mWebView;
	}
	
	private View mCustomView;
	private int mOriginalSystemUiVisibility;
	private int mOriginalOrientation;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	private String mUrl;
	private ProgressBar mProgressBar;
	
	
	public static MyWebView newInstance(String url) {
		MyWebView fragment = new MyWebView();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mUrl = bundle.getString("url");
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mProgressBar =(ProgressBar) getActivity().findViewById(R.id.progressBar);
		
		mWebView = new WebView(getContext());
		
		initWebView();
		
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient() {
			
			private FrameLayout mFrameLayout;
			
			@Override
			public Bitmap getDefaultVideoPoster() {
				return null;
			}
			
			@Override
			public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
				// if a view already exists then immediately terminate the new one
				if (mCustomView != null) {
					onHideCustomView();
					return;
				}
				
				// 1. Stash the current state
				mCustomView = view;
				mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
				mOriginalOrientation = getActivity().getRequestedOrientation();
				
				// 2. Stash the custom view callback
				mCustomViewCallback = callback;
				
				// 3. Add the custom view to the view hierarchy
				FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
				
				mFrameLayout = new FrameLayout(getContext());
				mFrameLayout.setBackgroundColor(Color.BLACK);
				decor.addView(mFrameLayout,new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				
				mFrameLayout.addView(mCustomView);
				
				
				// 4. Change the state of the window
				fullScreen(true);
				mFrameLayout.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
					@Override
					public void onSystemUiVisibilityChange(int i) {
						if (View.SYSTEM_UI_FLAG_VISIBLE == i) {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									fullScreen(true);
								}
							}, 1000L);
						}
					}
					
				});
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			
			@Override
			public void onHideCustomView() {
				fullScreen(false);
				// 1. Remove the custom view
				FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
				
				decor.removeView(mFrameLayout);
				mCustomView = null;
				
				// 2. Restore the state to it's original form
				getActivity().getWindow().getDecorView()
						.setSystemUiVisibility(mOriginalSystemUiVisibility);
				getActivity().setRequestedOrientation(mOriginalOrientation);
				
				// 3. Call the custom view callback
				mCustomViewCallback.onCustomViewHidden();
				mCustomViewCallback = null;
				
			}
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress < 100) {
					mProgressBar.setVisibility(View.VISIBLE);
				} else {
					mProgressBar.setVisibility(View.GONE);
				}
				mProgressBar.setProgress(newProgress);
			}
		});
		
		mWebView.loadUrl(mUrl);
		
		return mWebView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
	
		mWebView.onPause();
		mWebView.pauseTimers();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mWebView.onResume();
		mWebView.resumeTimers();
	}
	
	@Override
	public void onDestroy() {
		mWebView.loadUrl("about:blank");
		mWebView.stopLoading();
		mWebView.setWebChromeClient(null);
		mWebView.setWebViewClient(null);
		mWebView.destroy();
		mWebView = null;
		super.onDestroy();
	}
	
	/**
	 * 初始化 webSettings
	 */
	private void initWebView() {
		
		WebSettings settings = mWebView.getSettings();
		
		//设置 WebView 是否可以运行 JavaScript
		settings.setJavaScriptEnabled(true);
		
		//设置 WebView 是否可以由 JavaScript 自动打开窗口, 默认为 false，通常与 JavaScript 的 window.open() 配合使用
		settings.setJavaScriptCanOpenWindowsAutomatically(false);
		
		//启用或禁用WebView访问文件数据。
		settings.setAllowFileAccess(true);
		
		//禁止 WebView从网络上加载图片, 默认 false , 状态改变时 reload 才会生效
		settings.setBlockNetworkImage(false);
		
		//设置是否支持缩放。
		settings.setSupportZoom(true);
		
		//显示或不显示缩放按钮（wap网页不支持）。
		settings.setBuiltInZoomControls(false);
		
		// 设置WebView是否支持多窗口。
		settings.setSupportMultipleWindows(false);
		
		//：指定WebView的页面布局显示形式，调用该方法会引起页面重绘
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		
		//通知WebView是否需要设置一个节点获取焦点当WebView#requestFocus(int,android.graphics.Rect)被调用时，默认为true。
		settings.setNeedInitialFocus(true);
		
		//：启用或禁用应用缓存。
		settings.setAppCacheEnabled(true);
		
		//设置应用缓存路径，这个路径必须是可以让app写入文件的。该方法应该只被调用一次，重复调用会被无视 ~
		settings.setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
		
		//用来设置WebView的缓存模式。当我们加载页面或从上一个页面返回的时候，会按照设置的缓存模式去检查并使用（或不使用）缓存。
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		/*缓存模式有四种：
		1. LOAD_DEFAULT：默认的缓存使用模式。在进行页面前进或后退的操作时，如果缓存可用并未过期就优先加载缓存，否则从网络上加载数据。这样可以减少页面的网络请求次数。
		2. LOAD_CACHE_ELSE_NETWORK：只要缓存可用就加载缓存，哪怕它们已经过期失效。如果缓存不可用就从网络上加载数据。
		3. LOAD_NO_CACHE：不加载缓存，只从网络加载数据。
		4. LOAD_CACHE_ONLY：不从网络加载数据，只从缓存加载数据。
		通常我们可以根据网络情况将这几种模式结合使用，比如有网的时候使用LOAD_DEFAULT，离线时使用LOAD_CACHE_ONLY、LOAD_CACHE_ELSE_NETWORK，
		让用户不至于在离线时啥都看不到。*/
		
		//：启用或禁用数据库缓存。
		settings.setDatabaseEnabled(true);
		
		//：启用或禁用DOM缓存。
		settings.setDomStorageEnabled(true);
		
		
		// Enable Javascript
		settings.setJavaScriptEnabled(true);
		
		// Use WideViewport and Zoom out if there is no viewport defined
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		
		
		
		
		
		/*
		settings.setUserAgentString(String ua);//：设置WebView的UserAgent值。
		
		settings.setDefaultEncodingName(String encoding);//：设置编码格式，通常都设为“UTF - 8”。
		
		settings.setStandardFontFamily(String font);//：设置标准的字体族，默认“sans - serif”。
		
		settings.setCursiveFontFamily;//：设置草书字体族，默认“cursive”。
		
		settings.setFantasyFontFamily;//：设置CursiveFont字体族，默认“cursive”。
		
		settings.setFixedFontFamily;//：设置混合字体族，默认“monospace”。
		
		settings.setSansSerifFontFamily;//：设置梵文字体族，默认“sans - serif”。
		
		settings.setSerifFontFamily;//：设置衬线字体族，默认“sans - serif”
		
		settings.setDefaultFixedFontSize( int size);//：设置默认填充字体大小，默认16，取值区间为[1 - 72]，超过范围，使用其上限值。
		
		settings.setDefaultFontSize( int size);//：设置默认字体大小，默认16，取值区间[1 - 72]，超过范围，使用其上限值。
		
		settings.setMinimumFontSize;//：设置最小字体，默认8.取值区间[1 - 72]，超过范围，使用其上限值。
		
		settings.setMinimumLogicalFontSize;//：设置最小逻辑字体，默认8.取值区间[1 - 72]，超过范围，使用其上限值。
		
		*/
		
	}
	
	
	/**
	 * 动态全屏 隐藏状态栏
	 *
	 * @param enable
	 */
	private void fullScreen(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getActivity().getWindow().setAttributes(lp);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getActivity().getWindow().setAttributes(attr);
			getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	
	
	public void loadUrl(String url) {
		mWebView.loadUrl(url);
	}
}
