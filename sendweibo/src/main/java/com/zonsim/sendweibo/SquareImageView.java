package com.zonsim.sendweibo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * CopyRight
 * Created by tang-jw on 2016/6/20.
 */
public class SquareImageView extends ImageView {
	public SquareImageView(Context context) {
		super(context);
	}
	
	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		/*int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int mScale = 1;
		
		if (width < (int) (mScale * height + 0.5)) {
			width = (int) (mScale * height + 0.5);
		} else {
			height = (int) (width / mScale + 0.5);
		}
		
		super.onMeasure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
		);
		*/
		
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, width);
		
	}
}
