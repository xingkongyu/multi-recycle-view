package com.cc.multirecycleview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


import com.cc.multirecycleview.adapter.CategoryListAdapter;
import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.bean.CategoryItemData;
import com.cc.multirecycleview.bean.CategoryProBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_FLOW;
import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_GRID;
import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_LIST;
import static com.cc.multirecycleview.view.EmptyView.EMPTY_STATUS_EMPTY;
import static com.cc.multirecycleview.view.EmptyView.EMPTY_STATUS_ERROR;
import static com.cc.multirecycleview.view.EmptyView.EMPTY_STATUS_LOADING;
import static com.cc.multirecycleview.view.FooterView.LOADING_STATUS_COMPELETED;
import static com.cc.multirecycleview.view.FooterView.LOADING_STATUS_EMPTY;
import static com.cc.multirecycleview.view.FooterView.LOADING_STATUS_ERROR;
import static com.cc.multirecycleview.view.FooterView.LOADING_STATUS_LOADING;

/**
 * 服务页面底部分类列表
 *
 * @author 陈聪 2020-05-06 11:45
 */
public abstract class CategoryView extends FrameLayout implements OnUserVisibleChange, EmptyView.OnLoadRetryCallBack, FooterView.OnLoadMoreRetryCallBack {

    /** 数据展示类型 */
    public CategoryProBean dataType = null;
    /** 需要展示的数据集合 */
    private ArrayList<CategoryItemData> mDataList = new ArrayList<CategoryItemData>();
    /** 是否正在加载更多数据 */
    private boolean isLoadingData = false;
    /** 数据适配器 */
    private CategoryListAdapter mAdapter;
    /** 用于页面加载数据的回调接口 */
    private OnLoadMoreDataListener onLoadMoreDataListener;
    /** 用于显示加载更多时加载更多提示和加载完成得提醒 */
    private FooterView footerView;
    /** 用于显示加载更多时加载更多提示和加载完成得提醒 */
    private EmptyView emptyView;
    private ChildRecyclerView recyclerView;
    /** 上次刷新数据时间，5s之后没有结果，则可以再次触发数据加载 */
    private long lastRefreshTime = 0;

    public CategoryView(Context context, CategoryProBean bean, OnLoadMoreDataListener onLoadMoreDataListener) {
        super(context);
        this.dataType = bean;
        this.onLoadMoreDataListener = onLoadMoreDataListener;
        initRecyclerView();
        initLoadMore();
    }

    public CategoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecyclerView();
        initLoadMore();
    }

    /**
     * 初始化列表展示
     */
    private void initRecyclerView() {
        recyclerView = new ChildRecyclerView(getContext());
        if (dataType.bgColor != 0) {
            setBackgroundColor(ContextCompat.getColor(getContext(), dataType.bgColor));
        }
        setLayoutManager();
        mAdapter = getCategoryListAdapter(mDataList, dataType);
        mAdapter.setNewData(mDataList);
        recyclerView.setAdapter(mAdapter);
        addView(recyclerView);
        setFootView();
        emptyView = new EmptyView(getContext(), CategoryView.this);
        emptyView.setEmptyStatus(EMPTY_STATUS_LOADING);
        addView(emptyView);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置适配器
     */
    public abstract CategoryListAdapter getCategoryListAdapter(ArrayList<CategoryItemData> mDataList, CategoryProBean dataType);

    /**
     * 设置底部footview
     */
    private void setFootView() {
        footerView = new FooterView(getContext(), CategoryView.this);
        footerView.setLoadingStatus(LOADING_STATUS_EMPTY);
        mAdapter.setFooterView(footerView);
    }

    /**
     * 设置列表展示效果
     */
    private void setLayoutManager() {
        if (dataType.itemType == CATEGORY_TYPE_FLOW) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(dataType.hcount, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        } else if (dataType.itemType == CATEGORY_TYPE_LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else if (dataType.itemType == CATEGORY_TYPE_GRID) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), dataType.hcount));
        }
    }


    /**
     * 配置加载更多监听
     */
    private void initLoadMore() {
//        if (dataType.itemType == CATEGORY_TYPE_FLOW) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    tryLoadMoreIfNeed();
                }
            });
//        } else if (dataType.itemType == CATEGORY_TYPE_LIST || dataType.itemType == CATEGORY_TYPE_GRID) {
//            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//                @Override
//                public void onLoadMoreRequested() {
//                    tryLoadMoreIfNeed();
//                }
//            }, recyclerView);
//        }
    }

    private void tryLoadMoreIfNeed() {
        if (mAdapter != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] last = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
                for (int i = 0; i < last.length; i++) {
                    if ((last[i] >= mAdapter.getItemCount() - 4) && !isLoadingData) {
                        loadMore();
                        break;
                    }
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if ((last >= mAdapter.getItemCount() - 4) && !isLoadingData) {
                    loadMore();
                }
            }
        }
    }

    /**
     * 加载的新数据
     *
     * @param data
     */
    public void setData(Object data) {
        isLoadingData = false;
        if (dataType.itemType == CATEGORY_TYPE_FLOW) {
            forFlow(data);
        } else {
            forListAndGrid(data);
        }
        if (mDataList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 数据处理
     */
    private void forListAndGrid(Object obj) {
        //加载数据失败
        if (obj instanceof Throwable) {
            if (mDataList.isEmpty()) {
                if (emptyView != null) {
                    emptyView.setEmptyStatus(EMPTY_STATUS_ERROR);
                }
            }else{
                mAdapter.loadMoreFail();
            }
        }
        if (obj instanceof ArrayList) {
            ArrayList<CategoryItemData> data = (ArrayList<CategoryItemData>) obj;
            if (data.isEmpty()) {
                if (emptyView != null) {
                    emptyView.setEmptyStatus(EMPTY_STATUS_EMPTY);
                }
                mAdapter.loadMoreEnd();
                return;
            }
            mAdapter.loadMoreComplete();
            boolean isrefresh = dataType.pageIndex == 0;
            if (isrefresh) {
                mDataList.clear();
            }
            mDataList.addAll(data);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 数据处理
     */
    private void forFlow(Object obj) {
        //加载数据失败
        if (obj instanceof Throwable) {
            if (mDataList.size() > 0) {
                if (footerView != null) {
                    footerView.setLoadingStatus(LOADING_STATUS_ERROR);
                }
            } else {
                if (footerView != null) {
                    footerView.setLoadingStatus(LOADING_STATUS_EMPTY);
                }
                if (emptyView != null) {
                    emptyView.setEmptyStatus(EMPTY_STATUS_ERROR);
                }
            }
        }
        if (obj instanceof ArrayList) {
            final ArrayList<CategoryItemData> data = (ArrayList<CategoryItemData>) obj;
            if (data.isEmpty()) {
                if (mDataList.size() > 0) {
                    if (footerView != null) {
                        footerView.setLoadingStatus(LOADING_STATUS_COMPELETED);
                    }
                    dataType.setLoadFinished(true);
                } else {
                    if (footerView != null) {
                        footerView.setLoadingStatus(LOADING_STATUS_EMPTY);
                    }
                    if (emptyView != null) {
                        emptyView.setEmptyStatus(EMPTY_STATUS_EMPTY);
                    }
                }
                return;
            }
            final boolean isrefresh = dataType.pageIndex == 0;
            if (isrefresh) {
                mDataList.clear();
            }
            mDataList.addAll(data);
            if (mAdapter != null) {
                dataType.pageIndex++;
                //此处data.size() > 1是因为当请求到的数据只有1个时，
                //StaggeredGridLayoutManager.findLastVisibleItemPositions(null)方法会报错，导致崩溃。
                if (!isrefresh && data.size() > 10) {
                    mAdapter.notifyItemRangeChanged(mDataList.size() - data.size(), mDataList.size());
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 触发加载更多
     *
     * @return
     */
    private void loadMore() {
        if (onLoadMoreDataListener != null && !isLoadingData&&mAdapter.getData().size()>0) {
            isLoadingData = true;
            dataType.pageIndex++;
            onLoadMoreDataListener.loadData(dataType);
            if (footerView != null) {
                footerView.setLoadingStatus(LOADING_STATUS_LOADING);
            }
        }
    }

    @Override
    public void onUserVisibleChange(boolean isVisibleToUser) {
        if (isVisibleToUser && !dataType.isLoadFinished()) {
            //  此处可在列表可见的时候刷新数据
            if (mDataList.isEmpty() && onLoadMoreDataListener != null
                    && (!isLoadingData || System.currentTimeMillis() - lastRefreshTime > 5 * 1000)) {
                startRefreshData();
            }
        }
    }

    /**
     * 开始刷新数据
     */
    private void startRefreshData() {
        isLoadingData = true;
        dataType.pageIndex = 0;
        lastRefreshTime = System.currentTimeMillis();
        onLoadMoreDataListener.loadData(dataType);
        if (mDataList.isEmpty()) {
            if (emptyView != null) {
                emptyView.setEmptyStatus(EMPTY_STATUS_LOADING);
            }
        }
    }

    /**
     * 设置刷新和加载更多监听
     *
     * @param listener
     */
    public void setOnLoadMoreDataListener(OnLoadMoreDataListener listener) {
        this.onLoadMoreDataListener = listener;
    }

    @Override
    public void onRetry() {
        startRefreshData();
    }

    @Override
    public void onLoadMoreRetry() {
        loadMore();
    }

    public ChildRecyclerView getRecycleView() {
        return recyclerView;
    }

    /**
     * 刷新类别数据的时候相关数据引用和配置刷新
     */
    public void setCategoryData(CategoryProBean bean) {
        dataType = bean;
        setLayoutManager();
        if (dataType.itemType != CATEGORY_TYPE_FLOW) {
            if (footerView != null) {
                mAdapter.removeFooterView(footerView);
            }
        } else {
            if (footerView == null) {
                setFootView();
            }
        }
        lastRefreshTime = 0;
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setEmptyStatus(EMPTY_STATUS_LOADING);
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.setCategoryData(dataType);
        startRefreshData();
    }


}
