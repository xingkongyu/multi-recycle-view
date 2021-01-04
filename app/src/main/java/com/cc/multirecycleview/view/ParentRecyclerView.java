package com.cc.multirecycleview.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cc.multirecycleview.adapter.MultiTypeAdapter;
import com.cc.multirecycleview.helper.FlingHelper;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 外层列表
 *
 * @author 陈聪 2020-05-06 18:47
 */
public class ParentRecyclerView extends RecyclerView {

    private int mMaxDistance = 0;
    private Context context;
    private FlingHelper mFlingHelper;
    /**
     * 记录上次Event事件的y坐标
     */
    private float lastY = 0f;

    int totalDy = 0;
    /**
     * 用于判断RecyclerView是否在fling
     */
    boolean isStartFling = false;
    /**
     * 记录当前滑动的y轴加速度
     */
    private int velocityY = 0;
    private AtomicBoolean canScrollVertically = null;

    public ParentRecyclerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        mFlingHelper = new FlingHelper(getContext());
        mMaxDistance = mFlingHelper.getVelocityByDistance((double) (Resources.getSystem().getDisplayMetrics().heightPixels * 4));
        canScrollVertically = new AtomicBoolean(true);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果父RecyclerView fling过程中已经到底部，需要让子RecyclerView滑动剩余的fling
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    dispatchChildFling();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isStartFling) {
                    totalDy = 0;
                    isStartFling = false;
                }
                //在RecyclerView fling情况下，记录当前RecyclerView在y轴的偏移
                totalDy += dy;
            }
        });
    }

    private void dispatchChildFling() {
        if (isScrollEnd() && velocityY != 0) {
            double splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY);
            if (splineFlingDistance > totalDy) {
                childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - (double) totalDy));
            }
        }
        totalDy = 0;
        velocityY = 0;
    }

    private void childFling(int velY) {
        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
        if (childRecyclerView != null) {
            childRecyclerView.fling(0, velY);
        }
    }

    public void initLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
                try {
                    return super.scrollVerticallyBy(dy, recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            public void onLayoutChildren(Recycler recycler, State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean canScrollVertically() {
                ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
                if (canScrollVertically.get() || childRecyclerView == null || childRecyclerView.isScrollTop()) {
                    return true;
                }
                return false;
            }

            @Override
            public void addDisappearingView(View child) {
                try {
                    super.addDisappearingView(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            //ACTION_DOWN的时候重置加速度
            velocityY = 0;
            stopScroll();
        }
        if (!(ev == null || ev.getAction() == MotionEvent.ACTION_MOVE)) {
            //在ACTION_MOVE的情况下，将lastY置为0
            lastY = 0f;
            canScrollVertically.set(!isScrollEnd());
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (lastY == 0f) {
            lastY = e.getY();
        }
        if (isScrollEnd()) {
            //如果父RecyclerView已经滑动到底部，需要让子RecyclerView滑动剩余的距离
            ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
            int deltaY = (int) (lastY - e.getY());
            canScrollVertically.set(false);
            if (childRecyclerView != null) {
                childRecyclerView.scrollBy(0, deltaY);
            }

        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            canScrollVertically.set(true);
        }
        lastY = e.getY();
        try {
            return super.onTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean fling(int velX, int velY) {
        boolean fling = super.fling(velX, velY);
        if (!fling || velY <= 0) {
            velocityY = 0;
        } else {
            isStartFling = true;
            velocityY = velY;
        }
        return fling;
    }

    public boolean isScrollEnd() {
        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        return !canScrollVertically(1);
    }

    public ChildRecyclerView findNestedScrollingChildRecyclerView() {
        if (getAdapter() != null && getAdapter() instanceof MultiTypeAdapter) {
            return ((MultiTypeAdapter) getAdapter()).getCurrentChildRecyclerView();
        }
        return null;
    }


    @Override
    public void scrollToPosition(final int position) {
        //处理一键置顶会出现卡顿的问题
        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
        if (childRecyclerView != null) {
            childRecyclerView.scrollToPosition(position);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ParentRecyclerView.super.scrollToPosition(position);
            }
        }, 50);
    }


    //----------------------------------------------------------------------------------------------
    // NestedScroll. fix：当ChildRecyclerView下滑时(手指未放开)，ChildRecyclerView滑动到顶部（非fling），此时ParentRecyclerView不会继续下滑。
    //----------------------------------------------------------------------------------------------


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (target != null) && target instanceof ChildRecyclerView;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
        //1.当前Parent RecyclerView没有滑动底，且dy> 0 是下滑
        boolean isParentCanScroll = dy > 0 && !isScrollEnd();
        //2.当前Child RecyclerView滑到顶部了，且dy < 0,即上滑
        boolean isChildCanNotScroll = !(dy >= 0 || childRecyclerView == null || !childRecyclerView.isScrollTop());
        //以上两种情况都需要让Parent RecyclerView去scroll，和下面onNestedPreFling机制类似
        if (isParentCanScroll || isChildCanNotScroll) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
        boolean isParentCanFling = velocityY > 0f && !isScrollEnd();
        boolean isChildCanNotFling = !(velocityY >= 0 || childRecyclerView == null || !childRecyclerView.isScrollTop());
        if (!isParentCanFling && !isChildCanNotFling) {
            return false;
        }
        fling(0, (int) velocityY);
        return true;
    }

    public boolean isChildRecyclerViewCanScrollUp() {
        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
        if (childRecyclerView == null) {
            return false;
        }
        return !childRecyclerView.isScrollTop();
    }

    //----------------------------------------------------------------------------------------------
    // NestedScroll. fix：当ChildRecyclerView下滑时(手指未放开)，ChildRecyclerView滑动到顶部（非fling），此时ParentRecyclerView不会继续下滑。
    //----------------------------------------------------------------------------------------------


}
