package com.oliver.news.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oliver.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager vp;

    /*三个图片*/
    private int[] pages_id = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    /*储存图片的容器*/
    private List<ImageView> mIv_datas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 数据：三个 View
     */
    private void initData() {
        /**for 循环添加数据(图片)*/
        for (int i = 0; i < pages_id.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(pages_id[i]);

            /*填充模式*/
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            mIv_datas.add(iv);
        }


        /**创建适配器*/
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter();
        vp.setAdapter(mPagerAdapter);

    }

    /**
     * ViewPager 的适配器
     * 每次创建 ViewPager 或滑动过程中，以下四个方法会被调用
     */
    private class MyPagerAdapter extends PagerAdapter {

        /**
         * @return 返回当前有效视图的个数
         */
        @Override
        public int getCount() {
            return mIv_datas.size();
        }

        /**
         * 判断 instantiateItem() 函数所返回的 key 与一个页面视图是否代表的同一个视图
         *
         * @param view
         * @param object
         * @return 如果对应的是同一个 View，返回 true or false
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        /**
         * 创建指定位置的页面视图
         *
         * @param container
         * @param position
         * @return 返回一个代表新增视图页面的 object
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mIv_datas.get(position);

            container.addView(v);

            return v;
        }

        /**
         * 移除一个给定位置的页面
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private void initView() {
        setContentView(R.layout.activity_guide);
        vp = (ViewPager) findViewById(R.id.vp_guide_setpages);
    }
}
