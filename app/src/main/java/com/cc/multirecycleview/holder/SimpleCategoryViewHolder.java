package com.cc.multirecycleview.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.multirecycleview.R;
import com.cc.multirecycleview.adapter.CategoryPagerAdapter;
import com.cc.multirecycleview.bean.CategoryBean;
import com.cc.multirecycleview.bean.CategoryProBean;
import com.cc.multirecycleview.view.CategoryView;
import com.cc.multirecycleview.view.CategoryViewImp;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * 服务页面底部列表页面holder
 *
 * @author 陈聪 2020-05-06 15:34
 */
public class SimpleCategoryViewHolder extends CategoryViewHolder {

    /** 流式布局 */
    public static final int CATEGORY_TYPE_FLOW = -3;
    /** 列表布局 */
    public static final int CATEGORY_TYPE_LIST = -2;
    /** GRID布局 */
    public static final int CATEGORY_TYPE_GRID = -1;
    /** 无数据id */
    public static final int CATEGORY_TYPE_UNLOAD_DATA = -10000;

    public SimpleCategoryViewHolder(@NonNull final View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof ArrayList) {
            ArrayList<CategoryBean> categoryMenusResponses = (ArrayList<CategoryBean>) data;
            if (!hadLoadData) {
                hadLoadData = true;
                if (categoryMenusResponses == null || categoryMenusResponses.size() == 0) {
                    return;
                }
                viewList.clear();
                if (cacheVies.size() > categoryMenusResponses.size()) {
                    mViewPager.removeAllViews();
                }
                for (CategoryBean bean : categoryMenusResponses) {
                    CategoryView categoryView = cacheVies.get(bean.getType());
                    if (categoryView == null || categoryView.getParent() != mViewPager) {
                        categoryView = new CategoryViewImp(itemView.getContext(), (CategoryProBean) bean, onLoadMoreDataListener);
                    } else {
                        categoryView.setCategoryData((CategoryProBean) bean);
                    }
                    cacheVies.put(bean.getType(), categoryView);
                    viewList.add(categoryView);
                }
                mCurrentRecyclerView = viewList.get(0).getRecycleView();
                mViewPager.setAdapter(new CategoryPagerAdapter(viewList, categoryMenusResponses));
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        viewList.get(position).onUserVisibleChange(true);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setCurrentItem(0);
                viewList.get(0).onUserVisibleChange(true);
            }
        }
    }
}
