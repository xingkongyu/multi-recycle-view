package com.cc.multirecycleview.holder;

import android.view.View;
import android.view.ViewGroup;

import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.bean.BaseItemData;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 用于抽取共用属性
 *
 * @author 陈聪 2020-05-09 12:22
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {
    /** 用于判断是否已加载数据，防止过度绘制造成卡顿 */
    public boolean hadLoadData = false;
    /** 用于配置holder的类型 */
    public int viewType;
    public OnLoadMoreDataListener onLoadMoreDataListener;

    public BaseHolder(@NonNull View itemView) {
        super(itemView);
    }

    public BaseHolder(@NonNull View itemView,int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    /**
     * 退出时释放资源
     */
    public void release() {
        if (itemView instanceof ViewGroup) {
            ((ViewGroup) itemView).removeAllViews();
        }
    }

    /**
     * 用于设置数据
     * 其子类实现此方法用于数据的更新
     *
     * @param data 相关数据
     */
    public abstract void setData(Object data);

    public void setOnLoadMoreDataListener(OnLoadMoreDataListener onLoadMoreDataListener) {
        this.onLoadMoreDataListener = onLoadMoreDataListener;
    }
}
