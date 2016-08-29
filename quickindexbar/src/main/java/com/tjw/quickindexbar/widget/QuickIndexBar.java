package com.tjw.quickindexbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tjw.quickindexbar.R;

/**
 * ^-^
 * Created by tang-jw on 8/28.
 */
public class QuickIndexBar extends View {
	
	private OnLetterUpdateListener onLetterUpdateListener;
	
	public interface OnLetterUpdateListener {
		void onLetterUpdate(String letter);
	}
	
	public OnLetterUpdateListener getOnLetterUpdateListener() {
		return onLetterUpdateListener;
	}
	
	public void setOnLetterUpdateListener(
			OnLetterUpdateListener onLetterUpdateListener) {
		this.onLetterUpdateListener = onLetterUpdateListener;
	}
	
	/**
	 * 默认的26个大写字母
	 */
	private static final String[] LETTERS = new String[]{
			"A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X",
			"Y", "Z"
	};
	
	private Paint mPaint;
	/**
	 * 每个字母所在格子的宽度
	 */
	private int mCellWidth;
	/**
	 * 每个字母所在格子的高度,不能用int类型, 否则底部会留空白
	 */
	private float mCellHeight;
	private Rect mBounds;
	
	private int mLastIndex = -1;
	
	public QuickIndexBar(Context context) {
		this(context, null);
	}
	
	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
		
		float textSize = typedArray.getDimension(R.styleable.QuickIndexBar_textSize, 30.0f);
		String strColor = typedArray.getString(R.styleable.QuickIndexBar_textColor);
		typedArray.recycle();
		
		// new 一个抗锯齿的画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// 设置字体 
		mPaint.setTypeface(Typeface.DEFAULT);
		
		if (!TextUtils.isEmpty(strColor)) {
			mPaint.setColor(Color.parseColor(strColor));
		} else {
			mPaint.setColor(Color.BLACK);
		}
		
		mPaint.setTextSize(textSize);
		
		mBounds = new Rect();
		
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mCellWidth = getMeasuredWidth();
		mCellHeight = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) * 1.0f / LETTERS.length;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (int i = 0; i < LETTERS.length; i++) {
			String letter = LETTERS[i];
			
			float x = mCellWidth * 0.5f - mPaint.measureText(letter) * 0.5f;
			
			mPaint.getTextBounds(letter, 0, letter.length(), mBounds);
			
			float y = mCellHeight * 0.5f + mBounds.height() * 0.5f + i * mCellHeight + getPaddingTop();

//			mPaint.setColor(i == mLastIndex ? Color.GRAY : Color.WHITE);
//			System.out.println("x-->" + x + "\ny-->" + y);
			canvas.drawText(letter, x, y, mPaint);
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y;
		int currentIndex;
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 获取被点击到的字母索引
				y = event.getY();
				// 根据y值, 计算当前按下的字母位置
				currentIndex = (int) (y / mCellHeight);
				if (currentIndex != mLastIndex) {
					if (currentIndex >= 0 && currentIndex < LETTERS.length) {
						String letter = LETTERS[currentIndex];
						if (onLetterUpdateListener != null) {
							onLetterUpdateListener.onLetterUpdate(letter);
						}
//						System.out.println("letter: " + letter);
						// 记录上一次触摸的字母
						mLastIndex = currentIndex;
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				// 获取被点击到的字母索引
				y = event.getY();
				// 根据y值, 计算当前按下的字母位置
				currentIndex = (int) (y / mCellHeight);
				if (currentIndex != mLastIndex) {
					if (currentIndex >= 0 && currentIndex < LETTERS.length) {
						String letter = LETTERS[currentIndex];
						if (onLetterUpdateListener != null) {
							onLetterUpdateListener.onLetterUpdate(letter);
						}
						System.out.println("letter: " + letter);
						// 记录上一次触摸的字母
						mLastIndex = currentIndex;
					}
				}
				
				break;
			case MotionEvent.ACTION_UP:
				mLastIndex = -1;
				break;
			default:
				break;
		}
		invalidate();
		
		return super.onTouchEvent(event);
	}
}
