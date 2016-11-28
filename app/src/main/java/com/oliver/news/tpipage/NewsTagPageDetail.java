package com.oliver.news.tpipage;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.activity.NewsDetailActivity;
import com.oliver.news.domain.NewsCenterData_GosnFormat;
import com.oliver.news.domain.NewsvCenterDetailData;
import com.oliver.news.fragment.RefreshListView;
import com.oliver.news.utils.DensityUtils;
import com.oliver.news.utils.L;
import com.oliver.news.utils.MyConstaints;
import com.oliver.news.utils.SPUtils;
import com.oliver.news.utils.T;

import java.util.List;

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
    private RefreshListView lv_newsdata;

    private List<NewsvCenterDetailData.DataBean.TopnewsBean> topNews;
    private ViewPagerAdapter vpAdapter;
    private final BitmapUtils bitmapUtils;
    private MyHandler mHandler;
    private float downX;
    private float downY;
    private long downTime;
    private List<NewsvCenterDetailData.DataBean.NewsBean> mListNews;
    private LvAdapter mLvAdapter;
    private View lunboRootView;
    private String newsDetailUrl;

    private boolean isRefresh = false;


    private String moreUrl;//加载更多数据的 url


    public NewsTagPageDetail(HomeActivity context, NewsCenterData_GosnFormat.DataBean.ChildrenBean childrenBean) {
        this.mContext = context;
        this.mChildrenBean = childrenBean;


        /**加载图片工具*/
        bitmapUtils = new BitmapUtils(mContext);

        initView();
        initData();
        initEvent();
    }


    /**
     * 轮播图的page change 事件
     */
    private void initEvent() {

        /**ListView 添加 item 点击事件*/
        lv_newsdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**点击新闻*/

                /*获取当前点击的数据*/
                L.d("点击的 item 位置 - " + position);
                NewsvCenterDetailData.DataBean.NewsBean news = mListNews.get(position - 1);
                /*传递新闻的 url*/
                String newsUrl = news.getUrl();

                /**点打开新闻*/
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(MyConstaints.NEWSDETAILURL, newsUrl);
                mContext.startActivity(intent);






            }
        });


        /**RefreshView 添加数据监听事件
         * 1. 下拉刷新
         * 2. 上拉加载更多*/
        lv_newsdata.setOnRefreshDataListener(new RefreshListView.OnRefreshDataListener() {
            @Override
            public void freshData() {

                isRefresh = true;
                getDataFromeNet(newsDetailUrl, false);

//                /*更新数据*/
//                new Thread(){
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(2000);
//                        /**更新状态*/
//                        mContext.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                /**更新状态*/
//                                lv_newsdata.updateRefreshState();
//                            }
//                        });
//                    }
//                }.start();
            }

            /**
             * 加载更多的数据回调
             */
            @Override
            public void loadMore() {
                L.d("加载更多数据的回调");
                /*更新数据*/
                /**获取更多的数据*/
                if (!TextUtils.isEmpty(moreUrl)) {
                    /**有更多数据*/
                    getDataFromeNet(moreUrl, true);

                } else {
                    //没有更多数据
                    T.showShort(mContext, "没有更多数据");

                    //更新状态
                    lv_newsdata.updataState();
                }


            }
        });


        vp_lunbo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*页面停止改变
                * 选中那个位置 设置当前页面信息
                */
                setPointEnableAndPicDes(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        /**网络获取数据*/

        newsDetailUrl = mContext.getResources().getString(R.string.baseurl) + mChildrenBean.getUrl();


        /**本地缓存*/
        String locaJson = SPUtils.getString(mContext, MyConstaints.LUNBOANDLISTNEWS, null);
        if (!TextUtils.isEmpty(locaJson)) {
            /**有缓存数据，解析处理*/
            NewsvCenterDetailData detailData = parseJsonData(locaJson);
            processData(detailData);


        }
        getDataFromeNet(newsDetailUrl, false);//false 不是加载更多的数据
    }

    /**
     * 网络获取数据
     *
     * @param url
     */
    private void getDataFromeNet(String url, final boolean isLoadingMore) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.d("网络请求数据成功 - detail");

                /**1. 获取 json 数据*/
                String jsonDataStr = responseInfo.result;

                /**本地缓存*/
                SPUtils.putString(mContext, MyConstaints.LUNBOANDLISTNEWS, jsonDataStr);

                /**2. 解析 json 数据*/
                NewsvCenterDetailData newsvCenterDetailData = parseJsonData(jsonDataStr);

                if (!isLoadingMore) {//初始化 或者 刷新数据
                    /**3. 处理 json 数据*/
                    processData(newsvCenterDetailData);

                    /**检测是否是刷新数据*/
                    if (isRefresh) {
                        T.showShort(mContext, "刷新数据成功");

                        isRefresh = false;
                        /**刷新数据成功 更新状态*/
                        lv_newsdata.updataState();
                        T.showShort(mContext, "加载更多数据成功");
                    }
                } else {
                    //加载更多,在原先的新闻数据基础上 添加更多新的新闻数据
                    mListNews.addAll(newsvCenterDetailData.getData().getNews());
                    //通知界面
                    mLvAdapter.notifyDataSetChanged();
                    /**刷新数据成功 更新状态*/
                    lv_newsdata.updataState();

                }


            }

            @Override
            public void onFailure(HttpException error, String msg) {
                L.d("网络请求数据失败 - detail");

            }
        });
    }

    /**
     * 处理 json 数据
     *
     * @param newsvCenterDetailData
     */
    private void processData(NewsvCenterDetailData newsvCenterDetailData) {
        L.d(" - " + newsvCenterDetailData.getData().getTopnews().get(0).getTitle());

        /**顶部轮播图的数据*/
        topNews = newsvCenterDetailData.getData().getTopnews();
        setLunBoData();

        /**初始化点的数据*/
        initPoints();


        /**设置点可选+文字描述信息*/
        setPointEnableAndPicDes(0);

        /**自动轮播*/
        startLunbo();

        /**设置列表新闻数据*/
        mListNews = newsvCenterDetailData.getData().getNews();
        setListNewsData();


    }

    private void startLunbo() {
        if (mHandler == null)
            mHandler = new MyHandler();
        mHandler.startLunbo();


    }


    private class MyHandler extends Handler implements Runnable {
        @Override
        public void run() {
            vp_lunbo.setCurrentItem((vp_lunbo.getCurrentItem() + 1) % vp_lunbo.getAdapter().getCount());

            /**连续执行*/
            postDelayed(this, 2000);
        }

        /**
         * 开始轮播
         */
        public void startLunbo() {
            stopLunbo();//停止轮播
            postDelayed(this, 2000);

        }

        /**
         * 结束轮播
         * 清空所有消息
         */
        public void stopLunbo() {
            removeCallbacksAndMessages(null);

        }

    }

//    private void startLunbo() {
//
//        /**可能执行多次*/
//        if (mHandler == null) {
//            mHandler = new Handler();
//        }
//
//        /**每次调用时，回收*/
//        mHandler.removeCallbacksAndMessages(null);
//
//        /**发消息*/
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //功能
//                //主线程
//                L.d("自动轮播 - 当前条目 " + vp_lunbo.getCurrentItem());
//                L.d("自动轮播 - 求余条目 " + (vp_lunbo.getCurrentItem() + 1) % vp_lunbo.getAdapter().getCount());
//                vp_lunbo.setCurrentItem((vp_lunbo.getCurrentItem() + 1) % vp_lunbo.getAdapter().getCount());
//
//                /**连续执行*/
//                mHandler.postDelayed(this, 2000);
//
//            }
//        }, 2000);//两秒 发一个消息
//
//    }

    /**
     * 设置点可选+文字描述信息
     *
     * @param lunboIndex 默认第一个图片
     */
    private void setPointEnableAndPicDes(int lunboIndex) {
        /*1. 文字的描述*/
        tv_desc.setText(topNews.get(lunboIndex).getTitle());
        /*2. 选中点 可用状态的设置*/
        for (int i = 0; i < topNews.size(); i++) {
            /**取出 View*/
            View v = ll_point.getChildAt(i);


            /*是否可用*/
            v.setEnabled(i == lunboIndex);

        }

    }

    /**
     * 初始化点的显示，<>点的个数和轮播图片格式一致</>
     */
    public void initPoints() {

        /*会执行多次，先清空点*/
        ll_point.removeAllViews();

        for (int i = 0; i < topNews.size(); i++) {

            View view = new View(mContext);
            view.setBackgroundResource(R.drawable.lubo_v_point_selector);
            view.setEnabled(false);

            /**设置点的参数 大小 间距*/
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(mContext, 5), DensityUtils.dp2px(mContext, 5));

            lp.leftMargin = DensityUtils.dp2px(mContext, 8);
            view.setLayoutParams(lp);

            ll_point.addView(view);

        }

    }

    /**
     * ListView 的适配器
     */
    private void setListNewsData() {
        if (mLvAdapter == null) {
            mLvAdapter = new LvAdapter();
            lv_newsdata.setAdapter(mLvAdapter);

        } else {
            mLvAdapter.notifyDataSetChanged();

        }
    }


    /**
     * ListView 的 adapter
     */
    private class LvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (lv_newsdata != null)
                return mListNews.size();
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
            /**数据的显示*/
            ViewHolder mViewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_listnews, null);

                mViewHolder = new ViewHolder();
                /**图片*/
                mViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_item_listnews_pic);
                /**标题*/
                mViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_listnews_title);
                /**时间*/
                mViewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_listnews_time);
                /**菜单*/
                mViewHolder.iv_menu = (ImageView) convertView.findViewById(R.id.iv_item_listnews_menu);


                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();

            }

            /**获取数据 赋值*/
            NewsvCenterDetailData.DataBean.NewsBean newsBean = mListNews.get(position);
            /**设置 图片*/
            bitmapUtils.display(mViewHolder.iv_icon, newsBean.getListimage());
            /**设置 标题*/
            mViewHolder.tv_title.setText(newsBean.getTitle());
            /**设置 时间*/
            mViewHolder.tv_time.setText(newsBean.getPubdate());
            /**设置 菜单*/


            return convertView;
        }
    }

    /**
     * 复用
     */
    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
        ImageView iv_menu;
    }


    private void setLunBoData() {
        if (vpAdapter == null) {
            vpAdapter = new ViewPagerAdapter();
            vp_lunbo.setAdapter(vpAdapter);

        } else {
            vpAdapter.notifyDataSetChanged();
        }

    }

    /**
     * ViewPager 适配器
     */
    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            /**图片*/
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            /**给图片添加触摸事件*/
            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下

                            downX = event.getX();
                            downY = event.getY();
                            downTime = System.currentTimeMillis();
                            /**停止轮播*/
                            mHandler.stopLunbo();

                            break;
                        case MotionEvent.ACTION_UP://松开
                            float upX = event.getX();
                            float upY = event.getY();
                            if (Math.abs(upX - downX) < 5 && Math.abs(upY - downY) < 5) {
                                /**同一点松开*/
                                long upTime = System.currentTimeMillis();
                                if (upTime - downTime < 500) {
                                    //轮播图片的单击事件
                                    lunboPicclicked();
                                }

                            }

                            /**继续轮播*/
                            mHandler.startLunbo();

                            break;

                        case MotionEvent.ACTION_CANCEL://取消点击
                            mHandler.startLunbo();
                            break;
                        default:
                            break;

                    }
                    return true;//自己处理事件
                }
            });


            /**数据*/
            NewsvCenterDetailData.DataBean.TopnewsBean topNewsBean = topNews.get(position);
            String picUrl = topNewsBean.getTopimage();

            /**设置显示数据*/
            L.d("picUrl " + picUrl);

//            String url = "http://fdfs.xmcdn.com/group16/M08/F1/13/wKgDbFal40bR7Uc6AAH3JpWhLiQ015_android_large.jpg";

            bitmapUtils.display(iv, picUrl);

            container.addView(iv);

            return iv;
        }

        /**
         * 完成此方法 实现图片的单击事件
         */
        private void lunboPicclicked() {
            L.d("轮播图片单击");
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public int getCount() {
            if (topNews != null) {
                return topNews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


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


        /**加载更多数据的 url*/
        String more = newsvCenterDetailData.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            //有更多数据
            moreUrl = mContext.getResources().getString(R.string.baseurl) + more;

        } else {
            moreUrl = "";
        }
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

        lunboRootView = View.inflate(mContext, R.layout.lunbotu_view, null);

        ViewUtils.inject(this, lunboRootView);

        ViewUtils.inject(this, rootView);

        lv_newsdata.addLunbo(lunboRootView);
    }


}
