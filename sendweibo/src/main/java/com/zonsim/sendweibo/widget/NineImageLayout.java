package com.zonsim.sendweibo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zonsim.sendweibo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/8.
 */
public class NineImageLayout extends ViewGroup {
	
	public static final int MODE_FILL = 0;          //填充模式，类似于微信
	public static final int MODE_GRID = 1;          //网格模式，类似于QQ，4张图会 2X2布局
	
	private static ImageLoader mImageLoader;        //全局的图片加载器(必须设置,否者不显示图片)
	
	private int mSingleImageSize = 250;              // 单张图片时的最大大小,单位dp
	private float mSingleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
	private int mMaxImageSize = 9;                   // 最大显示的图片数
	private int mGridSpacing = 3;                    // 宫格间距，单位dp
	private int mode = MODE_FILL;                   // 默认使用fill模式
	
	private int columnCount;    // 列数
	private int rowCount;       // 行数
	private int mGridWidth;      // 宫格宽度
	private int mGridHeight;     // 宫格高度
	
	private List<ImageView> mImageViewList;
	private List<ImageInfo> mImageInfoList;
//	private NineGridViewAdapter mAdapter;
	
	public NineImageLayout(Context context) {
		this(context, null);
	}
	
	public NineImageLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public NineImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}
	
	/**
	 * 初始化
	 *
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineImageLayout);
		mGridSpacing = (int) typedArray.getDimension(R.styleable.NineImageLayout_gridSpacing, mGridSpacing);
		
		mSingleImageSize = (int) typedArray.getDimension(R.styleable.NineImageLayout_singleImageSize, mSingleImageSize);
		mSingleImageRatio = typedArray.getFloat(R.styleable.NineImageLayout_singleImageRatio, mSingleImageRatio);
		
		mMaxImageSize = typedArray.getInt(R.styleable.NineImageLayout_maxSize, mMaxImageSize);
		
		typedArray.recycle();
		
		mImageViewList = new ArrayList<>();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = 0;
		int totalWidth = width - getPaddingLeft() - getPaddingRight();
		
		if (mImageInfoList != null && mImageInfoList.size() > 0) {
			
			if (mImageInfoList.size() == 1) {
				
				mGridWidth = mSingleImageSize > totalWidth ? totalWidth : mSingleImageSize;
				mGridHeight = (int) (mGridWidth / mSingleImageRatio);
				
				//矫正图片显示区域大小，不允许超过最大显示范围
				if (mGridHeight > mSingleImageSize) {
					float ratio = mSingleImageSize * 1.0f / mGridHeight;
					mGridWidth = (int) (mGridWidth * ratio);
					mGridHeight = mSingleImageSize;
				}
			} else {
//                mGridWidth = mGridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
				//这里无论是几张图片，宽高都按总宽度的 1/3
				mGridWidth = mGridHeight = (totalWidth - mGridSpacing * 2) / 3;
			}
			width = mGridWidth * columnCount + mGridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
			height = mGridHeight * rowCount + mGridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
		}
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
	}
	
}
