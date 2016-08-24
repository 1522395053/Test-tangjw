package com.tjw.refreshlistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.tjw.refreshlistview.rlistview.RListView;

public class MainActivity extends AppCompatActivity implements RListView.RListViewListener {
	
	private RListView mRListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mRListView = (RListView) findViewById(R.id.rlv_listview);
		
		
		mRListView.setAdapter(new MyAdapter());
		mRListView.setRListViewListener(this);
		mRListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println(position);
			}
		});
	}
	
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mRListView.stopRefresh();
			}
		}, 1000L);
	}
	
	@Override
	public void onLoadMore() {
		
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return 10;
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
			if (convertView == null) {
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_listview, parent, false);
			}
			return convertView;
		}
	}
}
