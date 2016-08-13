package com.tjw.weibook.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tjw.weibook.MainActivity;
import com.tjw.weibook.R;
import com.tjw.weibook.util.MyToast;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * ^-^
 * Created by tang-jw on 8/13.
 */
public class LoginActivity extends Activity {
	
	/**
	 * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
	 */
	private SsoHandler mSsoHandler;
	
	/**
	 * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
	 */
	private Oauth2AccessToken mAccessToken;
	
	/**
	 * 显示认证后的信息，如 AccessToken
	 */
	private TextView mTokenText;
	
	private AuthInfo mAuthInfo;
	
	/**
	 * 用户信息接口
	 */
	private UsersAPI mUsersAPI;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		// 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
		mTokenText = (TextView) findViewById(R.id.token_text_view);
		
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
		
		
		// SSO 授权, 仅客户端
		findViewById(R.id.obtain_token_via_sso).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorizeClientSso(new AuthListener());
			}
		});
		
		// SSO 授权, 仅Web
		findViewById(R.id.obtain_token_via_web).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorizeWeb(new AuthListener());
			}
		});
		// 获取用户信息
		findViewById(R.id.btn_getUserInfo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取当前已保存过的 Token
				if (mAccessToken == null) {
					mAccessToken = AccessTokenKeeper.readAccessToken(LoginActivity.this);
				}
				// 获取用户信息接口
				mUsersAPI = new UsersAPI(LoginActivity.this, Constants.APP_KEY, mAccessToken);
				
				//String uid = mAccessToken.getUid();
				long uid = Long.parseLong(mAccessToken.getUid());
				mUsersAPI.show(uid, mListener);
			}
		});
		// SSO 授权, 仅Web
		findViewById(R.id.btn_getAll).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
		});
		
	}
	
	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 *
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
	}
	
	/**
	 * 微博认证授权回调类。
	 * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
	 * 该回调才会被执行。
	 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
	 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {
		
		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			//从这里获取用户输入的 电话号码信息 
			String phoneNum = mAccessToken.getPhoneNum();
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				updateTokenView(false);
				
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
				MyToast.show(LoginActivity.this, "授权成功!");
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "授权失败!";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				MyToast.show(LoginActivity.this, message);
			}
		}
		
		@Override
		public void onCancel() {
			MyToast.show(LoginActivity.this, "取消授权");
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
			MyToast.show(LoginActivity.this, "Auth exception : " + e.getMessage());
		}
	}
	
	
	/**
	 * 显示当前 Token 信息。
	 *
	 * @param hasExisted 配置文件中是否已存在 token 信息并且合法
	 */
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(
				new java.util.Date(mAccessToken.getExpiresTime()));
		String format = "Token：%1$s \n有效期：%2$s";
		mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
		Logger.i(mAccessToken.getToken());
		
		String message = String.format(format, mAccessToken.getToken(), date);
		if (hasExisted) {
			message = "Token 仍在有效期内，无需再次登录。" + "\n" + message;
		}
		mTokenText.setText(message);
	}
	
	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				Logger.json(response);
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					Logger.i(user.avatar_hd);
					MyToast.show(LoginActivity.this, "获取User信息成功，用户昵称：" + user.screen_name);
				} else {
					MyToast.show(LoginActivity.this, response);
				}
			}
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			if (info != null) {
				MyToast.show(LoginActivity.this, info.toString());
			}
		}
	};
}
