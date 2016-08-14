package com.tjw.weibook.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by HMY
 */
public class ImageLoaderUtil {
	
	public static ImageLoader getImageLoader(Context context) {
		return ImageLoader.getInstance();
	}
	
	public static DisplayImageOptions getPhotoImageOption() {
		Integer extra = 1;
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
//				.showImageForEmptyUri(R.drawable.banner_default).showImageOnFail(R.drawable.banner_default)
//				.showImageOnLoading(R.drawable.banner_default)
//				.extraForDownloader(extra)
//				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();
		return options;
	}
	
	public static void displayImage(Context context, @NonNull ImageView imageView, String url, DisplayImageOptions options) {
		getImageLoader(context).displayImage(url, imageView, options);
	}
	
	public static void displayImage(Context context, @NonNull ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
		getImageLoader(context).displayImage(url, imageView, options, listener);
	}
}
