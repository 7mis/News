package com.oliver.news.activity;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.oliver.news.R;

/**
 * Created by Administrator on 2016/11/18.
 */
public class HomeActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    private void initView() {
        // 左侧菜单布局
        setBehindContentView(R.layout.left_menu);

        // 内容的布局
        setContentView(R.layout.main_content);

        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setBehindOffset(450);// 左侧菜单显示完全后剩余的宽度
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // .setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setMode(SlidingMenu.LEFT);

    }
}
