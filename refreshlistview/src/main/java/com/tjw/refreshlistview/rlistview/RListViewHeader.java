package com.tjw.refreshlistview.rlistview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tjw.refreshlistview.R;


public class RListViewHeader extends FrameLayout {
	
	private FrameLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;
	
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	
	private final int ROTATE_ANIM_DURATION = 180;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	public final static int STATE_SUCCESS = 3;
	private TextView xlistview_header_time;
	public LinearLayout mHeaderViewContent;
	public boolean mShowTimeViewFlag;
	
	public RListViewHeader(Context context) {
		super(context);
		initView(context);
	}
	
	public RListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public RListViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.rlistview_header, null);
		addView(mContainer, lp);
		
		mHeaderViewContent = (LinearLayout) findViewById(R.id.xlistview_header_content);
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
		xlistview_header_time = (TextView) findViewById(R.id.xlistview_header_time);
		if (mShowTimeViewFlag) {
			xlistview_header_time.setVisibility(VISIBLE);
		}
		
		
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		
	}
	
	public void setState(int state) {
		if (state == mState) return;
		if (state == STATE_REFRESHING) {    // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {    // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch (state) {
			case STATE_NORMAL:
				mArrowImageView.setImageResource(R.drawable.arrow_down);
				if (mState == STATE_READY) {
					mArrowImageView.startAnimation(mRotateDownAnim);
				}
				if (mState == STATE_REFRESHING) {
					mArrowImageView.clearAnimation();
				}
				mHintTextView.setText("下拉刷新");
				
				break;
			case STATE_READY:
				if (mState != STATE_READY) {
					mArrowImageView.clearAnimation();
					mArrowImageView.startAnimation(mRotateUpAnim);
					mHintTextView.setText("释放更新");
				}
				break;
			case STATE_REFRESHING:
				mHintTextView.setText("加载中....");
				break;
			case STATE_SUCCESS:
				mHintTextView.setText("加载中....");
				mArrowImageView.setVisibility(INVISIBLE);
				mProgressBar.setVisibility(VISIBLE);
				if (mShowTimeViewFlag) {
					SharedPreferences.Editor editor = preferences.edit();//获取编辑器
					editor.putLong(UPDATED_AT + mId, System.currentTimeMillis());
					editor.apply();//提交修改	
				}
				break;
		}
		mState = state;
	}
	
	
	public void setVisibleHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	
	public int getVisibleHeight() {
		return mContainer.getLayoutParams().height;
	}
	
	
	/**
	 * 一分钟的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MINUTE = 60 * 1000;
	
	/**
	 * 一小时的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	
	/**
	 * 一天的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;
	
	/**
	 * 一月的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;
	
	/**
	 * 一年的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;
	
	private SharedPreferences preferences;
	
	/**
	 * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	private int mId = -1;
	
	/**
	 * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
	 */
	private static final String UPDATED_AT = "updated_at";
	
	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	public void refreshUpdatedAtValue() {
		if (!mShowTimeViewFlag) return;
		//上次更新时间的毫秒值
		long lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = "从未更新";
		} else if (timePassed < 0) {
			updateAtValue = "时间error";
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = "刚刚更新";
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format("上次更新%1$s前", value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format("上次更新%1$s前", value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format("上次更新%1$s前", value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format("上次更新%1$s前", value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format("上次更新%1$s前", value);
		}
		xlistview_header_time.setText(updateAtValue);
	}
	
	public TextView getmHintTextView() {
		return mHintTextView;
	}
	
}
