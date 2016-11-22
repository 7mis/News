package com.oliver.news.tpipage;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.oliver.news.domain.NewsvCenterDetailData;
import com.oliver.news.utils.L;

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
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        /**网络获取数据*/

        String newsDetailUrl = mContext.getResources().getString(R.string.baseurl) + mChildrenBean.getUrl();

        getDataFromeNet(newsDetailUrl);
    }

    /**
     * 网络获取数据
     *
     * @param url
     */
    private void getDataFromeNet(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.d("网络请求数据成功 - detail");

                /**1. 获取 json 数据*/
                String jsonDataStr = responseInfo.result;
                /**2. 解析 json 数据*/
                NewsvCenterDetailData newsvCenterDetailData = parseJsonData(jsonDataStr);
                /**3. 处理 json 数据*/
                processData(newsvCenterDetailData);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                L.d("网络请求数据成功 - detail");

            }
        });
    }

    /**处理 json 数据
     * @param newsvCenterDetailData
     */
    private void processData(NewsvCenterDetailData newsvCenterDetailData) {
        L.d(" - "+newsvCenterDetailData.getData().getTopnews().get(0).getTitle());

    }

    /**
     * Gson 解析 json 数据
     *
     * @param jsonDataStr
     */
    private NewsvCenterDetailData parseJsonData(String jsonDataStr) {
        Gson gson = new Gson();

        /**创建一个类*/
        NewsvCenterDetailData newsvCenterDetailData = gson.fromJson(jsonDataStr, NewsvCenterDetailData.class);
        /**获取结果*/
        return newsvCenterDetailData;

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
