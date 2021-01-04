package com.cc.multirecycleview.adapter;

import android.view.View;
import android.view.ViewGroup;


import com.cc.multirecycleview.bean.CategoryBean;
import com.cc.multirecycleview.view.CategoryView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/**
 * 请描述使用该类使用方法！！！
 *
 * @author 陈聪 2020-05-06 16:05
 */
public class CategoryPagerAdapter extends PagerAdapter {

    private ArrayList<CategoryView> viewList = null;
    private ArrayList<CategoryBean> tabTitleList = null;

    public CategoryPagerAdapter(ArrayList<CategoryView> viewList, ArrayList<CategoryBean> tabTitleList) {
        this.viewList = viewList;
        this.tabTitleList = tabTitleList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewList.get(position);
        if (container == view.getParent()) {
            container.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position).getTitle();
    }

}
