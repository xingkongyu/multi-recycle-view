package com.cc.multirecycleview.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cc.multirecycleview.adapter.MultiTypeAdapter;
import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.adapter.OnViewHolderCreateListener;
import com.cc.multirecycleview.bean.CategoryBean;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

/**
 * 列表或复杂布局可使用此方式
 * 好处：1.可以动态配置排序，2.可减少页面跳转时间优化交互体验
 *
 * @author 陈聪 2020-12-31 14:16
 */
public class MultiRecycleView {

    /** view配置 */
    private Builder mBuilder;
    /** 条目适配器 */
    private MultiTypeAdapter multiTypeAdapter;
    /** 下拉刷新 */
    private StoreSwipeRefreshLayout refreshLayout;
    private ParentRecyclerView listView;

    /**
     * 初始化View
     */
    public View createView() {
        if (multiTypeAdapter != null) {
            throw new IllegalStateException("请勿重复创建");
        }
        mBuilder = mBuilder == null ? getDefaultBuilder() : mBuilder;
        return initView();
    }

    /**
     * 初始化view
     */
    private View initView() {
        View rootView;
        //设置是否下拉刷新
        listView = new ParentRecyclerView(mBuilder.context);
        multiTypeAdapter = new MultiTypeAdapter();
        listView.initLayoutManager();
        listView.setAdapter(multiTypeAdapter);
        multiTypeAdapter.setOnLoadMoreDataListener(mBuilder.onLoadMoreDataListener);
        multiTypeAdapter.setOnViewHolderCreateListener(mBuilder.onViewHolderCreateListener);
        if (mBuilder.enableRefresh) {
            refreshLayout = new StoreSwipeRefreshLayout(mBuilder.context);
            refreshLayout.setRefreshHeader(new ClassicsHeader(mBuilder.context));
            refreshLayout.addView(listView);
            refreshLayout.setOnRefreshListener(mBuilder.onRefreshListener);
            refreshLayout.setNestedScrollingEnabled(false);
            rootView = refreshLayout;
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            listView.setLayoutParams(params);
        } else {
            rootView = listView;
        }
        //设置背景颜色
        if (mBuilder.bgColor != 0) {
            rootView.setBackgroundColor(ContextCompat.getColor(mBuilder.context, mBuilder.bgColor));
        }
        return rootView;
    }

    /**
     * 子列表更新数据
     */
    public void setCategoryListData(CategoryBean categoryType, Object data) {
        if (this.multiTypeAdapter != null) {
            this.multiTypeAdapter.setCategoryListData(categoryType, data);
        }
    }

    /**
     * 设置对应类型的条目数据
     *
     * @param itemViewType 条目类型
     * @param data         关联数据
     */
    public void setData(int itemViewType, Object data) {
        if (this.multiTypeAdapter != null) {
            this.multiTypeAdapter.setData(itemViewType, data);
        }
    }

    /**
     * 配置默认
     *
     * @return
     */
    private Builder getDefaultBuilder() {
        return new Builder();
    }

    /**
     * 动态配置
     */
    public static class Builder {

        /**刷新回调*/
        private OnRefreshListener onRefreshListener;
        /** 配置是否使用下拉刷新-不配置将不增加对应的刷新控件 */
        private boolean enableRefresh = true;
        /** 背景颜色配置 */
        private @ColorRes int bgColor = 0;
        private Context context;
        /**holder创建回调*/
        private OnViewHolderCreateListener onViewHolderCreateListener;
        /** 子列表加载更多监听 */
        private OnLoadMoreDataListener onLoadMoreDataListener;

        public MultiRecycleView build(Context context) {
            this.context = context;
            MultiRecycleView multiRecycleView = new MultiRecycleView();
            multiRecycleView.mBuilder = this;
            return multiRecycleView;
        }

        public Builder setEnableRefresh(boolean enableRefresh) {
            this.enableRefresh = enableRefresh;
            return this;
        }

        public Builder setBgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder setOnViewHolderCreateListener(OnViewHolderCreateListener onViewHolderCreateListener) {
            this.onViewHolderCreateListener = onViewHolderCreateListener;
            return this;
        }

        public Builder setOnRefreshListener(OnRefreshListener onRefreshListener) {
            this.onRefreshListener = onRefreshListener;
            return this;
        }

        public Builder setOnLoadMoreDataListener(OnLoadMoreDataListener onLoadMoreDataListener) {
            this.onLoadMoreDataListener = onLoadMoreDataListener;
            return this;
        }
    }

    /**
     * 资源释放
     */
    public void onDestory() {
        if (multiTypeAdapter != null) {
            multiTypeAdapter.destroy();
        }
    }
}
