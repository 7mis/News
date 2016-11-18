package com.oliver.news.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;

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


    private List<TextView> pages = new ArrayList<>();


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
            View v = pages.get(position);

            container.addView(v);

            return v;
        }
    }

    /*索引值*/
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

    /**设置当前选择的页面*/
    private void switchPages() {
        vp_pagers.setCurrentItem(selectIndex);
    }

    @Override
    public void initData() {

        /*默认选择第一个页面*/
        rg_radios.check(R.id.rb_home);


        for (int i = 0; i < 5; i++) {
            TextView tv = new TextView(mContext);
            tv.setText("tv" + i);
            pages.add(tv);
        }


        MyAdapter adapter = new MyAdapter();
        vp_pagers.setAdapter(adapter);


        super.initData();
    }



}
