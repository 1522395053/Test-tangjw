package com.tjw.textview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView mTextView = (TextView) findViewById(R.id.textView);
		// 构造多个超链接的html, 通过选中的位置来获取用户名  
		StringBuilder sbBuilder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sbBuilder.append("username-").append(i).append("、");
		}
		String likeUsers = sbBuilder.substring(0, sbBuilder.lastIndexOf("、"));
		mTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mTextView.setText(addClickablePart(likeUsers), TextView.BufferType.SPANNABLE);
		
	}
	private SpannableStringBuilder addClickablePart(String str) {
		// 第一个赞图标  
		ImageSpan span = new ImageSpan(this, R.mipmap.ic_launcher);
		SpannableString spanStr = new SpannableString("p.");
		spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
		ssb.append(str) ;
		String[] likeUsers = str.split("、");
		if (likeUsers.length > 0) {
			// 最后一个  
			for (final String name : likeUsers) {
				final int start = str.indexOf(name) + spanStr.length();
				ssb.setSpan(new ClickableSpan() {
					
					@Override
					public void onClick(View widget) {
						Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void updateDrawState(TextPaint ds) {
						super.updateDrawState(ds);
						 ds.setColor(Color.BLUE); // 设置文本颜色  
						// 去掉下划线  
						ds.setUnderlineText(false);
//						ds.setColorFilter()
					}
					
				}, start, start + name.length(), 0);
			}
		}
		return ssb.append("等" + likeUsers.length + "个人赞了您.");
	} // end of addClickablePart
	
}  