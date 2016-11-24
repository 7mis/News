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


    private float downX;
    private float downY;

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

        /**有条件的拦截：
         * 1. 当前页面的索引值
         * 2. move
         */

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*按下*/

                /**获取按下的坐标*/
                downX = ev.getX();
                downY = ev.getY();


                break;
            case MotionEvent.ACTION_MOVE:
                /*移动*/
                float moveX = ev.getX();
                float moveY = ev.getY();

                float dx = moveX - downX;
                float dy = moveY - downY;

                /**判断横向还是纵向*/
                if (Math.abs(dx) > Math.abs(dy)) {
                    /**横向移动
                     * 不是最后的 page 并且从右往左滑动，申请父控件不拦截
                     */
                    if (getCurrentItem() < getChildCount() - 1 && dx < 0) {
                        getParent().requestDisallowInterceptTouchEvent(true);//不拦截
                    } else if (getCurrentItem() != 0 && dx > 0) {
                        getParent().requestDisallowInterceptTouchEvent(true);//不拦截
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);//拦截
                    }
                } else {
                    /**纵向 默认*/
                    getParent().requestDisallowInterceptTouchEvent(false);//拦截
                }

                break;
            default:
                break;


        }

        return super.dispatchTouchEvent(ev);
    }
}
