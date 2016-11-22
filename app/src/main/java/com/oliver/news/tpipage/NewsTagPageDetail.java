package com.oliver.news.tpipage;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.domain.NewsCenterData_GosnFormat;

/**
 * @desc 新闻数据具体显示页面
 * @auther Oliver
 * @time 2016/11/22 11:11
 */
public class NewsTagPageDetail {
    private HomeActivity mContext;
    private View rootView;

    private NewsCenterData_GosnFormat.DataBean.ChildrenBean mChildrenBean;


    @ViewInject(R.id.vp_tpi_page_detail_lunbo)
    private ViewPager vp_lunbo;

    @ViewInject(R.id.tv_tpi_page_detail_desc)
    private TextView tv_desc;

    @ViewInject(R.id.ll_tpi_page_detail_points)
    private LinearLayout ll_point;

    @ViewInject(R.id.lv_tpi_page_detail_newsdata)
    private ListView lv_newsdata;


    public NewsTagPageDetail(HomeActivity context, NewsCenterData_GosnFormat.DataBean.ChildrenBean childrenBean) {
        this.mContext = context;
        this.mChildrenBean = childrenBean;

        initView();
    }


    public View getRootView() {
        return rootView;
    }

    /**
     * 页面数据显示
     *
     * @return
     */
    private void initView() {

        rootView = View.inflate(mContext, R.layout.tpi_page_detail, null);

        ViewUtils.inject(this, rootView);
    }


}
