package com.cc.multirecycleview.view;


import android.content.Context;
import android.util.AttributeSet;

import com.cc.multirecycleview.R;
import com.cc.multirecycleview.adapter.CategoryListAdapter;
import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.bean.CategoryItemData;
import com.cc.multirecycleview.bean.CategoryProBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * 服务页面底部分类列表
 *
 * @author 陈聪 2020-05-06 11:45
 */
public class CategoryViewImp extends CategoryView {

    public CategoryViewImp(Context context, CategoryProBean bean, OnLoadMoreDataListener onLoadMoreDataListener) {
        super(context, bean, onLoadMoreDataListener);
    }

    public CategoryViewImp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public CategoryListAdapter getCategoryListAdapter(ArrayList<CategoryItemData> mDataList, CategoryProBean dataType) {
        return new CategoryListAdapter(mDataList, dataType) {
            @Override
            public void onConvert(BaseViewHolder helper, CategoryItemData item) {
                helper.setText(R.id.tv_test, System.currentTimeMillis() + "");
            }
        };
    }
}
