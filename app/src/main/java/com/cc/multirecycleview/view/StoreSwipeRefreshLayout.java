package com.cc.multirecycleview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 服务页面下拉刷新控件
 * 对相关滑动事件进行了处理
 *
 * @author 陈聪 2020-05-06 18:37
 */
public class StoreSwipeRefreshLayout extends SmartRefreshLayout {

    private ParentRecyclerView mParentRecyclerView = null;

    public StoreSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public StoreSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float dx = 0;
    private float dy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = ev.getX();
                dy = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - dx) < Math.abs(ev.getY() - dy)) {
                    return true;
                }
                break;
            default:
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mParentRecyclerView != null && mParentRecyclerView.findNestedScrollingChildRecyclerView() != null) {
            setEnableRefresh(!mParentRecyclerView.isScrollEnd());
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mParentRecyclerView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof ParentRecyclerView) {
                    mParentRecyclerView = (ParentRecyclerView) child;
                    break;
                }
            }
        }
    }

//    @Override
//    public boolean canChildScrollUp() {
//        boolean b = false;
//        if (mParentRecyclerView != null) {
//            b = mParentRecyclerView.isChildRecyclerViewCanScrollUp();
//        }
//        return super.canChildScrollUp() || b;
//    }

}
