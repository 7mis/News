package com.oliver.news.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oliver.news.R;
import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.oliver.news.utils.L;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class LeftFragment extends BaseFragment {
    private List<NewsCenterData_GosnFormat.DataBean> mLeftMenuData;
    private ListView lv;
    private MyAdapter adapter;


    /**
     * 默认第一个被选中
     */
    private int selectIndex = 0;


    @Override
    public View initView() {
        lv = new ListView(mContext);
        lv.setBackgroundColor(Color.BLACK);//背景 黑色
        lv.setDividerHeight(0);//没有分割线

        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置选中的颜色为透明
        lv.setPadding(0, 80, 0, 0);//顶部的 padding

        adapter = new MyAdapter();
        lv.setAdapter(adapter);


        return lv;
    }

    /**
     * ListView 条目的点击事件
     */
    @Override
    public void initEvent() {
        /**设置 tv 选中的状态*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**设置选中天目的 tv 设置成可用*/
                selectIndex = position;

                /**更新界面*/
                adapter.notifyDataSetChanged();


            }
        });

        super.initEvent();
    }


    /**
     * ListView 适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mLeftMenuData != null) {

                return mLeftMenuData.size();
            }
            return 0;

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_leftmenu_lv, null);
            }
            TextView tv = (TextView) convertView;
            /*数据*/

            tv.setText(mLeftMenuData.get(position).getTitle());

            /**设置选中的位置可用*/
            if (selectIndex == position) {
                tv.setEnabled(true);
            } else {
                tv.setEnabled(false);
            }

            return tv;
        }
    }

    /**
     * 1. 数据是在新闻中心获取到的
     * 2. 左侧菜单的数据是在 LeftFragment 中
     * 1. LeftFragment 中要有一个接收数据的方法
     * 2. 参数类型：data 中有个4个值
     * 3. 暴露该 Api 接收数据
     * 4. 只需要获取菜单数据：data.getTitle()
     *
     * @param leftMenuData
     */
    public void setLeftMenuData(List<NewsCenterData_GosnFormat.DataBean> leftMenuData) {
        mLeftMenuData = leftMenuData;

        L.d(" - " + mLeftMenuData.get(0).getChildren().get(0).getUrl());


        /**显示左侧菜单 ListView 数据*/
        /*更新界面*/
        adapter.notifyDataSetChanged();

    }
}
