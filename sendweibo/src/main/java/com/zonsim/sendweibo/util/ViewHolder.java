package com.zonsim.sendweibo.util;

import android.util.SparseArray;
import android.view.View;

/**
 * CopyRight
 * Created by tang-jw on 2016/7/18.
 */
public class ViewHolder {
	// I added a generic return type to reduce the casting noise in client code  
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}