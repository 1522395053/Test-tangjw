package com.zonsim.sendweibo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zonsim.sendweibo.R;
import com.zonsim.sendweibo.util.ViewHolder;
import com.zonsim.sendweibo.util.LocalImageLoager;
import com.zonsim.sendweibo.widget.RatioImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/2.
 */
public class SendFragment extends android.support.v4.app.Fragment {
	
	private GridView mGridView;
	private TextView mTvSend;
	private TextView mTvCancle;
	private HashMap<Integer, String> mImgPathMap;
	private MyAdapter mAdapter;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_send, container, false);
		mGridView = (GridView) view.findViewById(R.id.gv_img);
		mTvSend = (TextView) view.findViewById(R.id.tv_send);
		mTvSend = (TextView) view.findViewById(R.id.tv_send);
		mTvCancle = (TextView) view.findViewById(R.id.tv_cancel);
		
		mImgPathMap = new HashMap<>();
		
		mAdapter = new MyAdapter();
		
		mGridView.setAdapter(mAdapter);
		
		mTvSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Map.Entry<Integer, String> entry : mImgPathMap.entrySet()) {
					System.out.println("键:" + entry.getKey() + ", 值:" + entry.getValue());
				}
			}
		});
		
		mTvCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		
		
		return view;
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return mImgPathMap.size() == 9 ? 9 : mImgPathMap.size() + 1;
		}
		
		@Override
		public String getItem(int position) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.item_img, null);
			}
			RatioImageView img = ViewHolder.get(convertView, R.id.siv_img);
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pickImage(position);
				}
			});
			ImageView del = ViewHolder.get(convertView, R.id.iv_close);
			if (mImgPathMap.size() > 0 && position < mImgPathMap.size()) {
				img.setImageBitmap(LocalImageLoager.parseFile(mImgPathMap.get(position), img.getWidth(), img.getHeight(), null, Bitmap.Config.RGB_565));
				del.setVisibility(View.VISIBLE);
				del.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for (int i = position; i < mImgPathMap.size(); i++) {
							mImgPathMap.put(i, mImgPathMap.get(i + 1));
						}
						mImgPathMap.remove(mImgPathMap.size() - 1);
						notifyDataSetChanged();
					}
				});
			} else {
				img.setImageResource(R.drawable.compose_pic_add_highlighted);
				del.setVisibility(View.GONE);
			}
			return convertView;
		}
	}
	
	
	private void pickImage(int requestCode) {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, requestCode);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		String path = uri2path(data.getData());
		
		if (requestCode < 9 && requestCode >= 0) {
			System.out.println("所选图片的真实路径: " + path);
			mImgPathMap.put(requestCode, path);
			mAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * Uri 转 path
	 *
	 * @param uri
	 * @return
	 */
	public String uri2path(Uri uri) {
		String path = "";
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(column_index);
			cursor.close();
		}
		return path;
	}
}
