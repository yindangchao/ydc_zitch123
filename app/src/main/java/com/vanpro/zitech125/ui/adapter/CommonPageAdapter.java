package com.vanpro.zitech125.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Jinsen on 16/4/10.
 */
public class CommonPageAdapter extends PagerAdapter {
    private static final String TAG = CommonPageAdapter.class.getName();
    private List<View> views;

    public CommonPageAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        try {
            container.addView(view);
        } catch (Exception e) {
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
