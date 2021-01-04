package com.cc.multirecycleview.adapter;

import android.view.ViewGroup;


import com.cc.multirecycleview.bean.BaseItemData;
import com.cc.multirecycleview.bean.CategoryBean;
import com.cc.multirecycleview.holder.BaseHolder;
import com.cc.multirecycleview.holder.CategoryViewHolder;
import com.cc.multirecycleview.view.ChildRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.cc.multirecycleview.bean.BaseItemData.ITEM_TYPE_BOTTOM_LIST_TYPE;

/**
 * 用于配置列表各个视图的适配器
 *
 * @author 陈聪 2020-05-06 09:47
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseItemData> dataSet = new ArrayList<>();
    private HashMap<Integer, BaseHolder> holders = new HashMap<>();
    private CategoryViewHolder mCategoryViewHolder = null;
    private OnLoadMoreDataListener onLoadMoreDataListener;

    public MultiTypeAdapter() {
    }

    private OnViewHolderCreateListener onViewHolderCreateListener;

    /**
     * 用于监听viewHolder的创建
     */
    public void setOnViewHolderCreateListener(OnViewHolderCreateListener onViewHolderCreateListener) {
        this.onViewHolderCreateListener = onViewHolderCreateListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        if (holders != null) {
            holder = holders.get(viewType);
            if (holder == null) {
                if (onViewHolderCreateListener != null) {
                    holder = onViewHolderCreateListener.onCreateHolder(parent, viewType);
                    if (holder == null) {
                        throw new IllegalArgumentException("创建holder失败，请检查您的代码！");
                    }
                    holders.put(viewType, holder);
                    if (holder instanceof CategoryViewHolder) {
                        mCategoryViewHolder = (CategoryViewHolder) holder;
                        holder.setOnLoadMoreDataListener(onLoadMoreDataListener);
                    }
                } else {
                    throw new IllegalArgumentException("请给定创建好的holder或者实现setOnViewHolderCreateListener回调");
                }
            }
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseHolder) {
            ((BaseHolder) holder).setData(getData(((BaseHolder) holder).viewType));
        }
    }

    /**
     * 获取展示数据
     *
     * @param viewType 条目类型
     * @return 类型对应的数据
     */
    private Object getData(int viewType) {
        for (BaseItemData bItem : dataSet) {
            if (bItem.itemType == viewType) {
                return bItem.data;
            }
        }
        return null;
    }

    /**
     * 获取展示条目数据
     *
     * @param viewType 条目类型
     */
    private BaseItemData getItemData(int viewType) {
        for (BaseItemData bItem : dataSet) {
            if (bItem.itemType == viewType) {
                return bItem;
            }
        }
        return null;
    }

    /**
     * 根据指定条目类型删除
     */
    public void removeItem(int itemType) {
        for (BaseItemData bItem : dataSet) {
            if (bItem.itemType == itemType) {
                dataSet.remove(bItem);
                notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 根据条目类型设置数据
     *
     * @param itemViewType 数据展示类型
     * @param data         数据
     */
    public void setData(int itemViewType, Object data) {
        BaseHolder holder;
        if (holders != null) {
            holder = holders.get(itemViewType);
            if (holder != null) {
                holder.hadLoadData = false;
            }
        }
        //插入或者更新数据
        if (data == null) {
            removeItem(itemViewType);
        } else {
            BaseItemData item = getItemData(itemViewType);
            if (item != null) {
                item.data = data;
            } else {
                dataSet.add(new BaseItemData(itemViewType, data));
            }
        }
        //根据类型调整展示位置
        Collections.sort(dataSet, new Comparator<BaseItemData>() {
            @Override
            public int compare(BaseItemData o1, BaseItemData o2) {
                return (o1.itemType < o2.itemType) ? -1 : ((o1.itemType != o2.itemType) ? 1 : 0);
            }
        });
        notifyDataSetChanged();
    }


    /**
     * 子列表更新数据
     */
    public void setCategoryListData(CategoryBean categoryType, Object data) {
        CategoryViewHolder holder = (CategoryViewHolder) holders.get(ITEM_TYPE_BOTTOM_LIST_TYPE);
        if (holder != null) {
            holder.setCategoryData(categoryType, data);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).itemType;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     * 获取当前底部当前展示的列表
     */
    public ChildRecyclerView getCurrentChildRecyclerView() {
        if (mCategoryViewHolder == null) {
            return null;
        }
        return mCategoryViewHolder.getCurrentChildRecyclerView();
    }

    /**
     * 页面退出时释放资源
     */
    public void destroy() {
        if (holders != null) {
            for (BaseHolder holder : holders.values()) {
                holder.release();
            }
            holders.clear();
            holders = null;
        }
        if (dataSet != null) {
            dataSet.clear();
            dataSet = null;
        }
        if (mCategoryViewHolder != null) {
            mCategoryViewHolder.destroy();
            mCategoryViewHolder = null;
        }
    }

    /**
     * 获取指定类型条目的位置
     *
     * @return 没有配置的情况下返回-1
     */
    public int getPosition(int key) {
        BaseItemData item = getItemData(key);
        if (item != null) {
            return dataSet.indexOf(item);
        } else {
            return -1;
        }
    }

    public void setOnLoadMoreDataListener(OnLoadMoreDataListener onLoadMoreDataListener) {
        this.onLoadMoreDataListener = onLoadMoreDataListener;
    }
}
