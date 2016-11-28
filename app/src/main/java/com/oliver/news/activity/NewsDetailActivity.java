package com.oliver.news.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oliver.news.R;

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
    private ProgressBar pv_news;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

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
        pv_news = (ProgressBar) findViewById(R.id.pb_newsdetail);

    }
}
