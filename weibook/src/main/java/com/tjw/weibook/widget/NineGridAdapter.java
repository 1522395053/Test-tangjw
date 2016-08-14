package com.tjw.weibook.widget;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/9.
 */
public abstract class NineGridAdapter {
	
	protected Context mContext;
	private List<String> mImageUrlList;
	
	public NineGridAdapter(Context context, List<String> imageInfoList) {
		mContext = context;
		mImageUrlList = imageInfoList;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public List<String> getImageUrlList() {
		return mImageUrlList;
	}
	
	public void setImageUrlList(List<String> imageUrlList) {
		mImageUrlList = imageUrlList;
	}
	
	/**
	 * 如果要实现图片点击的逻辑，重写此方法即可
	 *
	 * @param context         上下文
	 * @param nineGridViewGroup 九宫格控件
	 * @param position        当前点击图片的的位置
	 * @param imageUrlList   图片地址的数据集合
	 */
	protected void onImageItemClick(Context context, NineGridViewGroup nineGridViewGroup, int position,
	                                List<String> imageUrlList) {
	}
	
	/**
	 * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
	 * 如果需要自定义图片展示效果，重写此方法即可
	 *
	 * @param context 上下文
	 * @return 生成的 ImageView
	 */
	protected ImageView createImageView(Context context) {
		RatioImageView imageView = new RatioImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setRatio(1f);
		
		return imageView;
	}
	
}
