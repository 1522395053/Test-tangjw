package com.zonsim.sendweibo;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * ^-^
 * Created by tang-jw on 8/7.
 */
public class MyApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);
		
//		OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .addInterceptor(new LoggerInterceptor("TAG"))
//				.connectTimeout(5000L, TimeUnit.MILLISECONDS)
//				.readTimeout(5000L, TimeUnit.MILLISECONDS)
//				//其他配置
//				.build();
//		
//		OkHttpUtils.initClient(okHttpClient);
		
	}
}
