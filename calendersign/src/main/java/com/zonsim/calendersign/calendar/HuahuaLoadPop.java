package com.zonsim.calendersign.calendar;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zonsim.calendersign.R;

public class HuahuaLoadPop{
	private PopupWindow mPopupWindow;
	private LayoutInflater mInflater;
	private View mLlMain;
	private Context mContext;
	
	public HuahuaLoadPop(Context context, View view){
		this.mContext = context;
		this.mLlMain = view;
		initPop();
	}
	
	public void initPop() {
		mInflater = LayoutInflater.from(mContext);
		View view = mInflater.inflate(R.layout.pop_loading, null);
		mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
	}
	public void showPop() {
		mPopupWindow.showAtLocation(mLlMain, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	public void dismissPop() {
		mPopupWindow.dismiss();
	}
	
	public void setAnimationStyle(int style){
		mPopupWindow.setAnimationStyle(style);
	}
	public void setOutSideTouchable(boolean b){
		mPopupWindow.setFocusable(b);
		mPopupWindow.setOutsideTouchable(b);
	}
}
