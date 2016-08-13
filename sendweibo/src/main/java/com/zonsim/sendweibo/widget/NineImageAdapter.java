package com.zonsim.sendweibo.widget;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/9.
 */
public abstract class NineImageAdapter {
	
	protected Context mContext;
	private List<String> mImageInfoList;
	
	public NineImageAdapter(Context context, List<String> imageInfoList) {
		mContext = context;
		mImageInfoList = imageInfoList;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public List<String> getImageInfoList() {
		return mImageInfoList;
	}
	
	public void setImageInfoList(List<String> imageInfoList) {
		mImageInfoList = imageInfoList;
	}
	
	/**
	 * 如果要实现图片点击的逻辑，重写此方法即可
	 *
	 * @param context         上下文
	 * @param nineImageLayout 九宫格控件
	 * @param position        当前点击图片的的位置
	 * @param imageInfoList   图片地址的数据集合
	 */
	protected void onImageItemClick(Context context, NineImageLayout nineImageLayout, int position,
	                                List<String> imageInfoList) {
	}
	
	/**
	 * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
	 * 如果需要自定义图片展示效果，重写此方法即可
	 *
	 * @param context 上下文
	 * @return 生成的 ImageView
	 */
	protected ImageView createImageView(Context context) {
		ImageView imageView = new RatioImageView(context);
		if (mImageInfoList.size() > 1) {
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} 
		return imageView;
	}
	
}
