package com.cc.multirecycleview.helper;

import android.content.Context;
import android.view.ViewConfiguration;

/**
 * Fling 帮助类
 *
 * @author 陈聪 2020-05-06 18:20
 */
public class FlingHelper {
    private Context context;
    private float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private float mFlingFriction = ViewConfiguration.getScrollFriction();

    public FlingHelper(Context context) {
        this.context = context;
        mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
    }

    private float mPhysicalCoeff = 0f;

    private double getSplineDeceleration(int i) {
        return Math.log((double) (0.35f * (float) Math.abs(i) / (mFlingFriction * mPhysicalCoeff)));
    }

    private double getSplineDecelerationByDistance(double d) {
        return ((double) DECELERATION_RATE - 1.0) * Math.log(d / ((double) (mFlingFriction * mPhysicalCoeff)) / ((double) DECELERATION_RATE));
    }

    /**
     * 根据加速度来获取需要fling的距离
     *
     * @param i 加速度
     * @return fling的距离
     */
    public double getSplineFlingDistance(int i) {
        return Math.exp(getSplineDeceleration(i) * ((double) DECELERATION_RATE / ((double) DECELERATION_RATE - 1.0))) * (double) (mFlingFriction * mPhysicalCoeff);
    }

    /**
     * 根据距离来获取加速度
     *
     * @param d 距离
     * @return 返回加速度
     */
    public int getVelocityByDistance(double d) {
        return Math.abs((int) (Math.exp(getSplineDecelerationByDistance(d)) * (double) mFlingFriction * (double) mPhysicalCoeff / 0.3499999940395355));
    }


}
