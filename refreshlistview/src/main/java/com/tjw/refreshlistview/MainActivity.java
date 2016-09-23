package com.tjw.refreshlistview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tjw.refreshlistview.rlistview.RListView;
import com.tjw.refreshlistview.rollviewpager.RollPagerView;
import com.tjw.refreshlistview.rollviewpager.Util;
import com.tjw.refreshlistview.rollviewpager.adapter.LoopPagerAdapter;
import com.tjw.refreshlistview.rollviewpager.hintview.ColorPointHintView;

public class MainActivity extends AppCompatActivity implements RListView.RListViewListener {
	
	private RListView mRListView;
	
	private int[] mImgss = new int[0];
	private MyBannerAdapter mBannerAdapter;
	private RollPagerView mRollPagerView;
	private LinearLayout mTitleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//		    this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		    this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//			getWindow().addFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
		
		mTitleBar = (LinearLayout) findViewById(R.id.ll_title_bar);
		
		mRListView = (RListView) findViewById(R.id.rlv_listview);
//		mRListView.setPullLoadEnable(true);
		
		initListener();
		
		mRollPagerView = new RollPagerView(this);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				Util.dip2px(this, 180f));
		mRollPagerView.setLayoutParams(params);
		
		mRListView.addHeaderView(mRollPagerView);
		mRListView.setAdapter(new MyAdapter());
		mRListView.setPullLoadEnable(true);
		mRListView.setRListViewListener(this);
		mRListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyToast.show(MainActivity.this, "点击了" + position);
			}
		});
		
		
		mRollPagerView.setPlayDelay(3000);
		mRollPagerView.setAnimationDurtion(1000);
		//mRollPagerView.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
		mRollPagerView.setHintView(new ColorPointHintView(this, Color.RED, Color.WHITE));
//		mRollPagerView.setHintView(new TextHintView(this));
//		mRollPagerView.setHintView(null);
		mBannerAdapter = new MyBannerAdapter(mRollPagerView);
		
		mRollPagerView.setAdapter(mBannerAdapter);
		mImgss = new int[]{
				R.drawable.img1,
				R.drawable.img2
		};
		mBannerAdapter.add(mImgss);
	}
	
	
	
	@Override
	public void onRefresh() {
		mRollPagerView.pause();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				mImgss = new int[]{
						R.drawable.img1,
						R.drawable.img2,
						R.drawable.img3,
						R.drawable.img4
				};
				mBannerAdapter.add(mImgss);
				mRListView.stopRefresh();
				mRollPagerView.resume();
			}
		}, 2000L);
	}
	
	@Override
	public void onLoadMore() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mRListView.stopLoadMore();
			}
		}, 2000L);
	}
	
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return 20;
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
				convertView = View.inflate(MainActivity.this, R.layout.item_listview, null);
			}
			return convertView;
		}
		
	}
	private class MyBannerAdapter extends LoopPagerAdapter {
		
		private int[] mImgs = new int[0];
		
		private int mCount = 0;
		private void add(int[] imgs) {
			mImgs = imgs;
			mCount = imgs.length;
			notifyDataSetChanged();
		}
		
		private MyBannerAdapter(RollPagerView viewPager) {
			super(viewPager);
		}
		
		@Override
		public View getView(ViewGroup container, int position) {
			ImageView view = new ImageView(container.getContext());
			view.setImageResource(mImgs[position]);
			view.setScaleType(ImageView.ScaleType.CENTER_CROP);
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return view;
		}
		
		@Override
		public int getRealCount() {
			return mCount;
		}
		
	}
	
	
	private void fullScreen(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	
	
	
	
	private void initListener() {
		mRListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int deltaHeight = -mRollPagerView.getTop();
				
//				System.out.println("高度" + deltaHeight);
				
				if (deltaHeight > 200) {
					mTitleBar.setVisibility(View.VISIBLE);
//					fullScreen(false);
				} else {
					mTitleBar.setVisibility(View.INVISIBLE);
//					fullScreen(true);
				} 
				
			}
		
		});
		
		
	}
}
