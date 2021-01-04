package com.cc.multirecycleview.bean;

/**
 * 底部分页类型
 *
 * @author 陈聪 2020-05-06 15:33
 */
public class CategoryBean {

    public int pageIndex;
    /** 分页标题 */
    private String title;
    /** 列表类型 */
    private String type;

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
}
