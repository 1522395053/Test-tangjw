package com.tjw.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.jude.rollviewpager.RollPagerView;

/**
 * ^-^
 * Created by tang-jw on 8/24.
 */
public class BannerView extends FrameLayout {
	
	private RollPagerView mRollPagerView;
	
	public BannerView(Context context) {
		this(context,null);
	}
	
	public BannerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		View inflate = LayoutInflater.from(getContext()).inflate(R.layout.viewpager_header, null);
		addView(inflate);
		inflate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mRollPagerView = (RollPagerView) inflate.findViewById(R.id.rvp_banner);
		
		
	}
}
