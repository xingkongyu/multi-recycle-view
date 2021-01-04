package com.cc.multirecycleview.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cc.multirecycleview.bean.CategoryProBean;
import com.cc.multirecycleview.bean.CategoryItemData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 页面相关适配器
 *
 * @author 陈聪 2020-05-06 14:06
 */
public abstract class CategoryListAdapter extends BaseQuickAdapter<CategoryItemData, BaseViewHolder> {
    /** 需要展示的数据 */
    private List<CategoryItemData> data;
    /** 类别 */
    private CategoryProBean dataType;


    public CategoryListAdapter(@Nullable List<CategoryItemData> data, CategoryProBean dataType) {
        super(data);
        this.data = data;
        this.dataType = dataType;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return data.get(position).getViewType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < HEADER_VIEW) {
            return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(dataType.layout, parent, false));
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, final CategoryItemData item) {
        onConvert(helper, item);
    }

    public abstract void onConvert(BaseViewHolder helper, final CategoryItemData item);

    /**
     * 设置列表展示类型
     *
     * @param dataType 列表类型
     */
    public void setCategoryData(CategoryProBean dataType) {
        this.dataType = dataType;
    }
}
