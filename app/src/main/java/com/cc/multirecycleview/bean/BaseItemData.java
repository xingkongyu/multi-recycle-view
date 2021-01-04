package com.cc.multirecycleview.bean;

/**
 * 条目基类
 * 主要用于填装数据以及方便排序
 *
 * @author 陈聪 2020-09-25 11:45
 */
public class BaseItemData {
    /** 展示类型 */
    public int itemType;
    /** 展示数据 */
    public Object data;
    /** 底部多列表数据类型，默认值，其他类型不得使用此值 ，也不得大于此值 */
    public static final int ITEM_TYPE_BOTTOM_LIST_TYPE = 500;

    public BaseItemData(int itemViewType, Object data) {
        itemType = itemViewType;
        this.data = data;
    }
}
