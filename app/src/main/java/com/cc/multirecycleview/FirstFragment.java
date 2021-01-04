package com.cc.multirecycleview;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.multirecycleview.adapter.OnLoadMoreDataListener;
import com.cc.multirecycleview.adapter.OnViewHolderCreateListener;
import com.cc.multirecycleview.bean.CategoryBean;
import com.cc.multirecycleview.bean.CategoryItemData;
import com.cc.multirecycleview.bean.CategoryProBean;
import com.cc.multirecycleview.holder.BaseHolder;
import com.cc.multirecycleview.holder.SimpleCategoryViewHolder;
import com.cc.multirecycleview.view.MultiRecycleView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.cc.multirecycleview.bean.BaseItemData.ITEM_TYPE_BOTTOM_LIST_TYPE;
import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_FLOW;
import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_GRID;
import static com.cc.multirecycleview.holder.SimpleCategoryViewHolder.CATEGORY_TYPE_LIST;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    MultiRecycleView multiRecycleView;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
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
    }

    public static final int ITEM_TYPE_TXT = 1;
    public static final int ITEM_TYPE_IMG = 2;

}