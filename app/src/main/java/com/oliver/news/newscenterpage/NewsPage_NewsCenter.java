package com.oliver.news.newscenterpage;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * @desc 新闻 页面
 * @auther Oliver
 * @time 2016/11/21 10:57
 */
public class NewsPage_NewsCenter extends BaseNewsCenterPage {


    /*两个子 View*/

    @ViewInject(R.id.tpi_newspage_pagetag)
    private TabPageIndicator tpi_pagetag;

    @ViewInject(R.id.vp_newspage_pages)
    private ViewPager vp_pages;

    /**
     * 只需要一个 children
     */
    private List<NewsCenterData_GosnFormat.DataBean.ChildrenBean> mChildrenDatas;


    /**
     * @param context
     * @param childrenDatas NewsCenterPage 传递过来的数据
     */
    public NewsPage_NewsCenter(HomeActivity context, List<NewsCenterData_GosnFormat.DataBean.ChildrenBean> childrenDatas) {
        super(context);
        this.mChildrenDatas = childrenDatas;


        /*构造函数执行完，才调用该方法*/
        initData();
    }



    /**
     * 设置数据
     */
    @Override
    public void initData() {
        MyAdapter adapter = new MyAdapter();

        /*设置适配器*/
        vp_pages.setAdapter(adapter);

        /*把 ViewPager 和 tpi 绑定*/
        tpi_pagetag.setViewPager(vp_pages);

        super.initData();
    }

    /**
     * ViewPager 的输配器
     */
    private class MyAdapter extends PagerAdapter {

        /**页签 显示数据调用的方法
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildrenDatas.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mChildrenDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {


            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TextView tv = new TextView(mContext);
            tv.setTextColor(Color.WHITE);
            tv.setText(mChildrenDatas.get(position).getTitle());

            tv.setGravity(Gravity.CENTER);

            container.addView(tv);

            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public View initView() {
//
//        /*内容*/
//        TextView textView = new TextView(mContext);
//        textView.setText("新闻 --- 的内容");
//        textView.setTextSize(30);
//        textView.setGravity(Gravity.CENTER);
        View rootView = View.inflate(mContext, R.layout.newscenter_news, null);

        /*注入*/
        ViewUtils.inject(this, rootView);

        return rootView;
    }
}