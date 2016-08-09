package com.zonsim.sendweibo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zonsim.sendweibo.widget.NineImageLayout;
import com.zonsim.sendweibo.widget.NineImageLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	private ListView mListView;
	private ArrayList<String> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.lv_weibo);
		
		
		mList = new ArrayList<>();
		
		mList.add("http://img4.imgtn.bdimg.com/it/u=2868470793,2681632895&fm=21&gp=0.jpg");
//		mList.add("http://preview.orderpic.com/chineseview039/east-ep-a11-419959.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg");
//		mList.add("http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg");
//		mList.add("http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg");
//		mList.add("http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg");
//		mList.add("http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=944538271,3669748807&fm=21&gp=0.jpg");
		mListView.setAdapter(new MyAdapter());
		NineImageLayout.setImageLoader(new UniversalImageLoader());
	}
	
	public void send(View view) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.replace(R.id.fl_container, new SendFragment());
		ft.addToBackStack(null);
		ft.commit();
		
	}
	
	private class MyAdapter extends BaseAdapter {
		
		MyNineImageAdapter adapter;
		
		@Override
		public int getCount() {
			return 10;
		}
		
		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = View.inflate(MainActivity.this, R.layout.item_weibo, null);
				holder = new ViewHolder();
				holder.layout = (NineImageLayout) convertView.findViewById(R.id.layout_nine_grid);
				holder.singleImage = (ImageView) convertView.findViewById(R.id.iv_single);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (mList.size() == 1) {
				holder.singleImage.setVisibility(View.VISIBLE);
				Glide.with(MainActivity.this).load(mList.get(0))
//						.placeholder(R.drawable.banner_default)
//						.error(R.drawable.banner_default)
						.diskCacheStrategy(DiskCacheStrategy.ALL)
						.into(holder.singleImage);
			} else {
				holder.singleImage.setVisibility(View.GONE);
				
				if (adapter == null) {
					adapter = new MyNineImageAdapter(MainActivity.this, mList);
					holder.layout.setAdapter(adapter);
				} else {
					adapter.setImageInfoList(mList);
					holder.layout.setAdapter(adapter);
				}
			}
			return convertView;
		}
		
		private class ViewHolder {
			NineImageLayout layout;
			ImageView singleImage;
		}
	}
	
	
	private class MyNineImageAdapter extends NineImageLayoutAdapter {
		public MyNineImageAdapter(Context context, List<String> imageInfoList) {
			super(context, imageInfoList);
		}
		
		@Override
		protected void onImageItemClick(Context context, NineImageLayout nineImageLayout, int position,
		                                List<String> imageInfoList) {
			System.out.println(imageInfoList.get(position));
		}
	}
	
	/**
	 * UniversalImageLoader加载
	 */
	private class UniversalImageLoader implements NineImageLayout.ImageLoader {
		@Override
		public void onDisplayImage(Context context, ImageView imageView, String url) {
			ImageLoaderUtil.displayImage(context, imageView, url, ImageLoaderUtil.getPhotoImageOption());
		}
		
		@Override
		public Bitmap getCacheImage(String url) {
			return null;
		}
	}
}
