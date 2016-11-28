package com.oliver.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oliver.news.R;
import com.oliver.news.utils.MyConstaints;

/**
 * @desc 新闻显示界面
 * @auther Oliver
 * @time 2016/11/28 13:00
 */
public class NewsDetailActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ImageView iv_textSize;
    private ImageView iv_share;
    private WebView wv_news;
    private ProgressBar pb_loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();
        initEvent();
    }

    /**
     * 数据
     */
    private void initData() {
        String newsUrl = getIntent().getStringExtra(MyConstaints.NEWSDETAILURL);

        /*webview*/
        wv_news.loadUrl(newsUrl);//加载 url 对应的页面数据


    }

    /**
     * 事件
     */
    private void initEvent() {
        /**监听数据加载完成的事件*/
        wv_news.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                /**页面加载数据完成的回调*/
                /**隐藏进度条*/
                pb_loading.setVisibility(View.GONE);

                super.onPageFinished(view, url);
            }
        });

    }

    /**
     * 界面
     */
    private void initView() {
        setContentView(R.layout.activity_news_detail);

        /*隐藏 menu*/
        findViewById(R.id.iv_basepage_menu).setVisibility(View.GONE);
        /*隐藏 标题*/
        findViewById(R.id.tv_basepage_title).setVisibility(View.GONE);

        /*显示箭头*/
        iv_back = (ImageView) findViewById(R.id.iv_basepage_arrow);
        iv_back.setVisibility(View.VISIBLE);
        /*显示字体大小*/
        iv_textSize = (ImageView) findViewById(R.id.iv_basepage_textsize);
        iv_textSize.setVisibility(View.VISIBLE);
        /*显示分享*/
        iv_share = (ImageView) findViewById(R.id.iv_basepage_share);
        iv_share.setVisibility(View.VISIBLE);


        /*webview*/
        wv_news = (WebView) findViewById(R.id.wv_newsdetail);

        /*progressbar*/
        pb_loading = (ProgressBar) findViewById(R.id.pb_newsdetail);

    }
}
