package com.zonsim.calendersign.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zonsim.calendersign.R;

public class CalendarSignView extends LinearLayout implements OnClickListener,OnItemClickListener {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private GridView mGv_Calendar;
	private TextView mTv_Current_Time;
	private CalendarViewAdapter mCalendarViewAdapter;
	private CalendarViewAdapter.onClickSignInCallBack mClickSignInCallBack;
	private int mCurrentTime ;
	public CalendarSignView(Context context, CalendarViewAdapter.onClickSignInCallBack callBack){
		super(context);
		this.mContext = context;
		this.mClickSignInCallBack = callBack;
		initView();
		initData();
	} 
	public CalendarSignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		initView();
		initData();
	}

	public CalendarSignView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		
		initView();
		initData();
	}

	public void initView(){
		mLayoutInflater = LayoutInflater.from(mContext);
		mLayoutInflater.inflate(R.layout.view_calendar,this);
		
		mTv_Current_Time = (TextView)findViewById(R.id.tv_current_time);
		mGv_Calendar = (GridView)findViewById(R.id.gv_calendar);
	}

	public void initData(){
		mTv_Current_Time.setText(Utils.getTime(System.currentTimeMillis()));
		mCalendarViewAdapter = new CalendarViewAdapter(mContext, Utils.getCurrentMonthDay(),mTv_Current_Time);
		mCalendarViewAdapter.setonClickSignInCallBack(mClickSignInCallBack);
		mGv_Calendar.setAdapter(mCalendarViewAdapter);
		mGv_Calendar.setOnItemClickListener(this);
		getCurrentTimePosition();
	}
	@Override
	public void onClick(View v) {
		
	}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
			mCalendarViewAdapter.onRefresh(position, true);
			android.util.Log.e("POSITION", getCurrentTimePosition() + "");
	}
	
	public void setOnClickSignInCallBack(CalendarViewAdapter.onClickSignInCallBack callBack){
		this.mClickSignInCallBack = callBack;
		mCalendarViewAdapter.setonClickSignInCallBack(mClickSignInCallBack);	
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public int  getCurrentTime(){
		   return Utils.getDayOfMonth();
	}
	
	/**
	 * 获取当前日前的位置
	 * @return
	 */
	public int getCurrentTimePosition(){
		int position ;
		if (Utils.getCurrentMonthStart() == 7) {
			position = 0;
		}else
			position= Utils.getCurrentMonthStart();
		return    getCurrentTime() + 7 + position ; 
		
	}
}
