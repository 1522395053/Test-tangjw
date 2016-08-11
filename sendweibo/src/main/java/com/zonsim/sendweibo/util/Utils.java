package com.zonsim.sendweibo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/10.
 */
public class Utils {
	public static void setImageViewMathParent(Context context,
	                                          ImageView image, Bitmap bitmap) {
		
		//获得ImageView的参数类
		ViewGroup.LayoutParams vgl = image.getLayoutParams();
		
		if (bitmap == null) {
			return;
		}
		//获取bitmap的宽度
		float bitWidth = bitmap.getWidth();
		//获取bitmap的宽度
		float bithight = bitmap.getHeight();
		
		//计算出图片的宽高比，然后按照图片的比列去缩放图片
		float bitScalew = bitWidth / bithight;
		//如果是图片的宽大于高  则采用屏幕的五分之三设置图片的宽 高按照比例计算
		float imgWidth = getScreenWith(context) * 3 / 5;
		//如果是图片的高大于宽  则采用屏幕的三分之一设置图片的高 宽按照比例计算
		float imgHight = getScreenHeight(context) * 1 / 3;
		//如果图片宽度大于高度
		if (bitWidth > bithight) {
			vgl.width = (int) imgWidth;
			vgl.height = (int) (imgWidth / bitScalew);
			
		} else {
			//当图片的高度大于宽度的
			vgl.width = (int) (imgHight * bitScalew);
			vgl.height = (int) imgHight;
			
		}
		//设置图片充满ImageView控件
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//等比例缩放
		image.setAdjustViewBounds(true);
		image.setLayoutParams(vgl);
		image.setImageBitmap(bitmap);
		
		
	}
//下面是获取屏幕宽度和高度的方法，最好放在跟屏幕相关的工具类
	
	public static int getScreenWith(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
}
