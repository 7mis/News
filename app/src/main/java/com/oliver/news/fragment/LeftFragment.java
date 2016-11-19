package com.oliver.news.fragment;

import android.view.View;
import android.widget.TextView;

import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.oliver.news.utils.L;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class LeftFragment extends BaseFragment {
    private List<NewsCenterData_GosnFormat.DataBean> mLeftMenuData;


    @Override
    public View initView() {
        TextView tv = new TextView(mContext);
        tv.setText("LeftFragment");
        tv.setTextSize(30);

        return tv;
    }

    /**
     * 1. 数据是在新闻中心获取到的
     * 2. 左侧菜单的数据是在 LeftFragment 中
     *      1. LeftFragment 中要有一个接收数据的方法
     *      2. 参数类型：data 中有个4个值
     *      3. 暴露该 Api 接收数据
     *      4. 只需要获取菜单数据：data.getTitle()
     *
     * @param leftMenuData
     */
    public void setLeftMenuData(List<NewsCenterData_GosnFormat.DataBean> leftMenuData) {
        mLeftMenuData = leftMenuData;

        L.d(" - " + mLeftMenuData.get(0).getChildren().get(0).getUrl());

    }
}
