package com.tjw.weibook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjw.weibook.R;
import com.tjw.weibook.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/8.
 */
public class NineImageLayout extends ViewGroup {
	
	private int mSingleImageSize = 250;              // 单张图片时的最大大小,单位dp
	private float mSingleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
	private int mGridSpacing = 3;                    // 宫格间距，单位dp
	
	private int columnCount;    // 列数
	private int rowCount;       // 行数
	private int mGridWidth;      // 宫格宽度
	private int mGridHeight;     // 宫格高度
	
	private int mSingleImageWidth;      // 单一图片宽度
	
	private int mSingleImageHeight;     // 单一图片高度
	
	private List<ImageView> mImageViewList;
	private List<String> mImageUrls;
	
	private NineImageAdapter mAdapter;
	
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
	
	public void setSingleImageHeight(int singleImageHeight) {
		mSingleImageHeight = singleImageHeight;
	}
	
	public void setSingleImageWidth(int singleImageWidth) {
		mSingleImageWidth = singleImageWidth;
	}
	
	
	public void setAdapter(@NonNull NineImageAdapter adapter) {
		mAdapter = adapter;
		initAdapter();
	}
	
	/**
	 * 初始化
	 *
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineImageLayout);
		mGridSpacing = (int) typedArray.getDimension(R.styleable.NineImageLayout_gridSpacing, mGridSpacing);
		//dp-->px
		mSingleImageSize = (int) typedArray.getDimension(R.styleable.NineImageLayout_singleImageSize, mSingleImageSize);
		mSingleImageRatio = typedArray.getFloat(R.styleable.NineImageLayout_singleImageRatio, mSingleImageRatio);
		
		typedArray.recycle();
		
		mImageViewList = new ArrayList<>();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = 0;
		int totalWidth = width - getPaddingLeft() - getPaddingRight();
		
		mGridWidth = mGridHeight = (totalWidth - mGridSpacing * 2) / 3;
		if (mImageUrls != null && mImageUrls.size() > 0) {
			if (mImageUrls.size() == 1 && mSingleImageHeight != 0 && mSingleImageWidth != 0) {
				
				if (mSingleImageWidth > totalWidth) {
					mSingleImageHeight = totalWidth * mSingleImageHeight / mSingleImageWidth;
					mSingleImageWidth = totalWidth;
				} else if (mSingleImageWidth < 3 * mGridWidth / 2) {
					mSingleImageHeight = 3 * mGridWidth / 2 * mSingleImageHeight / mSingleImageWidth;
					mSingleImageWidth = 3 * mGridWidth / 2;
				}
				if (mSingleImageHeight > 3 * mGridWidth) {
					mSingleImageHeight = 3 * mGridWidth;
				} else if (mSingleImageHeight < 4 * mGridWidth / 3) {
					mSingleImageWidth = 4 * mGridWidth / 3 * mSingleImageWidth / mSingleImageHeight;
					mSingleImageWidth = mSingleImageWidth > totalWidth ? totalWidth : mSingleImageWidth;
					mSingleImageHeight = 4 * mGridWidth / 3;
				}
				
				width = totalWidth;
				height = mSingleImageHeight + getPaddingTop() + getPaddingBottom();
			} else {
				width = mGridWidth * columnCount + mGridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
				height = mGridHeight * rowCount + mGridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
			}
		}
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mImageUrls == null) return;
		int childrenCount = mImageUrls.size();
		if (childrenCount == 1 && mSingleImageHeight != 0 && mSingleImageWidth != 0) {
			ImageView childImageView = (ImageView) getChildAt(0);
			onDisplayImage(childImageView, mImageUrls.get(0));
			childImageView.layout(0, 0, mSingleImageWidth, mSingleImageHeight);
		} else {
			for (int i = 0; i < childrenCount; i++) {
				ImageView childImageView = (ImageView) getChildAt(i);
				if (childImageView != null) {
					onDisplayImage( childImageView, mImageUrls.get(i));
					
					//每个ImageView所在行的位置0,1,2 (x轴坐标)
					int rowNum = i / columnCount;
					//每个ImageView所在列的位置0,1,2 (y轴坐标)
					int columnNum = i % columnCount;
					//每个ImageView的上下左右位置 -->例如: 第1张图位置是 0*前一个图的位置 + padding值
					int left = (mGridWidth + mGridSpacing) * columnNum + getPaddingLeft();
					int top = (mGridHeight + mGridSpacing) * rowNum + getPaddingTop();
					int right = left + mGridWidth;
					int bottom = top + mGridHeight;
					childImageView.layout(left, top, right, bottom);
				}
			}
		}
	}
	
	
	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		if (mAdapter == null) return;
		
		List<String> imageInfoList = mAdapter.getImageInfoList();
		if (imageInfoList == null || imageInfoList.isEmpty()) {
			setVisibility(GONE);
			return;
		} else {
			setVisibility(VISIBLE);
		}
		
		int imageCount = imageInfoList.size();
		
		int maxImageSize = 9;
		if (imageCount > maxImageSize) {
			imageInfoList = imageInfoList.subList(0, maxImageSize);
			imageCount = imageInfoList.size();
		}
		
		if (imageCount == 4) {
			rowCount = 2;
			columnCount = 2;
		} else {
			rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
			columnCount = 3;
		}
		
		//复用ImageView
		if (mImageUrls == null) {
			for (int i = 0; i < imageCount; i++) {
				ImageView iv = getImageView(i);
				if (iv == null) return;
				addView(iv, generateDefaultLayoutParams());
			}
		} else {
			int oldViewCount = mImageUrls.size();
			int newViewCount = imageCount;
			if (oldViewCount > newViewCount) {
				removeViews(newViewCount, oldViewCount - newViewCount);
			} else if (oldViewCount < newViewCount) {
				for (int i = oldViewCount; i < newViewCount; i++) {
					ImageView iv = getImageView(i);
					if (iv == null) return;
					addView(iv, generateDefaultLayoutParams());
				}
			}
		}
		
		mImageUrls = imageInfoList;
		requestLayout();
	}
	
	/**
	 * 获得 ImageView 保证了 ImageView 的重用
	 */
	private ImageView getImageView(final int position) {
		ImageView imageView;
		if (position < mImageViewList.size()) {
			imageView = mImageViewList.get(position);
		} else {
			imageView = mAdapter.createImageView(getContext());
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mAdapter.onImageItemClick(getContext(), NineImageLayout.this, position, mAdapter.getImageInfoList());
				}
			});
			mImageViewList.add(imageView);
		}
		return imageView;
	}
	
	
	private void onDisplayImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
	}
}
