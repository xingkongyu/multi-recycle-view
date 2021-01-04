package com.cc.multirecycleview.adapter;

import android.view.ViewGroup;

import com.cc.multirecycleview.holder.BaseHolder;

/**
 * 用于回调holder创建
 *
 * @author 陈聪 2020-12-31 14:37
 */
public interface OnViewHolderCreateListener {

    /**
     * 创建BaseHolder
     * @param holderType 给定的holder类型
     * @return  根据给定的类型创建holder
     */
    BaseHolder onCreateHolder(ViewGroup parent,int holderType);
}
