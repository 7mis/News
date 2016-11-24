package com.oliver.news.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.viewpagerindicator.TabPageIndicator;

/**
 * @desc 页签：申请父控件不拦截
 * @auther Oliver
 * @time 2016/11/24 21:51
 */
public class InterceptTabPageIndicator extends TabPageIndicator {
    public InterceptTabPageIndicator(Context context) {
        super(context);
    }

    public InterceptTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**解决：页签可以滑出侧滑菜单*/
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
