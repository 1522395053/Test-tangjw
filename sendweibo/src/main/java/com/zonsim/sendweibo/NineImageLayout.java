package com.zonsim.sendweibo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/8.
 */
public class NineImageLayout extends ViewGroup {
	
	/**
	 * 默认间距
	 */
	private static final float DEFAULT_SPACING = 0f;
	private Context mContext;
	/**
	 * 图片的url集合
	 */
	private List<String> mUrlList;
	/**
	 * 该布局的总宽度
	 */
	private int mTotalWidth;
	/**
	 * 每格图片的间距
	 */
	private float mSpacing;
	/**
	 * 每格的宽度
	 */
	private int mSingleWidth;
	
	public NineImageLayout(Context context) {
		this(context, null);
	}
	
	public NineImageLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public NineImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineImageLayout);
		
		mSpacing = typedArray.getDimension(R.styleable.NineImageLayout_spacing, DEFAULT_SPACING);
		
		typedArray.recycle();
		
		init(context);
	}
	
	/**
	 * 初始化
	 *
	 * @param context
	 */
	private void init(Context context) {
		mContext = context;
		mUrlList = new ArrayList<>();
		if (mUrlList.size() == 0) {
			setVisibility(GONE);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mTotalWidth = r - l;
		
		mSingleWidth = (int) (mTotalWidth - 2 * mSpacing) / 3;
		
		refreshViewGroup();
	}
	
	/**
	 * 刷新布局
	 */
	private void refreshViewGroup() {
		removeAllViews();
		
		if (mUrlList.size() > 0) {
			setVisibility(VISIBLE);
		} else {
			setVisibility(GONE);
		}
		
		//只有一张图
		if (mUrlList.size() == 1) {
			System.out.println(mUrlList.get(0));
			RatioImageView imageView = createImage(0);
			//给图片设置默认的宽高, 防止受到其他item的影响
			
			LayoutParams params = getLayoutParams();
			params.height = mSingleWidth;
			setLayoutParams(params);
			
			imageView.layout(0, 0, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			loadSingleImage(imageView, mUrlList.get(0), mTotalWidth);
			addView(imageView);
			return;
		}
		generateChildrenLayout(mUrlList.size());
		layoutParams();
		
		for (int i = 0; i < mUrlList.size(); i++) {
			String url = mUrlList.get(i);
			RatioImageView imageView= createImage(i);
		
			layoutImageView(imageView, i, url);
				
		}
		
		
	}
	
	private int mColumns;
	private int mRows;
	/**
	 * 根据图片个数确定行列数量
	 *
	 * @param length
	 */
	private void generateChildrenLayout(int length) {
		if (length <= 3) {
			mRows = 1;
			mColumns = length;
		} else if (length <= 6) {
			mRows = 2;
			mColumns = 3;
			if (length == 4) {
				mColumns = 2;
			}
		} else {
			mColumns = 3;
			mRows = 3;
		}
	}
	
	private void layoutParams() {
		int singleHeight = mSingleWidth;
		
		//根据子view数量确定高度
		LayoutParams params = getLayoutParams();
		params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
		setLayoutParams(params);
	}
	
	/**
	 * 显示单一图片
	 */
	private void loadSingleImage(final RatioImageView imageView, String url, final int parentWidth) {
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
				if (h > w * 3) {//h:w = 5:3
					newW = parentWidth / 2;
					newH = newW * 5 / 3;
				} else if (h < w) {//h:w = 2:3
					newW = parentWidth * 2 / 3;
					newH = newW * 2 / 3;
				} else {//newH:h = newW :w
					newW = parentWidth / 2;
					newH = h * newW / w;
				}
				setOneImageLayoutParams(imageView, newW, newH);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
	}
	
	private void setOneImageLayoutParams(RatioImageView imageView, int newW, int newH) {
		imageView.setLayoutParams(new LayoutParams(newW, newH));
		imageView.layout(0, 0, newW, newH);
		
		LayoutParams params = getLayoutParams();
//        params.width = width;
		params.height = newH;
		setLayoutParams(params);
	}
	
	/**
	 * 创建一个ImageView
	 *
	 * @param position 图片位置
	 */
	private RatioImageView createImage(final int position) {
		RatioImageView imageView = new RatioImageView(mContext);
		
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("所点击的图片:" + position);
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
		
		ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
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
	
	public void setUrlList(List<String> urlList) {
		if (urlList.size() == 0) {
			setVisibility(GONE);
			return;
		}
		setVisibility(VISIBLE);
		
		mUrlList.clear();
		mUrlList.addAll(urlList);
	}
}
