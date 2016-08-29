package com.tjw.quickindexbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tjw.quickindexbar.domain.PersonName;
import com.tjw.quickindexbar.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
	
	private ListView mListView;
	private ArrayList<PersonName> mPersonNames;
	private QuickIndexBar mIndexBar;
	private TextView mTv_Index_Toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.lv_contact);
		mIndexBar = (QuickIndexBar) findViewById(R.id.quickindexbar);
		mTv_Index_Toast = (TextView) findViewById(R.id.tv_index_toast);
		
		mPersonNames = new ArrayList<>();
		
		for (int i = 0; i < Cheeses.NAMES.length; i++) {
			mPersonNames.add(new PersonName(Cheeses.NAMES[i]));
		}
		
		Collections.sort(mPersonNames);
		
		mListView.setAdapter(new MyAdapter());
		
		mIndexBar.setBackgroundResource(R.drawable.selector_indexbar_bg);
		
		mIndexBar.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
			@Override
			public void onLetterUpdate(String letter) {
				showLetter(letter);
				for (int i = 0; i < mPersonNames.size(); i++) {
					String l = mPersonNames.get(i).getPinyin().charAt(0) + "";
					if(TextUtils.equals(letter, l)){
						// 找到第一个首字母是letter条目.
						mListView.setSelection(i);
						break;
					}
				}
			}
		});
		
	}
	
	private Handler mHandler = new Handler();
	
	/**
	 * 在屏幕中间显示一个字母提示
	 * @param letter
	 */
	protected void showLetter(String letter) {
		mTv_Index_Toast.setText(letter);
		mTv_Index_Toast.setVisibility(View.VISIBLE);
		
		// 移除之前的延时操作
		mHandler.removeCallbacksAndMessages(null);
		// 延时两秒消除
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mTv_Index_Toast.setVisibility(View.GONE);
			}
			
		}, 2000);
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return mPersonNames.size();
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(MainActivity.this, R.layout.item_contact, null);
				viewHolder = new ViewHolder();
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			
			PersonName personName = mPersonNames.get(position);
			
			String currentInitial = personName.getPinyin().charAt(0) + "";
			String tempInitial = null;
			if (position == 0) {
				tempInitial = currentInitial;
				viewHolder.tv_index.setVisibility(View.VISIBLE);
			} else {
				viewHolder.tv_index.setVisibility(View.GONE);
				if (!currentInitial.equals(mPersonNames.get(position - 1).getPinyin().charAt(0) + "")) {
					tempInitial = currentInitial;
					viewHolder.tv_index.setVisibility(View.VISIBLE);
				}
			}
			
			viewHolder.tv_index.setText(tempInitial);
			viewHolder.tv_name.setText(personName.getName());
			
			return convertView;
		}
		
		
	}
	
	private class ViewHolder {
		protected TextView tv_index;
		protected TextView tv_name;
	}
}
