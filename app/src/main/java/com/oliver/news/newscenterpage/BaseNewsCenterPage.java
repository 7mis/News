package com.oliver.news.newscenterpage;

import android.view.View;

import com.oliver.news.activity.HomeActivity;

/**
 * @desc 新闻 专题 主图 互动 的基类
 * @auther Oliver
 * @time 2016/11/21 10:51
 */
public abstract class BaseNewsCenterPage {

    /**
     * 获取上下文,
     * 子类可以使用
     */
    protected HomeActivity mContext;

    private View rootView;

    public BaseNewsCenterPage(HomeActivity mContext) {
        this.mContext = mContext;
        rootView = initView();

        /**事件跟数据没有关系，可以直接在构造函数中调用*/
        initEvent();

        /*如果在此处调用：NullPointerException
        * 1. 先父类构造函数执行
        * 2. 此处会先执行 该方法，后执行 构造函数
        * 3. 调用覆盖的方法，子类构造函数未被赋值， 空指针
        */
//      initData();
    }

    /**
     * 页面
     */
    public abstract View initView();

    public View getRootView() {
        return rootView;
    }

    public void initData() {

    }

    public void initEvent() {

    }
}
