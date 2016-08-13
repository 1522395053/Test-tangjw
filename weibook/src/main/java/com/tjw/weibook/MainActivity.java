package com.tjw.weibook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tjw.weibook.bean.PostsListBean;
import com.tjw.weibook.fragment.SendFragment;
import com.tjw.weibook.util.MyToast;
import com.tjw.weibook.widget.NineImageAdapter;
import com.tjw.weibook.widget.NineImageLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	private ListView mListView;
	private ArrayList<String> mList;
	private List<PostsListBean.DataBean> mBeanList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.lv_weibo);

//		initData();
		
		mList = new ArrayList<>();

//		mList.add("http://img4.imgtn.bdimg.com/it/u=2868470793,2681632895&fm=21&gp=0.jpg");
//		mList.add("http://preview.orderpic.com/chineseview039/east-ep-a11-419959.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg");
		mList.add("http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg");
		mList.add("http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg");
//		mList.add("http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg");
//		mList.add("http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg");
//		mList.add("http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg");
//		mList.add("http://img4.imgtn.bdimg.com/it/u=944538271,3669748807&fm=21&gp=0.jpg");
		mListView.setAdapter(new MyAdapter());
	}
	
	private void initData() {
		OkHttpUtils.get()
				.url("http://192.168.1.233:8080/posts.json")
				.build()
				.execute(new StringCallback() {
					
					@Override
					public void onError(okhttp3.Call call, Exception e, int id) {
						
					}
					
					@Override
					public void onResponse(String response, int id) {
						System.out.println("response:-->" + response);
						Gson gson = new Gson();
						PostsListBean postsListBean = gson.fromJson(response, PostsListBean.class);
						mBeanList = postsListBean.getData();
						
						mListView.setAdapter(new MyAdapter());
					}
				});
		
		
	}
	
	public void send(View view) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.replace(R.id.fl_container, new SendFragment());
		ft.addToBackStack(null);
		ft.commit();
		
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return 10;
		}
		
		@Override
		public PostsListBean.DataBean getItem(int position) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = View.inflate(MainActivity.this, R.layout.item_weibo, null);
				holder = new ViewHolder();
				holder.layout = (NineImageLayout) convertView.findViewById(R.id.layout_nine_grid);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (mList.size() == 1) {
				holder.layout.setSingleImageWidth(600);
				holder.layout.setSingleImageHeight(400);
			}
			holder.layout.setAdapter(new MyNineImageAdapter(MainActivity.this, mList));
			
			return convertView;
		}
		
		private class ViewHolder {
			NineImageLayout layout;
		}
	}
	
	
	private class MyNineImageAdapter extends NineImageAdapter {
		public MyNineImageAdapter(Context context, List<String> imageInfoList) {
			super(context, imageInfoList);
		}
		
		@Override
		protected void onImageItemClick(Context context, NineImageLayout nineImageLayout, int position, List<String> imageInfoList) {
//			Toast.makeText(context, "点击了" + position, Toast.LENGTH_SHORT).show();
			MyToast.show(context,"点击了" + position);
		}
	}
}
