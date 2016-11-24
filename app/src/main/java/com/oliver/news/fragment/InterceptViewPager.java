package com.oliver.news.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @desc 申请父类不拦截 ViewPager(轮播图) 事件
 * <p/>
 * 1. 轮播图是一个 ViewPager
 * 2. 外层有两个 ViewPager
 * 3. 它的事件被 ViewPagerIndicator<北京、中国、国际。。。> 抢了
 * 4. 轮播图申请父控件不拦截它的事件，条件：
 * - 1. 不是最后一张：从右往左滑，响应自己的事件
 * - 2. 不是第一张：从左往右滑，响应自己的事件
 * - 3. 以上不拦截
 * @auther Oliver
 * @time 2016/11/24 15:31
 */
public class InterceptViewPager extends ViewPager {


    public InterceptViewPager(Context context) {
        super(context);
    }

    public InterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        /**申请不拦截当前的事件*/
        getParent().requestDisallowInterceptTouchEvent(true);


        return super.dispatchTouchEvent(ev);
    }
}
