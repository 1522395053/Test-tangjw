package com.zonsim.calendersign.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zonsim.calendersign.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewAdapter extends BaseAdapter {

	private Context mContext;
	private int mCountDay = 49;
	private int mCurrent_mouth_Countday;
	private int mCurrent_Week;
	private int id[] = { R.string.week7, R.string.week1, R.string.week2,
			R.string.week3, R.string.week4, R.string.week5, R.string.week6 };
	private ViewHolder mViewHolder;
	private List<Boolean> booleans;
	private HuahuaLoadPop mLoadPop;
	private View mView;
	private onClickSignInCallBack mCallBack;
	public CalendarViewAdapter(Context context, int countday, View view) {
		this.mContext = context;
		this.mCurrent_Week = Utils.getCurrentMonthStart();
		this.mCurrent_mouth_Countday = countday;
		booleans = new ArrayList<Boolean>();
		mView = view;
		mLoadPop = new HuahuaLoadPop(mContext, mView);
		for (int i = 0; i < mCountDay; i++) {
			booleans.add(i, false);
		}
	}

	public void setonClickSignInCallBack(onClickSignInCallBack callBack){
		this.mCallBack = callBack;
	}
	@Override
	public int getCount() {
		return mCountDay;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_calendar, null);
			mViewHolder.mTv_calendar_day = (TextView) convertView
					.findViewById(R.id.tv_calendar_day);
			mViewHolder.mFl_calendar = (FrameLayout)convertView.findViewById(R.id.fl_calendar);
			convertView.setTag(mViewHolder);
		} else
			mViewHolder = (ViewHolder) convertView.getTag();
		if (position <= 6) {
			mViewHolder.mTv_calendar_day.setTextColor(Color.BLACK);
			mViewHolder.mTv_calendar_day.setTextSize(mContext.getResources()
					.getDimension(R.dimen.text_size_7));
			mViewHolder.mTv_calendar_day.setText(mContext.getResources().getString(
					id[position]));
		} else {
			if (mCurrent_Week == 7 && (position -6) <= mCurrent_mouth_Countday) {
				mViewHolder.mTv_calendar_day.setText(position-6 + "");
			} else if (position -7>= mCurrent_Week
					&& position - mCurrent_Week -6 <= mCurrent_mouth_Countday) {
				mViewHolder.mTv_calendar_day.setText(position - mCurrent_Week -6
						+ "");
				
			}
		}
		if (position % 7 == 6) {
			mViewHolder.mFl_calendar.setBackgroundResource(R.drawable.line_right);
		}else if (position % 7 == 0) {
			mViewHolder.mFl_calendar.setBackgroundResource(R.drawable.line_left);
		}
		
		if (booleans.get(position)) {
			mViewHolder.mTv_calendar_day.setBackground(mContext.getResources().getDrawable(R.drawable.icon_sign_in));
		}else {
			mViewHolder.mTv_calendar_day.setBackground(null);
		}
		return convertView;
	}

	public void onRefresh(int position ,Boolean isClick){
		if (position <= 6) {
		
		} else {
			if (mCurrent_Week == 7 && (position -6) <= mCurrent_mouth_Countday) {
				if (!booleans.get(position)) {
					booleans.set(position, isClick);
					mCallBack.onSucess();
				}
			} else if (position -7>= mCurrent_Week
					&& position - mCurrent_Week -6 <= mCurrent_mouth_Countday) {
				if (!booleans.get(position)) {
					booleans.set(position, isClick);
					mCallBack.onSucess();
				}
			}
		}
		notifyDataSetChanged();
	}
	public class ViewHolder {
		TextView mTv_calendar_day;
		FrameLayout mFl_calendar;
	}
	
	
	public interface onClickSignInCallBack {
		
		public void onSucess();
		public void onFail(String error);
		public void onTimeOut(String msg);
	}
}
