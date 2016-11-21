package com.oliver.news.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.page.BasePage;
import com.oliver.news.page.GovaAffairsPage;
import com.oliver.news.page.HomePage;
import com.oliver.news.page.NewsCenterPage;
import com.oliver.news.page.SettingCenterPage;
import com.oliver.news.page.SmartServicesPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class MainFragment extends BaseFragment {

    @ViewInject(R.id.vp_mainfragment_pages)
    private ViewPager vp_pagers;

    @ViewInject(R.id.rg_radios)
    private RadioGroup rg_radios;


    private List<BasePage> pages = new ArrayList<>();


    @Override
    public View initView() {
        View root = View.inflate(mContext, R.layout.main_content, null);//上下文，布局，父控件

        /*获取需要的 组件：通过注解*/
        ViewUtils.inject(this, root);


        return root;
    }

    /*ViewPager 适配器*/
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage page = pages.get(position);

            View view = page.getRootView();

            /**初始化当前页面的数据*/
            page.initData();

            container.addView(view);

            return view;
        }
    }

    /**索引值*/
    private int selectIndex = 0;

    /**
     * RadioButton 事件设置
     */
    @Override
    public void initEvent() {
        rg_radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                /*确定 ViewPager 选择的哪一个页面*/
                switch (checkedId) {
                    case R.id.rb_home:
                        selectIndex = 0;
                        break;
                    case R.id.rb_news:
                        selectIndex = 1;
                        break;
                    case R.id.rb_smartservices:
                        selectIndex = 2;
                        break;
                    case R.id.rb_govaffiar:
                        selectIndex = 3;
                        break;
                    case R.id.rb_settingcenter:
                        selectIndex = 4;
                        break;
                }//

                /*选择页面*/
                switchPages();
            }
        });


        super.initEvent();
    }

    /**
     * 设置当前选择的页面
     */
    private void switchPages() {
        vp_pagers.setCurrentItem(selectIndex);


        /**左侧菜单是否可以拖动*/
        /*第一个和最后一个不可以拖动*/
        if (selectIndex == 0 || selectIndex == 4) {
            mContext.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        } else {
            mContext.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        }

    }

    /**获取选中的页面<5个中的其中一个>
     * @return 返回选中页面的 item
     */
    public BasePage getSelectPage() {
        return pages.get(selectIndex);
    }

    @Override
    public void initData() {

        /*默认选择第一个页面*/
        rg_radios.check(R.id.rb_home);

        /**添加 5 个页面*/
        pages.add(new HomePage(mContext));
        pages.add(new NewsCenterPage(mContext));
        pages.add(new SmartServicesPage(mContext));
        pages.add(new GovaAffairsPage(mContext));
        pages.add(new SettingCenterPage(mContext));


        MyAdapter adapter = new MyAdapter();
        vp_pagers.setAdapter(adapter);


        /*初始化，*/
        switchPages();

        super.initData();
    }


}
