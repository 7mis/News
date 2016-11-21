package com.oliver.news.newscenterpage;

import android.view.View;

import com.oliver.news.activity.HomeActivity;

/**
 * @desc 新闻 专题 主图 互动 的基类
 * @auther Oliver
 * @time 2016/11/21 10:51
 */
public abstract class BaseNewsCenterPage {

    /**获取上下文,
     * 子类可以使用*/
    protected HomeActivity mContext;

    private View rootView;

    public BaseNewsCenterPage(HomeActivity mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    /**页面*/
    public  abstract View initView();

    public View getRootView() {
        return rootView;
    }

}
