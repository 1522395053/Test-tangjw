package com.zonsim.sendweibo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zonsim.sendweibo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/8.
 */
public class NineImageLayout extends ViewGroup {
	
	private static ImageLoader mImageLoader;        //全局的图片加载器(必须设置,否者不显示图片)
	
	private int mSingleImageSize = 250;              // 单张图片时的最大大小,单位dp
	private float mSingleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
	private int mMaxImageSize = 9;                   // 最大显示的图片数
	private int mGridSpacing = 3;                    // 宫格间距，单位dp
	
	private int columnCount;    // 列数
	private int rowCount;       // 行数
	private int mGridWidth;      // 宫格宽度
	private int mGridHeight;     // 宫格高度
	private int mMaxSingleImageWidth;     // 单一图片最大宽度
	private int mMaxSingleImageHeight;     // 单一图片最大高度
	
	private List<ImageView> mImageViewList;
	private List<String> mImageInfoList;
	private NineImageLayoutAdapter mAdapter;
	private int mWidth1;
	private int mHeight1;
	
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
	 * set/get方法
	 */
	public static ImageLoader getImageLoader() {
		return mImageLoader;
	}
	
	public static void setImageLoader(ImageLoader ImageLoader) {
		mImageLoader = ImageLoader;
	}
	
	public int getSingleImageSize() {
		return mSingleImageSize;
	}
	
	public void setSingleImageSize(int singleImageSize) {
		mSingleImageSize = singleImageSize;
	}
	
	public float getSingleImageRatio() {
		return mSingleImageRatio;
	}
	
	public void setSingleImageRatio(float singleImageRatio) {
		mSingleImageRatio = singleImageRatio;
	}
	
	public int getGridSpacing() {
		return mGridSpacing;
	}
	
	public void setGridSpacing(int gridSpacing) {
		mGridSpacing = gridSpacing;
	}
	
	
	public void setAdapter(@NonNull NineImageLayoutAdapter adapter) {
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

//		mMaxImageSize = typedArray.getInt(R.styleable.NineImageLayout_maxSize, mMaxImageSize);
		
		
		typedArray.recycle();
		
		mImageViewList = new ArrayList<>();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = 0;
		int totalWidth = width - getPaddingLeft() - getPaddingRight();
		System.out.println(totalWidth);
		if (mImageInfoList != null && mImageInfoList.size() > 0) {
//			if (mImageInfoList.size() == 1) {
				mGridWidth = mSingleImageSize > totalWidth ? totalWidth : mSingleImageSize;
				mGridHeight = (int) (mGridWidth / mSingleImageRatio);
				
				//矫正图片显示区域大小，不允许超过最大显示范围
				if (mGridHeight > mSingleImageSize) {
					float ratio = mSingleImageSize * 1.0f / mGridHeight;
					mGridWidth = (int) (mGridWidth * ratio);
					mGridHeight = mSingleImageSize;
				}
				
				//*******************
				View singleView = getChildAt(0);
				LayoutParams params = singleView.getLayoutParams();
				if (params == null) {
					params = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
				}
				int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
				int lpHeight = params.height;
				int childHeightSpec;
				if (lpHeight > 0) {
					childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
				} else {
					childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				}
				singleView.measure(childWidthSpec, childHeightSpec);
				
				mWidth1 = singleView.getMeasuredWidth();
				mHeight1 = singleView.getMeasuredHeight();
//				setMeasuredDimension(mWidth1, mHeight1);
//				return;
				
				System.out.println(mWidth1+"---------"+mHeight1);
				//**************************************
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				//******************************************
				
//			} else {
//                mGridWidth = mGridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
				//这里无论是几张图片，宽高都按总宽度的 1/3
				mGridWidth = mGridHeight = (totalWidth - mGridSpacing * 2) / 3;
//			}
			width = mGridWidth * columnCount + mGridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
			height = mGridHeight * rowCount + mGridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
			System.out.println("width: " + width + ";  height: " + height);
		}
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mImageInfoList == null) return;
		int childrenCount = mImageInfoList.size();
		if (childrenCount == 1) {
			ImageView childImageView = (ImageView) getChildAt(0);
			System.out.println(0 + "---" + 0 + "---" + mWidth1 + "---" + mHeight1);
			childImageView.layout(0, 0, mWidth1, mHeight1);
			
			return;
		}
		for (int i = 0; i < childrenCount; i++) {
			ImageView childImageView = (ImageView) getChildAt(i);
			if (mImageLoader != null) {
				mImageLoader.onDisplayImage(getContext(), childImageView, mImageInfoList.get(i));
			}
			
			//每个ImageView所在行的位置0,1,2 (x轴坐标)
			int rowNum = i / columnCount;
			//每个ImageView所在列的位置0,1,2 (y轴坐标)
			int columnNum = i % columnCount;
			//每个ImageView的上下左右位置 -->例如: 第1张图位置是 0*前一个图的位置 + padding值
			int left = (mGridWidth + mGridSpacing) * columnNum + getPaddingLeft();
			int top = (mGridHeight + mGridSpacing) * rowNum + getPaddingTop();
			int right = left + mGridWidth;
			int bottom = top + mGridHeight;
			System.out.println(left + "---" + top + "---" + right + "---" + bottom);
			childImageView.layout(left, top, right, bottom);
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
		
		if (mMaxImageSize > 0 && imageCount > mMaxImageSize) {
			imageInfoList = imageInfoList.subList(0, mMaxImageSize);
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
		if (mImageInfoList == null) {
			for (int i = 0; i < imageCount; i++) {
				ImageView iv = getImageView(i);
				if (iv == null) return;
				addView(iv, generateDefaultLayoutParams());
			}
		} else {
			int oldViewCount = mImageInfoList.size();
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
		
		//修改最后一个条目，决定是否显示更多
		/*if (adapter.getImageInfo().size() > maxImageSize) {
			View child = getChildAt(maxImageSize - 1);
			if (child instanceof NineGridViewWrapper) {
				NineGridViewWrapper imageView = (NineGridViewWrapper) child;
				imageView.setMoreNum(adapter.getImageInfo().size() - maxImageSize);
			}
		}*/
		
		mImageInfoList = imageInfoList;
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
	
	/**
	 * ImageLoader接口,子类去实现
	 */
	public interface ImageLoader {
		/**
		 * 需要子类实现该方法，以确定如何加载和显示图片
		 *
		 * @param context   上下文
		 * @param imageView 需要展示图片的ImageView
		 * @param url       图片地址
		 */
		void onDisplayImage(Context context, ImageView imageView, String url);
		
		/**
		 * @param url 图片的地址
		 * @return 当前框架的本地缓存图片
		 */
		Bitmap getCacheImage(String url);
	}
	
}
