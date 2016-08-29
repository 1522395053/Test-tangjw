package com.tjw.refreshlistview.rollviewpager.adapter;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tjw.refreshlistview.rollviewpager.HintView;
import com.tjw.refreshlistview.rollviewpager.RollPagerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Mr.Jude on 2016/1/9.
 */
public abstract class LoopPagerAdapter extends PagerAdapter{
    private RollPagerView mViewPager;

    private ArrayList<View> mViewList = new ArrayList<>();

    private class LoopHintViewDelegate implements RollPagerView.HintViewDelegate{
        @Override
        public void setCurrentPosition(int position, HintView hintView) {
            if (hintView!=null)
                hintView.setCurrent(position%getRealCount());
        }

        @Override
        public void initView(int length, int gravity, HintView hintView) {
            if (hintView!=null)
                hintView.initView(getRealCount(),gravity);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initPosition();
//        if (getCount() <= 1)return;
//        int half = Integer.MAX_VALUE/2;
//        int start = half - half%getRealCount();
//        mViewPager.getViewPager().setCurrentItem(start,false);
    }

    //一定要用这个回调,因为它只有第一次设置Adapter才会被回调。而除了这个时候去设置位置都是...ANR
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initPosition();
    }

    private void initPosition(){
        if (getCount() <= 1)return;
        int half = Integer.MAX_VALUE/2;
        int start = half - half%getRealCount();
        
        try {
            Field field = ViewPager.class.getDeclaredField("mCurItem");
            field.setAccessible(true);
            //index 为我们想要的第一次就展示的页面index
            field.set(mViewPager.getViewPager(), start);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoopPagerAdapter(RollPagerView viewPager){
        this.mViewPager = viewPager;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container,int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container, int position);

    @Deprecated
    @Override
    public final int getCount() {
        return getRealCount()<=1?getRealCount():Integer.MAX_VALUE;
    }

    public abstract int getRealCount();
}
