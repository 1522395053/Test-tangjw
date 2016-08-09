package com.zonsim.sendweibo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 根据宽高比例自动计算高度ImageView
 */
public class RatioImageView extends ImageView {
	
	/**
	 * 宽高比例
	 */
	private float mRatio;
	
	public RatioImageView(Context context) {
		this(context, null);
	}
	
	public RatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
		mRatio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);
		typedArray.recycle();
	}
	
	/**
	 * 设置ImageView的宽高比
	 *
	 * @param ratio
	 */
	public void setRatio(float ratio) {
		mRatio = ratio;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (mRatio != 0) {
			float height = width / mRatio;
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		setClickable(true);
		Drawable drawable = getDrawable();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (drawable != null) {
					drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				
			case MotionEvent.ACTION_UP:
				if (drawable != null) {
					drawable.clearColorFilter();
				}
				break;
		}
		
		return super.onTouchEvent(event);
	}
	
	
}
