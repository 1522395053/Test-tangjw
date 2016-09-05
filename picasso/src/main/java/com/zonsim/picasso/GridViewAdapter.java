package com.zonsim.picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

/**
 * CopyRight
 * Created by tang-jw on 2016/9/5.
 */
public class GridViewAdapter extends BaseAdapter {
	
	private final Context context;
	private final String[] urls;
	
	public GridViewAdapter(Context context, String[] urls) {
		this.context = context;
		this.urls = urls;
	}
	
	@Override
	public int getCount() {
		return urls.length;
	}
	
	@Override
	public String getItem(int i) {
		return urls[i];
	}
	
	@Override
	public long getItemId(int i) {
		return i;
	}
	
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		SquaredImageView imageView = (SquaredImageView) view;
		if (imageView == null) {
			imageView = new SquaredImageView(context);
//			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		
		Picasso.with(context)
				.load(getItem(i))
				.transform(new FilletTransform(40, 20))
				.placeholder(R.mipmap.ic_launcher)
				.error(R.mipmap.ic_launcher)
				.fit()
				.tag(context)
				.into(imageView);
		
		return imageView;
	}
}
