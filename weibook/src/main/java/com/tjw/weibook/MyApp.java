package com.tjw.weibook;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
		
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(5000L, TimeUnit.MILLISECONDS)
				.readTimeout(5000L, TimeUnit.MILLISECONDS)
				//其他配置
				.build();
		
		OkHttpUtils.initClient(okHttpClient);
		
		Logger.init("weibook").methodCount(3).logLevel(LogLevel.FULL);
		
	}
}
