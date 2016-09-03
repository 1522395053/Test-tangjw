package com.tjw.refreshlistview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.tjw.refreshlistview.rlistview.RListView;

/**
 * ^-^
 * Created by tang-jw on 9/3.
 */
public class ContactsActivity extends AppCompatActivity {
	
	private RListView mListView;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_contacts);
		
		mListView = (RListView) findViewById(R.id.rlv_listview);
		
		mListView.setAdapter(new MyAdapter());
		
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
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(ContactsActivity.this, R.layout.item_timeline, null);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.ll_month = (LinearLayout) convertView.findViewById(R.id.ll_month);
			if (position % 2 != 0) {
				viewHolder.ll_month.setVisibility(View.GONE);
			}
			return convertView;
		}
		
		class ViewHolder {
			LinearLayout ll_month;
		}
	}
}
