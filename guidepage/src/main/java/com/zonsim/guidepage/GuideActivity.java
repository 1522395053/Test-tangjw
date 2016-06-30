package com.zonsim.guidepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * CopyRight
 * Created by tang-jw on 2016/6/30.
 */
public class GuideActivity extends Activity {
	private ViewPager mViewPager;
	
	private ArrayList<ImageView> imageViews;
	
	private int[] mImageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
	private ImageView iv;
	private LinearLayout llContainer;
	private ImageView ivRedPoint;   
	private int mPointDis;
	private Button btnStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		
		btnStart = (Button) findViewById(R.id.btn_start);
		
		initData();
		
		mViewPager.setAdapter(new GuideAdapter());  
		
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin = leftMargin;
				ivRedPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageSelected(int position) {
				if (position == imageViews.size() - 1) {
					btnStart.setVisibility(View.VISIBLE);
				} else {
					btnStart.setVisibility(View.INVISIBLE);
				}
			}
			@Override
			public void onPageScrollStateChanged(int state) {
				//页面状态发生变化的回调,不滑->滑动,滑动->不滑
			}
		});
		
		//监听layout方法结束的事件, 位置确定好之后再获取圆点间距
		//视图树,添加一个全局的layout的监听
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//移除监听, 避免重复回调
				ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//layout方法执行结束的回调
				mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();

			}
		});
		
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//更新sp,不是第一次
//				PrefUtils.setBoolean(this, "is_first_enter", false);
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
		
		
	}
	
	//初始化数据
	private void initData() {
		
		imageViews = new ArrayList<>();
		LinearLayout.LayoutParams params;
		
		for (int i = 0; i < mImageIds.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(mImageIds[i]);   //background图片填充父窗体
			
			imageViews.add(iv);
			
			//初始化小圆点
			ImageView point = new ImageView(this);
			//设置图片
			point.setImageResource(R.drawable.shape_point_red);
			
			//初始化布局参数,宽高WRAP_CONTENT,父控件是谁,就是谁声明的布局参数
			params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			if (i > 0) {
				//设置左边距,从第二个点开始
				params.leftMargin = DensityUtils.dip2px(this, 10);
			}
			
			point.setLayoutParams(params);
			
			//给容器添加圆点
			llContainer.addView(point);
			
		}
	}
	
	class GuideAdapter extends PagerAdapter {
		//item的个数
		@Override
		public int getCount() {
			return imageViews.size();
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		//初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = imageViews.get(position);
			container.addView(view);
			
			return view;
		}
		
		//销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
