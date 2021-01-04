package com.cc.multirecycleview.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cc.multirecycleview.R;


/**
 * @author 陈聪
 * date 2020/5/14:10:37 AM
 * 服务模块中视频底部加载更多提醒
 */
public class FooterView extends LinearLayout {

    private ProgressBar mPgbar;
    private TextView mTvLoading;

    /** 加载中 */
    public static final int LOADING_STATUS_LOADING = 0;
    /** 加载失败 */
    public static final int LOADING_STATUS_ERROR = 1;
    /** 加载成功 */
    public static final int LOADING_STATUS_SUCCESS = 2;
    /** 加载完成，没有更多数据 */
    public static final int LOADING_STATUS_COMPELETED = 3;
    /** 用于在无数据时不显示 */
    public static final int LOADING_STATUS_EMPTY = 4;
    private final View rootView;
    private OnLoadMoreRetryCallBack onLoadMoreRetryCallBack;
    private int status;

    public FooterView(Context context, OnLoadMoreRetryCallBack callBack) {
        super(context);
        this.onLoadMoreRetryCallBack = callBack;
        rootView = View.inflate(context, R.layout.footer_view, this);
        mPgbar = rootView.findViewById(R.id.pgbar);
        mTvLoading = rootView.findViewById(R.id.tv_loading);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadMoreRetryCallBack != null && status == LOADING_STATUS_ERROR) {
                    onLoadMoreRetryCallBack.onLoadMoreRetry();
                }
            }
        });
    }

    /**
     * 根据状态进行展示
     */
    public void setLoadingStatus(int status) {
        this.status = status;
        switch (status) {
            case LOADING_STATUS_LOADING:
                rootView.setVisibility(View.VISIBLE);
                mPgbar.setVisibility(View.VISIBLE);
                mTvLoading.setText("加载中，请稍等....");
                break;
            case LOADING_STATUS_ERROR:
                mTvLoading.setText("加载失败，点击重试");
                rootView.setVisibility(View.VISIBLE);
                mPgbar.setVisibility(View.GONE);
                break;
            case LOADING_STATUS_SUCCESS:
                mTvLoading.setText("加载成功");
                rootView.setVisibility(View.VISIBLE);
                mPgbar.setVisibility(View.GONE);
                break;
            case LOADING_STATUS_COMPELETED:
                mTvLoading.setText("没有更多数据了");
                rootView.setVisibility(View.VISIBLE);
                mPgbar.setVisibility(View.GONE);
                break;
            case LOADING_STATUS_EMPTY:
                rootView.setVisibility(View.GONE);
                break;
            default:
        }
    }

    public interface OnLoadMoreRetryCallBack {
        void onLoadMoreRetry();
    }
}
