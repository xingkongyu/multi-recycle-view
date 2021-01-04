package com.cc.multirecycleview.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cc.multirecycleview.R;

/**
 * @author 陈聪
 * date 2020/5/14:10:37 AM
 * 服务模块中资讯列表空数据时展示
 */
public class EmptyView extends LinearLayout {

    private ProgressBar mPgbar;
    private TextView mTvLoading;
    private LinearLayout mLlEmptyOrError;
    private ImageView mIvTop;
    private TextView mTvDesc;
    private Button mBtnRetry;
    private LinearLayout mLlLoading;

    /** 加载中 */
    public static final int EMPTY_STATUS_LOADING = 0;
    /** 加载失败 */
    public static final int EMPTY_STATUS_ERROR = 1;
    /** 空数据 */
    public static final int EMPTY_STATUS_EMPTY = 2;
    private final View rootView;
    private OnLoadRetryCallBack retryCallBack;

    public EmptyView(Context context, final OnLoadRetryCallBack retryCallBack) {
        super(context);
        this.retryCallBack = retryCallBack;
        rootView = View.inflate(context, R.layout.empty_view, this);
        mPgbar = rootView.findViewById(R.id.pgbar);
        mTvLoading = rootView.findViewById(R.id.tv_loading);

        mLlLoading = rootView.findViewById(R.id.ll_loading);
        mLlEmptyOrError = rootView.findViewById(R.id.ll_empty_or_error);

        mIvTop = rootView.findViewById(R.id.iv_top);
        mTvDesc = rootView.findViewById(R.id.tv_desc);
        mBtnRetry = rootView.findViewById(R.id.btn_retry);

        mBtnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryCallBack != null) {
                    retryCallBack.onRetry();
                }
            }
        });
    }

    /**
     * 根据状态进行展示
     */
    public void setEmptyStatus(int status) {
        switch (status) {
            case EMPTY_STATUS_LOADING:
                mLlLoading.setVisibility(View.VISIBLE);
                mLlEmptyOrError.setVisibility(View.GONE);
                break;
            case EMPTY_STATUS_ERROR:
                mTvDesc.setText("数据加载失败，点击重新加载");
                mIvTop.setImageResource(R.mipmap.load_fail_icon);
                mLlEmptyOrError.setVisibility(View.VISIBLE);
                mBtnRetry.setVisibility(View.VISIBLE);
                mLlLoading.setVisibility(View.GONE);
                break;
            case EMPTY_STATUS_EMPTY:
                mTvDesc.setText("暂无相关数据哦");
                mIvTop.setImageResource(R.mipmap.load_empty_icon);
                mLlEmptyOrError.setVisibility(View.VISIBLE);
                mBtnRetry.setVisibility(View.GONE);
                mLlLoading.setVisibility(View.GONE);
                break;
            default:
        }
    }

    public interface OnLoadRetryCallBack {
        void onRetry();
    }
}
