package com.oliver.news.page;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.oliver.news.activity.HomeActivity;

/**
 * Created by Administrator on 2016/11/18.
 */
public class HomePage extends BasePage {
    /**
     * 构造函数，默认的布局
     *
     * @param context
     */
    public HomePage(HomeActivity context) {
        super(context);
    }

    @Override
    public void initData() {
        tv_title.setText("首页");

        /*隐藏 Menu*/
        iv_menu.setVisibility(View.GONE);

        /*内容*/
        TextView textView = new TextView(mcontxt);
        textView.setText("首页的内容");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);

        /*添加到内容中*/
        fl_content.addView(textView);
    }
}
