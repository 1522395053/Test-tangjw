package com.tjw.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/31.
 */
public class BottomBar extends LinearLayout {
	
	private ImageView mIvIcon;
	private TextView mTvName;
	private View mPoint;
	
	private Drawable[] mSelectImageSrc;
	private int[] mSelectTextColor;
	
	/**
	 * 图片选择器
	 *
	 * @param selectImageSrc 2个图片,0是正常状态,1是选中状态
	 */
	public void setSelectImageSrc(Drawable[] selectImageSrc) {
		mSelectImageSrc = selectImageSrc;
	}
	
	/**
	 * 图片选择器
	 *
	 * @param selectTextColor 2个颜色,0是正常状态,1是选中状态
	 */
	public void setSelectTextColor(int[] selectTextColor) {
		mSelectTextColor = selectTextColor;
	}
	
	public BottomBar(Context context) {
		this(context, null);
	}
	
	public BottomBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		LinearLayout floatLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_bottombar, null);
		addView(floatLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mIvIcon = (ImageView) floatLayout.findViewById(R.id.bottom_bar_icon);
		mTvName = (TextView) floatLayout.findViewById(R.id.bottom_bar_name);
		mPoint = floatLayout.findViewById(R.id.bottom_bar_point);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
		
		mTvName.setTextSize(typedArray.getDimension(R.styleable.BottomBar_textSize, 10.0f));
		mTvName.setText(typedArray.getString(R.styleable.BottomBar_text));
		
		mSelectTextColor = new int[]{
				typedArray.getColor(R.styleable.BottomBar_textColorNormal, 0X99999999),
				typedArray.getColor(R.styleable.BottomBar_textColorChecked, 0X00000000)
		};
		
		mSelectImageSrc = new Drawable[]{
				typedArray.getDrawable(R.styleable.BottomBar_srcNormal),
				typedArray.getDrawable(R.styleable.BottomBar_srcChecked)
		};
		
		setChecked(typedArray.getBoolean(R.styleable.BottomBar_checked, false));
		
		typedArray.recycle();
		
	}
	
	/**
	 * 设置是否显示小红点
	 *
	 * @param isShow true显示,false不显示
	 */
	public void showPoint(boolean isShow) {
		mPoint.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	
	/**
	 * 设置BottomBar是否被选中
	 *
	 * @param isChecked true选中,false未选中
	 */
	public void setChecked(boolean isChecked) {
		mIvIcon.setImageDrawable(isChecked ? mSelectImageSrc[1] : mSelectImageSrc[0]);
		mTvName.setTextColor(isChecked ? mSelectTextColor[1] : mSelectTextColor[0]);
	}
	
	/**
	 * 设置BottomBar的文字
	 *
	 * @param text     文字内容
	 * @param textSize 文字大小
	 */
	public void setTexts(String text, int textSize) {
		mTvName.setText(text);
		if (textSize > 0) {
			mTvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		}
	}
	
	/**
	 * 设置BottomBar的文字
	 *
	 * @param text 文字内容
	 */
	public void setTexts(String text) {
		this.setTexts(text, 0);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		onTouchEvent(ev);
		return super.onInterceptTouchEvent(ev);
	}
}
