package com.oliver.news.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**不可滑动的 ViewPager
 * Created by Administrator on 2016/11/18.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**onTouch 事件分发机制*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
