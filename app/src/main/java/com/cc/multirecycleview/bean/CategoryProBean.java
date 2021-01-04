package com.cc.multirecycleview.bean;

import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_LIST;

/**
 * 底部分页类型
 *
 * @author 陈聪 2020-05-06 15:33
 */
public class CategoryProBean extends CategoryBean {

    /** 配置的展示布局 */
    public int layout = 0;
    /** 配置背景颜色 */
    public int bgColor = 0;
    /** 配置的展示布局排版类型 */
    public int itemType = CATEGORY_TYPE_LIST;
    /** itemType为grid或flow时，配置的横向展示个数 */
    public int hcount;
    /**是否已加载完数据，刷新时改为false*/
    private boolean loadFinished;

    public CategoryProBean(String title, String type, int layout, int itemType, int hcount) {
        setTitle(title);
        setType(type);
        this.layout = layout;
        this.itemType = itemType;
        this.hcount = hcount;
    }

    public CategoryProBean(String title, String type, int layout, int itemType, int hcount, int bgColor) {
        setTitle(title);
        setType(type);
        this.layout = layout;
        this.itemType = itemType;
        this.hcount = hcount;
        this.bgColor = bgColor;
    }

    public boolean isLoadFinished() {
        return loadFinished;
    }

    public void setLoadFinished(boolean loadFinished) {
        this.loadFinished = loadFinished;
    }
}
