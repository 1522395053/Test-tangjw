package com.tjw.weibook.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjw.weibook.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ^-^
 * Created by tang-jw on 8/14.
 */
public class NineGridViewGroup extends ViewGroup {
	
	/**
	 * 默认的九宫格间隙
	 */
	private int mGridSpacing = 10;
	/**
	 * 九宫格每格的边长
	 */
	private int mGridLength;
	/**
	 * childView的集合
	 */
	private List<ImageView> mChildViewList;
	
	private List<String> mImageUrlList;
	
	private NineGridAdapter mAdapter;
	
	private int mColumnCount;    // 列数
	private int mRowCount;       // 行数
	private int mSingleWidth;
	private int mSingleHeight;
	
	public NineGridViewGroup(Context context) {
		super(context);
		mChildViewList = new ArrayList<>();
	}
	
	public NineGridViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mChildViewList = new ArrayList<>();
	}
	
	public NineGridViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mChildViewList = new ArrayList<>();
	}
	
	public void setAdapter(@NonNull NineGridAdapter adapter) {
		mAdapter = adapter;
		initAdapter();
	}
	
	
	//计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高 
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if (mImageUrlList == null || mImageUrlList.isEmpty()) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		//获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		//计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		//记录如果是wrap_content是设置的宽和高
		int width = 0;
		int height = 0;
		
		int childWidth = 0;
		int childHeight = 0;
		
		
		if (mImageUrlList.size() == 1) {
			ImageView childView = (ImageView) getChildAt(0);
			childWidth = childView.getMeasuredWidth();
			childHeight = childView.getMeasuredHeight();
			width = childWidth;
			height = childHeight;
			setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
					heightMode == MeasureSpec.EXACTLY ? heightSize : height);
			return;
		}
		mGridLength = Math.max(mGridLength, ((widthSize - getPaddingLeft() - getPaddingRight() - 2 * mGridSpacing) / 3));
		
		width = mGridLength * mColumnCount + mGridSpacing * (mColumnCount - 1) + getPaddingLeft() + getPaddingRight();
		height = mGridLength * mRowCount + mGridSpacing * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();
		
		setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
				heightMode == MeasureSpec.EXACTLY ? heightSize : height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mImageUrlList == null || mImageUrlList.isEmpty()) {
			return;
		}
		int childCount = mImageUrlList.size();
//		int childCount = getChildCount();
		
		int childWidth = 0;
		int childHeight = 0;
		
		
		int childLeft = 0, childTop = 0;
		if (childCount == 1) {
			final ImageView singleImageView = (ImageView) getChildAt(0);
			singleImageView.setVisibility(VISIBLE);
			onDisplayImage(singleImageView, mImageUrlList.get(0));
			
			
			childHeight = singleImageView.getMeasuredHeight();
			childWidth = singleImageView.getMeasuredWidth();
			System.out.println(mImageUrlList.get(0));
			System.out.println(childWidth + "-----" + childHeight);
			if (childWidth != 0 && childHeight != 0 && mGridLength != 0) {
				if (childWidth > childHeight) {
					
						childHeight = 3 * mGridLength  / 2;
						childWidth = 2 * mGridLength;
					
				} else if (childWidth < childHeight) {
						
						childHeight = 2 * mGridLength;
						childWidth = 3 * mGridLength  / 2;
					
				} else {
					childWidth = childHeight = 3 * mGridLength / 2;
				}
			}
			singleImageView.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + childWidth, getPaddingTop() + childHeight);
			
			return;
		}
		
		//遍历所有childView根据其宽和高，以及margin进行布局
		getChildAt(0).setVisibility(GONE);
		for (int i = 0; i < childCount; i++) {
			ImageView childView = (ImageView) getChildAt(i + 1);
			
			onDisplayImage(childView, mImageUrlList.get(i));
			
			//每个ImageView所在行的位置0,1,2 (x轴坐标)
			int rowNum = i / mColumnCount;
			//每个ImageView所在列的位置0,1,2 (y轴坐标)
			int columnNum = i % mColumnCount;
			//每个ImageView的上下左右位置 -->例如: 第1张图位置是 0*前一个图的位置 + padding值
			childLeft = (mGridLength + mGridSpacing) * columnNum + getPaddingLeft();
			childTop = (mGridLength + mGridSpacing) * rowNum + getPaddingTop();
			
			childView.layout(childLeft, childTop, childLeft + mGridLength, childTop + mGridLength);
		}
	}
	
	
	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		if (mAdapter == null) return;
		
		List<String> imageUrlList = mAdapter.getImageUrlList();
		if (imageUrlList == null || imageUrlList.isEmpty()) {
			setVisibility(GONE);
			return;
		} else {
			setVisibility(VISIBLE);
		}
		
		int imageCount = imageUrlList.size();
		
		if (imageCount > 9) {
			imageUrlList = imageUrlList.subList(0, 9);
			imageCount = 9;
		}
		if (imageCount == 4) {
			mRowCount = 2;
			mColumnCount = 2;
		} else {
			mRowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
			mColumnCount = imageCount > 3 ? 3 : imageCount;
		}
		mImageUrlList = null;
		
		//复用ImageView
		for (int i = 0; i < imageCount; i++) {
			ImageView iv = getImageView(i);
			if (iv == null) return;
			addView(iv, generateDefaultLayoutParams());
		}
		
		mImageUrlList = imageUrlList;
		requestLayout();
	}
	
	/**
	 * 获得 ImageView 保证了 ImageView 的重用
	 */
	private ImageView getImageView(final int position) {
		ImageView imageView;
		
		imageView = mAdapter.createImageView(getContext());
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapter.onImageItemClick(getContext(), NineGridViewGroup.this, position, mAdapter.getImageUrlList());
			}
		});
//			mChildViewList.add(imageView);
		
		return imageView;
	}
	
	
	private void onDisplayImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
	}
	
	
}
