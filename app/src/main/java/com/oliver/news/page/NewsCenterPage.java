package com.oliver.news.page;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
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
import com.oliver.news.newscenterpage.BaseNewsCenterPage;
import com.oliver.news.newscenterpage.InterectPage_NewsCenter;
import com.oliver.news.newscenterpage.NewsPage_NewsCenter;
import com.oliver.news.newscenterpage.PhotoPage_NewsCenter;
import com.oliver.news.newscenterpage.TopicPage_NewsCenter;
import com.oliver.news.utils.L;
import com.oliver.news.utils.MyConstaints;
import com.oliver.news.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class NewsCenterPage extends BasePage {

    private List<BaseNewsCenterPage> mBaseNewsCenterPages = new ArrayList<>();

    private NewsCenterData_GosnFormat newsCenterData;

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
        L.d("新闻数据 -- 懒加载");
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

        /**1. 本地数据获取：解决网络差/没开网络
         *
         *  - 1. 先判断是否有本地数据
         *  - 2. 先读取本地数据，再从网络获取数据
         *
         */
        String localJson = SPUtils.getString(mContxt, MyConstaints.LOCALNEWSDATAJSON, null);
        if (!TextUtils.isEmpty(localJson)) {
            /**不为空，有本地缓存数据*/
            /*解析数据*/
            newsCenterData = parseJsonData(localJson);
            /*处理数据*/
            processData(newsCenterData);

        }

        getDataFromNet(url);


    }


    /**
     * 设置显示的新闻条目页面
     * 在父类中声明 子类都可以实现
     *
     * @param pageIndex - 运行时多态
     */
    public void selectPage(int pageIndex) {
        L.d("不使用接口 " + pageIndex + " 页面 - NewsCenterPage");
        changePage(pageIndex);

    }

    /**
     * 完成页面的切换
     *
     * @param pageIndex
     */
    private void changePage(int pageIndex) {
        viewDatas(pageIndex);
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

                /**保存到本地缓存*/
                SPUtils.putString(mContxt, MyConstaints.LOCALNEWSDATAJSON, jsonDataStr);



                /**3. 解析 json 数据*/

                newsCenterData = parseJsonData(jsonDataStr);

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

        /**设置左侧菜单数据*/
        setLeftMenuData(newsCenterData);

        /**设置四个页面*/
        initNewsPages(newsCenterData);

        /**显示默认显示第一个*/
        viewDatas(0);


    }

    private void viewDatas(int index) {
        //标题
        tv_title.setText(newsCenterData.getData().get(index).getTitle());

        //清空
        fl_content.removeAllViews();


        //加载
        BaseNewsCenterPage baseNewsCenterPage = mBaseNewsCenterPages.get(index);

        /**判断 page 的类型
         * 1. 组图页面显示按钮
         */
        if (baseNewsCenterPage instanceof PhotoPage_NewsCenter) {
            iv_showListOrGrid.setVisibility(View.VISIBLE);

            /**设置标记：
             * 1. setTag 属于 View 类的方法
             * 2. 把页面 设置成一个 tag 标记
             * 3. 点击时就有这个页面了，显示才可以点击
             *
             */
            iv_showListOrGrid.setTag(baseNewsCenterPage);



        } else {
            iv_showListOrGrid.setVisibility(View.GONE);
        }

        View view = baseNewsCenterPage.getRootView();

        /**初始化数据*/
        baseNewsCenterPage.initData();

       /*添加到内容中*/
        fl_content.addView(view);
    }


    /**
     * 动态添加 4 个页面
     */
    private void initNewsPages(NewsCenterData_GosnFormat newsCenterData) {

        /**清空原有数据,使用缓存可能加载两次，8个页面*/
        mBaseNewsCenterPages.clear();

        for (NewsCenterData_GosnFormat.DataBean data : newsCenterData.getData()) {
            int type = data.getType();
            switch (type) {
                case 1:
                    /**加载 -新闻- 页面*/
                    mBaseNewsCenterPages.add(new NewsPage_NewsCenter(mContxt, newsCenterData.getData().get(0).getChildren()));
                    break;
                case 10:
                    /**加载 -专题- 页面*/
                    mBaseNewsCenterPages.add(new TopicPage_NewsCenter(mContxt));
                    break;
                case 2:
                    /**加载 -组图- 页面*/
                    mBaseNewsCenterPages.add(new PhotoPage_NewsCenter(mContxt));

                    break;
                case 3:
                    /**加载 -互动- 页面*/
                    mBaseNewsCenterPages.add(new InterectPage_NewsCenter(mContxt));
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 设置左侧菜单数据
     */
    private void setLeftMenuData(NewsCenterData_GosnFormat newsCenterData) {
        /**新闻中心中处理数据
         *  1. 设置数据给左侧菜单
         *  2. 数据有了，设置数据的 Api 也有，只需要获取到 LeftFragment 即可
         *         - mContext.getLeftFragment()
         *         - setLeftMenuData()
         */
        LeftFragment leftFragment = mContxt.getLeftFragment();
        leftFragment.setLeftMenuData(newsCenterData.getData());

        /**实现该接口：优先使用接口回调*/
        leftFragment.setOnLeftMenuPageChangeListener(new LeftFragment.OnLeftMenuPageChangeListener() {
            @Override
            public void selectPage(int selectIndex) {
                L.d("接口回调：" + selectIndex + "页面");
                changePage(selectIndex);
            }
        });
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
