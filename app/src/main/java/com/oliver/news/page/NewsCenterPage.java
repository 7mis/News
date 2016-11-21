package com.oliver.news.page;

import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.oliver.news.fragment.LeftFragment;
import com.oliver.news.utils.L;

/**
 * Created by Administrator on 2016/11/18.
 */
public class NewsCenterPage extends BasePage {
    /**
     * 构造函数，默认的布局
     *
     * @param context
     */
    public NewsCenterPage(HomeActivity context) {
        super(context);
    }

    @Override
    public void initData() {
        tv_title.setText("新闻中心");

        /*内容*/
        TextView textView = new TextView(mContxt);
        textView.setText("政新闻中心的内容");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);

        /*添加到内容中*/
        fl_content.addView(textView);


        /**动态获取服务器的数据
         *
         * 1. 请求 url http://192.168.1.101:8080/zhbj/categories.json
         *
         * 2. 获取 json 数据
         * 3. 解析 json 数据
         * 4. 左侧菜单显示数据*/

//        String url = "http://192.168.1.101:8080/zhbj/categories.json";
        String url = mContxt.getResources().getString(R.string.newscenterurl);

        getDataFromNet(url);


    }


    /**
     * 设置显示的新闻条目页面
     * 在父类中声明 子类都可以实现
     * @param pageIndex
     *  - 运行时多态
     */
    public void selectPage(int pageIndex) {
        L.d("显示 "+pageIndex+" 页面 - NewsCenterPage");

    }

    /**
     * 从服务器获取数据
     * 1. 请求 url
     */
    public void getDataFromNet(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.d("网络连接成功");

                /**2. 获取 json 数据*/
                String jsonDataStr = responseInfo.result;

                /**3. 解析 json 数据*/

                NewsCenterData_GosnFormat newsCenterData = parseJsonData(jsonDataStr);

                /**4. 处理数据*/
                processData(newsCenterData);


            }

            @Override
            public void onFailure(HttpException e, String s) {
                /**请求数据失败*/
                L.d("网络连接失败");


            }
        });
    }

    /**
     * 处理数据
     *
     * @param newsCenterData
     */
    private void processData(NewsCenterData_GosnFormat newsCenterData) {

//        L.d("- " + newsCenterData.data.get(0).getChildren().get(0).getTitle());
        L.d("+ " + newsCenterData.getData().get(0).getChildren().get(0).getTitle());


        /**新闻中心中处理数据
         *  1. 设置数据给左侧菜单
         *  2. 数据有了，设置数据的 Api 也有，只需要获取到 LeftFragment 即可
         *         - mContext.getLeftFragment()
         *         - setLeftMenuData()
         */
        LeftFragment leftFragment = mContxt.getLeftFragment();
        leftFragment.setLeftMenuData(newsCenterData.getData());

    }

    /**
     * Gson 解析 json 数据
     *
     * @param jsonDataStr
     */
    private NewsCenterData_GosnFormat parseJsonData(String jsonDataStr) {
        Gson gson = new Gson();
        /**1. 创建 class*/
        NewsCenterData_GosnFormat newsCenterData = gson.fromJson(jsonDataStr, NewsCenterData_GosnFormat.class);

        return newsCenterData;

    }
}
