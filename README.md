# MultiRecycleView

#### 介绍
{**以下是 Gitee 平台说明，您可以替换此简介**
Gitee 是 OSCHINA 推出的基于 Git 的代码托管平台（同时支持 SVN）。专为开发者提供稳定、高效、安全的云端软件开发协作平台
无论是个人、团队、或是企业，都能够用 Gitee 实现代码托管、项目管理、协作开发。企业项目请看 [https://gitee.com/enterprises](https://gitee.com/enterprises)}

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

示例代码：
```
multiRecycleView = new MultiRecycleView.Builder()
                .setOnViewHolderCreateListener(new OnViewHolderCreateListener() {
                    @Override
                    public BaseHolder onCreateHolder(ViewGroup parent, int holderType) {
                        switch (holderType) {
                            case ITEM_TYPE_TXT:
                                return new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_txt, parent, false), holderType) {
                                    @Override
                                    public void setData(Object data) {
                                        if (!hadLoadData) {
                                            if (data instanceof String) {
                                                ((TextView) itemView.findViewById(R.id.tv_test)).setText((String) data);
                                            }
                                        }
                                    }
                                };
                            case ITEM_TYPE_IMG:
                                return new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_img, parent, false), holderType) {
                                    @Override
                                    public void setData(Object data) {
                                        if (!hadLoadData) {
                                            if (data instanceof Integer) {
                                                ((ImageView) itemView.findViewById(R.id.iv_test)).setImageResource((int) data);
                                            }
                                        }
                                    }
                                };
                            case ITEM_TYPE_BOTTOM_LIST_TYPE:
                                return new SimpleCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category, parent, false), holderType);
                            default:
                        }
                        return new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_img, parent, false), holderType) {
                            @Override
                            public void setData(Object data) {
                                if (!hadLoadData) {
                                    if (data instanceof Integer) {
                                        ((ImageView) itemView.findViewById(R.id.iv_test)).setImageResource((int) data);
                                    }
                                }
                            }
                        };
                    }
                })
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        multiRecycleView.setData(ITEM_TYPE_TXT, "测试数据" + System.currentTimeMillis());
                        refreshLayout.finishRefresh();
                    }

                })
                .setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                    @Override
                    public void loadData(final CategoryBean dataType) {
                        ArrayList<CategoryItemData> aa = new ArrayList();
                        for (int i = 0; i < 30; i++) {
                            CategoryItemData categoryItemData = new CategoryItemData();
                            aa.add(categoryItemData);
                        }
                        multiRecycleView.setCategoryListData(dataType, aa);
                    }
                })
                .build(getActivity());
        View conview = multiRecycleView.createView();
        ((LinearLayout) view.findViewById(R.id.ll_content)).addView(conview);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) conview.getLayoutParams();
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        conview.setLayoutParams(params);
        multiRecycleView.setData(ITEM_TYPE_TXT, "测试数据");
        multiRecycleView.setData(ITEM_TYPE_IMG, R.mipmap.ic_launcher);
        for (int i = 0; i < 4; i++) {
            multiRecycleView.setData(3+i, R.mipmap.ic_launcher);
        }

        ArrayList<CategoryProBean> menus = new ArrayList<>();
        CategoryProBean categoryProBean = new CategoryProBean("菜单一", "类型1", R.layout.item_test_txt, CATEGORY_TYPE_LIST, 1);
        CategoryProBean categoryProBean2 = new CategoryProBean("菜单二", "类型2", R.layout.item_test_txt, CATEGORY_TYPE_GRID, 2);
        CategoryProBean categoryProBean3 = new CategoryProBean("菜单3", "类型3", R.layout.item_test_txt, CATEGORY_TYPE_FLOW, 3);
        menus.add(categoryProBean);
        menus.add(categoryProBean2);
        menus.add(categoryProBean3);
        multiRecycleView.setData(ITEM_TYPE_BOTTOM_LIST_TYPE, menus);
```

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
