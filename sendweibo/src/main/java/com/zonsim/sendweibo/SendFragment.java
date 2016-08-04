package com.zonsim.sendweibo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/2.
 */
public class SendFragment extends android.support.v4.app.Fragment {
	
	private GridView mGridView;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_send, container, false);
		mGridView = (GridView) view.findViewById(R.id.gv_img);
		
		mGridView.setAdapter(new MyAdapter());
		
		
		return view;
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return 9;
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
				convertView = View.inflate(getContext(), R.layout.item_img, null);
			}
			
			return convertView;
		}
	}
}
