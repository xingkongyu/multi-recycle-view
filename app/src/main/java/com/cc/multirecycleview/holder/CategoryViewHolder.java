package com.cc.multirecycleview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.multirecycleview.R;
import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.bean.CategoryBean;
import com.cc.multirecycleview.view.CategoryView;
import com.cc.multirecycleview.view.ChildRecyclerView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * 页面底部列表页面holder
 * 如果需自定义配置，可继承此类
 * SimpleCategoryViewHolder为实现了基本几种类型的实现，可以参考或使用
 *
 * @author 陈聪 2020-05-06 15:34
 */
public abstract class CategoryViewHolder extends BaseHolder {

    /** viewpager */
    public ViewPager mViewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
    /** 列表view集合 */
    public ArrayList<CategoryView> viewList = new ArrayList<CategoryView>();
    /** 菜单栏 */
    public TabLayout mTabLayout = (TabLayout) itemView.findViewById(R.id.tabs);
    /** 缓存的列表 */
    public HashMap<String, CategoryView> cacheVies = new HashMap<String, CategoryView>();
    /** 当前显示的列表 */
    public ChildRecyclerView mCurrentRecyclerView = null;

    public CategoryViewHolder(@NonNull final View itemView) {
        super(itemView);
        initTabs(itemView);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (!viewList.isEmpty()) {
                    mCurrentRecyclerView = viewList.get(position).getRecycleView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化菜单栏
     * 需要菜单定制的可以重写此方法
     */
    public void initTabs(@NonNull final View itemView) {
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(itemView.getContext(), R.color.blue));
        mTabLayout.setTabTextColors(ContextCompat.getColor(itemView.getContext(), R.color.black), ContextCompat.getColor(itemView.getContext(), R.color.blue));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                title.setTextAppearance(itemView.getContext(), R.style.TabLayoutTextStyleSelected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                title.setTextAppearance(itemView.getContext(), R.style.TabLayoutTextStyleNormal);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                title.setTextAppearance(itemView.getContext(), R.style.TabLayoutTextStyleNormal);
            }
        });
    }

    @Override
    public void release() {
        mViewPager.removeAllViews();
        mViewPager = null;
        viewList.clear();
        viewList = null;
        cacheVies.clear();
        cacheVies = null;
        mTabLayout.removeAllViews();
        mTabLayout = null;
        mCurrentRecyclerView = null;
        super.release();
    }

    /**
     * 获取当前显示的列表对象
     *
     * @return
     */
    public ChildRecyclerView getCurrentChildRecyclerView() {
        return mCurrentRecyclerView;
    }

    public void destroy() {
        cacheVies.clear();
        viewList.clear();
    }

    /**
     * 根据菜单类型进行数据加载
     *
     * @param categoryType 菜单类型
     * @param data         类型数据
     */
    public void setCategoryData(CategoryBean categoryType, Object data) {
        if (cacheVies != null && categoryType != null) {
            if (data == null) {
                data = new ArrayList<>();
            }
            cacheVies.get(categoryType.getType()).setData(data);
        }
    }
}
