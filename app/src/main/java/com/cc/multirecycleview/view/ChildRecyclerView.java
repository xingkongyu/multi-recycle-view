package com.cc.multirecycleview.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.cc.multirecycleview.helper.FlingHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 *
 * @author 陈聪 QQ617909447
 * @date 2020/5/8:10:28 AM
 */
public class ChildRecyclerView extends RecyclerView {

    private FlingHelper mFlingHelper;
    private int mMaxDistance = 0;
    private int mVelocityY = 0;

    boolean isStartFling = false;
    int totalDy = 0;
    ParentRecyclerView mParentRecyclerView = null;

    public ChildRecyclerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mFlingHelper = new FlingHelper(context);
        mMaxDistance = mFlingHelper.getVelocityByDistance((double) (Resources.getSystem().getDisplayMetrics().heightPixels * 4));
        setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        initScrollListener();
    }

    private void initScrollListener() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    dispatchParentFling();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isStartFling) {
                    totalDy = 0;
                    isStartFling = false;
                }
                totalDy += dy;
            }
        });
    }

    private void dispatchParentFling() {
        mParentRecyclerView = findParentRecyclerView();
        if (isScrollTop() && mVelocityY != 0) {
            //当前ChildRecyclerView已经滑动到顶部，且竖直方向加速度不为0,如果有多余的需要交由父RecyclerView继续fling
            double flingDistance = mFlingHelper.getSplineFlingDistance(mVelocityY);
            if (flingDistance > (Math.abs(mParentRecyclerView.totalDy))) {
                fling(0, -mFlingHelper.getVelocityByDistance(flingDistance + mParentRecyclerView.totalDy));
            }
            //fix 在run方法里面，注意 this@ChildRecyclerView的使用，否则使用的是ParentRecyclerView的变量
            mParentRecyclerView.totalDy = 0;
            mVelocityY = 0;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mVelocityY = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (!isAttachedToWindow()) {
            return false;
        }
        boolean fling = super.fling(velocityX, velocityY);
        if (!fling || velocityY >= 0) {
            //fling为false表示加速度达不到fling的要求，将mVelocityY重置
            mVelocityY = 0;
        } else {
            //正在进行fling
            isStartFling = true;
            mVelocityY = velocityY;
        }
        return fling;
    }


    /**
     * 判断是否滑动到顶部了
     */
    boolean isScrollTop() {
        //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !canScrollVertically(-1);
    }

    private ParentRecyclerView findParentRecyclerView() {
        ViewParent parentView = getParent();
        while (!(parentView instanceof ParentRecyclerView)) {
            parentView = parentView.getParent();
        }
        return (ParentRecyclerView) parentView;
    }

}
