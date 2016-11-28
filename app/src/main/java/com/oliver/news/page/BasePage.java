package com.oliver.news.page;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.newscenterpage.PhotoPage_NewsCenter;
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
    protected ImageView iv_showListOrGrid;


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

        iv_showListOrGrid = (ImageView) rootView.findViewById(R.id.iv_basepage_gridlistbutton);



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
        L.d("不使用接口  "+pageIndex+" 页面 - BasePage");
    }



    /**
     * 控制左侧菜单的关闭
     */
    public void initEvent() {
        iv_showListOrGrid.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v 谁触发这个事件，传递谁的 View
             *          通过 tag 获取这个页面
             */
            @Override
            public void onClick(View v) {
                //只调用一次
                //photoView 完成 ListView 和 GridView 的切换
                PhotoPage_NewsCenter photoPage = (PhotoPage_NewsCenter) v.getTag();
                photoPage.swtichListOrGrid((ImageView) v);//切换 View 显示

            }
        });

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContxt.getSlidingMenu().toggle();
            }
        });
    }

}
