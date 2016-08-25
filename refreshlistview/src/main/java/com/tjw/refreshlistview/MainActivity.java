package com.tjw.refreshlistview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.tjw.refreshlistview.rlistview.RListView;

public class MainActivity extends AppCompatActivity implements RListView.RListViewListener {
	
	private RListView mRListView;
	private RollPagerView mRollPagerView;
	
	private int[] mImgs = new int[0];
	private MyBannerAdapter mBannerAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mRListView = (RListView) findViewById(R.id.rlv_listview);
//		mRListView.setPullLoadEnable(true);
		BannerView bannerView = new BannerView(this);
		
		mRListView.addHeaderView(bannerView);
		mRListView.setAdapter(new MyAdapter());
		mRListView.setRListViewListener(this);
		mRListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println(position);
			}
		});
		
		
		mRollPagerView = (RollPagerView) bannerView.findViewById(R.id.rvp_banner);
		mRollPagerView.setPlayDelay(3000);
//		mRollPagerView.setAnimationDurtion(1000);
		//mRollPagerView.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
		mRollPagerView.setHintView(new ColorPointHintView(this, Color.RED, Color.WHITE));
//		mRollPagerView.setHintView(new TextHintView(this));
//		mRollPagerView.setHintView(null);
		mBannerAdapter = new MyBannerAdapter(mRollPagerView);
		mRollPagerView.setAdapter(mBannerAdapter);
		
		mImgs = new int[]{
				R.drawable.img1,
				R.drawable.img2
		};
		mBannerAdapter.add(mImgs);
		
	}
	
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mImgs = new int[]{
						R.drawable.img1,
						R.drawable.img2,
						R.drawable.img3,
						R.drawable.img4
				};
				mBannerAdapter.add(mImgs);
				mRListView.stopRefresh();
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
			return 30;
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
//				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_listview, parent, false);
				convertView = View.inflate(MainActivity.this, R.layout.item_listview, null);
			}
			return convertView;
		}
	}
	
	private class MyBannerAdapter extends LoopPagerAdapter {
		
		private int[] mImgs = new int[0];
		private int mCount = 0;
		
		public void add(int[] imgs) {
			mImgs = imgs;
			mCount = mImgs.length;
			notifyDataSetChanged();
		}
		
		public MyBannerAdapter(RollPagerView viewPager) {
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
}
