package com.oliver.news.page;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.utils.L;

/**
 * 5 个页面的基类：
 * 1. 界面
 * 2. 数据
 * 3。 事件
 * Created by Administrator on 2016/11/18.
 */
public class BasePage {

    protected HomeActivity mContxt;
    private View rootView;
    protected TextView tv_title;
    protected ImageView iv_menu;
    protected FrameLayout fl_content;


    /**
     * 构造函数，默认的布局
     */
    public BasePage(HomeActivity context) {
        this.mContxt = context;

        initView();
        initEvent();
    }

    /**界面*/
    public void initView() {
        rootView = View.inflate(mContxt, R.layout.base_page_view, null);

        tv_title = (TextView) rootView.findViewById(R.id.tv_basepage_title);
        iv_menu = (ImageView) rootView.findViewById(R.id.iv_basepage_menu);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_basepage_content);


    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 子类去实现
     */
    public void initData() {

    }

    /** 子类自己去实现完成页面的切换:
     * 面向对象，多态
     *  - 编译时看父类
     *  - 运行时具体子类
     * @param pageIndex
     */
    public void selectPage(int pageIndex) {
        L.d("显示  "+pageIndex+" 页面 - BasePage");
    }



    /**
     * 控制左侧菜单的关闭
     */
    public void initEvent() {
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContxt.getSlidingMenu().toggle();
            }
        });
    }

}
