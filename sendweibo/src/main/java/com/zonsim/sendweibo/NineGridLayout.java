package com.zonsim.sendweibo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/5.
 */
public class NineGridLayout extends ViewGroup {
	
	private static final float DEFAULT_SPACING = 3f;
	
	protected Context mContext;
	private float mSpacing;
	private int mColumns;
	private int mRows;
	private int mTotalWidth;
	private int mSingleWidth;
	
	private boolean mIsFirst;
	private List<String> mUrlList;
	
	public NineGridLayout(Context context) {
		this(context, null);
	}
	
	public NineGridLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}
	
	public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
		mSpacing = typedArray.getDimension(R.styleable.NineGridLayout_sapcing, DEFAULT_SPACING);
		typedArray.recycle();
		init(context);
	}
	
	private void init(Context context) {
		mContext = context;
		mIsFirst = true;
		mUrlList = new ArrayList<>();
		if (getListSize(mUrlList) == 0) {
			setVisibility(GONE);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mTotalWidth = right - left;
		mSingleWidth = (int) ((mTotalWidth - mSpacing * 2) / 3);
		if (mIsFirst) {
			refreshLayout();
			mIsFirst = false;
		}
	}
	
	/**
	 * 设置间隔
	 *
	 * @param spacing
	 */
	public void setSpacing(float spacing) {
		mSpacing = spacing;
	}
	
	
	public void setUrlList(List<String> urlList) {
		if (getListSize(urlList) == 0) {
			setVisibility(GONE);
			return;
		}
		setVisibility(VISIBLE);
		
		mUrlList.clear();
		mUrlList.addAll(urlList);
		
		if (!mIsFirst) {
			refreshLayout();
		}
	}
	
	public void refreshLayout() {
		removeAllViews();
		int size = getListSize(mUrlList);
		if (size > 0) {
			setVisibility(VISIBLE);
		} else {
			setVisibility(GONE);
			return;
		}
		
		if (size == 1) {
			String url = mUrlList.get(0);
			RatioImageView imageView = createImageView(0, mUrlList.get(0));
			
			//避免在ListView中一张图未加载成功时，布局高度受其他item影响
			LayoutParams params = getLayoutParams();
			params.height = mSingleWidth;
			setLayoutParams(params);
			imageView.layout(0, 0, mSingleWidth, mSingleWidth);
			
			displaySingleImage(imageView, url, mTotalWidth);
			
			return;
		}
		
		setChildrenLayout(size);
		layoutParams();
		
		for (int i = 0; i < size; i++) {
			String url = mUrlList.get(i);
			RatioImageView imageView = createImageView(i, url);
			layoutImageView(imageView, i, url);
		}
	}
	
	private void layoutParams() {
		int singleHeight = mSingleWidth;
		
		//根据子view数量确定高度
		LayoutParams params = getLayoutParams();
		params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
		setLayoutParams(params);
	}
	
	private RatioImageView createImageView(final int i, final String url) {
		RatioImageView imageView = new RatioImageView(mContext);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickImage(i, url, mUrlList);
			}
		});
		return imageView;
	}
	
	/**
	 * @param imageView
	 * @param url
	 */
	private void layoutImageView(RatioImageView imageView, int i, String url) {
		final int singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
		int singleHeight = singleWidth;
		
		int[] position = findPosition(i);
		int left = (int) ((singleWidth + mSpacing) * position[1]);
		int top = (int) ((singleHeight + mSpacing) * position[0]);
		int right = left + singleWidth;
		int bottom = top + singleHeight;
		
		imageView.layout(left, top, right, bottom);
		addView(imageView);
		displayImage(imageView, url);
	}
	
	private int[] findPosition(int childNum) {
		int[] position = new int[2];
		for (int i = 0; i < mRows; i++) {
			for (int j = 0; j < mColumns; j++) {
				if ((i * mColumns + j) == childNum) {
					position[0] = i;//行
					position[1] = j;//列
					break;
				}
			}
		}
		return position;
	}
	
	/**
	 * 根据图片个数确定行列数量
	 *
	 * @param size 图片url的集合
	 */
	private void setChildrenLayout(int size) {
		if (size <= 3) {
			mRows = 1;
			mColumns = size;
		} else if (size <= 6) {
			mRows = 2;
			if (size == 4) {
				mColumns = 2;
			} else {
				mColumns = 3;
			}
		} else {
			mColumns = 3;
			mRows = 3;
		}
	}
	
	private void setOneImageLayoutParams(RatioImageView imageView, int width, int height) {
		imageView.setLayoutParams(new LayoutParams(width, height));
		imageView.layout(0, 0, width, height);
		
		LayoutParams params = getLayoutParams();
//        params.width = width;
		params.height = height;
		setLayoutParams(params);
	}
	
	private int getListSize(List<String> list) {
		if (list == null || list.size() == 0) {
			return 0;
		}
		return list.size();
	}
	
	private int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		Paint.FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}
	
	/**
	 * @param imageView
	 * @param url
	 * @param parentWidth 父控件宽度
	 * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
	 */
	protected boolean displaySingleImage(final RatioImageView imageView, String url, final int parentWidth) {
		
		ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
			
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				
				int newW;
				int newH;
				if (h > w * 3) {                //h:w = 5:3
					newW = parentWidth / 2;
					newH = newW * 5 / 3;
				} else if (h < w) {             //h:w = 2:3
					newW = parentWidth * 2 / 3;
					newH = newW * 2 / 3;
				} else {                        //newH:h = newW :w
					newW = parentWidth / 2;
					newH = h * newW / w;
				}
				setOneImageLayoutParams(imageView, newW, newH);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
		return false;
	}
	
	protected void displayImage(RatioImageView imageView, String url) {
		ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
	}
	
	protected void onClickImage(int i, String url, List<String> urlList) {
		Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
	}
}