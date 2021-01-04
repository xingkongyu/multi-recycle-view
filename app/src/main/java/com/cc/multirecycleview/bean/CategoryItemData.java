package com.cc.multirecycleview.bean;

/**
 * 底部分页类型
 *
 * @author 陈聪 2020-05-06 15:33
 */
public class CategoryItemData {

    /** 分页标题 */
    private String title;
    /** 列表类型 */
    private String type;
    /**子item类型*/
    private int viewType = 1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
