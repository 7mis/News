package com.oliver.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.oliver.news.R;
import com.oliver.news.fragment.LeftFragment;
import com.oliver.news.fragment.MainFragment;
import com.oliver.news.utils.L;

/**
 * Created by Administrator on 2016/11/18.
 */
public class HomeActivity extends SlidingFragmentActivity {

    /*两个 tag*/
    public static final String LEFT_FRAGMENT = "left_fragment";
    public static final String MAIN_FRAGMENT = "main_fragment";

    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();

        initData();

    }

    /**
     * 恢复显示
     */
    @Override
    protected void onResume() {
        L.d("onResume ------");
        getMainFragment().initCurrentPage();
        super.onResume();
    }

    /**
     * 初始化 Fragment
     */
    private void initData() {
        /*fragment 的替换，由fragment 负责数据的显示*/

        /**1. 获取 Fragment 管理器*/
        fragmentManager = getSupportFragmentManager();

        /**2. 开始事务*/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        /**3. 替换*/
        fragmentTransaction.replace(R.id.fl_left_menu, new LeftFragment(), LEFT_FRAGMENT);
        fragmentTransaction.replace(R.id.fl_content, new MainFragment(), MAIN_FRAGMENT);

        /**4. 提交事务*/
        fragmentTransaction.commit();
    }

    /*获取 fragment*/
    public LeftFragment getLeftFragment() {
        return (LeftFragment) fragmentManager.findFragmentByTag(LEFT_FRAGMENT);
    }

    public MainFragment getMainFragment() {
        return (MainFragment) fragmentManager.findFragmentByTag(MAIN_FRAGMENT);
    }


    private void initView() {
        // 左侧菜单布局
        setBehindContentView(R.layout.left_menu);

        // 内容的布局
        setContentView(R.layout.content);

        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setBehindOffset(450);// 左侧菜单显示完全后剩余的宽度
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // .setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setMode(SlidingMenu.LEFT);

    }
}
