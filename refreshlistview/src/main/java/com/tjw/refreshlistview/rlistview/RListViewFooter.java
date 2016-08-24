package com.tjw.refreshlistview.rlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tjw.refreshlistview.R;

public class RListViewFooter extends FrameLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	
	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	
	public RListViewFooter(Context context) {
		super(context);
		initView();
	}
	
	public RListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public RListViewFooter(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}
	
	private void initView() {
		View inflate = LayoutInflater.from(getContext()).inflate(R.layout.rlistview_footer, null);
		addView(inflate);
		inflate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = inflate.findViewById(R.id.xlistview_footer_content);
		mProgressBar = inflate.findViewById(R.id.xlistview_header_progressbar);
		mHintView = (TextView) inflate.findViewById(R.id.xlistview_footer_hint_textview);
		
	}
	
	public void setState(int state) {
		
		if (state == STATE_READY) {
			mHintView.setText("松开加载更多");
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
			mHintView.setText("加载中....");
		} else {
			mProgressBar.setVisibility(View.GONE);
			mHintView.setText("更多");
		}
	}
	
	public void setBottomMargin(int height) {
		if (height < 0) return;
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	/**
	 * normal status
	 */
	public void normal() {
		mProgressBar.setVisibility(View.GONE);
	}
	
	
	/**
	 * loading status
	 */
	public void loading() {
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	
	public TextView getmHintView() {
		return mHintView;
	}
}
